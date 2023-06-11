package es.studium.artistic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class NuevoEstilo implements WindowListener, ActionListener {
    //Elementos de la venta Nuevo Estilo
    Frame ventanaNuevoEstilo = new Frame("Añadir un estilo");
    Label lblNuevoEstilo = new Label(" --- NUEVO ESTILO --- ");
    Label lblNombreEstilo = new Label("Nombre :");
    TextField txtNombreEstilo = new TextField(25);
    Button btnCrear = new Button("Añadir");
    Button btnCancelar = new Button("Cancelar");
    Dialog dlgError = new Dialog(ventanaNuevoEstilo,"ERROR",true);
    Label lblError = new Label("Introduce un nombre de estilo");
    Dialog dlgExito = new Dialog(ventanaNuevoEstilo,"ESTILO AÑADIDO");
    Label lblExito = new Label("ESTILO CREADO CORRECTAMENTE");
    Button btnAceptar = new Button("Aceptar");
    //Fuente
    Font myFont = new Font("Verdana",Font.BOLD, 18);
    //Conexion
    Conexion conexion = new Conexion();
    String usuLog;
    NuevoEstilo(String usuL){
        this.usuLog = usuL;
        ventanaNuevoEstilo.setSize(240,180);
        ventanaNuevoEstilo.setLayout(new FlowLayout());
        ventanaNuevoEstilo.addWindowListener(this);

        lblNuevoEstilo.setFont(myFont);
        ventanaNuevoEstilo.add(lblNuevoEstilo);
        ventanaNuevoEstilo.add(lblNombreEstilo);
        ventanaNuevoEstilo.add(txtNombreEstilo);
        btnCrear.addActionListener(this);
        ventanaNuevoEstilo.add(btnCrear);
        btnCancelar.addActionListener(this);
        ventanaNuevoEstilo.add(btnCancelar);

        ventanaNuevoEstilo.setResizable(false);
        ventanaNuevoEstilo.setLocationRelativeTo(null);
        ventanaNuevoEstilo.setVisible(true);
    }
    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        if (ventanaNuevoEstilo.isActive()){
            ventanaNuevoEstilo.setVisible(false);
        } else if (dlgError.isActive()) {
            dlgError.setVisible(false);
        } else if (dlgExito.isActive()) {
            dlgExito.setVisible(false);
            txtNombreEstilo.setText("");
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
        if (e.getSource().equals(btnCrear)){
            if (txtNombreEstilo.getText().length() >0){
                if(conexion.insertarEstilo(txtNombreEstilo.getText(),usuLog)){
                    dlgExito.setSize(300,125);
                    dlgExito.setLayout(new FlowLayout());
                    dlgExito.addWindowListener(this);

                    dlgExito.add(lblExito);
                    btnAceptar.addActionListener(this);
                    dlgExito.add(btnAceptar);

                    dlgExito.setResizable(false);
                    dlgExito.setLocationRelativeTo(null);
                    dlgExito.setVisible(true);
                }
            }else {
                dlgError.setSize(230,100);
                dlgError.setLayout(new FlowLayout());
                dlgError.addWindowListener(this);


                dlgError.add(lblError);

                dlgError.setResizable(false);
                dlgError.setLocationRelativeTo(null);
                dlgError.setVisible(true);

            }
        } else if (e.getSource().equals(btnCancelar)) {
            ventanaNuevoEstilo.setVisible(false);
        } else if (e.getSource().equals(btnAceptar)) {
            dlgExito.setVisible(false);
            txtNombreEstilo.setText("");
        }
    }

}
