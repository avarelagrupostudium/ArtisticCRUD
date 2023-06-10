package es.studium.artistic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class BajaEstilo implements WindowListener, ActionListener {
    //Elementos de la ventana baja Estilo.
    Frame ventanaBajaEstilo = new Frame("Baja de estilo");
    Label lblBajaEstilo = new Label("--- BAJA DE ESTILO ---");
    Choice choEstilo = new Choice();
    Button btnEliminar = new Button("Eliminar");

    Button btnCancelar = new Button("Cancelar");
    //Dialog de confirmación
    Dialog dlgConfirmarBaja = new Dialog(ventanaBajaEstilo,"ATENCIÓN!!",true);
    Label lblConfirmarBaja = new Label();
    Label lblPreguntarConfirmarBaja = new Label("¿Estás seguro que deseas borrarlo permanentemente?");
    Button btnSi = new Button("SÍ");
    Button btnNo = new Button("NO");
    //Dialog borrado confirmado
    Dialog dlgBajaConfirmada = new Dialog(ventanaBajaEstilo,"BORRADO REALIZADO",true);
    Label lblBajaConfirmada = new Label("Estilo borrado correctamente");
    Button btnContinuar= new Button("Continuar");
    //Dialog borrado fallido
    Dialog dlgError = new Dialog(ventanaBajaEstilo,"ERROR",true);
    Label lblError = new Label("Algo ha salido mal");
    //Conexion
    Conexion conexion = new Conexion();
    //Fuente
    Font myFont = new Font("Verdana",Font.BOLD, 18);
    String usuLog = "";
    BajaEstilo(String usuLogPropagado){
        this.usuLog = usuLogPropagado;
        ventanaBajaEstilo.setSize(280,150);
        ventanaBajaEstilo.setLayout(new FlowLayout());
        ventanaBajaEstilo.addWindowListener(this);

        lblBajaEstilo.setFont(myFont);
        ventanaBajaEstilo.add(lblBajaEstilo);
        //Rellenar el Choice con los elementos de la tabla estilos
        conexion.rellenarChoiceEstilos(choEstilo);
        ventanaBajaEstilo.add(choEstilo);

        btnEliminar.addActionListener(this);
        ventanaBajaEstilo.add(btnEliminar);
        btnCancelar.addActionListener(this);
        ventanaBajaEstilo.add(btnCancelar);

        ventanaBajaEstilo.setResizable(false);
        ventanaBajaEstilo.setLocationRelativeTo(null);
        ventanaBajaEstilo.setVisible(true);
        
    }
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {
        if (ventanaBajaEstilo.isActive()){
            ventanaBajaEstilo.setVisible(false);
        } else if (dlgConfirmarBaja.isActive()) {
            dlgConfirmarBaja.setVisible(false);
        } else if (dlgBajaConfirmada.isActive()) {
            dlgConfirmarBaja.setVisible(false);
            dlgBajaConfirmada.setVisible(false);
            conexion.rellenarChoiceEstilos(choEstilo);
        } else if (dlgError.isActive()) {
            dlgConfirmarBaja.setVisible(false);
            dlgBajaConfirmada.setVisible(false);
            conexion.rellenarChoiceEstilos(choEstilo);
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
            ventanaBajaEstilo.setVisible(false);
        } else if (e.getSource().equals(btnEliminar)){
            if (choEstilo.getSelectedIndex() != 0) {
                dlgConfirmarBaja.setSize(360,160);
                dlgConfirmarBaja.setLayout(new FlowLayout());
                dlgConfirmarBaja.addWindowListener(this);
                lblConfirmarBaja.setText("El estilo "+choEstilo.getSelectedItem()+" será borrado de la base de datos ");
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
            String[] partes = choEstilo.getSelectedItem().split("-");
            String idEstilo = partes[0];
            if (conexion.eliminarEstilo(idEstilo ,usuLog)) {
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
            conexion.rellenarChoiceEstilos(choEstilo);
        }
    }

}
