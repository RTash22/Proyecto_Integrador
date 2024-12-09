package Vistas.Gestion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionUsuariosVista extends JPanel {
    private JTable tablaUsuarios;
    private JButton agregarButton;
    private JButton editarButton;
    private JButton eliminarButton;

    // Colores de la paleta
    private static final Color BACKGROUND_COLOR = new Color(46, 46, 46); // Gris Oscuro
    private static final Color TEXT_COLOR = new Color(255, 255, 255); // Blanco
    private static final Color ACCENT_COLOR = new Color(127, 255, 212); // Acuamarina
    private static final Color BUTTON_HOVER_COLOR = new Color(100, 226, 201); // Hover Acuamarina

    public GestionUsuariosVista() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR); // Fondo oscuro

        // Panel Superior: Título
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panelSuperior.setBackground(BACKGROUND_COLOR);
        JLabel tituloLabel = new JLabel("Gestión de Usuarios");
        tituloLabel.setForeground(ACCENT_COLOR);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        panelSuperior.add(tituloLabel);
        add(panelSuperior, BorderLayout.NORTH);

        // Inicializar la tabla de usuarios
        tablaUsuarios = new JTable(new DefaultTableModel(
                new Object[]{"ID Usuario", "Nombre Usuario", "Contraseña", "Rol", "Fecha Creación", "Estado"},
                0
        ));
        estilizarTabla(tablaUsuarios);
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        add(scrollPane, BorderLayout.CENTER);

        // Panel Inferior: Botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.setBackground(BACKGROUND_COLOR);

        agregarButton = new JButton("Agregar");
        editarButton = new JButton("Editar");
        eliminarButton = new JButton("Eliminar");

        estilizarBoton(agregarButton);
        estilizarBoton(editarButton);
        estilizarBoton(eliminarButton);

        panelBotones.add(agregarButton);
        panelBotones.add(editarButton);
        panelBotones.add(eliminarButton);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Aplica estilos adicionales al botón para efectos de hover.
     *
     * @param button El botón a estilizar.
     */
    private void estilizarBoton(JButton button) {
        button.setBackground(ACCENT_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
        });
    }

    /**
     * Estiliza las tablas para que se adapten a la paleta oscura.
     *
     * @param table La JTable a estilizar.
     */
    private void estilizarTabla(JTable table) {
        table.setBackground(new Color(60, 63, 65)); // Gris Medio
        table.setForeground(TEXT_COLOR); // Blanco
        table.setGridColor(ACCENT_COLOR);
        table.setSelectionBackground(new Color(75, 110, 175)); // Azul Oscuro para selección
        table.setSelectionForeground(TEXT_COLOR);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setBackground(new Color(30, 30, 30)); // Más oscuro
        table.getTableHeader().setForeground(ACCENT_COLOR);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    // Getters para los botones y la tabla
    public JButton getAgregarButton() {
        return agregarButton;
    }

    public JButton getEditarButton() {
        return editarButton;
    }

    public JButton getEliminarButton() {
        return eliminarButton;
    }

    public JTable getTablaUsuarios() {
        return tablaUsuarios;
    }

    // Método para agregar una fila a la tabla
    public void agregarFila(Object[] fila) {
        DefaultTableModel model = (DefaultTableModel) tablaUsuarios.getModel();
        model.addRow(fila);
    }

    // Método para limpiar la tabla
    public void limpiarTabla() {
        DefaultTableModel model = (DefaultTableModel) tablaUsuarios.getModel();
        model.setRowCount(0);
    }
}
