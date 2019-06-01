package model;

import java.util.Date;

public class NoticiaAdministrador extends Noticia {

    private Administrador administradorPublicador;

    public NoticiaAdministrador(String abstractNoticia, String descripcion, Date fechaPublicacion, Administrador administradorPublicador) {
        super(abstractNoticia, descripcion, fechaPublicacion, TipoNoticia.ADMINISTRADOR);
        this.administradorPublicador = administradorPublicador;
    }

    public Administrador getAdministradorPublicador() {
        return administradorPublicador;
    }

    public void setAdministradorPublicador(Administrador administradorPublicador) {
        this.administradorPublicador = administradorPublicador;
    }
}
