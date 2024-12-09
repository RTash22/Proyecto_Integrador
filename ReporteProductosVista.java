package Vistas.Reportes;

import javax.swing.*;
import java.awt.*;

public class ReporteProductosVista extends JPanel {
    private JButton generarReporteButton;
    private JTextArea reporteArea;

    // Colores de la paleta
    private static final Color BACKGROUND_COLOR = new Color(46, 46, 46); // Gris Oscuro
    private static final Color TEXT_COLOR = new Color(255, 255, 255); // Blanco
    private static final Color ACCENT_COLOR = new Color(127, 255, 212); // Acuamarina

    public ReporteProductosVista() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR); // Fondo oscuro

        // Panel superior con botón para generar reporte
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSuperior.setBackground(BACKGROUND_COLOR);

        generarReporteButton = new JButton("Generar Reporte Productos");
        generarReporteButton.setBackground(ACCENT_COLOR);
        generarReporteButton.setForeground(TEXT_COLOR);
        generarReporteButton.setFocusPainted(false);
        generarReporteButton.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
        generarReporteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        estilizarBoton(generarReporteButton);
        panelSuperior.add(generarReporteButton);
        add(panelSuperior, BorderLayout.NORTH);

        // Área de texto para mostrar el reporte
        reporteArea = new JTextArea();
        reporteArea.setEditable(false);
        reporteArea.setBackground(new Color(60, 63, 65)); // Gris Medio
        reporteArea.setForeground(TEXT_COLOR);
        reporteArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reporteArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollReporte = new JScrollPane(reporteArea);
        scrollReporte.setBackground(BACKGROUND_COLOR);
        add(scrollReporte, BorderLayout.CENTER);
    }

    /**
     * Aplica estilos adicionales al botón para efectos de hover.
     *
     * @param button El botón a estilizar.
     */
    private void estilizarBoton(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 226, 201)); // Hover color
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
        });
    }

    // Getters
    public JButton getGenerarReporteButton() {
        return generarReporteButton;
    }

    public JTextArea getReporteArea() {
        return reporteArea;
    }

    // Método para mostrar el reporte en el área de texto
    public void mostrarReporte(String reporte) {
        reporteArea.setText(reporte);
    }
}
