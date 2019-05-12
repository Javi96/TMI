package com.example.textrecognition2.domain;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

/**
 * <h1>Interfaz que vinculará la Base de Datos de Room con la relación entre Platos e Ingredientes</h1>
 */
@Dao
public interface IngrPlatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(IngredientesPlatos ingredientesPlatos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(IngredientesPlatos... ingredientesPlatos);

    @Delete
    int delete(IngredientesPlatos ingredientesPlatos);

    @Query("DELETE FROM ingr_plat_table")
    int deleteAll();

    @Transaction @Query("SELECT * FROM ingr_plat_table")
    List<IngredientesPlatos> getAll();

    @Transaction @Query("SELECT id, nombre, unidades, quantity FROM ingr_plat_table INNER JOIN ingredients_table ON ingrId = ingredients_table.id WHERE platId = :platId")
    List<IngrCant> getRecipeForPlate(long platId);

    @Transaction @Query("SELECT * FROM ingr_plat_table WHERE platId = :platId")
    List<IngredientesPlatos> getIngredientsForPlate(long platId);

    @Transaction @Query("SELECT id, nombre, unidades, SUM(quantity) AS quantity FROM ingr_plat_table INNER JOIN ingredients_table ON ingrId = ingredients_table.id WHERE platId IN (:plates) GROUP BY ingrId " ) //", nombre ")
    List<IngrCant> getNecessaryIngredients(List<Long> plates);

    @Transaction @Query("SELECT id, nombre, unidades, SUM(quantity) AS quantity FROM ingr_plat_table INNER JOIN ingredients_table ON ingrId = ingredients_table.id WHERE platId = :plate GROUP BY ingrId " ) //", nombre ")
    List<IngrCant> getIngredientsByIds(long plate);

    @Transaction @Query("SELECT id, nombre FROM ingr_plat_table INNER JOIN plates_table ON platId = plates_table.id  WHERE ingrId = :ingrId")
    List<Plate> loadPlatesWithIngredient(long ingrId);

}
