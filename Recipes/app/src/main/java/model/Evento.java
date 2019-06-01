package model;

import java.util.Date;

public class Evento {

    private Date fecha;
    private String sitio;
    private String descripcion;

    public Evento(Date fecha, String sitio, String descripcion) {
        this.fecha = fecha;
        this.sitio = sitio;
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getSitio() {
        return sitio;
    }

    public void setSitio(String sitio) {
        this.sitio = sitio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
