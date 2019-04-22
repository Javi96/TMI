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
        mAllIngredients = ingredientDao.getAll();
        plateDao = db.plateDao();
        mAllPlates = plateDao.getAll();
        ingrPlatDao = db.ingrPlatDao();
        mAllIngPla = ingrPlatDao.getAll();
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

    /*
    private static class insertIngredientAsyncTask extends AsyncTask<Ingredient, Void, Void> {

        private IngredientDao mAsyncTaskDao;

        insertIngredientAsyncTask(IngredientDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Ingredient... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    */

    /*
    private static class insertPlateAsyncTask extends AsyncTask<Plate, Void, Void> {

        private PlateDao mAsyncTaskDao;

        insertPlateAsyncTask(PlateDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Plate... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    */

    @SuppressLint("StaticFieldLeak")
    public void insertIngredient (final Ingredient ingredient) {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                ingredientDao.insert(ingredient);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void insertIngredients (final List<Ingredient> ingredients) {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                for (Ingredient ing : ingredients)
                    ingredientDao.insert(ing);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void insertPlate (final Plate plate) {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                plateDao.insert(plate);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void insertPlates (final List<Plate> plates) {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                for (Plate pla : plates)
                    plateDao.insert(pla);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void insertIngredientsPlate (final Plate plate, final List<Ingredient> ingredients, final int[] cantidades) {
        new AsyncTask<Void, Void,  Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                int id = plate.getId();
                for(int i = 0; i < ingredients.size(); i++){
                    ingrPlatDao.insert(new IngredientesPlatos(ingredients.get(i).getId(), id, cantidades[i]));
                }
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void insertIngredientsPlate (final String plate, final List<String> ingredients, final int[] cantidades) {
        new AsyncTask<Void, Void,  Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                int id = ingredientDao.findByName(plate).getValue().getId();
                for(int i = 0; i < ingredients.size(); i++){
                    ingrPlatDao.insert(new IngredientesPlatos( ingredientDao.findByName(ingredients.get(i)).getValue().getId(),
                                        id, cantidades[i]));
                }
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public List<IngrCant> getIngredientsForPlate (final Plate plate) {
        return new AsyncTask<Void, Void,  List<IngrCant>>() {
            @Override
            protected List<IngrCant> doInBackground(Void... params) {
                return ingrPlatDao.getRecipeForPlate(plate.getId()).getValue();
            }
        }.doInBackground();
    }

    @SuppressLint("StaticFieldLeak")
    public Plate getPlate (final String name) {
        final Plate[] plato = new Plate[1];

        new AsyncTask<Void, Void,  Plate>() {

            @Override
            protected Plate doInBackground(Void... params) {
                //resul = plateDao.findByName(name).getValue();
                //plato[0] = plateDao.findByName(name).getValue();
                return plateDao.findByName(name).getValue();
            }


            @Override
            protected void onPostExecute(Plate resul) {
                super.onPostExecute(resul);
                plato[0] = resul;
            }

        }.execute();

        return plato[0];

    }

    @SuppressLint("StaticFieldLeak")
    public Ingredient getIngredient (final String name) {
        //new insertIngredientAsyncTask(ingredientDao).execute(ingredient);
        return new AsyncTask<Void, Void,  Ingredient>() {
            @Override
            protected Ingredient doInBackground(Void... params) {
                return ingredientDao.findByName(name).getValue();
            }
        }.doInBackground();
    }

}
