package com.example.textrecognition2.domain;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Ingredient ingredient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Ingredient... ingredients);

    @Delete
    void delete(Ingredient ingredient);

    @Query("DELETE FROM ingredients_table")
    void deleteAll();

    @Query("SELECT id FROM ingredients_table WHERE nombre LIKE :name LIMIT 1")
    Integer getIdByName(String name);

    @Query("SELECT * FROM ingredients_table WHERE id = :id LIMIT 1")
    Ingredient findById(long id);

    @Query("SELECT * FROM ingredients_table WHERE nombre LIKE :name LIMIT 1")
    Ingredient findByName(String name);

    @Query("SELECT * FROM ingredients_table")
    List<Ingredient> getAll();

    @Query("SELECT * FROM ingredients_table WHERE id IN (:ingrIds)")
    List<Ingredient> loadAllByIds(long[] ingrIds);

}
