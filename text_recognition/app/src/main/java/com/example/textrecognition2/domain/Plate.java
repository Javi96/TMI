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
    private long id;

    @NonNull
    @ColumnInfo(name = "nombre")
    private String name;

    @Ignore
    private ArrayList<IngrCant> ingredients;

    public Plate( @NonNull String name) {
        this.name = name;
        this.ingredients = new ArrayList<IngrCant>();
    }

    public Plate( @NonNull String name, ArrayList<IngrCant> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public long getId(){ return this.id; }
    public String getName() {
        return name;
    }

    public void setId(long id){ this.id = id; }
    public void setName(@NonNull String name) {
        this.name = name;
    }

    @Ignore
    public void setIngredients(ArrayList<IngrCant> ingredients) {
        this.ingredients = ingredients;
    }

    @Ignore
    public ArrayList<IngrCant> getIngredients() {
        return ingredients;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.name);
        if( this.ingredients != null && this.ingredients.size() > 0 )
            for (IngrCant ingredient : ingredients) {
                sb.append('\n');
                sb.append(ingredient.getNombre());
                sb.append('_' + ingredient.getQuantity());
                sb.append('_' + ingredient.getUnidades());
            }


        return sb.toString();
    }
}
