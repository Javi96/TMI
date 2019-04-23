package com.example.textrecognition2.domain;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IngrPlatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(IngredientesPlatos ingredientesPlatos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(IngredientesPlatos... ingredientesPlatos);

    @Delete
    void delete(IngredientesPlatos ingredientesPlatos);


    @Query("DELETE FROM ingr_plat_table")
    void deleteAll();

    @Query("SELECT * FROM ingr_plat_table")
    List<IngredientesPlatos> getAll();

    @Query(value = "SELECT id, nombre, unidades, quantity FROM ingr_plat_table INNER JOIN ingredients_table ON ingr_plat_table.ingrId = ingredients_table.id WHERE platId = :platId")
    List<IngrCant> getRecipeForPlate(long platId);

    @Query("SELECT * FROM ingr_plat_table  WHERE platId = :platId")
    List<IngredientesPlatos> getIngredientsForPlate(long platId);

    @Query("SELECT plates_table.id, plates_table.nombre FROM ingr_plat_table INNER JOIN plates_table ON ingr_plat_table.platId = plates_table.id  WHERE ingrId = :ingrId")
    List<Plate> loadPlatesWithIngredient(long ingrId);

}
