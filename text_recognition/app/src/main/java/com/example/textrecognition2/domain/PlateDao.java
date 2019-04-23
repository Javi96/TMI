package com.example.textrecognition2.domain;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PlateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Plate plate);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Plate... plates);

    @Delete
    void delete(Plate plate);

    @Query("DELETE FROM plates_table")
    void deleteAll();

    @Query("SELECT id FROM plates_table WHERE nombre LIKE :name LIMIT 1")
    long getIdByName(String name);

    @Query("SELECT * FROM plates_table")
    List<Plate> getAll();

    @Query("SELECT * FROM plates_table WHERE id IN (:platIds)")
    List<Plate> loadAllByIds(long[] platIds);

    @Query("SELECT * FROM plates_table WHERE nombre LIKE :name LIMIT 1")
    Plate findByName(String name);

}