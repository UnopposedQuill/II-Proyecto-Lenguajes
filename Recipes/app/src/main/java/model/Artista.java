package model;

import java.util.ArrayList;

public class Artista extends Usuario {

    private ArrayList<Evento> eventosPropios;
    private ArrayList<Articulo> articulosPropios;
    private ArrayList<Disco> discosPropios;
    //private ArrayList<NoticiaArtista> noticiasPropias;
    private ArrayList<Calificacion> calificacionesPropias;

    public Artista(String email, String contrasenha, String nombre) {
        super(email, contrasenha, nombre, TipoUsuario.ARTISTA);
        this.eventosPropios = new ArrayList<>();
        this.articulosPropios = new ArrayList<>();
        this.discosPropios = new ArrayList<>();
        //this.noticiasPropias = new ArrayList<>();
        this.calificacionesPropias = new ArrayList<>();
    }

    public ArrayList<Evento> getEventosPropios() {
        return eventosPropios;
    }

    public void setEventosPropios(ArrayList<Evento> eventosPropios) {
        this.eventosPropios = eventosPropios;
    }

    public ArrayList<Articulo> getArticulosPropios() {
        return articulosPropios;
    }

    public void setArticulosPropios(ArrayList<Articulo> articulosPropios) {
        this.articulosPropios = articulosPropios;
    }

    public ArrayList<Disco> getDiscosPropios() {
        return discosPropios;
    }

    public void setDiscosPropios(ArrayList<Disco> discosPropios) {
        this.discosPropios = discosPropios;
    }

    public ArrayList<Calificacion> getCalificacionesPropias() {
        return calificacionesPropias;
    }

    public void setCalificacionesPropias(ArrayList<Calificacion> calificacionesPropias) {
        this.calificacionesPropias = calificacionesPropias;
    }

    /*
    public ArrayList<NoticiaArtista> getNoticiasPropias() {
        return noticiasPropias;
    }

    public void setNoticiasPropias(ArrayList<NoticiaArtista> noticiasPropias) {
        this.noticiasPropias = noticiasPropias;
    }
    */
}
