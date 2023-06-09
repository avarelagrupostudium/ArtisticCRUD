package es.studium.artistic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Login implements WindowListener, ActionListener {
    //Elementos de la ventana de Login
    Frame ventanaLogin = new Frame("BIENVENIDO A ARTISTIC");
    Label lblInicio = new Label("   ARTISTIC   ");
    Label lblUsuario = new Label("Usuario: ");
    Label lblPassword = new Label("Contraseña:");
    TextField txtUsuario = new TextField(30);
    TextField txtPassword = new TextField(30);
    Button btnLogin = new Button("Iniciar sesion");
    Button btnExit = new Button("Salir");
    //Elementos ventana aviso de credenciales incorrectas
    Dialog dlgPasswordError = new Dialog(ventanaLogin,"Error!",true);
    Label lblPasswordError = new Label("Credenciales incorrectas");
    Label lblPasswordError2 = new Label("Inténtelo de nuevo");
    Button btnPasswordError = new Button("Aceptar");
    //FUENTE
    Font myFont = new Font("Verdana",Font.BOLD, 32);
    //CONEXION
    Conexion conexion = new Conexion();
    String usuario = "";
    String password = "";



    //Montamos la ventana
    Login(){

        ventanaLogin.setSize(300,250);
        ventanaLogin.setLayout(new FlowLayout());
        ventanaLogin.addWindowListener(this);

        lblInicio.setFont(myFont);
        ventanaLogin.add(lblInicio);
        ventanaLogin.add(lblUsuario);
        ventanaLogin.add(txtUsuario);
        ventanaLogin.add(lblPassword);
        txtPassword.setEchoChar('*');
        ventanaLogin.add(txtPassword);
        btnLogin.addActionListener(this);
        ventanaLogin.add(btnLogin);
        btnExit.addActionListener(this);
        ventanaLogin.add(btnExit);

        ventanaLogin.setResizable(false);
        ventanaLogin.setLocationRelativeTo(null);
        ventanaLogin.setVisible(true);

    }

    public static void main(String[] args) {
        new Login();
    }
    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        if (dlgPasswordError.isActive()){
            dlgPasswordError.setVisible(false);
        }else {
            System.exit(0);
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
        if (e.getSource().equals(btnExit)){
            System.exit(0);
        } else if (e.getSource().equals(btnLogin)) {
            usuario = txtUsuario.getText();
            password = txtPassword.getText();
            if (conexion.comprobarCredenciales(usuario,password)){
                String[] datosUsuario = conexion.obtenerUsuario(usuario);
                new MenuPrincipal(Integer.parseInt(datosUsuario[0]),usuario);
                ventanaLogin.setVisible(false);
            }else{
                dlgPasswordError.setSize(190,150);
                dlgPasswordError.setLayout(new FlowLayout());
                dlgPasswordError.add(lblPasswordError);
                dlgPasswordError.add(lblPasswordError2);
                btnPasswordError.addActionListener(this);
                dlgPasswordError.add(btnPasswordError);

                dlgPasswordError.setResizable(false);
                dlgPasswordError.setLocationRelativeTo(null);
                dlgPasswordError.setVisible(true);
            }
        } else if (e.getSource().equals(btnPasswordError)) {
            txtPassword.setText("");
            dlgPasswordError.setVisible(false);
        }
    }

}