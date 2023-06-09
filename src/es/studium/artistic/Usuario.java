package es.studium.artistic;

public class Usuario {
    private int idUsuario;
    private  int tipoUsuario;
    private String nombre;
    private String email;

    public Usuario(){
        idUsuario = 0;
        tipoUsuario = 0;
        nombre = "";
        email = "";
    }


    public Usuario(String u, String e, int id, int t){
        idUsuario = id;
        tipoUsuario = t;
        nombre = u;
        email = e;
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
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

}
