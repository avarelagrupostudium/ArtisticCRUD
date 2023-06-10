package es.studium.artistic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ListadoUsuario implements WindowListener, ActionListener {
    Frame ventanaUsuarioListado = new Frame("Listado de usuarios");
    Label lblListado = new Label("LISTADO DE USUARIOS:");
    TextArea areaListado = new TextArea(18, 57);
    Button btnExportExcel = new Button("Excel");
    Button btnExportPdf = new Button("Pdf");

    Conexion conexion = new Conexion();
    String usuLog;

    ListadoUsuario(String usuarioPropagado) {
        this.usuLog = usuarioPropagado;
        ventanaUsuarioListado.setSize(480, 400);
        ventanaUsuarioListado.setLayout(new FlowLayout());
        ventanaUsuarioListado.addWindowListener(this);
        ventanaUsuarioListado.add(lblListado);
        areaListado.append("Id\tRol\tNombre\t\tCorreo electrónico\n");
        areaListado.setSize(390, 300);
        ventanaUsuarioListado.add(areaListado);
        ventanaUsuarioListado.add(btnExportExcel);
        btnExportExcel.addActionListener(this);
        ventanaUsuarioListado.add(btnExportPdf);
        btnExportPdf.addActionListener(this);

        ventanaUsuarioListado.setResizable(false);
        ventanaUsuarioListado.setLocationRelativeTo(null);
        ventanaUsuarioListado.setVisible(true);
        areaListado.append(conexion.listadoUsuario(usuLog));
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (ventanaUsuarioListado.isActive()) {
            ventanaUsuarioListado.setVisible(false);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnExportPdf)){
            conexion.usuarioListadoPDF(usuLog);
        } else if (e.getSource().equals(btnExportExcel)) {
            conexion.usuarioListadoExcel(usuLog);
        }
    }
}

