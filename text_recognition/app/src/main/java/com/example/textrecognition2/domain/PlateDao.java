package com.example.textrecognition2.domain;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

@Dao
public interface PlateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Plate plate);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Plate... plates);

    @Delete
    int delete(Plate plate);

    @Query("DELETE FROM plates_table")
    int deleteAll();

    @Transaction @Query("UPDATE plates_table SET nombre = :name WHERE id = :id")
    long update(long id, String name);

    @Query("SELECT id FROM plates_table WHERE nombre LIKE :name ")
    long getIdByName(String name);

    @Transaction @Query("SELECT * FROM plates_table")
    List<Plate> getAll();

    @Transaction @Query("SELECT * FROM plates_table WHERE id IN (:platIds)")
    List<Plate> loadAllByIds(long[] platIds);

    @Query("SELECT * FROM plates_table WHERE nombre LIKE :name ")
    Plate findByName(String name);

    @Transaction @Query("SELECT * FROM plates_table WHERE nombre IN (:names) ")
    List<Plate> findByName(List<String> names);

    @Transaction @Query("SELECT id FROM plates_table WHERE nombre IN (:names) ")
    long[] findIdsByName(List<String> names);

}
