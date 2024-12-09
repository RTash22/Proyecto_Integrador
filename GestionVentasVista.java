package Vistas.Gestion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GestionVentasVista extends JPanel {
    private JTable tablaCarrito;
    private JButton agregarButton;
    private JButton buscarButton;
    private JTextField buscarField;
    private JTable tablaProductos;
    private JButton okButton;
    private JButton verDetalleButton;
    private JTextField cantidadField;

    // Colores de la paleta
    private static final Color BACKGROUND_COLOR = new Color(46, 46, 46); // Gris Oscuro
    private static final Color TEXT_COLOR = new Color(255, 255, 255); // Blanco
    private static final Color ACCENT_COLOR = new Color(127, 255, 212); // Acuamarina
    private static final Color BUTTON_HOVER_COLOR = new Color(100, 226, 201); // Hover Acuamarina

    public GestionVentasVista() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR); // Fondo oscuro

        // Panel para Productos (antes estaba a la derecha, ahora estará a la izquierda)
        JPanel panelProductos = new JPanel(new BorderLayout());
        panelProductos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT_COLOR), "Productos"));
        estilizarTitledBorder(panelProductos);
        panelProductos.setBackground(BACKGROUND_COLOR);

        // Panel de Búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusqueda.setBackground(BACKGROUND_COLOR);

        buscarField = new JTextField(20);
        buscarField.setBackground(new Color(60, 63, 65)); // Gris Medio
        buscarField.setForeground(TEXT_COLOR);
        buscarField.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));

        buscarButton = new JButton("Buscar");
        buscarButton.setBackground(ACCENT_COLOR);
        buscarButton.setForeground(TEXT_COLOR);
        buscarButton.setFocusPainted(false);
        buscarButton.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
        buscarButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        estilizarBoton(buscarButton);

        JLabel buscarLabel = new JLabel("Buscar:");
        buscarLabel.setForeground(TEXT_COLOR);

        panelBusqueda.add(buscarLabel);
        panelBusqueda.add(buscarField);
        panelBusqueda.add(buscarButton);
        panelProductos.add(panelBusqueda, BorderLayout.NORTH);

        // Tabla de Productos
        tablaProductos = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Precio", "Categoría", "Stock"},
                0
        ));
        estilizarTabla(tablaProductos);
        JScrollPane scrollProductos = new JScrollPane(tablaProductos);
        panelProductos.add(scrollProductos, BorderLayout.CENTER);

        // Panel Botón Agregar
        JPanel panelBotonAgregar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotonAgregar.setBackground(BACKGROUND_COLOR);
        agregarButton = new JButton("+ Agregar");
        agregarButton.setBackground(ACCENT_COLOR);
        agregarButton.setForeground(TEXT_COLOR);
        agregarButton.setFocusPainted(false);
        agregarButton.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
        agregarButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        estilizarBoton(agregarButton);
        agregarButton.setPreferredSize(new Dimension(120, 35));
        panelBotonAgregar.add(agregarButton);
        panelProductos.add(panelBotonAgregar, BorderLayout.SOUTH);

        // Panel para Carrito de Compras (antes estaba a la izquierda, ahora estará a la derecha)
        JPanel panelCarrito = new JPanel(new BorderLayout());
        panelCarrito.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT_COLOR), "Carrito de Compras"));
        estilizarTitledBorder(panelCarrito);
        panelCarrito.setBackground(BACKGROUND_COLOR);
        panelCarrito.setPreferredSize(new Dimension(450, 0));

        tablaCarrito = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Cantidad", "Precio", "Subtotal"},
                0
        ));
        estilizarTabla(tablaCarrito);
        JScrollPane scrollCarrito = new JScrollPane(tablaCarrito);
        panelCarrito.add(scrollCarrito, BorderLayout.CENTER);

        JPanel panelCantidad = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelCantidad.setBackground(BACKGROUND_COLOR);
        JLabel cantidadLabel = new JLabel("Cantidad:");
        cantidadLabel.setForeground(TEXT_COLOR);
        cantidadField = new JTextField(5);
        cantidadField.setBackground(new Color(60, 63, 65)); // Gris Medio
        cantidadField.setForeground(TEXT_COLOR);
        cantidadField.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
        panelCantidad.add(cantidadLabel);
        panelCantidad.add(cantidadField);
        panelCarrito.add(panelCantidad, BorderLayout.SOUTH);

        // JSplitPane invertido: ahora los Productos van a la izquierda y el Carrito a la derecha
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelProductos, panelCarrito);
        splitPane.setDividerLocation(550); // Ajuste opcional del divisor
        splitPane.setDividerSize(5);
        splitPane.setBackground(BACKGROUND_COLOR);
        add(splitPane, BorderLayout.CENTER);

        // Panel Inferior: Botones Ok y Ver Detalle
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        panelInferior.setBackground(BACKGROUND_COLOR);
        verDetalleButton = new JButton("Ver Detalle");
        verDetalleButton.setBackground(ACCENT_COLOR);
        verDetalleButton.setForeground(TEXT_COLOR);
        verDetalleButton.setFocusPainted(false);
        verDetalleButton.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
        verDetalleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        estilizarBoton(verDetalleButton);
        verDetalleButton.setPreferredSize(new Dimension(130, 40));

        okButton = new JButton("Confirmar Venta");
        okButton.setBackground(ACCENT_COLOR);
        okButton.setForeground(TEXT_COLOR);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
        okButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        estilizarBoton(okButton);
        okButton.setPreferredSize(new Dimension(150, 40));

        panelInferior.add(verDetalleButton);
        panelInferior.add(okButton);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void estilizarBoton(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
        });
    }

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

    private void estilizarTitledBorder(JPanel panel) {
        if (panel.getBorder() instanceof javax.swing.border.TitledBorder) {
            javax.swing.border.TitledBorder border = (javax.swing.border.TitledBorder) panel.getBorder();
            border.setTitleColor(ACCENT_COLOR);
            border.setTitleFont(new Font("Segoe UI", Font.BOLD, 16));
        }
    }

    // Getters
    public JTable getTablaCarrito() {
        return tablaCarrito;
    }

    public JButton getAgregarButton() {
        return agregarButton;
    }

    public JButton getBuscarButton() {
        return buscarButton;
    }

    public JTextField getBuscarField() {
        return buscarField;
    }

    public JTable getTablaProductos() {
        return tablaProductos;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getVerDetalleButton() {
        return verDetalleButton;
    }

    public JTextField getCantidadField() {
        return cantidadField;
    }

    public void agregarProductoAlCarrito(Object[] fila) {
        DefaultTableModel model = (DefaultTableModel) tablaCarrito.getModel();
        model.addRow(fila);
    }

    public void limpiarCarrito() {
        DefaultTableModel model = (DefaultTableModel) tablaCarrito.getModel();
        model.setRowCount(0);
    }

    public void cargarProductosEnTabla(JTable tabla, ArrayList<String[]> productos) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0); // Limpiar la tabla
        for (String[] producto : productos) {
            model.addRow(producto);
        }
    }
}
