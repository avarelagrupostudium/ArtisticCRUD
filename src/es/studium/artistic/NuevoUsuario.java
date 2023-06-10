package es.studium.artistic;

import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class NuevoUsuario implements WindowListener, ActionListener, ItemListener {
    //Elementos de la ventana de Alta de usuario
    Frame ventanaNuevoUsuario = new Frame("Alta de usuario");
    Label lblNuevoUsuario = new Label("--- NUEVO USUARIO ---");
    Label lblNombre = new Label("Nombre:");
    TextField txtNombre = new TextField(30);
    Label lblEmail = new Label("Email:");
    TextField txtEmail = new TextField(30);
    Label lblPassword = new Label("Contraseña:");
    TextField txtPassword = new TextField(30);
    Label lblPassword2 = new Label("Repite la contraseña:");
    TextField txtPassword2 = new TextField(30);
    Button btnCrear = new Button("Crear");
    Button btnCancelar = new Button("Cancelar");
    Label lblUsuarioAdmin = new Label("   Usuario administrador? \t\t\t\t                      ");
    CheckboxGroup cbgAdmin = new CheckboxGroup();
    Checkbox c1 = new Checkbox("Sí     ",cbgAdmin,false);
    Checkbox c2 = new Checkbox("No                                          ",cbgAdmin,true);
    //Dialog Exito
    Dialog dlgAltaConfirmada = new Dialog(ventanaNuevoUsuario,"Alta confirmada",true);
    Label lblAltaConfirmada = new Label("Usuario creado con éxito");
    Button btnAceptar = new Button("Aceptar");
    //Dialog Error
    Dialog dlgAltaError = new Dialog(ventanaNuevoUsuario,"Error de creación",true);
    Label lblAltaError = new Label("Error en el alta de usuario");
    //Conexion
    Conexion conexion = new Conexion();
    //Fuente
    Font myFont = new Font("Verdana",Font.BOLD, 18);
    int tipoUsuario;
    int tipoUsuarioNuevo = 0;
    String usuLog;
    NuevoUsuario(int i, String usuarioPropagado){
        //Tipo de Usuario
        this.tipoUsuario = i;
        this.usuLog = usuarioPropagado;
        //Montamos la ventana
        ventanaNuevoUsuario.setSize(280,400);
        ventanaNuevoUsuario.setLayout(new FlowLayout());
        ventanaNuevoUsuario.addWindowListener(this);

        lblNuevoUsuario.setFont(myFont);
        ventanaNuevoUsuario.add(lblNuevoUsuario);
        ventanaNuevoUsuario.add(lblNombre);
        ventanaNuevoUsuario.add(txtNombre);
        ventanaNuevoUsuario.add(lblEmail);
        ventanaNuevoUsuario.add(txtEmail);
        ventanaNuevoUsuario.add(lblPassword);
        txtPassword.setEchoChar('*');
        ventanaNuevoUsuario.add(txtPassword);
        ventanaNuevoUsuario.add(lblPassword2);
        txtPassword2.setEchoChar('*');
        ventanaNuevoUsuario.add(txtPassword2);

        if (tipoUsuario == 0) {
            ventanaNuevoUsuario.add(lblUsuarioAdmin);
            c1.addItemListener(this);
            c2.addItemListener(this);
            ventanaNuevoUsuario.add(c1);
            ventanaNuevoUsuario.add(c2);
        }

        btnCrear.addActionListener(this);
        ventanaNuevoUsuario.add(btnCrear);
        btnCancelar.addActionListener(this);
        ventanaNuevoUsuario.add(btnCancelar);

        ventanaNuevoUsuario.setResizable(false);
        ventanaNuevoUsuario.setLocationRelativeTo(null);
        ventanaNuevoUsuario.setVisible(true);
    }
    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        if (ventanaNuevoUsuario.isActive()){
            ventanaNuevoUsuario.setVisible(false);
        } else if (dlgAltaError.isActive()) {
            dlgAltaError.setVisible(false);
        } else if (dlgAltaConfirmada.isActive()) {
            dlgAltaConfirmada.setVisible(false);
            txtNombre.setText("");
            txtEmail.setText("");
            txtPassword.setText("");
            txtPassword2.setText("");
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
            ventanaNuevoUsuario.setVisible(false);
        } else if (e.getSource().equals(btnCrear)) {
            String nombre = txtNombre.getText();
            String password = txtPassword.getText();
            String email = txtEmail.getText();
            if (cbgAdmin.getSelectedCheckbox().equals(c1)){
                tipoUsuarioNuevo = 0;
            } else if (cbgAdmin.getSelectedCheckbox().equals(c2)) {
                tipoUsuarioNuevo = 1;
            }
            if (txtPassword.getText().equals(txtPassword2.getText()) && nombre.length() >0
                    && email.length() >0 && password.length() >0){

                if (conexion.insertarUsuario(nombre,password,email,tipoUsuarioNuevo, usuLog)) {
                    dlgAltaConfirmada.setSize(200, 125);
                    dlgAltaConfirmada.setLayout(new FlowLayout());
                    dlgAltaConfirmada.addWindowListener(this);
                    dlgAltaConfirmada.add(lblAltaConfirmada);
                    btnAceptar.addActionListener(this);
                    dlgAltaConfirmada.add(btnAceptar);
                    dlgAltaConfirmada.setResizable(false);
                    dlgAltaConfirmada.setLocationRelativeTo(null);
                    dlgAltaConfirmada.setVisible(true);
                }
            }else {
                dlgAltaError.setSize(200,125);
                dlgAltaError.setLayout(new FlowLayout());
                dlgAltaError.addWindowListener(this);
                dlgAltaError.add(lblAltaError);
                btnAceptar.addActionListener(this);
                dlgAltaError.add(btnAceptar);
                dlgAltaError.setResizable(false);
                dlgAltaError.setLocationRelativeTo(null);
                dlgAltaError.setVisible(true);
            }
        } else if (e.getSource().equals(btnAceptar) && dlgAltaConfirmada.isActive()) {
            dlgAltaConfirmada.setVisible(false);
            txtNombre.setText("");
            txtEmail.setText("");
            txtPassword.setText("");
            txtPassword2.setText("");
        } else if (e.getSource().equals(btnAceptar) && dlgAltaError.isActive()) {
            dlgAltaError.setVisible(false);
        }
    }
    @Override
    public void itemStateChanged(ItemEvent e) {
    }


}
