package es.studium.artistic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class BajaArtista implements WindowListener, ActionListener {
    //Elementos de la ventana baja Artista.
    Frame ventanaBajaArtista = new Frame("Baja de Artsita");
    Label lblBajaArtista = new Label("--- BAJA DE ARTISTA ---");
    Choice choArtista = new Choice();
    Button btnEliminar = new Button("Eliminar");

    Button btnCancelar = new Button("Cancelar");
    //Dialog de confirmación
    Dialog dlgConfirmarBaja = new Dialog(ventanaBajaArtista,"ATENCIÓN!!",true);
    Label lblConfirmarBaja = new Label();
    Label lblPreguntarConfirmarBaja = new Label("¿Estás seguro que deseas borrarlo permanentemente?");
    Button btnSi = new Button("SÍ");
    Button btnNo = new Button("NO");
    //Dialog borrado confirmado
    Dialog dlgBajaConfirmada = new Dialog(ventanaBajaArtista,"BORRADO REALIZADO",true);
    Label lblBajaConfirmada = new Label("Estilo borrado correctamente");
    Button btnContinuar= new Button("Continuar");
    //Dialog borrado fallido
    Dialog dlgError = new Dialog(ventanaBajaArtista,"ERROR",true);
    Label lblError = new Label("Algo ha salido mal");
    //Conexion
    Conexion conexion = new Conexion();
    //Fuente
    Font myFont = new Font("Verdana",Font.BOLD, 18);
    String usuLog = "";
    BajaArtista(String usuLogPropagado){
        this.usuLog = usuLogPropagado;
        ventanaBajaArtista.setSize(280,150);
        ventanaBajaArtista.setLayout(new FlowLayout());
        ventanaBajaArtista.addWindowListener(this);

        lblBajaArtista.setFont(myFont);
        ventanaBajaArtista.add(lblBajaArtista);
        //Rellenar el Choice con los elementos de la tabla estilos
        conexion.rellenarChoiceArtistas(choArtista);
        ventanaBajaArtista.add(choArtista);

        btnEliminar.addActionListener(this);
        ventanaBajaArtista.add(btnEliminar);
        btnCancelar.addActionListener(this);
        ventanaBajaArtista.add(btnCancelar);

        ventanaBajaArtista.setResizable(false);
        ventanaBajaArtista.setLocationRelativeTo(null);
        ventanaBajaArtista.setVisible(true);

    }
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {
        if (ventanaBajaArtista.isActive()){
            ventanaBajaArtista.setVisible(false);
        } else if (dlgConfirmarBaja.isActive()) {
            dlgConfirmarBaja.setVisible(false);
        } else if (dlgBajaConfirmada.isActive()) {
            dlgBajaConfirmada.setVisible(false);
            dlgConfirmarBaja.setVisible(false);
            conexion.rellenarChoiceEstilos(choArtista);
        } else if (dlgError.isActive()) {
            dlgError.setVisible(false);
        }
    }
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(btnCancelar)){
            ventanaBajaArtista.setVisible(false);
        } else if (e.getSource().equals(btnEliminar)){
            if (choArtista.getSelectedIndex() != 0) {
                dlgConfirmarBaja.setSize(360,160);
                dlgConfirmarBaja.setLayout(new FlowLayout());
                dlgConfirmarBaja.addWindowListener(this);
                lblConfirmarBaja.setText("El estilo "+ choArtista.getSelectedItem()+" será borrado de la base de datos ");
                dlgConfirmarBaja.add(lblConfirmarBaja);
                dlgConfirmarBaja.add(lblPreguntarConfirmarBaja);
                btnSi.addActionListener(this);
                dlgConfirmarBaja.add(btnSi);
                btnNo.addActionListener(this);
                dlgConfirmarBaja.add(btnNo);

                dlgConfirmarBaja.setResizable(false);
                dlgConfirmarBaja.setLocationRelativeTo(null);
                dlgConfirmarBaja.setVisible(true);
            }

        } else if (e.getSource().equals(btnNo)) {
            dlgConfirmarBaja.setVisible(false);
        } else if (e.getSource().equals(btnSi)) {
            String[] partes = choArtista.getSelectedItem().split("-");
            String idArtista = partes[0];
            if (conexion.eliminarArtista(idArtista ,usuLog)) {
                //Baja confirmada
                dlgBajaConfirmada.setSize(250,100);
                dlgBajaConfirmada.setLayout(new FlowLayout());
                dlgBajaConfirmada.addWindowListener(this);

                dlgBajaConfirmada.add(lblBajaConfirmada);
                btnContinuar.addActionListener(this);
                dlgBajaConfirmada.add(btnContinuar);

                dlgBajaConfirmada.setResizable(false);
                dlgBajaConfirmada.setLocationRelativeTo(null);
                dlgBajaConfirmada.setVisible(true);
            }else {
                dlgError.setSize(250,100);
                dlgError.setLayout(new FlowLayout());
                dlgError.addWindowListener(this);

                lblError.setFont(myFont);
                dlgError.add(lblError);

                dlgError.setResizable(false);
                dlgError.setLocationRelativeTo(null);
                dlgError.setVisible(true);

            }
        } else if (e.getSource().equals(btnContinuar)) {
            dlgBajaConfirmada.setVisible(false);
            dlgConfirmarBaja.setVisible(false);
            conexion.rellenarChoiceArtistas(choArtista);
        }
    }

}
