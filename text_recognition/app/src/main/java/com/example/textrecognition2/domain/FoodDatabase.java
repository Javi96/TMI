package com.example.textrecognition2.domain;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {Ingredient.class, Plate.class, IngredientesPlatos.class}, version = 1 , exportSchema = false)
public abstract class FoodDatabase extends RoomDatabase {

    public abstract IngredientDao ingredientDao();

    public abstract PlateDao plateDao();

    public abstract IngrPlatDao ingrPlatDao();

    private static FoodDatabase DATABASE;

    public static FoodDatabase getDatabase(final Context context) {
        if (DATABASE == null)
            synchronized (FoodDatabase.class) {
                if (DATABASE == null)
                    DATABASE =
                            Room.inMemoryDatabaseBuilder( context.getApplicationContext(), FoodDatabase.class )
                            //Room.databaseBuilder(context.getApplicationContext(), FoodDatabase.class, "food_database")

                            .allowMainThreadQueries()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
            }

        return DATABASE;
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final IngredientDao ingredientDao;
        private final PlateDao plateDao;
        private final IngrPlatDao ingrPlatDao;

        PopulateDbAsync(FoodDatabase db) {
            ingredientDao = db.ingredientDao();
            plateDao = db.plateDao();
            ingrPlatDao = db.ingrPlatDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            //DATABASE.runInTransaction(new Runnable() {
                //@Override
                //public void run() {
                    ingredientDao.deleteAll();
                    plateDao.deleteAll();
                    ingrPlatDao.deleteAll();
                    long i1 = ingredientDao.insert(new Ingredient("leche", "ml"));
                    long i2 = ingredientDao.insert(new Ingredient("huevo", "count"));
                    Log.e("Mensaje de prueba", "Esto es un mensaje de prueba");
                    Log.e("IDs inserciones", "El id de leche es " + i1 + " y el de huevo es " + i2);
                    long i3 = plateDao.insert(new Plate("flan"));
                    Log.e("ID plato", "El id de flan es " + i3);
                    ingrPlatDao.insert(new IngredientesPlatos( i1, i3, 6));
                    ingrPlatDao.insert(new IngredientesPlatos(i2, i3, 500));
                    List<IngrCant> resul = ingrPlatDao.getRecipeForPlate(i3);
                    Log.e("Ingredientes", "El flan lleva: " + resul.get(0).getNombre() + " + " + resul.get(1).getNombre());
                //}
            //});
            return null;
        }


    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(DATABASE).execute();
                }
            };
}
