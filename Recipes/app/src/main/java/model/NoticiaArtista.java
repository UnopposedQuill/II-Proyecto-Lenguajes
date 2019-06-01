package model;

import java.util.Date;

public class NoticiaArtista extends Noticia {

    private Artista artistaPublicador;

    public NoticiaArtista(String abstractNoticia, String descripcion, Date fechaPublicacion, Artista artistaPublicador) {
        super(abstractNoticia, descripcion, fechaPublicacion, TipoNoticia.ARTISTA);
        this.artistaPublicador = artistaPublicador;
    }

    public Artista getArtistaPublicador() {
        return artistaPublicador;
    }

    public void setArtistaPublicador(Artista artistaPublicador) {
        this.artistaPublicador = artistaPublicador;
    }
}
