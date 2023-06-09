package es.studium.artistic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ListadoEstilo implements WindowListener, ActionListener {
    Frame ventanaEstilosListado = new Frame("Listado de usuarios");
    Label lblListado = new Label("LISTADO DE USUARIOS:");
    TextArea areaListado = new TextArea(10, 25);
    Button btnExportExcel = new Button("Excel");
    Button btnExportPdf = new Button("Pdf");

    Conexion conexion = new Conexion();
    ListadoEstilo(String usuLog){
        ventanaEstilosListado.setSize(250, 300);
        ventanaEstilosListado.setLayout(new FlowLayout());
        ventanaEstilosListado.addWindowListener(this);
        ventanaEstilosListado.add(lblListado);
        areaListado.append("Id\tNombre \n");
        ventanaEstilosListado.add(areaListado);
        ventanaEstilosListado.add(btnExportExcel);
        btnExportExcel.addActionListener(this);
        ventanaEstilosListado.add(btnExportPdf);
        btnExportPdf.addActionListener(this);


        ventanaEstilosListado.setLocationRelativeTo(null);
        ventanaEstilosListado.setVisible(true);
        areaListado.append(conexion.listadoEstilos(usuLog));
    }
    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (ventanaEstilosListado.isActive()) {
            ventanaEstilosListado.setVisible(false);
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
    public void actionPerformed(ActionEvent e) {}

}


