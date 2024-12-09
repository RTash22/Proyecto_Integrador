package Vistas;
import javax.swing.*;
import java.awt.*;

public class InicioVista extends JPanel {
    public InicioVista() {
        setLayout(new BorderLayout());

        JLabel bienvenidaLabel = new JLabel("Bienvenido al Sistema de Punto de Venta", JLabel.CENTER);
        bienvenidaLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(bienvenidaLabel, BorderLayout.CENTER);
    }
}
