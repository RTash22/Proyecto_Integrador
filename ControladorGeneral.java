package Controlador;

import Vistas.Main;
import Vistas.Gestion.*;
import Vistas.Reportes.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import Modelo.ConexionBD;
import Modelo.BaseDatos;
import java.util.ArrayList;

public class ControladorGeneral {
    private Main mainView;

    private ControladorGestion controladorGestion;
    private ControladorReportes controladorReportes;

    public ControladorGeneral(Main mainView, JMenuItem gestionProductosItem, JMenuItem gestionUsuariosItem,
                              JMenuItem gestionVentasItem, JMenuItem gestionComprasItem,
                              JMenuItem reporteVentasItem, JMenuItem reporteProductosItem,
                              JMenuItem salirItem) {
        this.mainView = mainView;

        // Inicializar la conexión y BaseDatos
        try {
            Connection conexion = ConexionBD.conectarMySQL();
            BaseDatos.initialize(conexion);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainView, "Error al conectar a la base de datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Inicializar subcontroladores
        controladorGestion = new ControladorGestion(mainView, this);
        controladorReportes = new ControladorReportes(mainView);

        // Configurar eventos del menú
        configurarEventosMenu(gestionProductosItem, gestionUsuariosItem, gestionVentasItem, gestionComprasItem,
                reporteVentasItem, reporteProductosItem, salirItem);
    }

    public static ArrayList<String[]> consultarUsuarios(String nombreUsuario) {
        // Consulta en la tabla usuarios donde el nombre_usuario es igual al proporcionado
        String condicion = "nombre_usuario = '" + nombreUsuario + "'";
        return BaseDatos.getInstance().consultar("usuarios", "*", condicion);
    }

    private void configurarEventosMenu(JMenuItem gestionProductosItem, JMenuItem gestionUsuariosItem,
                                       JMenuItem gestionVentasItem, JMenuItem gestionComprasItem,
                                       JMenuItem reporteVentasItem, JMenuItem reporteProductosItem,
                                       JMenuItem salirItem) {
        gestionProductosItem.addActionListener(e -> controladorGestion.mostrarGestionProductos());
        gestionUsuariosItem.addActionListener(e -> controladorGestion.mostrarGestionUsuarios());
        gestionVentasItem.addActionListener(e -> controladorGestion.mostrarGestionVentas());
        gestionComprasItem.addActionListener(e -> controladorGestion.mostrarGestionCompras());
        reporteVentasItem.addActionListener(e -> controladorReportes.mostrarReporteVentas());
        reporteProductosItem.addActionListener(e -> controladorReportes.mostrarReporteProductos());
        salirItem.addActionListener(e -> System.exit(0));
    }

    public void iniciarAplicacion() {
        mainView.setVisible(true);
    }

    public static void agregarRegistro(String tabla, String campos, String[] valores) {
        BaseDatos.getInstance().insertar(tabla, campos, valores);
    }

    public static ArrayList<String[]> consultarProductos(){
        return BaseDatos.getInstance().consultar("productos", null, null);
    }

    
    public static ArrayList<String[]> consultar(String tabla, String campos, String condicion){
        return BaseDatos.getInstance().consultar(tabla, campos, condicion);
    }

    public static void eliminarRegistro(String tabla, String campo, String valor) {
        BaseDatos.getInstance().eliminar(tabla, campo, valor);
    }

    public static void editarRegistro(String tabla, String campoCondicion, String valorCampo, String condicion,
                               String campoActualizar, String valorActualizar) {
        BaseDatos.getInstance().modificar(tabla, campoCondicion, valorCampo, condicion, campoActualizar, valorActualizar);
    }

    public static boolean existeRegistro(String tabla, String campo, String valor) {
        ArrayList<String[]> resultado = BaseDatos.getInstance().consultar(tabla, campo, campo + " = " + valor);
        return !resultado.isEmpty();
    }
}
