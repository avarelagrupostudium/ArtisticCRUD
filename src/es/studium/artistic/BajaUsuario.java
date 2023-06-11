package es.studium.artistic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class BajaUsuario implements WindowListener, ActionListener {
    //Elementos de la ventana baja usuario.
    Frame ventanaBajaUsuario = new Frame("Baja de usuario");
    Label lblBajaUsuario = new Label("--- BAJA DE USUARIO ---");
    Choice choUsuario = new Choice();
    Button btnEliminar = new Button("Eliminar");
    Button btnCancelar = new Button("Cancelar");
    //Dialog de confirmación
    Dialog dlgConfirmarBaja = new Dialog(ventanaBajaUsuario,"ATENCIÓN!!",true);
    Label lblConfirmarBaja = new Label();
    Label lblPreguntarConfirmarBaja = new Label("¿Estás seguro que deseas borrarlo permanentemente?");
    Button btnSi = new Button("SÍ");
    Button btnNo = new Button("NO");
    //Dialog borrado confirmado
    Dialog dlgBajaConfirmada = new Dialog(ventanaBajaUsuario,"BORRADO REALIZADO",true);
    Label lblBajaConfirmada = new Label("Usuario borrado correctamente");
    Button btnContinuar= new Button("Continuar");
    //Dialog borrado fallido
    Dialog dlgError = new Dialog(ventanaBajaUsuario,"ERROR",true);
    Label lblError = new Label("Error en la baja");
    //Conexion
    Conexion conexion = new Conexion();
    //Fuente
    Font myFont = new Font("Verdana",Font.BOLD, 18);
    String usuLog = "";
    BajaUsuario(String usuLogPropagado){
        this.usuLog = usuLogPropagado;
        ventanaBajaUsuario.setSize(280,150);
        ventanaBajaUsuario.setLayout(new FlowLayout());
        ventanaBajaUsuario.addWindowListener(this);

        lblBajaUsuario.setFont(myFont);
        ventanaBajaUsuario.add(lblBajaUsuario);
        //Rellenar el Choice con los elementos de la tabla usuarios
        conexion.rellenarChoiceUsuarios(choUsuario);
        ventanaBajaUsuario.add(choUsuario);

        btnEliminar.addActionListener(this);
        ventanaBajaUsuario.add(btnEliminar);
        btnCancelar.addActionListener(this);
        ventanaBajaUsuario.add(btnCancelar);

        ventanaBajaUsuario.setResizable(false);
        ventanaBajaUsuario.setLocationRelativeTo(null);
        ventanaBajaUsuario.setVisible(true);
    }
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {
        if (ventanaBajaUsuario.isActive()){
            ventanaBajaUsuario.setVisible(false);
        } else if (dlgConfirmarBaja.isActive()) {
            dlgConfirmarBaja.setVisible(false);
        } else if (dlgBajaConfirmada.isActive()) {
            dlgBajaConfirmada.setVisible(false);
            dlgConfirmarBaja.setVisible(false);
            conexion.rellenarChoiceUsuarios(choUsuario);;
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
            ventanaBajaUsuario.setVisible(false);
        } else if (e.getSource().equals(btnEliminar)){
            if (choUsuario.getSelectedIndex() != 0) {
                dlgConfirmarBaja.setSize(360,160);
                dlgConfirmarBaja.setLayout(new FlowLayout());
                dlgConfirmarBaja.addWindowListener(this);
                lblConfirmarBaja.setText("El usuario "+choUsuario.getSelectedItem()+" será borrado de la base de datos ");
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
            String[] partes = choUsuario.getSelectedItem().split("-");
            String idUsuario = partes[0];
            if (conexion.eliminarUsuario(idUsuario, usuLog)) {
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
            conexion.rellenarChoiceUsuarios(choUsuario);
        }
    }
}