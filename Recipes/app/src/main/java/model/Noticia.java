package model;

import java.util.Date;

public abstract class Noticia {

    private String abstractNoticia;
    private String descripcion;
    private Date fechaPublicacion;
    private TipoNoticia tipoNoticia;

    public Noticia(String abstractNoticia, String descripcion, Date fechaPublicacion, TipoNoticia tipoNoticia) {
        this.abstractNoticia = abstractNoticia;
        this.descripcion = descripcion;
        this.fechaPublicacion = fechaPublicacion;
        this.tipoNoticia = tipoNoticia;
    }

    public String getAbstractNoticia() {
        return abstractNoticia;
    }

    public void setAbstractNoticia(String abstractNoticia) {
        this.abstractNoticia = abstractNoticia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public TipoNoticia getTipoNoticia() {
        return tipoNoticia;
    }

    public void setTipoNoticia(TipoNoticia tipoNoticia) {
        this.tipoNoticia = tipoNoticia;
    }
}
