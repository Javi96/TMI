package com.example.textrecognition2.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;


import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "ingr_plat_table",
        foreignKeys = {@ForeignKey(entity = Ingredient.class, parentColumns = "id", childColumns = "ing_id", onDelete = CASCADE),
                @ForeignKey(entity = Plate.class, parentColumns = "id", childColumns = "plat_id", onDelete = CASCADE)},
        primaryKeys = {"ing_", "plat_"})
public class PlatoIngredientes {


    @NonNull
    @Embedded(prefix = "ing_")
    private Ingredient ing;

    @NonNull
    @Embedded(prefix = "plat_")
    private Ingredient plat;

    @NonNull
    @ColumnInfo(name = "quantity")
    private int quantity;

    public PlatoIngredientes(@NonNull Ingredient ing, @NonNull Plate pla, @NonNull int quantity) {
        this.ing = ing;
        this.plat = plat;
        this.quantity = quantity;
    }

}
