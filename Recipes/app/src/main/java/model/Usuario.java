package model;

public abstract class Usuario {

    private String nombre;
    private String email;
    private String contrasenha;
    private TipoUsuario tipoUsuario;


    public Usuario(String nombreUsuario, String contrasenha, String nombre, TipoUsuario tipoUsuario) {
        this.email = nombreUsuario;
        this.contrasenha = contrasenha;
        this.nombre = nombre;
        this.tipoUsuario = tipoUsuario;
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

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
