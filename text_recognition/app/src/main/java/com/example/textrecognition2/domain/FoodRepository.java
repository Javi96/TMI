package com.example.textrecognition2.domain;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.net.InetAddress;

import com.example.textrecognition2.DietScheduleActivity;
import com.example.textrecognition2.QueryUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FoodRepository {
    private static IngredientDao ingredientDao;
    private static PlateDao plateDao;
    private static IngrPlatDao ingrPlatDao;

    private static FoodDatabase db = null;

    /*
     * En la variable APIURL hay que poner la IP y el puerto hacia la que irán dirigidas las peticiones
     * es decir, la máquina que está corriendo la API de spyder
     */

    private static final String APIURL = "http://10.1.1.112:5555/recipeSpacy/";
    private static final String APIURLbad = "http://10.1.1.112:5555/recipe/";

    public FoodRepository(Application application) {
        if( db == null) {
            FoodDatabase db = FoodDatabase.getDatabase(application);
            ingredientDao = db.ingredientDao();
            plateDao = db.plateDao();
            ingrPlatDao = db.ingrPlatDao();
        }
    }

    public void insertIngredient (final Ingredient ingredient) {
        Ingredient aux = ingredientDao.findByName(ingredient.getName());
        if( aux != null)
            ingredientDao.update(aux.getId(), ingredient.getUnits());
        else
            ingredientDao.insert(ingredient);

    }

    public void insertIngredients (final Ingredient[] ingredients) {
        for ( Ingredient ing: ingredients ) {
            Ingredient aux = ingredientDao.findByName(ing.getName());
            if( aux != null)
                ingredientDao.update(aux.getId(), ing.getUnits());
            else
                ingredientDao.insert(ing);
        }
    }

    /*
    public long[] insertIngredients (final ArrayList<Ingredient> ingredients) {
        return ingredientDao.insertAll(( Ingredient[]) ingredients.toArray() );
    }
    */

    public long insertPlate (Plate plate) {
        long id = plateDao.insert(plate);
        if(plate.getIngredients() != null)
            for (IngrCant ing : plate.getIngredients()) {
                long ingId = ingredientDao.insert(new Ingredient(ing.getNombre(), ing.getUnidades()));
                ingrPlatDao.insert(new IngredientesPlatos(ingId, id, ing.getQuantity()));
            }
        return id;
    }

    public void insertPlates ( ArrayList<Plate> plates) {
        for (Plate pla : plates)
            insertPlate(pla);
    }

    public void insertIngredientsPlate (String plate, ArrayList<String> ingredients, int[] cantidades) {
        long id = ingredientDao.findByName(plate).getId();
        for(int i = 0; i < ingredients.size(); i++)
            ingrPlatDao.insert(new IngredientesPlatos(
                    ingredientDao.findByName(ingredients.get(i)).getId(), id, cantidades[i]));
    }

    public void insertPlateWithIngredients (String plate, ArrayList<String> ingredients, int[] cantidades) {
        long id = plateDao.insert(new Plate(plate));
        for(int i = 0; i < ingredients.size(); i++)
            ingrPlatDao.insert(new IngredientesPlatos(
                    ingredientDao.findByName(ingredients.get(i)).getId(), id, cantidades[i]));
    }

    public void insertPlateWithIngredients (Plate plate) {
        long id =  plateDao.insert(plate);
        for(IngrCant ing : plate.getIngredients() ){
            long ingr;
            if(ingredientDao.findByName(ing.getNombre())==null)
                ingr = ingredientDao.insert(new Ingredient(ing.getNombre(), ing.getUnidades()));
            else
                ingr = ingredientDao.getIdByName(ing.getNombre());
            ingrPlatDao.insert(new IngredientesPlatos( ingr, id, ing.getQuantity()));
        }

    }

    public ArrayList<IngrCant> getIngredientsForPlate (String plate) {
        return (ArrayList<IngrCant>)ingrPlatDao.getRecipeForPlate(plateDao.getIdByName(plate));
    }

    public ArrayList<IngrCant> getIngredientsForPlate (Plate plate) {
        return (ArrayList<IngrCant>)ingrPlatDao.getRecipeForPlate(plate.getId());
    }

    public Plate getPlate (String name) {
        Plate resul = getPlateFromDB(name);
        if (resul == null)
            resul = getPlateFromAPI(name);
        else
            resul.setIngredients(getIngredientsForPlate(resul));
        return resul;
    }

    private Plate getPlateFromDB (String name) {
        return plateDao.findByName(name);
    }

    private Plate getPlateFromAPI (String name) {
        try {
            JSONObject jsonResponse = new JSONObject(QueryUtils.makeHttpRequest(new URL(APIURL + name.replace(' ', '+'))));
            if (jsonResponse.getString("state").compareToIgnoreCase("Success") != 0)
                return null;
            Log.d("respuesta server:",jsonResponse.toString());
            JSONArray ingredientes = jsonResponse.getJSONArray("recipe");
            ArrayList<IngrCant> ingredients =  new ArrayList<IngrCant>();
            for (int j = 0; j < ingredientes.length(); j++) {
                JSONObject act = ingredientes.getJSONObject(j);
                String nombre = act.getString("name");
                if (nombre.isEmpty())
                    continue;
                String unidades = act.getString("units");
                int cantidad = act.getInt("num");
                ingredients.add(new IngrCant(nombre, unidades, cantidad));

            }
            return new Plate(name, ingredients);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Ingredient getIngredient(String name) {
        return ingredientDao.findByName(name);
    }

    public ArrayList<IngrCant> getNecessaryIngredients ( String plato) {
        return (ArrayList<IngrCant>)ingrPlatDao.getIngredientsByIds( plateDao.getIdByName(plato) );
    }

    public ArrayList<IngrCant> getShoppingList(Application application){
        ArrayList<IngrCant> resul = new ArrayList<IngrCant>();

        HashMap<String, IngrCant> lista = new HashMap<String, IngrCant>();

        SharedPreferences inventory = application.getSharedPreferences("inventory", Context.MODE_PRIVATE);
        SharedPreferences menus = application.getSharedPreferences("menus", Context.MODE_PRIVATE);

        // Recorremos cada dia
        for ( String dia  : DietScheduleActivity.days ){
            // Para cada dia vemos los platos de la dieta
            Log.d("Buscando el dia: ", dia);
            for ( String plato : menus.getString(dia,"").split("_") ) {
                // Para cada plato sacamos los ingredientes necesarios
                for(IngrCant ing : getNecessaryIngredients(plato)){
                    IngrCant act = lista.get(ing.getNombre());
                    // Si ya tenemos ese ingrediente en la lista lo incrementamos
                    if(act!=null)
                        act.setQuantity(act.getQuantity() + ing.getQuantity());
                    // Si no lo tenemos lo añadimos
                    else
                        lista.put(ing.getNombre(), ing);
                }
            }
        }

        // Recorremos los ingredientes que estan en la lista
        for(Map.Entry<String,IngrCant> ent : lista.entrySet()){
            try {
                Log.d("Probando ingrediente: " , ent.getValue().toString());
                // Si ya tenemos en el inventario, esa cantidad no hace falta comprarla
                if( inventory.contains(ent.getKey()) ) {
                    Log.d("Habia: " , ent.getValue().toString());
                    ent.getValue().setQuantity(ent.getValue().getQuantity() - inventory.getInt(ent.getKey(), 0));
                    Log.d("Y ahora: " , ent.getValue().toString());
                }
                // Si ese ingrediente es incontable
                // o
                // si descontando el inventario sigue faltando,
                // se añade a la lista de la compra
                if( ent.getValue().getUnidades().equalsIgnoreCase("uncount") || ent.getValue().getQuantity() > 0 ){
                    resul.add(ent.getValue());
                    Log.d("Añadiendo ingrediente ", ent.getValue().toString());
                }

            } catch ( ClassCastException e){continue;} // Esto es por el getInt por si acaso

        }


        return resul;
    }
}
