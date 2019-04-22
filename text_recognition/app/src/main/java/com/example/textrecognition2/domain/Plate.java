package com.example.textrecognition2.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;

@Entity(tableName = "plates_table",
        indices = {@Index(value = "nombre", unique = true)})
public class Plate {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "nombre")
    private String name;



    public Plate( @NonNull String name) {
        this.name = name;
        this.ingredients = new ArrayList<String>();
    }

    public Plate( @NonNull String name, ArrayList<String> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public int getId(){ return this.id; }
    public String getName() {
        return name;
    }

    public void setId(int id){ this.id = id; }

    @Ignore
    private ArrayList<String> ingredients;

    @Ignore
    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Ignore
    public ArrayList<String> getIngredients() {
        return ingredients;
    }



    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.name);
        if(this.ingredients != null)
            for (String ingredient: ingredients)
                stringBuilder.append('_' + ingredient );

        return stringBuilder.toString();
    }
}
