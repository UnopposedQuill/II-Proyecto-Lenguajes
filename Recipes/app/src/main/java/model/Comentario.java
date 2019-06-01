package model;

import java.util.Date;

public class Comentario {
    private final Date fechaPublicacion;
    private final Usuario autorDelComentario;
    private final String mensaje;

    public Comentario(Date fechaPublicacion, String mensaje, Usuario autorDelComentario) {
        this.fechaPublicacion = fechaPublicacion;
        this.mensaje = mensaje;
        this.autorDelComentario = autorDelComentario;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public Usuario getAutorDelComentario() {
        return autorDelComentario;
    }

    public String getMensaje() {
        return mensaje;
    }
}
