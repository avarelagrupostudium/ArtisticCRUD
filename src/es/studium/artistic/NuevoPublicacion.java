package es.studium.artistic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NuevoPublicacion implements WindowListener, ActionListener {
    Frame ventanaNuevaPublicacion = new Frame("Nueva publicación");
    Label lblNuevaPublicacion = new Label(" --- NUEVA PUBLICACIÓN --- ");
    Label lblArtista = new Label("Selecciona al artista: ");
    Choice choArtista = new Choice();
    Label lblEstilo = new Label("Selecciona el estilo: ");
    Choice choEstilo = new Choice();
    Label lblCaption = new Label("Descripción: ");
    TextArea txtaCaption = new TextArea(5,30);
    Button btnCrear = new Button("Crear");
    Button btnCancerlar = new Button("Cancelar");
    //Dlg error
    Dialog dlgerror = new Dialog(ventanaNuevaPublicacion,"ERROR",true);
    Label lblError = new Label("ERROR!! Revise los datos introducidos");
    Dialog dlgExito = new Dialog(ventanaNuevaPublicacion,"Alta confiramda",true);
    Label lblExito = new Label("         Artista añadido        ");
    Button btnExito = new Button("Aceptar");
    //Usuario propagado
    String usuLog;
    //Fuente
    Font myFont = new Font("Verdana", Font.BOLD, 18);
    //Conexion
    Conexion conexion = new Conexion();

    NuevoPublicacion(String usuarioPropagado){
        this.usuLog = usuarioPropagado;
        ventanaNuevaPublicacion.setSize(280,350);
        ventanaNuevaPublicacion.setLayout(new FlowLayout());
        ventanaNuevaPublicacion.addWindowListener(this);
        lblNuevaPublicacion.setFont(myFont);

        ventanaNuevaPublicacion.add(lblNuevaPublicacion);
        ventanaNuevaPublicacion.add(lblArtista);
        conexion.rellenarChoiceArtistas(choArtista);
        ventanaNuevaPublicacion.add(choArtista);
        ventanaNuevaPublicacion.add(lblEstilo);
        conexion.rellenarChoiceEstilos(choEstilo);
        ventanaNuevaPublicacion.add(choEstilo);
        ventanaNuevaPublicacion.add(lblCaption);
        ventanaNuevaPublicacion.add(txtaCaption);
        btnCrear.addActionListener(this);
        ventanaNuevaPublicacion.add(btnCrear);
        btnCancerlar.addActionListener(this);
        ventanaNuevaPublicacion.add(btnCancerlar);

        ventanaNuevaPublicacion.setResizable(false);
        ventanaNuevaPublicacion.setLocationRelativeTo(null);
        ventanaNuevaPublicacion.setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {
        if (ventanaNuevaPublicacion.isActive()){
            ventanaNuevaPublicacion.setVisible(false);
        } else if (dlgExito.isActive()) {
            dlgExito.setVisible(false);
            conexion.rellenarChoiceArtistas(choArtista);
            conexion.rellenarChoiceEstilos(choEstilo);
            txtaCaption.setText("");
        } else if (dlgerror.isActive()) {
            dlgerror.setVisible(false);
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
        if (e.getSource().equals(btnCancerlar)){
            ventanaNuevaPublicacion.setVisible(false);
        } else if (e.getSource().equals(btnCrear)) {
            crearPublicacion();
        } else if (e.getSource().equals(btnExito)) {
            dlgExito.setVisible(false);
            conexion.rellenarChoiceArtistas(choArtista);
            conexion.rellenarChoiceEstilos(choEstilo);
            txtaCaption.setText("");
        }
    }

    private void crearPublicacion() {
        if (choArtista.getSelectedIndex() != 0 && choEstilo .getSelectedIndex() != 0 && txtaCaption.getText().length() > 0 ){
            String text = txtaCaption.getText();
            String[] partsArtista = choArtista.getSelectedItem().split("-");
            String[] partsEstilo = choEstilo.getSelectedItem().split("-");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fecha = dtf.format(LocalDateTime.now());
            if (conexion.insertarPublicacion(partsArtista[0],partsEstilo[0],fecha,text,usuLog)){
                pantallaExito();
            }else {
                pantallaError();
            }
        }else {
            pantallaError();
        }
    }
    private void pantallaError() {
        dlgerror.setSize(350,100);
        dlgerror.setLayout(new FlowLayout());
        dlgerror.addWindowListener(this);
        dlgerror.add(lblError);
        dlgerror.setResizable(false);
        dlgerror.setLocationRelativeTo(null);
        dlgerror.setVisible(true);

    }
    private void pantallaExito(){
        dlgExito.setSize(200,150);
        dlgExito.setLayout(new FlowLayout());
        dlgExito.addWindowListener(this);
        dlgExito.add(lblExito);
        btnExito.addActionListener(this);
        dlgExito.add(btnExito);
        dlgExito.setResizable(false);
        dlgExito.setLocationRelativeTo(null);
        dlgExito.setVisible(true);
    }

}
