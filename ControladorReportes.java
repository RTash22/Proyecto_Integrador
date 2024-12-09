package Controlador;

import Vistas.Main;
import Vistas.Reportes.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ControladorReportes {
    private Main mainView;

    /**
     * Constructor de ControladorReportes.
     *
     * @param mainView La ventana principal de la aplicación.
     */
    public ControladorReportes(Main mainView) {
        this.mainView = mainView;
    }

    /**
     * Muestra la vista de reporte de ventas.
     */
    public void mostrarReporteVentas() {
        ReporteVentasVista vista = new ReporteVentasVista();
        mainView.cambiarVista(vista);

        // Configuración de eventos
        vista.getGenerarReporteButton().addActionListener(e -> generarReporteVentas(vista));
    }

    /**
     * Muestra la vista de reporte de productos.
     */
    public void mostrarReporteProductos() {
        ReporteProductosVista vista = new ReporteProductosVista();
        mainView.cambiarVista(vista);

        // Configuración de eventos
        vista.getGenerarReporteButton().addActionListener(e -> generarReporteProductos(vista));
    }

    private void generarReporteVentas(ReporteVentasVista vista) {
        ArrayList<String[]> ventas = ControladorGeneral.consultar("ventas", null, null);
        if (ventas.isEmpty()) {
            JOptionPane.showMessageDialog(mainView, "No hay ventas registradas.");
            return;
        }

        // Crear una cadena con los detalles de las ventas
        StringBuilder reporte = new StringBuilder();
        reporte.append("Reporte de Ventas\n\n");
        reporte.append(String.format("%-15s %-30s %-20s\n", "ID Venta", "Fecha Venta",  "Total"));
        reporte.append("--------------------------------------------------------------------------\n");
        for (String[] venta : ventas) {
            reporte.append(String.format("%-15s %-30s %-20s \n", venta[0], venta[1], venta[2]));
        }

        // Mostrar el reporte en el área de texto
        vista.mostrarReporte(reporte.toString());
    }

    /**
     * Genera y muestra el reporte de productos en la vista correspondiente.
     *
     * @param vista La vista de reporte de productos.
     */
    private void generarReporteProductos(ReporteProductosVista vista) {
        ArrayList<String[]> productos = ControladorGeneral.consultar("productos", null, null);
        if (productos.isEmpty()) {
            JOptionPane.showMessageDialog(mainView, "No hay productos registrados.");
            return;
        }

        // Crear una cadena con los detalles de los productos
        StringBuilder reporte = new StringBuilder();
        reporte.append("Reporte de Productos\n\n");
        reporte.append(String.format("%-10s %-25s %-10s %-10s %-15s\n", "ID", "Nombre", "Precio", "Stock", "Categoría"));
        reporte.append("--------------------------------------------------------------------------\n");
        for (String[] producto : productos) {
            reporte.append(String.format("%-10s %-25s %-10s %-10s %-15s\n", producto[0], producto[1], producto[2], producto[3], producto[4]));
        }

        // Mostrar el reporte en el área de texto
        vista.mostrarReporte(reporte.toString());
    }
}
