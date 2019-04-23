package com.example.textrecognition2.domain;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

public class FoodRepository {
    private IngredientDao ingredientDao;
    private PlateDao plateDao;
    private IngrPlatDao ingrPlatDao;

    private LiveData<List<Ingredient>> mAllIngredients;
    private LiveData<List<Plate>> mAllPlates;
    private LiveData<List<IngredientesPlatos>> mAllIngPla;

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

    public List<IngrCant> getIngredientsForPlate (Plate plate) {
        return ingrPlatDao.getRecipeForPlate(plate.getId());
    }

    public Plate getPlate (String name) {
        return plateDao.findByName(name);
    }

    public Ingredient getIngredient(String name) {
        return ingredientDao.findByName(name);
    }

}
