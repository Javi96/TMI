package com.example.textrecognition2.domain;

public class IngrCant {
    private int id;
    private String nombre;
    private String unidades;
    private int quantity;

    public IngrCant(int id, String nombre, String unidades, int quantity) {
        this.id = id;
        this.nombre = nombre;
        this.unidades = unidades;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getNombre() { return nombre; }

    public String getUnidades() {
        return unidades;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
