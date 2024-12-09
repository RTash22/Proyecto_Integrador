package Vistas; 

import javax.swing.*;
import java.awt.*;
import Controlador.ControladorGeneral;
import Controlador.ControladorReportes;
import Controlador.ControladorGestion;

public class Main extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Nombres de las tarjetas
    private static final String VISTA_PRINCIPAL = "Principal";
    private static final String VISTA_GESTION_PRODUCTOS = "GestionProductosVista";
    private static final String VISTA_GESTION_USUARIOS = "GestionUsuariosVista";
    private static final String VISTA_GESTION_VENTAS = "GestionVentasVista";
    private static final String VISTA_GESTION_COMPRAS = "GestionComprasVista";
    private static final String VISTA_REPORTE_VENTAS = "ReporteVentasVista";
    private static final String VISTA_REPORTE_PRODUCTOS = "ReporteProductosVista";

    // Colores de la paleta
    private static final Color BACKGROUND_COLOR = new Color(46, 46, 46); // Gris Oscuro
    private static final Color TEXT_COLOR = new Color(255, 255, 255); // Blanco
    private static final Color ACCENT_COLOR = new Color(127, 255, 212); // Acuamarina

    // Constructor
    public Main(String rol) {
        setTitle("Punto de Venta");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana
        getContentPane().setBackground(BACKGROUND_COLOR); // Fondo oscuro

        // Crear el menú
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(BACKGROUND_COLOR);
        menuBar.setForeground(TEXT_COLOR);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_COLOR));

        JMenu menuGestion = new JMenu("Gestión");
        menuGestion.setForeground(TEXT_COLOR);

        JMenuItem gestionProductosItem = new JMenuItem("Productos");
        JMenuItem gestionUsuariosItem = new JMenuItem("Usuarios");
        JMenuItem gestionVentasItem = new JMenuItem("Ventas");
        JMenuItem gestionComprasItem = new JMenuItem("Compras");
        estilizarMenuItem(gestionProductosItem);
        estilizarMenuItem(gestionUsuariosItem);
        estilizarMenuItem(gestionVentasItem);
        estilizarMenuItem(gestionComprasItem);
        menuGestion.add(gestionProductosItem);
        menuGestion.add(gestionUsuariosItem);
        menuGestion.add(gestionVentasItem);
        menuGestion.add(gestionComprasItem);

        JMenu menuReportes = new JMenu("Reportes");
        menuReportes.setForeground(TEXT_COLOR);
        JMenuItem reporteVentasItem = new JMenuItem("Reporte Ventas");
        JMenuItem reporteProductosItem = new JMenuItem("Reporte Productos");
        estilizarMenuItem(reporteVentasItem);
        estilizarMenuItem(reporteProductosItem);
        menuReportes.add(reporteVentasItem);
        menuReportes.add(reporteProductosItem);

        // Desactivar elementos del menú según el rol
        if ("cajero".equalsIgnoreCase(rol)) {
            gestionProductosItem.setEnabled(false);
            gestionUsuariosItem.setEnabled(false);
            gestionComprasItem.setEnabled(false);
            reporteVentasItem.setEnabled(false);
            reporteProductosItem.setEnabled(false);
        }

        JMenu menuSalir = new JMenu("Salir");
        menuSalir.setForeground(TEXT_COLOR);
        JMenuItem salirItem = new JMenuItem("Salir");
        estilizarMenuItem(salirItem);
        menuSalir.add(salirItem);

        menuBar.add(menuGestion);
        menuBar.add(menuReportes);
        menuBar.add(menuSalir);
        setJMenuBar(menuBar);

        // Crear el panel principal con CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Agregar vistas al panel principal
        JPanel vistaPrincipal = new JPanel();
        vistaPrincipal.setLayout(new GridBagLayout());
        vistaPrincipal.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel bienvenidaLabel = new JLabel("Bienvenido al Sistema de Punto de Venta");
        bienvenidaLabel.setForeground(TEXT_COLOR);
        bienvenidaLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        vistaPrincipal.add(bienvenidaLabel, gbc);

        // Agregar una imagen o logo si es necesario
        // Por ejemplo:
        // JLabel logoLabel = new JLabel(new ImageIcon("ruta/a/logo.png"));
        // gbc.gridy = 1;
        // vistaPrincipal.add(logoLabel, gbc);

        mainPanel.add(vistaPrincipal, VISTA_PRINCIPAL);

        // Las otras vistas se agregarán dinámicamente desde los controladores
        add(mainPanel);

        // Inicializar el controlador general
        new ControladorGeneral(this, gestionProductosItem, gestionUsuariosItem, gestionVentasItem, gestionComprasItem,
                reporteVentasItem, reporteProductosItem, salirItem).iniciarAplicacion();
    }

    /**
     * Aplica estilos a los JMenuItem.
     *
     * @param item El JMenuItem a estilizar.
     */
    private void estilizarMenuItem(JMenuItem item) {
        item.setBackground(BACKGROUND_COLOR);
        item.setForeground(TEXT_COLOR);
        item.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        item.setFocusPainted(false);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // Añadir efecto hover
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(ACCENT_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(BACKGROUND_COLOR);
            }
        });
    }

    /**
     * Cambia la vista mostrada en el panel principal.
     *
     * @param vista El panel de vista a mostrar.
     */
    public void cambiarVista(JPanel vista) {
        String nombreVista = vista.getClass().getSimpleName();
        mainPanel.add(vista, nombreVista);
        cardLayout.show(mainPanel, nombreVista);
    }

    /**
     * Obtiene la vista actual visible en el panel principal.
     *
     * @return El componente de la vista actual.
     */
    public Component getCurrentView() {
        for (Component comp : mainPanel.getComponents()) {
            if (comp.isVisible()) {
                return comp;
            }
        }
        return null;
    }

    // Métodos para obtener los elementos del menú (para ControladorGeneral)
    public JMenuItem getGestionProductosItem() {
        return getMenuItemByName("Productos");
    }

    public JMenuItem getGestionUsuariosItem() {
        return getMenuItemByName("Usuarios");
    }

    public JMenuItem getGestionVentasItem() {
        return getMenuItemByName("Ventas");
    }

    public JMenuItem getGestionComprasItem() {
        return getMenuItemByName("Compras");
    }

    public JMenuItem getReporteVentasItem() {
        return getMenuItemByName("Reporte Ventas");
    }

    public JMenuItem getReporteProductosItem() {
        return getMenuItemByName("Reporte Productos");
    }

    public JMenuItem getSalirItem() {
        return getMenuItemByName("Salir");
    }

    /**
     * Busca un JMenuItem por su nombre.
     *
     * @param name El nombre del JMenuItem.
     * @return El JMenuItem si lo encuentra, null en caso contrario.
     */
    private JMenuItem getMenuItemByName(String name) {
        JMenuBar menuBar = getJMenuBar();
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            for (int j = 0; j < menu.getItemCount(); j++) {
                JMenuItem item = menu.getItem(j);
                if (item != null && item.getText().equals(name)) {
                    return item;
                }
            }
        }
        return null;
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args, String rol) {
        SwingUtilities.invokeLater(() -> {
            new Main(rol).setVisible(true);
        });
    }
}
