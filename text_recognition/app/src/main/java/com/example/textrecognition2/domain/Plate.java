package com.example.textrecognition2.domain;

import java.util.ArrayList;

public class Plate {

    private String name;

    private ArrayList<String> ingredients;

    public Plate(String name, ArrayList<String> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Plate{" +
                "name='" + name + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }
}
