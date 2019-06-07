package model;

import android.net.Uri;

import java.net.URI;
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

    public String ingredientesToString(){
        StringBuilder result = new StringBuilder();
        for(String ingrediente:this.ingredientes){
            result.append(ingrediente).append(", ");
        }
        return result.toString().replace(", ", "");
    }

    public String instructionsToString(){
        StringBuilder result = new StringBuilder("[");
        for(String instruction:this.instrucciones){
            result.append(instruction).append(", ");
        }
        return result.toString().replace(", ", "").concat("]");
    }

    public String imagenesToString(){
        StringBuilder result = new StringBuilder();
        for(Uri imagen:this.imagenes){
            result.append(imagen.toString()).append(", ");
        }
        return result.toString().replace(", ", "");
    }
}
