package model;

import android.net.Uri;

import java.util.ArrayList;

public class Receta {

    private String nombre;
    private String tipo;
    private ArrayList<String> ingredientes;
    private ArrayList<String> instrucciones;
    private ArrayList<Uri> imagenes;

    public Receta(String nombre, String tipo, ArrayList<String> ingredientes, ArrayList<String> instrucciones, ArrayList<Uri> imagenes) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ingredientes = ingredientes;
        this.instrucciones = instrucciones;
        this.imagenes = imagenes;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public ArrayList<String> getIngredientes() {
        return ingredientes;
    }

    public ArrayList<String> getInstrucciones() {
        return instrucciones;
    }

    public ArrayList<Uri> getImagenes() {
        return imagenes;
    }
}
