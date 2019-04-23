package com.example.textrecognition2.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "ingredients_table",
        indices = {@Index(value = "nombre", unique = true)})
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @NonNull

    @ColumnInfo(name = "nombre")
    private String name;

    @NonNull
    @ColumnInfo(name = "unidades")
    private String units;

    public Ingredient(/*@NonNull long id,*/ @NonNull String name, @NonNull String units) {
        //this.id = id;
        this.name = name;
        this.units = units;
    }

    public long getId(){ return this.id; }
    public String getName() { return this.name; }
    public String getUnits() { return this.units; }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setUnits(String units) { this.units = units; }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name +
                '}';
    }
}
