package Controlador;

import Vistas.Main;
import Vistas.Gestion.*;
import Vistas.Reportes.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ControladorGestion {
    private Main mainView;
    private ControladorGeneral controladorGeneral;

    public ControladorGestion(Main mainView, ControladorGeneral controladorGeneral) {
        this.mainView = mainView;
        this.controladorGeneral = controladorGeneral;
    }

    /**
     * Muestra la vista de gestión de productos.
     */
    public void mostrarGestionProductos() {
        GestionProductosVista vista = new GestionProductosVista();
        mainView.cambiarVista(vista);

        // Cargar los productos en la tabla
        cargarProductos(vista);

        // Configuración de eventos
        vista.getAgregarButton().addActionListener(e -> agregarProducto(vista));
        vista.getEditarButton().addActionListener(e -> editarProducto(vista));
        vista.getEliminarButton().addActionListener(e -> eliminarProducto(vista));
    }

    /**
     * Muestra la vista de gestión de usuarios.
     */
    public void mostrarGestionUsuarios() {
        GestionUsuariosVista vista = new GestionUsuariosVista();
        mainView.cambiarVista(vista);

        // Cargar los usuarios en la tabla
        cargarUsuarios(vista);

        // Configuración de eventos
        vista.getAgregarButton().addActionListener(e -> agregarUsuario(vista));
        vista.getEditarButton().addActionListener(e -> editarUsuario(vista));
        vista.getEliminarButton().addActionListener(e -> eliminarUsuario(vista));
    }

    /**
     * Muestra la vista de gestión de ventas.
     */
    public void mostrarGestionVentas() {
        GestionVentasVista vista = new GestionVentasVista();
        mainView.cambiarVista(vista);

        // Cargar los productos en la tabla de productos
        cargarProductosEnTablaProductos(vista);

        // Configuración de eventos
        vista.getBuscarButton().addActionListener(e -> buscarProductos(vista));
        vista.getAgregarButton().addActionListener(e -> agregarProductoAlCarrito(vista));
        vista.getOkButton().addActionListener(e -> registrarVenta(vista));
        vista.getVerDetalleButton().addActionListener(e -> verDetalleVentaButton(vista));
    }

    /**
     * Muestra la vista de gestión de compras.
     */
    public void mostrarGestionCompras() {
        GestionComprasVista vista = new GestionComprasVista();
        mainView.cambiarVista(vista);

        // Cargar las compras en la tabla de compras
        cargarCompras(vista);

        // Configuración de eventos
        vista.getRegistrarCompraButton().addActionListener(e -> registrarCompra(vista));
        vista.getVerDetalleButton().addActionListener(e -> verDetalleCompraButton(vista));
    }

    // -------------------- Métodos de Gestión de Productos --------------------

    /**
     * Carga los productos desde la base de datos y los muestra en la tabla de productos.
     *
     * @param vista La vista de gestión de productos.
     */
    private void cargarProductos(GestionProductosVista vista) {
        vista.limpiarTabla(); // Limpiar la tabla antes de cargar nuevos datos
        ArrayList<String[]> productos = controladorGeneral.consultar("productos", null, null);

        for (String[] producto : productos) {
            vista.agregarFila(producto);
        }
    }

    /**
     * Agrega un nuevo producto mediante un diálogo.
     *
     * @param vista La vista de gestión de productos.
     */
    private void agregarProducto(GestionProductosVista vista) {
        JDialog dialog = new JDialog(mainView, "Agregar Producto", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(7, 2, 10, 10));
        dialog.setLocationRelativeTo(mainView); // Centrar el diálogo

        dialog.add(new JLabel("ID Producto:"));
        JTextField idField = new JTextField();
        dialog.add(idField);

        dialog.add(new JLabel("Nombre:"));
        JTextField nombreField = new JTextField();
        dialog.add(nombreField);

        dialog.add(new JLabel("Precio:"));
        JTextField precioField = new JTextField();
        dialog.add(precioField);

        dialog.add(new JLabel("Stock:"));
        JTextField stockField = new JTextField("0"); // Valor predeterminado
        dialog.add(stockField);

        dialog.add(new JLabel("Categoría:"));
        JTextField categoriaField = new JTextField();
        dialog.add(categoriaField);

        dialog.add(new JLabel("Fecha Creación:"));
        JTextField fechaCreacionField = new JTextField(java.time.LocalDate.now().toString());
        fechaCreacionField.setEnabled(false); // No editable, se asigna automáticamente
        dialog.add(fechaCreacionField);

        // Espacio vacío para alineación
        dialog.add(new JLabel(""));

        JButton agregarButton = new JButton("Agregar");
        agregarButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String nombre = nombreField.getText().trim();
            String precio = precioField.getText().trim();
            String stock = stockField.getText().trim();
            String categoria = categoriaField.getText().trim();
            String fechaCreacion = fechaCreacionField.getText().trim();

            // Validaciones básicas
            if (id.isEmpty() || nombre.isEmpty() || precio.isEmpty() || stock.isEmpty() || categoria.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar que el precio y stock sean números
            try {
                Double.parseDouble(precio);
                Integer.parseInt(stock);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Precio y Stock deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si el ID ya existe
            if (controladorGeneral.existeRegistro("productos", "id", "'" + id + "'")) {
                JOptionPane.showMessageDialog(dialog, "El ID del producto ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] valores = {
                id,
                nombre,
                categoria,
                precio,
                stock,
                fechaCreacion,
                null // 'fecha_actualizacion' y 'ultima_compra' se manejan automáticamente
            };
            controladorGeneral.agregarRegistro("productos", "(id, nombre, categoria, precio, stock, fecha_creacion, ultima_compra)", valores);
            JOptionPane.showMessageDialog(mainView, "Producto Agregado Exitosamente");
            dialog.dispose(); // Cerrar el diálogo después de agregar

            // Recargar los productos en la tabla
            cargarProductos(vista);
        });
        dialog.add(agregarButton);

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(e -> dialog.dispose());
        dialog.add(cancelarButton);

        dialog.setVisible(true);
    }

    /**
     * Edita un producto seleccionado mediante un diálogo.
     *
     * @param vista La vista de gestión de productos.
     */
    private void editarProducto(GestionProductosVista vista) {
        JTable tabla = vista.getTablaProductos();
        int filaSeleccionada = tabla.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(mainView, "Por favor, selecciona un producto para editar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener los datos de la fila seleccionada
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        String idProducto = model.getValueAt(filaSeleccionada, 0).toString();
        String nombre = model.getValueAt(filaSeleccionada, 1).toString();
        String categoria = model.getValueAt(filaSeleccionada, 2).toString();
        String precio = model.getValueAt(filaSeleccionada, 3).toString();
        String stock = model.getValueAt(filaSeleccionada, 4).toString();
        String fechaCreacion = model.getValueAt(filaSeleccionada, 5).toString();

        // Crear el diálogo de edición
        JDialog dialog = new JDialog(mainView, "Editar Producto", true);
        dialog.setSize(400, 450);
        dialog.setLayout(new GridLayout(8, 2, 10, 10));
        dialog.setLocationRelativeTo(mainView); // Centrar el diálogo

        // ID Producto (Inhabilitado)
        dialog.add(new JLabel("ID Producto:"));
        JTextField idField = new JTextField(idProducto);
        idField.setEnabled(false);
        dialog.add(idField);

        // Nombre (Editable)
        dialog.add(new JLabel("Nombre:"));
        JTextField nombreField = new JTextField(nombre);
        dialog.add(nombreField);

        // Categoría (Editable)
        dialog.add(new JLabel("Categoría:"));
        JTextField categoriaField = new JTextField(categoria);
        dialog.add(categoriaField);

        // Precio (Editable)
        dialog.add(new JLabel("Precio:"));
        JTextField precioField = new JTextField(precio);
        dialog.add(precioField);

        // Stock (Editable)
        dialog.add(new JLabel("Stock:"));
        JTextField stockField = new JTextField(stock);
        dialog.add(stockField);

        // Fecha de Creación (Inhabilitado)
        dialog.add(new JLabel("Fecha Creación:"));
        JTextField fechaCreacionField = new JTextField(fechaCreacion);
        fechaCreacionField.setEnabled(false);
        dialog.add(fechaCreacionField);

        // Última Compra (Inhabilitado o Editable según requerimiento)
        dialog.add(new JLabel("Última Compra:"));
        JTextField ultimaCompraField = new JTextField();
        ultimaCompraField.setEnabled(false); // Puede habilitarse si deseas editar
        dialog.add(ultimaCompraField);

        // Espacio vacío para alineación
        dialog.add(new JLabel(""));

        // Botón de edición
        JButton editarButton = new JButton("Editar");
        editarButton.addActionListener(e -> {
            String nuevoNombre = nombreField.getText().trim();
            String nuevaCategoria = categoriaField.getText().trim();
            String nuevoPrecio = precioField.getText().trim();
            String nuevoStock = stockField.getText().trim();

            // Validaciones básicas
            if (nuevoNombre.isEmpty() || nuevaCategoria.isEmpty() || nuevoPrecio.isEmpty() || nuevoStock.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos los campos obligatorios deben ser llenados.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar que el precio y stock sean números
            try {
                Double.parseDouble(nuevoPrecio);
                Integer.parseInt(nuevoStock);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Precio y Stock deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Actualizar en la base de datos
            controladorGeneral.editarRegistro("productos", "id", idProducto, "=", "nombre", nuevoNombre);
            controladorGeneral.editarRegistro("productos", "id", idProducto, "=", "categoria", nuevaCategoria);
            controladorGeneral.editarRegistro("productos", "id", idProducto, "=", "precio", nuevoPrecio);
            controladorGeneral.editarRegistro("productos", "id", idProducto, "=", "stock", nuevoStock);

            // Actualizar la tabla en la vista
            model.setValueAt(nuevoNombre, filaSeleccionada, 1);
            model.setValueAt(nuevaCategoria, filaSeleccionada, 2);
            model.setValueAt(nuevoPrecio, filaSeleccionada, 3);
            model.setValueAt(nuevoStock, filaSeleccionada, 4);

            JOptionPane.showMessageDialog(mainView, "Producto Editado Exitosamente");
            dialog.dispose(); // Cerrar el diálogo después de editar
        });
        dialog.add(editarButton);

        // Botón de cancelar
        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(e -> dialog.dispose());
        dialog.add(cancelarButton);

        dialog.setVisible(true);
    }

    /**
     * Elimina un producto seleccionado.
     *
     * @param vista La vista de gestión de productos.
     */
    private void eliminarProducto(GestionProductosVista vista) {
        JTable tabla = vista.getTablaProductos();
        int filaSeleccionada = tabla.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(mainView, "Por favor, selecciona un producto para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener el ID del producto de la fila seleccionada
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        String idProducto = model.getValueAt(filaSeleccionada, 0).toString();

        // Confirmar eliminación
        int confirmacion = JOptionPane.showConfirmDialog(mainView, "¿Estás seguro de que deseas eliminar el producto con ID " + idProducto + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            // Eliminar el producto de la base de datos
            controladorGeneral.eliminarRegistro("productos", "id", "'" + idProducto + "'");

            // Eliminar la fila de la tabla
            model.removeRow(filaSeleccionada);

            JOptionPane.showMessageDialog(mainView, "Producto con ID " + idProducto + " eliminado exitosamente.");
        }
    }

    // -------------------- Métodos de Gestión de Usuarios --------------------

    /**
     * Carga los usuarios desde la base de datos y los muestra en la tabla de usuarios.
     *
     * @param vista La vista de gestión de usuarios.
     */
    private void cargarUsuarios(GestionUsuariosVista vista) {
        vista.limpiarTabla(); // Limpiar la tabla antes de cargar nuevos datos
        ArrayList<String[]> usuarios = controladorGeneral.consultar("usuarios", null, null);

        for (String[] usuario : usuarios) {
            vista.agregarFila(usuario);
        }
    }

    /**
     * Agrega un nuevo usuario mediante un diálogo.
     *
     * @param vista La vista de gestión de usuarios.
     */
    private void agregarUsuario(GestionUsuariosVista vista) {
        JDialog dialog = new JDialog(mainView, "Agregar Usuario", true);
        dialog.setSize(400, 450);
        dialog.setLayout(new GridLayout(8, 2, 10, 10));
        dialog.setLocationRelativeTo(mainView); // Centrar el diálogo

        dialog.add(new JLabel("ID Usuario:"));
        JTextField idField = new JTextField("U" + System.currentTimeMillis());
        idField.setEnabled(false); // ID generado automáticamente
        dialog.add(idField);

        dialog.add(new JLabel("Nombre Usuario:"));
        JTextField nombreUsuarioField = new JTextField();
        dialog.add(nombreUsuarioField);

        dialog.add(new JLabel("Contraseña:"));
        JPasswordField contrasenaField = new JPasswordField();
        dialog.add(contrasenaField);

        dialog.add(new JLabel("Rol:"));
        JComboBox<String> rolComboBox = new JComboBox<>(new String[] {"cajero", "administrador"});
        dialog.add(rolComboBox);

        dialog.add(new JLabel("Fecha Creación:"));
        JTextField fechaCreacionField = new JTextField(java.time.LocalDate.now().toString());
        fechaCreacionField.setEnabled(false); // No editable, se asigna automáticamente
        dialog.add(fechaCreacionField);

        dialog.add(new JLabel("Estado:"));
        JComboBox<String> estadoComboBox = new JComboBox<>(new String[] {"activo", "inactivo"});
        dialog.add(estadoComboBox);

        // Espacio vacío para alineación
        dialog.add(new JLabel(""));

        JButton agregarButton = new JButton("Agregar");
        agregarButton.addActionListener(e -> {
            String idUsuario = idField.getText().trim();
            String nombreUsuario = nombreUsuarioField.getText().trim();
            String contrasena = new String(contrasenaField.getPassword()).trim();
            String rol = (String) rolComboBox.getSelectedItem();
            String fechaCreacion = fechaCreacionField.getText().trim();
            String estado = (String) estadoComboBox.getSelectedItem();

            // Validaciones básicas
            if (nombreUsuario.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nombre de usuario y contraseña son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si el nombre de usuario ya existe
            if (controladorGeneral.existeRegistro("usuarios", "nombre_usuario", "'" + nombreUsuario + "'")) {
                JOptionPane.showMessageDialog(dialog, "El nombre de usuario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] valores = {
                idUsuario,
                nombreUsuario,
                contrasena,
                rol,
                fechaCreacion,
                estado
            };
            controladorGeneral.agregarRegistro("usuarios", "(id, nombre_usuario, contrasena, rol, fecha_creacion, estado)", valores);
            JOptionPane.showMessageDialog(mainView, "Usuario Agregado Exitosamente");
            dialog.dispose(); // Cerrar el diálogo después de agregar

            // Recargar los usuarios en la tabla
            cargarUsuarios(vista);
        });
        dialog.add(agregarButton);

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(e -> dialog.dispose());
        dialog.add(cancelarButton);

        dialog.setVisible(true);
    }

    /**
     * Edita un usuario seleccionado mediante un diálogo.
     *
     * @param vista La vista de gestión de usuarios.
     */
    private void editarUsuario(GestionUsuariosVista vista) {
        JTable tabla = vista.getTablaUsuarios();
        int filaSeleccionada = tabla.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(mainView, "Por favor, selecciona un usuario para editar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener los datos de la fila seleccionada
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        String idUsuario = model.getValueAt(filaSeleccionada, 0).toString();
        String nombreUsuario = model.getValueAt(filaSeleccionada, 1).toString();
        String contrasena = model.getValueAt(filaSeleccionada, 2).toString();
        String rol = model.getValueAt(filaSeleccionada, 3).toString();
        String fechaCreacion = model.getValueAt(filaSeleccionada, 4).toString();
        String estado = model.getValueAt(filaSeleccionada, 5).toString();

        // Crear el diálogo de edición
        JDialog dialog = new JDialog(mainView, "Editar Usuario", true);
        dialog.setSize(400, 450);
        dialog.setLayout(new GridLayout(8, 2, 10, 10));
        dialog.setLocationRelativeTo(mainView); // Centrar el diálogo

        // ID Usuario (Inhabilitado)
        dialog.add(new JLabel("ID Usuario:"));
        JTextField idField = new JTextField(idUsuario);
        idField.setEnabled(false);
        dialog.add(idField);

        // Nombre Usuario (Inhabilitado)
        dialog.add(new JLabel("Nombre Usuario:"));
        JTextField nombreUsuarioField = new JTextField(nombreUsuario);
        nombreUsuarioField.setEnabled(false);
        dialog.add(nombreUsuarioField);

        // Contraseña (Editable)
        dialog.add(new JLabel("Contraseña:"));
        JPasswordField contrasenaField = new JPasswordField(contrasena);
        dialog.add(contrasenaField);

        // Rol (Editable)
        dialog.add(new JLabel("Rol:"));
        JComboBox<String> rolComboBox = new JComboBox<>(new String[] {"cajero", "administrador"});
        rolComboBox.setSelectedItem(rol);
        dialog.add(rolComboBox);

        // Fecha Creación (Inhabilitado)
        dialog.add(new JLabel("Fecha Creación:"));
        JTextField fechaCreacionField = new JTextField(fechaCreacion);
        fechaCreacionField.setEnabled(false);
        dialog.add(fechaCreacionField);

        // Estado (Editable)
        dialog.add(new JLabel("Estado:"));
        JComboBox<String> estadoComboBox = new JComboBox<>(new String[] {"activo", "inactivo"});
        estadoComboBox.setSelectedItem(estado);
        dialog.add(estadoComboBox);

        // Espacio vacío para alineación
        dialog.add(new JLabel(""));

        // Botón de edición
        JButton editarButton = new JButton("Editar");
        editarButton.addActionListener(e -> {
            String nuevaContrasena = new String(contrasenaField.getPassword()).trim();
            String nuevoRol = (String) rolComboBox.getSelectedItem();
            String nuevoEstado = (String) estadoComboBox.getSelectedItem();

            // Validaciones básicas
            if (nuevaContrasena.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "La contraseña es obligatoria.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Actualizar en la base de datos
            controladorGeneral.editarRegistro("usuarios", "id", idUsuario, "=", "contrasena", nuevaContrasena);
            controladorGeneral.editarRegistro("usuarios", "id", idUsuario, "=", "rol", nuevoRol);
            controladorGeneral.editarRegistro("usuarios", "id", idUsuario, "=", "estado", nuevoEstado);

            // Actualizar la tabla en la vista
            model.setValueAt(nuevaContrasena, filaSeleccionada, 2);
            model.setValueAt(nuevoRol, filaSeleccionada, 3);
            model.setValueAt(nuevoEstado, filaSeleccionada, 5);

            JOptionPane.showMessageDialog(mainView, "Usuario Editado Exitosamente");
            dialog.dispose(); // Cerrar el diálogo después de editar
        });
        dialog.add(editarButton);

        // Botón de cancelar
        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(e -> dialog.dispose());
        dialog.add(cancelarButton);

        dialog.setVisible(true);
    }

    /**
     * Elimina un usuario seleccionado.
     *
     * @param vista La vista de gestión de usuarios.
     */
    private void eliminarUsuario(GestionUsuariosVista vista) {
        JTable tabla = vista.getTablaUsuarios();
        int filaSeleccionada = tabla.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(mainView, "Por favor, selecciona un usuario para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener el ID del usuario de la fila seleccionada
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        String idUsuario = model.getValueAt(filaSeleccionada, 0).toString();

        // Confirmar eliminación
        int confirmacion = JOptionPane.showConfirmDialog(mainView, "¿Estás seguro de que deseas eliminar el usuario con ID " + idUsuario + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            // Eliminar el usuario de la base de datos
            controladorGeneral.eliminarRegistro("usuarios", "id", "'" + idUsuario + "'");

            // Eliminar la fila de la tabla
            model.removeRow(filaSeleccionada);

            JOptionPane.showMessageDialog(mainView, "Usuario con ID " + idUsuario + " eliminado exitosamente.");
        }
    }

    // -------------------- Métodos de Gestión de Ventas --------------------

    /**
     * Carga los productos disponibles en la tabla de productos dentro de la vista de ventas.
     *
     * @param vista La vista de gestión de ventas.
     */
    private void cargarProductosEnTablaProductos(GestionVentasVista vista) {
        ArrayList<String[]> productos = controladorGeneral.consultar("productos", null, null);
        vista.cargarProductosEnTabla(vista.getTablaProductos(), productos);
    }

    /**
     * Busca productos según el término ingresado y actualiza la tabla de productos.
     *
     * @param vista La vista de gestión de ventas.
     */
    private void buscarProductos(GestionVentasVista vista) {
        String terminoBusqueda = vista.getBuscarField().getText().trim();

        String condicion = null;
        if (!terminoBusqueda.isEmpty()) {
            // Utilizar PreparedStatement en ControladorGeneral para evitar inyecciones SQL
            condicion = "nombre LIKE '%" + terminoBusqueda + "%' OR categoria LIKE '%" + terminoBusqueda + "%'";
        }

        ArrayList<String[]> productos = controladorGeneral.consultar("productos", null, condicion);
        vista.cargarProductosEnTabla(vista.getTablaProductos(), productos);
    }

    /**
     * Agrega un producto al carrito de compras.
     *
     * @param vista La vista de gestión de ventas.
     */
    private void agregarProductoAlCarrito(GestionVentasVista vista) {
        JTable tablaProductos = vista.getTablaProductos();
        int filaSeleccionada = tablaProductos.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(mainView, "Por favor, selecciona un producto para agregar al carrito.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cantidadStr = vista.getCantidadField().getText().trim();
        if (cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(mainView, "Por favor, ingresa la cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainView, "Cantidad inválida. Debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel modelProductos = (DefaultTableModel) tablaProductos.getModel();
        String idProducto = modelProductos.getValueAt(filaSeleccionada, 0).toString();
        String nombreProducto = modelProductos.getValueAt(filaSeleccionada, 1).toString();
        String precioStr = modelProductos.getValueAt(filaSeleccionada, 3).toString();
        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainView, "Precio inválido del producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calcular subtotal
        double subtotal = precio * cantidad;

        // Agregar al carrito
        Object[] filaCarrito = {idProducto, nombreProducto, cantidad, precio, subtotal};
        vista.agregarProductoAlCarrito(filaCarrito);

        // Limpiar el campo de cantidad
        vista.getCantidadField().setText("");
    }

    /**
     * Registra una nueva venta en la base de datos.
     *
     * @param vista La vista de gestión de ventas.
     */
    private void registrarVenta(GestionVentasVista vista) {
        JTable tablaCarrito = vista.getTablaCarrito();
        DefaultTableModel modelCarrito = (DefaultTableModel) tablaCarrito.getModel();

        int filasCarrito = modelCarrito.getRowCount();
        if (filasCarrito == 0) {
            JOptionPane.showMessageDialog(mainView, "El carrito está vacío. Agrega productos antes de registrar la venta.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener el ID del usuario actual (implementación según tu sistema de autenticación)
        String idUsuario = obtenerIdUsuarioActual();
        if (idUsuario == null) {
            JOptionPane.showMessageDialog(mainView, "No se pudo obtener el ID del usuario actual.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calcular el total de la venta
        double total = 0.0;
        for (int i = 0; i < filasCarrito; i++) {
            double subtotal = Double.parseDouble(modelCarrito.getValueAt(i, 4).toString());
            total += subtotal;
        }

        // Generar un ID único para la venta
        String idVenta = "V" + System.currentTimeMillis();

        // Obtener la fecha actual
        java.sql.Timestamp fechaVenta = new java.sql.Timestamp(System.currentTimeMillis());

        // Registrar la venta en la tabla 'ventas'
        String[] valoresVenta = {
            idVenta,
            fechaVenta.toString(),
            String.valueOf(total)
        };
        controladorGeneral.agregarRegistro("ventas", "(id, fecha_venta, total)", valoresVenta);

        // Registrar cada producto en la tabla 'VentaProductos'
        for (int i = 0; i < filasCarrito; i++) {
            String idProducto = modelCarrito.getValueAt(i, 0).toString();
            String cantidadStr = modelCarrito.getValueAt(i, 2).toString();
            String precioStr = modelCarrito.getValueAt(i, 3).toString();
            String idVentaProducto = "VP" + System.currentTimeMillis() + i; // Generar un ID único para VentaProductos

            String[] valoresVentaProducto = {
                idVentaProducto,
                idVenta,
                idProducto,
                cantidadStr,
                precioStr
            };
            controladorGeneral.agregarRegistro("VentaProductos", "(id, id_venta, id_producto, cantidad, precio)", valoresVentaProducto);
        }

        JOptionPane.showMessageDialog(mainView, "Venta Registrada Exitosamente");

        // Limpiar el carrito
        vista.limpiarCarrito();

        // Actualizar las tablas de productos (por ejemplo, reducir el stock)
        actualizarStockProductos(modelCarrito);
    }

    /**
     * Actualiza el stock de los productos después de una venta.
     *
     * @param modelCarrito El modelo de la tabla de carrito de compras.
     */
    private void actualizarStockProductos(DefaultTableModel modelCarrito) {
        for (int i = 0; i < modelCarrito.getRowCount(); i++) {
            String idProducto = modelCarrito.getValueAt(i, 0).toString();
            int cantidadVendida = Integer.parseInt(modelCarrito.getValueAt(i, 2).toString());

            // Obtener el stock actual
            ArrayList<String[]> productoData = controladorGeneral.consultar("productos", "stock", "id = '" + idProducto + "'");
            if (!productoData.isEmpty()) {
                int stockActual = Integer.parseInt(productoData.get(0)[0]);
                int nuevoStock = stockActual - cantidadVendida;

                if (nuevoStock < 0) {
                    JOptionPane.showMessageDialog(mainView, "Stock insuficiente para el producto ID: " + idProducto, "Error", JOptionPane.ERROR_MESSAGE);
                    continue; // O manejarlo según sea necesario
                }

                // Actualizar el stock en la base de datos
                controladorGeneral.editarRegistro("productos", "id", idProducto, "=", "stock", String.valueOf(nuevoStock));
            }
        }
    }

    // -------------------- Métodos de Gestión de Compras --------------------

    /**
     * Carga las compras desde la base de datos y las muestra en la tabla de compras.
     *
     * @param vista La vista de gestión de compras.
     */
    private void cargarCompras(GestionComprasVista vista) {
        vista.limpiarTabla(); // Limpiar la tabla antes de cargar nuevos datos
        ArrayList<String[]> compras = controladorGeneral.consultar("compras", null, null);

        for (String[] compra : compras) {
            vista.agregarFila(compra);
        }
    }

    /**
     * Registra una nueva compra en la base de datos.
     *
     * @param vista La vista de gestión de compras.
     */
    private void registrarCompra(GestionComprasVista vista) {
        // Crear el diálogo para registrar la compra
        JDialog dialog = new JDialog(mainView, "Registrar Compra", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new GridLayout(8, 2, 10, 10));
        dialog.setLocationRelativeTo(mainView);

        // ID Compra (generado automáticamente)
        dialog.add(new JLabel("ID Compra:"));
        JTextField idCompraField = new JTextField("C" + System.currentTimeMillis());
        idCompraField.setEnabled(false);
        dialog.add(idCompraField);

        // Producto (ComboBox)
        dialog.add(new JLabel("Producto:"));
        JComboBox<String> productoComboBox = new JComboBox<>();
        ArrayList<String[]> productos = controladorGeneral.consultar("productos", "id, nombre", null);
        for (String[] producto : productos) {
            productoComboBox.addItem(producto[0] + " - " + producto[1]);
        }
        dialog.add(productoComboBox);

        // Cantidad
        dialog.add(new JLabel("Cantidad:"));
        JTextField cantidadField = new JTextField();
        dialog.add(cantidadField);

        // Proveedor
        dialog.add(new JLabel("Proveedor:"));
        JTextField proveedorField = new JTextField();
        dialog.add(proveedorField);

        // Fecha Compra (actual)
        dialog.add(new JLabel("Fecha Compra:"));
        JTextField fechaCompraField = new JTextField(java.time.LocalDate.now().toString());
        fechaCompraField.setEnabled(false); // No editable, se asigna automáticamente
        dialog.add(fechaCompraField);

        // Última Compra (opcional)
        dialog.add(new JLabel("Última Compra:"));
        JTextField ultimaCompraField = new JTextField();
        ultimaCompraField.setEnabled(false); // Inhabilitado para edición
        dialog.add(ultimaCompraField);

        // Espacio vacío para alineación
        dialog.add(new JLabel(""));

        // Botón Registrar
        JButton registrarButton = new JButton("Registrar");
        registrarButton.addActionListener(e -> {
            String idCompra = idCompraField.getText().trim();
            String productoSeleccionado = (String) productoComboBox.getSelectedItem();
            String cantidadStr = cantidadField.getText().trim();
            String proveedor = proveedorField.getText().trim();
            String fechaCompra = fechaCompraField.getText().trim();

            // Validaciones
            if (productoSeleccionado == null || cantidadStr.isEmpty() || proveedor.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parsear cantidad
            int cantidad;
            try {
                cantidad = Integer.parseInt(cantidadStr);
                if (cantidad <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener ID del producto
            String idProducto = productoSeleccionado.split(" - ")[0];

            // Obtener precio del producto
            ArrayList<String[]> productoData = controladorGeneral.consultar("productos", "precio", "id = '" + idProducto + "'");
            if (productoData.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String precioStr = productoData.get(0)[0];
            double precio;
            try {
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Precio del producto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calcular subtotal
            double subtotal = precio * cantidad;

            // Registrar compra en la tabla 'compras'
            String[] valoresCompra = {
                idCompra,
                fechaCompra,
                proveedor
            };
            controladorGeneral.agregarRegistro("compras", "(id, fecha_compra, proveedor)", valoresCompra);

            // Registrar detalle de la compra en la tabla 'CompraProductos'
            // Nota: Asegúrate de que la tabla 'CompraProductos' exista en tu base de datos
            String idCompraProducto = idCompra;
            String[] valoresCompraProducto = {
                idCompraProducto,
                idProducto,
                String.valueOf(cantidad),
            };
            controladorGeneral.agregarRegistro("CompraProductos", "(idcompra, idproductos, cantidad)", valoresCompraProducto);

            // Actualizar stock del producto
            actualizarStockProducto(idProducto, cantidad);

            JOptionPane.showMessageDialog(mainView, "Compra Registrada Exitosamente");
            dialog.dispose();

            // Recargar la tabla de compras
            cargarCompras(vista);
        });
        dialog.add(registrarButton);

        // Botón Cancelar
        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(e -> dialog.dispose());
        dialog.add(cancelarButton);

        dialog.setVisible(true);
    }

    /**
     * Actualiza el stock de un producto después de una compra.
     *
     * @param idProducto      El ID del producto.
     * @param cantidadComprada La cantidad comprada.
     */
    private void actualizarStockProducto(String idProducto, int cantidadComprada) {
        ArrayList<String[]> productoData = controladorGeneral.consultar("productos", "stock", "id = '" + idProducto + "'");
        if (!productoData.isEmpty()) {
            int stockActual = Integer.parseInt(productoData.get(0)[0]);
            int nuevoStock = stockActual + cantidadComprada;
            controladorGeneral.editarRegistro("productos", "id", idProducto, "=", "stock", String.valueOf(nuevoStock));

            // Actualizar el campo 'ultima_compra' si es necesario
            controladorGeneral.editarRegistro("productos", "id", idProducto, "=", "ultima_compra", java.time.LocalDate.now().toString());
        }
    }

    /**
     * Obtiene el ID del usuario actual.
     *
     * @return El ID del usuario actual o null si no se puede obtener.
     */
    private String obtenerIdUsuarioActual() {
        // Implementa este método según cómo manejes la autenticación y el usuario actual en tu aplicación.
        // Por ejemplo, podrías almacenar el ID del usuario en una variable estática o en el controlador general.
        // Aquí, devolveremos un ID ficticio para ilustrar.

        return "U123456"; // Reemplaza esto con la implementación real
    }

    // -------------------- Métodos de Visualización de Detalles --------------------

    /**
     * Muestra los detalles de una venta seleccionada.
     *
     * @param vista La vista de gestión de ventas.
     */
    private void verDetalleVentaButton(GestionVentasVista vista) {
        // Solicitar al usuario el ID de la venta
        String idVenta = JOptionPane.showInputDialog(mainView, "Ingrese el ID de la Venta:", "Detalles de la Venta", JOptionPane.PLAIN_MESSAGE);
        if (idVenta == null || idVenta.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainView, "El ID de la venta es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Consultar la venta en la base de datos
        ArrayList<String[]> ventas = controladorGeneral.consultar("ventas", null, "id = '" + idVenta + "'");
        if (ventas.isEmpty()) {
            JOptionPane.showMessageDialog(mainView, "No se encontró la venta con ID " + idVenta, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener los detalles de VentaProductos
        ArrayList<String[]> ventaProductos = controladorGeneral.consultar("VentaProductos", null, "id_venta = '" + idVenta + "'");

        // Verificar si hay productos asociados a la venta
        if (ventaProductos.isEmpty()) {
            JOptionPane.showMessageDialog(mainView, "No hay productos asociados a esta venta.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Construir el detalle de la venta
        StringBuilder detalles = new StringBuilder();
        detalles.append("===== Detalles de la Venta =====\n\n");
        detalles.append("ID Venta: ").append(ventas.get(0)[0]).append("\n");
        detalles.append("Fecha Venta: ").append(ventas.get(0)[1]).append("\n");
        detalles.append("Total: $").append(ventas.get(0)[2]).append("\n\n");
        detalles.append("----- Productos Vendidos -----\n");

        double totalCalculado = 0.0;

        for (String[] vp : ventaProductos) {
            String idProducto = vp[2];
            String cantidadStr = vp[3];
            String precioStr = vp[4];

            // Obtener el nombre del producto
            ArrayList<String[]> productoData = controladorGeneral.consultar("productos", "nombre", "id = '" + idProducto + "'");
            String nombreProducto = productoData.isEmpty() ? "Desconocido" : productoData.get(0)[0];

            // Calcular subtotal
            int cantidad = 0;
            double precio = 0.0;
            try {
                cantidad = Integer.parseInt(cantidadStr);
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException ex) {
                // Manejar formato incorrecto
                JOptionPane.showMessageDialog(mainView, "Datos numéricos inválidos para el producto ID: " + idProducto, "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            double subtotal = cantidad * precio;
            totalCalculado += subtotal;

            // Agregar detalles del producto
            detalles.append("ID Producto: ").append(idProducto).append("\n");
            detalles.append("Nombre: ").append(nombreProducto).append("\n");
            detalles.append("Cantidad: ").append(cantidad).append("\n");
            detalles.append("Precio Unitario: $").append(precio).append("\n");
            detalles.append("Subtotal: $").append(String.format("%.2f", subtotal)).append("\n\n");
        }

        // Verificar que el total calculado coincida con el registrado
        double totalRegistrado = 0.0;
        try {
            totalRegistrado = Double.parseDouble(ventas.get(0)[2]);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainView, "Total de venta inválido en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (Math.abs(totalCalculado - totalRegistrado) > 0.01) { // Permitir una pequeña discrepancia
            detalles.append("Nota: El total calculado ($").append(String.format("%.2f", totalCalculado)).append(") no coincide con el total registrado ($").append(totalRegistrado).append(").\n");
        }

        // Mostrar los detalles en un cuadro de diálogo
        JTextArea textArea = new JTextArea(detalles.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Fuente monoespaciada para mejor legibilidad
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 500));

        JOptionPane.showMessageDialog(mainView, scrollPane, "Detalles de la Venta", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra los detalles de una compra seleccionada.
     *
     * @param vista La vista de gestión de compras.
     */
    private void verDetalleCompraButton(GestionComprasVista vista) {
        String idCompra = JOptionPane.showInputDialog(mainView, "Ingrese el ID de la Compra:", "Detalles de la Compra", JOptionPane.PLAIN_MESSAGE);
        if (idCompra == null || idCompra.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainView, "El ID de la compra es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Consultar la compra en la base de datos
        ArrayList<String[]> compras = controladorGeneral.consultar("compras", null, "id = '" + idCompra + "'");
        if (compras.isEmpty()) {
            JOptionPane.showMessageDialog(mainView, "No se encontró la compra con ID " + idCompra, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener los detalles de CompraProductos
        ArrayList<String[]> compraProductos = controladorGeneral.consultar("CompraProductos", null, "idcompra = '" + idCompra + "'");

        // Verificar si hay productos asociados a la compra
        if (compraProductos.isEmpty()) {
            JOptionPane.showMessageDialog(mainView, "No hay productos asociados a esta compra.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Construir el detalle de la compra
        StringBuilder detalles = new StringBuilder();
        detalles.append("===== Detalles de la Compra =====\n\n");
        detalles.append("ID Compra: ").append(compras.get(0)[0]).append("\n");
        detalles.append("Fecha Compra: ").append(compras.get(0)[1]).append("\n");
        detalles.append("Proveedor: ").append(compras.get(0)[2]).append("\n\n");
        detalles.append("----- Productos Comprados -----\n");

        double totalCalculado = 0.0;

        for (String[] cp : compraProductos) {
            String idProducto = cp[1];
            String cantidadStr = cp[2];

            // Obtener el nombre del producto
            ArrayList<String[]> productoData = controladorGeneral.consultar("productos", "nombre", "id = '" + idProducto + "'");
            String nombreProducto = productoData.isEmpty() ? "Desconocido" : productoData.get(0)[0];

            // Calcular subtotal
            int cantidad = 0;
            double precio = 0.0;
            try {
                cantidad = Integer.parseInt(cantidadStr);
            } catch (NumberFormatException ex) {
                // Manejar formato incorrecto
                JOptionPane.showMessageDialog(mainView, "Datos numéricos inválidos para el producto ID: " + idProducto, "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            double subtotal = cantidad * precio;
            totalCalculado += subtotal;

            // Agregar detalles del producto
            detalles.append("ID Producto: ").append(idProducto).append("\n");
            // Obtener el proveedor del producto
            ArrayList<String[]> proveedorData = controladorGeneral.consultar("productos", "proveedor", "id = '" + idProducto + "'");
            String proveedor = proveedorData.isEmpty() ? "Desconocido" : proveedorData.get(0)[0];

            // Agregar detalles del proveedor
            detalles.append("Nombre: ").append(nombreProducto).append("\n");
            detalles.append("Cantidad: ").append(cantidad).append("\n");
        }

        // Mostrar los detalles en un cuadro de diálogo
        JTextArea textArea = new JTextArea(detalles.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Fuente monoespaciada para mejor legibilidad
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 500));

        JOptionPane.showMessageDialog(mainView, scrollPane, "Detalles de la Compra", JOptionPane.INFORMATION_MESSAGE);
    }
}
