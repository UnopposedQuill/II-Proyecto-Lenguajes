package model;

public class Administrador extends Usuario {


    public Administrador(String email, String contrasenha, String nombre) {
        super(email, contrasenha, nombre, TipoUsuario.ADMINISTRADOR);
    }

}
