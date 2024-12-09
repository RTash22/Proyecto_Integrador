package Modelo;

public class Venta {
    private String id;
    private String idUsuario;
    private String fechaVenta;
    private double total;

    // Constructor
    public Venta(String id, String idUsuario, String fechaVenta, double total) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.fechaVenta = fechaVenta;
        this.total = total;
    }

    // Getters y Setters
    // ...
}

