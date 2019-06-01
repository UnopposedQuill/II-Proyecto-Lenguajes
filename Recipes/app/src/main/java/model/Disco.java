package model;

import java.util.Date;

public class Disco {
    private String nombreDisco;
    private Date fechaLanzamiento;

    public Disco(String nombreDisco, Date fechaLanzamiento) {
        this.nombreDisco = nombreDisco;
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public String getNombreDisco() {
        return nombreDisco;
    }

    public void setNombreDisco(String nombreDisco) {
        this.nombreDisco = nombreDisco;
    }

    public Date getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(Date fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }
}
