package Modelo;

public class Usuario {
    private String id;
    private String nombreUsuario;
    private String contrasena;
    private String rol; // "cajero" o "administrador"
    private String nombreCompleto;
    private String fechaCreacion;
    private String estado; // "activo" o "inactivo"

    // Constructor
    public Usuario(String id, String nombreUsuario, String contrasena, String rol, String nombreCompleto, String fechaCreacion, String estado) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.nombreCompleto = nombreCompleto;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    // Getters y Setters
    // ...
}
