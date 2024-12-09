package Modelo;

public class Compra {
    private String id;
    private String idUsuario;
    private String fechaCompra;
    private String proveedor;
    private double total;

    // Constructor
    public Compra(String id, String idUsuario, String fechaCompra, String proveedor, double total) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.fechaCompra = fechaCompra;
        this.proveedor = proveedor;
        this.total = total;
    }

    // Getters y Setters
    // ...
}
