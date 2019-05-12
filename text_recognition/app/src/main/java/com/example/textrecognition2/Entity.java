package com.example.textrecognition2;

import java.util.ArrayList;

/**
 * <h1>Clase que aqyuda a procesar la respuesta HTTP de Google Cloud</h1>
 * Hace de objeto DAO de cara a la aplciacion
 */
public class Entity {

    /**
     * Atributos privados
     */
    private String name;
    private ArrayList<String> types;
    private ArrayList<String> categories;

    /**
     * Constructor de clase
     * @param name Nombre del objeto
     * @param types Lista de tipos permitidos
     * @param categories Lista de categorias necesarias
     */
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
