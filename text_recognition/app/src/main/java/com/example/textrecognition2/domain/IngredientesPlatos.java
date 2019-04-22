package com.example.textrecognition2.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "ingr_plat_table",
        indices = {@Index( value = {"platId", "ingrId"}, unique = true)},
        foreignKeys = {@ForeignKey(entity = Ingredient.class, parentColumns = "id", childColumns = "ingrId", onDelete = CASCADE),
                        @ForeignKey(entity = Plate.class, parentColumns = "id", childColumns = "platId", onDelete = CASCADE)},
        primaryKeys = {"ingrId", "platId"})
public class IngredientesPlatos {


    @NonNull
    @ColumnInfo(name = "ingrId")
    private int ingrId;

    @NonNull
    @ColumnInfo(name = "platId")
    private int platId;

    @NonNull
    @ColumnInfo(name = "quantity")
    private int quantity;

    public IngredientesPlatos(@NonNull int ingrId, @NonNull int platId, @NonNull int quantity) {
        this.ingrId = ingrId;
        this.platId = platId;
        this.quantity = quantity;
    }

    public void setIngrId(int ingrId) {
        this.ingrId = ingrId;
    }

    public void setPlatId(int platId) {
        this.platId = platId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getIngrId() {
        return ingrId;
    }

    public int getPlatId() {
        return platId;
    }

    public int getQuantity() {
        return quantity;
    }
}
