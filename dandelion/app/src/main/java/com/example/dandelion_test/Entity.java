package com.example.dandelion_test;

import java.util.ArrayList;

public class Entity {
    private String name;
    private ArrayList<String> types;
    private ArrayList<String> categories;

    public Entity(String name, ArrayList<String> types, ArrayList<String> categories) {
        this.name = name;
        this.types = types;
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getType() {
        return types;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        String entityString = "";
        entityString = entityString + "Entity: " + this.name + "\n";

        entityString = entityString + "Types: " ;
        for (String s : this.types)
            entityString = entityString + s + ". ";
        entityString = entityString + "\n";

        entityString = entityString + "Categories: " ;
        for (String s : this.categories)
            entityString = entityString + s + ". ";
        entityString = entityString + "\n";

        return entityString;
    }
}
