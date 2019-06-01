package model;

public class Articulo {

    private String nombre;
    private float valor;
    private int cantidadEnExistencia;

    public Articulo(String nombre, float valor, int cantidadEnExistencia) {
        this.nombre = nombre;
        this.valor = valor;
        this.cantidadEnExistencia = cantidadEnExistencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public int getCantidadEnExistencia() {
        return cantidadEnExistencia;
    }

    public void setCantidadEnExistencia(int cantidadEnExistencia) {
        this.cantidadEnExistencia = cantidadEnExistencia;
    }
}
