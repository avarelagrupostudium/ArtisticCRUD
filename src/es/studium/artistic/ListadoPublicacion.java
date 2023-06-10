package es.studium.artistic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ListadoPublicacion implements WindowListener, ActionListener {
    Frame ventanaPublicacionListado = new Frame("Listado de usuarios");
    Label lblListado = new Label("LISTADO DE PUBLICACIONES:");
    TextArea areaListado = new TextArea(18, 70);
    Button btnExportExcel = new Button("Excel");
    Button btnExportPdf = new Button("Pdf");

    Conexion conexion = new Conexion();
    String usuLog;
    ListadoPublicacion(String usuarioPropagado) {

        this.usuLog = usuarioPropagado;
        ventanaPublicacionListado.setSize(550, 400);
        ventanaPublicacionListado.setLayout(new FlowLayout());
        ventanaPublicacionListado.addWindowListener(this);
        ventanaPublicacionListado.add(lblListado);
        areaListado.append("id\tNombre Artista\t\tEstilo\t\tFecha\t\tTexto\n");
        areaListado.setSize(390, 300);
        ventanaPublicacionListado.add(areaListado);
        ventanaPublicacionListado.add(btnExportExcel);
        btnExportExcel.addActionListener(this);
        ventanaPublicacionListado.add(btnExportPdf);
        btnExportPdf.addActionListener(this);


        ventanaPublicacionListado.setLocationRelativeTo(null);
        ventanaPublicacionListado.setVisible(true);
        areaListado.append(conexion.listadoPublicacion(usuLog));
    }


    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (ventanaPublicacionListado.isActive()) {
            ventanaPublicacionListado.setVisible(false);
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
            conexion.publicacionListadoPDF(usuLog);
        } else if (e.getSource().equals(btnExportExcel)) {
            conexion.publicacionListadoExcel(usuLog);
        }
    }

    public static void main(String[] args) {
        new ListadoPublicacion("Anxo");
    }

}
