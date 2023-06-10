package es.studium.artistic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public class NuevoArtista implements WindowListener, ActionListener {
    Frame ventanaNuevoArtista = new Frame("Alta artista");
    Label lblNuevoArtista = new Label("      --- Nuevo Artista ---      ");
    Label lblNombre = new Label("Nombre:");
    TextField txtNombre = new TextField(30);
    Label lblEmail = new Label("Email:");
    TextField txtEmail = new TextField(30);
    Label lblTlf = new Label("Teléfono:");
    TextField txtTlf = new TextField(30);
    Label lblInstagram = new Label("Intagram: ");
    TextField txtInstagram = new TextField(30);
    Label lblWeb = new Label("Web personal:");
    TextField txtWeb = new TextField(30);
    Label lblPassword = new Label("Contraseña:");
    TextField txtPassword = new TextField(30);
    Label lblPassword2 = new Label("Repite la contraseña:");
    TextField txtPassword2 = new TextField(30);
    Label lblBio = new Label("Biografía:");
    TextArea txtaBio = new TextArea(5,30);
    Button btnSiguiente = new Button("Siguiente");
    Button btnCancelar = new Button("Cancelar");
    //Dialog asignar estilos
    Dialog dlgAsignarEstilos = new Dialog(ventanaNuevoArtista,"Asigna estilos",true);
    Label lblAsignarEstilos = new Label(" --- Asignar estilos ---");
    Choice choEstilos = new Choice();
    Button btnAddEstilo = new Button("Añadir");
    TextArea txtaEstilosAsignar = new TextArea(5,30);
    Button btnCrear = new Button("Crear Artsita");
    //Dlg error
    Dialog dlgerror = new Dialog(dlgAsignarEstilos,"ERROR",true);
    Label lblError = new Label("ERROR!! Revise los datos introducidos");
    Dialog dlgExito = new Dialog(dlgAsignarEstilos,"Alta confiramda",true);
    Label lblExito = new Label("         Artista añadido        ");
    Button btnExito = new Button("Aceptar");

    //Conexion
    Conexion conexion = new Conexion();
    //Fuente
    Font myFont = new Font("Verdana",Font.BOLD, 18);
    String usuLog;


    NuevoArtista(String usuarioPropagado){
        this.usuLog = usuarioPropagado;
        ventanaNuevoArtista.setSize(280,650);
        ventanaNuevoArtista.setLayout(new FlowLayout());
        ventanaNuevoArtista.addWindowListener(this);
        lblNuevoArtista.setFont(myFont);
        ventanaNuevoArtista.add(lblNuevoArtista);
        ventanaNuevoArtista.add(lblNombre);
        ventanaNuevoArtista.add(txtNombre);
        ventanaNuevoArtista.add(lblEmail);
        ventanaNuevoArtista.add(txtEmail);
        ventanaNuevoArtista.add(lblTlf);
        ventanaNuevoArtista.add(txtTlf);
        ventanaNuevoArtista.add(lblInstagram);
        ventanaNuevoArtista.add(txtInstagram);
        ventanaNuevoArtista.add(lblWeb);
        ventanaNuevoArtista.add(txtWeb);
        ventanaNuevoArtista.add(lblPassword);
        txtPassword.setEchoChar('*');
        ventanaNuevoArtista.add(txtPassword);
        ventanaNuevoArtista.add(lblPassword2);
        txtPassword2.setEchoChar('*');
        ventanaNuevoArtista.add(txtPassword2);
        ventanaNuevoArtista.add(lblBio);
        ventanaNuevoArtista.add(txtaBio);

        btnSiguiente.addActionListener(this);
        ventanaNuevoArtista.add(btnSiguiente);
        btnCancelar.addActionListener(this);
        ventanaNuevoArtista.add(btnCancelar);

        ventanaNuevoArtista.setResizable(false);
        ventanaNuevoArtista.setLocationRelativeTo(null);
        ventanaNuevoArtista.setVisible(true);
    }
    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        if (ventanaNuevoArtista.isActive()){
            ventanaNuevoArtista.setVisible(false);
        } else if (dlgAsignarEstilos.isActive()) {
            dlgAsignarEstilos.setVisible(false);
            txtaEstilosAsignar.setText("");
            conexion.rellenarChoiceEstilos(choEstilos);
        } else if (dlgerror.isActive()) {
            dlgerror.setVisible(false);
            dlgAsignarEstilos.setVisible(false);
        } else if (dlgExito.isActive()) {
            borrarNuevoArtista();
            dlgAsignarEstilos.setVisible(false);
            dlgExito.setVisible(false);

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
        if (e.getSource().equals(btnCancelar)){
            ventanaNuevoArtista.setVisible(false);
        } else if (e.getSource().equals(btnSiguiente)) {
            if (comprobarCampos()) {
                ventanaAdignarEstilos();
            }else{pantallaError();}
        } else if (e.getSource().equals(btnAddEstilo)) {
            if (choEstilos.getSelectedIndex()>0){
                txtaEstilosAsignar.append(choEstilos.getSelectedItem()+"-\n");
                choEstilos.remove(choEstilos.getSelectedIndex());
                btnCrear.setEnabled(true);
            }
        } else if (e.getSource().equals(btnCrear)) {
            addNuevoArtista();
        } else if (e.getSource().equals(btnExito)) {
            dlgExito.setVisible(false);
            dlgAsignarEstilos.setVisible(false);
            borrarNuevoArtista();
        }
    }

    private boolean comprobarCampos() {
        if (txtPassword.getText().equals(txtPassword2.getText())
                && txtNombre.getText().length() >0
                && txtEmail.getText().length() >0
                && txtPassword.getText().length() >0
                && txtTlf.getText().length() >0
                && txtInstagram.getText().length() > 0
                && txtWeb.getText().length() >0
                && txtaBio.getText().length() >0) {
            return true;
        }
        return false;
    }

    private void borrarNuevoArtista() {
        txtNombre.setText("");
        txtEmail.setText("");
        txtTlf.setText("");
        txtInstagram.setText("");
        txtWeb.setText("");
        txtPassword.setText("");
        txtPassword2.setText("");
        txtaBio.setText("");
        txtaEstilosAsignar.setText("");
    }

    void ventanaAdignarEstilos(){
        dlgAsignarEstilos.setSize(280,250);
        dlgAsignarEstilos.setLayout(new FlowLayout());
        dlgAsignarEstilos.addWindowListener(this);

        lblAsignarEstilos.setFont(myFont);
        dlgAsignarEstilos.add(lblAsignarEstilos);
        conexion.rellenarChoiceEstilos(choEstilos);
        dlgAsignarEstilos.add(choEstilos);
        btnAddEstilo.addActionListener(this);
        dlgAsignarEstilos.add(btnAddEstilo);
        txtaEstilosAsignar.setEnabled(false);
        dlgAsignarEstilos.add(txtaEstilosAsignar);
        btnCrear.addActionListener(this);
        btnCrear.setEnabled(false);
        dlgAsignarEstilos.add(btnCrear);

        dlgAsignarEstilos.setResizable(false);
        dlgAsignarEstilos.setLocationRelativeTo(null);
        dlgAsignarEstilos.setVisible(true);


    }
    private void addNuevoArtista() {
        String nombre = txtNombre.getText();
        String email = txtEmail.getText();
        String tlf = txtTlf.getText();
        String instagram = txtInstagram.getText();
        String web = txtWeb.getText();
        String bio = txtaBio.getText();
        String cadenaEstilos = txtaEstilosAsignar.getText();
        cadenaEstilos = cadenaEstilos.replaceAll("\\n","");
        String [] partsEstilos = cadenaEstilos.split("-");
        int idEstilo;
        if (conexion.insertarUsuario(nombre,txtPassword.getText(),email,1, usuLog)){
            if (conexion.insertarArtista(nombre,tlf,instagram,web,bio,usuLog)) {
                for (int i = 0; i < partsEstilos.length; i +=2) {
                    idEstilo = Integer.parseInt(partsEstilos[i]);
                    conexion.insertarEstilosArtistas(nombre, idEstilo);

                }
                pantallaExito();
            }
        } else {
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
        txtaEstilosAsignar.setText("");
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
