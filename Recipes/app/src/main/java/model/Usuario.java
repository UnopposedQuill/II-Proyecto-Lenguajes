package model;

public class Usuario {

    private String nombre;
    private String email;
    private String contrasenha;


    public Usuario(String email, String contrasenha, String nombre) {
        this.email = email;
        this.contrasenha = contrasenha;
        this.nombre = nombre;
    }

    public String getContrasenha() {
        return contrasenha;
    }

    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
