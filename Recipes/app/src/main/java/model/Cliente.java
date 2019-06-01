package model;

import java.util.ArrayList;

public class Cliente extends Usuario {

    private ArrayList<Artista> artistasFavoritos;
    //private ArrayList<NoticiaAdministrador> noticias;//removida por reducción


    public Cliente(String email, String contrasenha, String nombre) {
        super(email, contrasenha, nombre, TipoUsuario.CLIENTE);
        this.artistasFavoritos = new ArrayList<>();
    }

    public ArrayList<Artista> getArtistasFavoritos() {
        return artistasFavoritos;
    }

    public void setArtistasFavoritos(ArrayList<Artista> artistasFavoritos) {
        this.artistasFavoritos = artistasFavoritos;
    }
}
