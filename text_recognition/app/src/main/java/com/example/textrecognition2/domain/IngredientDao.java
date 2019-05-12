package com.example.textrecognition2.domain;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

/**
 * <h1>Interfaz que vinculará la Base de Datos de Room con el objeto Ingredient asociado</h1>
 */
@Dao
public interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Ingredient ingredient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Ingredient... ingredients);

    @Delete
    int delete(Ingredient ingredient);

    @Query("DELETE FROM ingredients_table")
    int deleteAll();

    @Transaction @Query("UPDATE ingredients_table SET unidades = :units WHERE id = :id")
    long update(long id, String units);

    @Query("SELECT id FROM ingredients_table WHERE nombre LIKE :name LIMIT 1")
    long getIdByName(String name);

    @Query("SELECT * FROM ingredients_table WHERE id = :id LIMIT 1")
    Ingredient findById(long id);

    @Query("SELECT * FROM ingredients_table WHERE nombre LIKE :name LIMIT 1")
    Ingredient findByName(String name);

    @Transaction @Query("SELECT * FROM ingredients_table")
    List<Ingredient> getAll();

    @Transaction @Query("SELECT * FROM ingredients_table WHERE id IN (:ingrIds)")
    List<Ingredient> loadAllByIds(long[] ingrIds);

}
