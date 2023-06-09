package es.studium.artistic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Arrays;

public class ModArtista implements WindowListener, ActionListener {
    //Elementos de la ventana legir usuario a editar Artista
    Frame ventanaModArtista = new Frame("Modificación artista");
    Label lblModArtista = new Label(" MODIFICACIÓN DE ARTISTA ");
    Label lblModArtista2 = new Label(" MODIFICACIÓN DE ARTISTA ");
    Choice choArtista = new Choice();
    Button btnEditar = new Button("Editar");
    Button btnCancelar = new Button("Cancelar");

    //Dlg datos del artista
    Dialog dlgModArtista = new Dialog(ventanaModArtista,"Modificar artista",true);
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
    Button btnCancelarMod = new Button("Cancelar");
    //Dialog asignar estilos
    Dialog dlgAsignarEstilos = new Dialog(dlgModArtista,"Asigna estilos",true);
    Label lblAsignarEstilos = new Label(" --- Asignar estilos ---");
    Choice choEstilos = new Choice();
    Button btnAddEstilo = new Button("Añadir");
    Button btnDelEstilo = new Button("Eliminar");
    TextArea txtaEstilosAsignar = new TextArea(5,35);
    Button btnCrear = new Button("Editar Artsita");
    //Dlg error
    Dialog dlgerror = new Dialog(dlgAsignarEstilos,"ERROR",true);
    Label lblError = new Label("ERROR!! Revise los datos introducidos");
    //Dlg éxito
    Dialog dlgExito = new Dialog(dlgAsignarEstilos,"Alta confiramda",true);
    Label lblExito = new Label("         Artista añadido        ");
    Button btnExito = new Button("Aceptar");
    //Fuente
    Font myFont = new Font("Verdana", Font.BOLD, 18);
    //Conexion
    Conexion conexion = new Conexion();
    //Usuario Logueado
    String usuLog;
    Artista artista = new Artista();
    ModArtista(String usuarioPropagado){
        this.usuLog = usuarioPropagado;
        ventanaModArtista.setSize(280, 140);
        ventanaModArtista.setLayout(new FlowLayout());
        ventanaModArtista.addWindowListener(this);

        lblModArtista.setFont(myFont);
        ventanaModArtista.add(lblModArtista);
        //Rellenar el Choice con los elementos de la tabla usuarios.
        conexion.rellenarChoiceArtistas(choArtista);
        ventanaModArtista.add(choArtista);
        btnEditar.addActionListener(this);
        ventanaModArtista.add(btnEditar);
        btnCancelar.addActionListener(this);
        ventanaModArtista.add(btnCancelar);

        ventanaModArtista.setResizable(false);
        ventanaModArtista.setLocationRelativeTo(null);
        ventanaModArtista.setVisible(true);


    }
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {
        if (ventanaModArtista.isActive()){
            ventanaModArtista.setVisible(false);
        } else if (dlgModArtista.isActive()) {
            dlgModArtista.setVisible(false);
        } else if (dlgAsignarEstilos.isActive()) {
            dlgAsignarEstilos.setVisible(false);
        } else if (dlgExito.isActive()) {
            dlgExito.setVisible(false);
            dlgAsignarEstilos.setVisible(false);
            dlgModArtista.setVisible(false);
            borrarDatos();
            conexion.rellenarChoiceArtistas(choArtista);
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
        if (e.getSource().equals(btnCancelar)){
            ventanaModArtista.setVisible(false);
        } else if (e.getSource().equals(btnEditar)) {
            if (choArtista.getSelectedIndex() != 0) {
                datosArtista();
            }

        } else if (e.getSource().equals(btnCancelarMod)) {
            dlgModArtista.setVisible(false);
        } else if (e.getSource().equals(btnSiguiente)) {
             comprobarDatosArtista();
        } else if (e.getSource().equals(btnDelEstilo)) {
            conexion.rellenarChoiceEstilos(choEstilos);
            txtaEstilosAsignar.setText("");
            btnAddEstilo.setEnabled(true);
            btnCrear.setEnabled(false);
        } else if (e.getSource().equals(btnAddEstilo)) {
            if (choEstilos.getSelectedIndex() != 0) {
                txtaEstilosAsignar.append(choEstilos.getSelectedItem() + "-\n");
                choEstilos.remove(choEstilos.getSelectedIndex());
            }
            btnCrear.setEnabled(true);
        } else if (e.getSource().equals(btnCrear)) {
            modicarArtista();
        } else if (e.getSource().equals(btnExito)) {
            borrarDatos();
            dlgExito.setVisible(false);
            dlgAsignarEstilos.setVisible(false);
            dlgModArtista.setVisible(false);
            conexion.rellenarChoiceArtistas(choArtista);
        }
    }

    private void modicarArtista() {
        if (txtPassword.getText().length() > 0){
            conexion.updatePassword(txtPassword.getText(),artista.getIdUsuario(),usuLog);
        }
        String cadenaEstilos = txtaEstilosAsignar.getText();
        cadenaEstilos = cadenaEstilos.replaceAll("\\n","");
        String [] partsEstilos = cadenaEstilos.split("-");
        System.out.println(Arrays.toString(partsEstilos));
        if (conexion.updateArtista(artista,usuLog) > 0 && conexion.updateEstilosArtista(artista.getIdUsuario(),partsEstilos) > 0){
            pantallaExito();
        }else {
            pantallaError();
        }
    }

    private void comprobarDatosArtista() {
        String nombre = txtNombre.getText();
        String email = txtEmail.getText();
        String tlf = txtTlf.getText();
        String instagram = txtInstagram.getText();
        String web = txtWeb.getText();
        String bio = txtaBio.getText();

        if ((txtPassword.getText().length() == 0  || txtPassword.getText().equals(txtPassword2.getText()))
                && nombre.length() > 0
                && email.length() > 0
                && tlf.length() > 0
                && instagram.length() > 0
                && web.length() > 0
                && bio.length() > 0){
            artista.setNombre(nombre);
            artista.setEmail(email);
            artista.setTlf(tlf);
            artista.setInstagram(instagram);
            artista.setWeb(web);
            artista.setBiografia(bio);

            datosEstiloArtista();

        } else {
            pantallaError();
        }
    }

    private void datosArtista() {
        dlgModArtista.setSize(280,650);
        dlgModArtista.setLayout(new FlowLayout());
        dlgModArtista.addWindowListener(this);
        lblModArtista2.setFont(myFont);
        dlgModArtista.add(lblModArtista2);
        dlgModArtista.add(lblNombre);
        dlgModArtista.add(txtNombre);
        dlgModArtista.add(lblEmail);
        dlgModArtista.add(txtEmail);
        dlgModArtista.add(lblTlf);
        dlgModArtista.add(txtTlf);
        dlgModArtista.add(lblInstagram);
        dlgModArtista.add(txtInstagram);
        dlgModArtista.add(lblWeb);
        dlgModArtista.add(txtWeb);
        dlgModArtista.add(lblPassword);
        txtPassword.setEchoChar('*');
        dlgModArtista.add(txtPassword);
        dlgModArtista.add(lblPassword2);
        txtPassword2.setEchoChar('*');
        dlgModArtista.add(txtPassword2);
        dlgModArtista.add(lblBio);
        dlgModArtista.add(txtaBio);



        btnSiguiente.addActionListener(this);
        dlgModArtista.add(btnSiguiente);
        btnCancelarMod.addActionListener(this);
        dlgModArtista.add(btnCancelarMod);

        dlgModArtista.setResizable(false);
        dlgModArtista.setLocationRelativeTo(null);
        rellenarDatosArtista();
        dlgModArtista.setVisible(true);


    }
    private void rellenarDatosArtista() {
        String[] partes = choArtista.getSelectedItem().split("-");
        int idArtista = Integer.parseInt(partes[0]);
        artista.setIdUsuario(idArtista);
        artista = conexion.rellenarDatosEditarArtista(artista);
        txtNombre.setText(artista.getNombre());
        txtEmail.setText(artista.getEmail());
        txtTlf.setText(artista.getTlf());
        txtInstagram.setText(artista.getInstagram());
        txtWeb.setText(artista.getWeb());
        txtaBio.setText(artista.getBiografia());
    }
    void datosEstiloArtista(){
        dlgAsignarEstilos.setSize(350,250);
        dlgAsignarEstilos.setLayout(new FlowLayout());
        dlgAsignarEstilos.addWindowListener(this);

        lblAsignarEstilos.setFont(myFont);
        dlgAsignarEstilos.add(lblAsignarEstilos);
        conexion.rellenarChoiceEstilos(choEstilos);
        dlgAsignarEstilos.add(choEstilos);
        btnAddEstilo.addActionListener(this);
        dlgAsignarEstilos.add(btnAddEstilo);
        btnDelEstilo.addActionListener(this);
        dlgAsignarEstilos.add(btnDelEstilo);
        txtaEstilosAsignar.setEnabled(false);
        dlgAsignarEstilos.add(txtaEstilosAsignar);
        btnCrear.addActionListener(this);
        dlgAsignarEstilos.add(btnCrear);

        rellenarDatosEstilo();

        dlgAsignarEstilos.setResizable(false);
        dlgAsignarEstilos.setLocationRelativeTo(null);
        dlgAsignarEstilos.setVisible(true);

    }

    private void rellenarDatosEstilo() {
        String estilosArtista = conexion.rellenarDatosEditarEstilosArtista(artista.getIdUsuario());
        txtaEstilosAsignar.append(estilosArtista);
        String[] estilosInciales = estilosArtista.split("\n");
        for (int i = 0; estilosInciales.length > i; i++){
            //Eliminamos el último carácter de cada cadena para que coincida con lo que aparece en el choice y poder eliminarlo
            estilosInciales[i] = estilosInciales[i].substring(0, estilosInciales[i].length() - 1);
            choEstilos.remove(estilosInciales[i]);
        }
        btnAddEstilo.setEnabled(false);

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
    private void borrarDatos() {
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

    public static void main(String[] args) {
        new ModArtista("Anxo");
    }
}
