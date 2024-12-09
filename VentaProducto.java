package Modelo;

public class VentaProducto {
    private String id;
    private String idVenta;
    private String idProducto;
    private int cantidad;
    private double precio;

    // Constructor
    public VentaProducto(String id, String idVenta, String idProducto, int cantidad, double precio) {
        this.id = id;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    // Getters y Setters
    // ...
}
