package com.example.textrecognition2.domain;

import android.arch.persistence.room.Ignore;

public class IngrCant {
    private long id;
    private String nombre;
    private String unidades;
    private int quantity;

    public IngrCant(long id, String nombre, String unidades, int quantity) {
        this.id = id;
        this.nombre = nombre;
        this.unidades = unidades;
        this.quantity = quantity;
    }

    @Ignore
    public IngrCant(String nombre, String unidades, int quantity) {
        this.nombre = nombre;
        this.unidades = unidades;
        this.quantity = quantity;
    }

    @Ignore
    public IngrCant(String nombre, int quantity, String unidades) {
        this.nombre = nombre;
        this.unidades = unidades;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public String getNombre() { return nombre; }

    public String getUnidades() {
        return unidades;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    @Override
    public String toString() {
        return nombre + ' ' + quantity + ' ' + unidades ;
    }
}
