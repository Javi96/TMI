package com.example.textrecognition2.domain;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.net.InetAddress;

import com.example.textrecognition2.QueryUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
        ingredientDao.insert(ingredient);
    }

    public void insertIngredients (final ArrayList<Ingredient> ingredients) {
        ingredientDao.insertAll(( Ingredient[]) ingredients.toArray() );
    }

    public void insertPlate (Plate plate) {
        long id = plateDao.insert(plate);
        if(plate.getIngredients() != null)
            for (IngrCant ing : plate.getIngredients()){
                long ingId = ingredientDao.insert(new Ingredient(ing.getNombre(),ing.getUnidades()));
                ingrPlatDao.insert(new IngredientesPlatos(ingId, id, ing.getQuantity()) );
            }
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

    public List<IngrCant> getIngredientsForPlate (String plate) {
        return ingrPlatDao.getRecipeForPlate(plateDao.getIdByName(plate));
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

    public List<IngrCant> getNecessaryIngredients ( List<String> platos) {
        return ingrPlatDao.getIngredientsByIds( plateDao.findIdsByName(platos) );
    }

}
