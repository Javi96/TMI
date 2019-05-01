package com.example.textrecognition2.domain;

import android.app.Application;
import android.arch.lifecycle.LiveData;

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
    private IngredientDao ingredientDao;
    private PlateDao plateDao;
    private IngrPlatDao ingrPlatDao;

    private LiveData<List<Ingredient>> mAllIngredients;
    private LiveData<List<Plate>> mAllPlates;
    private LiveData<List<IngredientesPlatos>> mAllIngPla;

    /*
     * En la variable APIURL hay que poner la IP y el puerto hacia la que irán dirigidas las peticiones
     * es decir, la máquina que está corriendo la API de spyder
     */

    private static final String APIURL = "http://10.1.1.112:5555/recipeSpacy/";
    private static final String APIURLbad = "http://10.1.1.112:5555/recipe/";

    public FoodRepository(Application application) {
        FoodDatabase db = FoodDatabase.getDatabase(application);
        ingredientDao = db.ingredientDao();
        //mAllIngredients = ingredientDao.getAll();
        plateDao = db.plateDao();
        //mAllPlates = plateDao.getAll();
        ingrPlatDao = db.ingrPlatDao();
        //mAllIngPla = ingrPlatDao.getAll();
    }

    public LiveData<List<Ingredient>> getAllIngredients() {
        return mAllIngredients;
    }

    public LiveData<List<Plate>> getAllPlates() {
        return mAllPlates;
    }

    public LiveData<List<IngredientesPlatos>> getmAllIngPla() {
        return mAllIngPla;
    }



    public void insertIngredient (final Ingredient ingredient) {
        ingredientDao.insert(ingredient);
    }

    public void insertIngredients (final List<Ingredient> ingredients) {
         for (Ingredient ing : ingredients)
             ingredientDao.insert(ing);
    }

    public void insertPlate (Plate plate) {
        plateDao.insert(plate);
    }

    public void insertPlates ( List<Plate> plates) {
        for (Plate pla : plates)
            plateDao.insert(pla);
        //plateDao.insertAll(plates);
    }

    public void insertIngredientsPlate (Plate plate, List<Ingredient> ingredients, int[] cantidades) {
        long id = plate.getId();
        for(int i = 0; i < ingredients.size(); i++)
            ingrPlatDao.insert(new IngredientesPlatos(ingredients.get(i).getId(), id, cantidades[i]));

    }

    public void insertIngredientsPlate (String plate, List<String> ingredients, int[] cantidades) {
        long id = ingredientDao.findByName(plate).getId();
        for(int i = 0; i < ingredients.size(); i++)
            ingrPlatDao.insert(new IngredientesPlatos(
                    ingredientDao.findByName(ingredients.get(i)).getId(), id, cantidades[i]));
    }

    public void insertPlateWithIngredients (String plate, List<String> ingredients, int[] cantidades) {
        int id = (int) plateDao.insert(new Plate(plate));
        for(int i = 0; i < ingredients.size(); i++)
            ingrPlatDao.insert(new IngredientesPlatos(
                    ingredientDao.findByName(ingredients.get(i)).getId(), id, cantidades[i]));
    }

    public List<IngrCant> getIngredientsForPlate (String plate) {
        return ingrPlatDao.getRecipeForPlate(plateDao.getIdByName(plate));
    }

    public List<IngrCant> getIngredientsForPlate (Plate plate) {
        return ingrPlatDao.getRecipeForPlate(plate.getId());
    }

    public Plate getPlate (String name) {
        Plate resul = getPlateFromDB(name);
        if (resul == null)
            resul = getPlateFromAPI(name);
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

            JSONArray ingredientes = jsonResponse.getJSONArray("recipe");
            ArrayList<String> ingredients =  new ArrayList<String>();
            int[] cantidades = new int[ingredientes.length()];
            for (int j = 0; j < ingredientes.length(); j++) {
                JSONObject act = ingredientes.getJSONObject(j);
                String nombre = act.getString("name");
                ingredients.add(nombre);

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
