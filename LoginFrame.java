package Vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import Modelo.BaseDatos;
import Modelo.ConexionBD;

public class LoginFrame extends JFrame {

    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginButton;

    // Colores de la paleta
    private static final Color BACKGROUND_COLOR = new Color(46, 46, 46); // Gris Oscuro
    private static final Color TEXT_COLOR = new Color(255, 255, 255); // Blanco
    private static final Color ACCENT_COLOR = new Color(127, 255, 212); // Acuamarina
    private static final Color FIELD_BACKGROUND = new Color(60, 63, 65); // Gris Medio
    private static final Color FIELD_FOREGROUND = new Color(255, 255, 255); // Blanco

    public LoginFrame() {

        setTitle("Login");
        setSize(450, 300); // Tamaño aumentado para mayor comodidad
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_COLOR); // Fondo oscuro

        // Configuración de la interfaz
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Uso de GridBagLayout para un control más preciso
        panel.setBackground(BACKGROUND_COLOR); // Fondo oscuro

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15); // Márgenes alrededor de los componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título de la ventana de login
        JLabel tituloLabel = new JLabel("Iniciar Sesión");
        tituloLabel.setForeground(ACCENT_COLOR);
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(tituloLabel, gbc);

        gbc.gridwidth = 1; // Reset gridwidth

        // Etiqueta y campo para ID
        JLabel idLabel = new JLabel("ID:");
        idLabel.setForeground(TEXT_COLOR); // Texto blanco
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(idLabel, gbc);

        idField = new JTextField(20);
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Fuente moderna
        idField.setBackground(FIELD_BACKGROUND); // Fondo gris medio
        idField.setForeground(FIELD_FOREGROUND); // Texto blanco
        idField.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1)); // Borde acuamarina
        idField.setPreferredSize(new Dimension(200, 30)); // Tamaño del campo de texto
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(idField, gbc);

        // Etiqueta y campo para Contraseña
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setForeground(TEXT_COLOR); // Texto blanco
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Fuente moderna
        passwordField.setBackground(FIELD_BACKGROUND); // Fondo gris medio
        passwordField.setForeground(FIELD_FOREGROUND); // Texto blanco
        passwordField.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1)); // Borde acuamarina
        passwordField.setPreferredSize(new Dimension(200, 30)); // Tamaño del campo de texto
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passwordField, gbc);

        // Espacio entre campos y botón
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 15, 10, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        // Botón de Entrar
        loginButton = new JButton("Entrar");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Fuente bold
        loginButton.setBackground(ACCENT_COLOR); // Acuamarina
        loginButton.setForeground(TEXT_COLOR); // Texto blanco
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(56, 142, 60))); // Borde sutil
        loginButton.setPreferredSize(new Dimension(120, 40)); // Tamaño del botón
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cursor de mano
        loginButton.setOpaque(true); // Para que el color de fondo se vea
        estilizarBoton(loginButton);
        panel.add(loginButton, gbc);

        // Agregar el panel al JFrame
        add(panel);

        // Configuración del evento del botón de login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String password = new String(passwordField.getPassword());

                // Validar las credenciales
                if (id.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Por favor, ingrese el ID y la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    BaseDatos bd = null;
                    try {
                        bd = new BaseDatos(ConexionBD.conectarMySQL());
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(LoginFrame.this, "Error al conectar a la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                    ArrayList<String[]> usuarios = bd.consultar("usuarios", "*", null);
                    if (usuarios.isEmpty()) {
                        JOptionPane.showMessageDialog(LoginFrame.this, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String[] usuario = usuarios.get(0);
                        String contraseñaDB = usuario[2]; // Columna "contrasena"
                        String estadoDB = usuario[5]; // Columna "estado"

                        if (estadoDB.equals("inactivo")) {
                            JOptionPane.showMessageDialog(LoginFrame.this, "La cuenta está inactiva.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else if (contraseñaDB.equals(password)) {
                            // Aquí debes redirigir a la vista principal
                            String rol = usuario[3]; // Columna "rol"
                            JOptionPane.showMessageDialog(LoginFrame.this, "Bienvenido, " + rol, "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
                            // Iniciar la aplicación según el rol (Ejemplo)
                            new Main(rol).setVisible(true);  // Muestra la ventana principal del sistema
                            dispose();  // Cierra el JFrame de login
                        } else {
                            JOptionPane.showMessageDialog(LoginFrame.this, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
