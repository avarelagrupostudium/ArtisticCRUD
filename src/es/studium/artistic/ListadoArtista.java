package es.studium.artistic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ListadoArtista implements WindowListener, ActionListener {
    Frame ventanaArtistaListado = new Frame("Listado de usuarios");
    Label lblListado = new Label("LISTADO DE ARTISTAS:");
    TextArea areaListado = new TextArea(18, 110);
    Button btnExportExcel = new Button("Excel");
    Button btnExportPdf = new Button("Pdf");

    Conexion conexion = new Conexion();
    String usuLog;
    ListadoArtista(String usuarioPropagado) {

        this.usuLog = usuarioPropagado;
        ventanaArtistaListado.setSize(800, 400);
        ventanaArtistaListado.setLayout(new FlowLayout());
        ventanaArtistaListado.addWindowListener(this);
        ventanaArtistaListado.add(lblListado);
        areaListado.append("id\tNombre\t\tcorreo\t\t\tTlf\t\tInstagram\tWeb\t\tBiografía\t\tEstilos\n");
        areaListado.setSize(390, 300);
        ventanaArtistaListado.add(areaListado);
        ventanaArtistaListado.add(btnExportExcel);
        btnExportExcel.addActionListener(this);
        ventanaArtistaListado.add(btnExportPdf);
        btnExportPdf.addActionListener(this);


        ventanaArtistaListado.setLocationRelativeTo(null);
        ventanaArtistaListado.setVisible(true);
        areaListado.append(conexion.listadoArtista(usuLog));
    }


    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (ventanaArtistaListado.isActive()) {
            ventanaArtistaListado.setVisible(false);
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
            conexion.artistaListadoPDF(usuLog);
        } else if (e.getSource().equals(btnExportExcel)) {
            conexion.artistaListadoExcel(usuLog);
        }
    }

}
