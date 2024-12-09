package Modelo;

public class Producto {
    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private String fechaCreacion;
    private String fechaActualizacion;
    private String ultimaCompra;

    // Constructor
    public Producto(String id, String nombre, String descripcion, double precio, int stock, String fechaCreacion, String fechaActualizacion, String ultimaCompra) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.ultimaCompra = ultimaCompra;
    }

    // Getters y Setters
    // ...
}
