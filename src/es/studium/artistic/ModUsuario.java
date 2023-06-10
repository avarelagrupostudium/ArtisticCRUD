package es.studium.artistic;

import java.awt.*;
import java.awt.event.*;

public class ModUsuario implements WindowListener, ActionListener, ItemListener {
    //Elementos de la ventana legir usuario a editar Usuario
    Frame ventanaModUsuario = new Frame("Modificación usuario");
    Label lblModUsuario = new Label(" MODIFICACIÓN DE USUARIO ");
    Choice choUsuarioMod = new Choice();
    Button btnEditar = new Button("Editar");
    Button btnCancelar = new Button("Cancelar");
    //Elementos editar usuario
    Dialog dlgUsuarioEditar = new Dialog(ventanaModUsuario, "Editar usuario", true);
    Label lblUsuarioEditar = new Label();
    Label lblNombreUsuario = new Label("Nombre: ");
    TextField txtNombreUsuario = new TextField(30);
    Label lblCorreoUsuario = new Label("Correo: ");
    TextField txtCorreoUsuario = new TextField(30);
    Label lblPasswordUsuario = new Label("Constraseña: ");
    TextField txtPasswordUsuario = new TextField(30);
    Label lblPasswordUsuario2 = new Label("Repite la contraseña: ");
    TextField txtPasswordUsuario2 = new TextField(30);
    Button btnModificarUsuario = new Button("Modificar");
    Button btnCancelarEditarUsuario = new Button("Cancelar");
    Dialog dlgError = new Dialog(dlgUsuarioEditar, "ERROR", true);
    Label lblError = new Label();
    Dialog dlgUsuarioModificado = new Dialog(dlgUsuarioEditar,"MODIFICACIÓN CORRECTA",true);
    Label lblUsuarioModiciado = new Label("Usuario modificado correctamente");
    Label lblUsuarioAdmin = new Label("   Usuario administrador? \t\t\t\t                      ");
    CheckboxGroup cbgAdmin = new CheckboxGroup();
    Checkbox c1 = new Checkbox("Sí     ",cbgAdmin,false);
    Checkbox c2 = new Checkbox("No                                          ",cbgAdmin,true);
    //Fuente
    Font myFont = new Font("Verdana", Font.BOLD, 18);
    //Conexion
    Conexion conexion = new Conexion();
    Usuario usuario = new Usuario();

    String usuLog;
    ModUsuario(String u) {
        this.usuLog = u;
        ventanaModUsuario.setSize(280, 140);
        ventanaModUsuario.setLayout(new FlowLayout());
        ventanaModUsuario.addWindowListener(this);

        lblModUsuario.setFont(myFont);
        ventanaModUsuario.add(lblModUsuario);
        //Rellenar el Choice con los elementos de la tabla usuarios.
        conexion.rellenarChoiceUsuarios(choUsuarioMod);
        ventanaModUsuario.add(choUsuarioMod);
        btnEditar.addActionListener(this);
        ventanaModUsuario.add(btnEditar);
        btnCancelar.addActionListener(this);
        ventanaModUsuario.add(btnCancelar);

        ventanaModUsuario.setResizable(false);
        ventanaModUsuario.setLocationRelativeTo(null);
        ventanaModUsuario.setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (ventanaModUsuario.isActive()) {
            ventanaModUsuario.setVisible(false);
        } else if (dlgUsuarioEditar.isActive()) {
            dlgUsuarioEditar.setVisible(false);
        } else if (dlgError.isActive()) {
            dlgError.setVisible(false);
        } else if (dlgUsuarioModificado.isActive()) {
            dlgUsuarioEditar.setVisible(false);
            dlgUsuarioModificado.setVisible(false);
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
        if (e.getSource().equals(btnCancelar)) {
            ventanaModUsuario.setVisible(false);
        } else if (e.getSource().equals(btnEditar)) {
            String[] parts = choUsuarioMod.getSelectedItem().split("-");
            usuario = conexion.rellenarDatosEditarUsuario(parts[0]);


            dlgUsuarioEditar.setLayout(new FlowLayout());
            dlgUsuarioEditar.setSize(280, 450);
            dlgUsuarioEditar.addWindowListener(this);
            lblUsuarioEditar.setFont(myFont);
            lblUsuarioEditar.setText(" Modificar usuario          id: " + parts[0]);
            txtNombreUsuario.setText(usuario.getNombre());
            txtCorreoUsuario.setText(usuario.getEmail());
            dlgUsuarioEditar.add(lblUsuarioEditar);
            dlgUsuarioEditar.add(lblNombreUsuario);
            dlgUsuarioEditar.add(txtNombreUsuario);
            dlgUsuarioEditar.add(lblCorreoUsuario);
            dlgUsuarioEditar.add(txtCorreoUsuario);
            dlgUsuarioEditar.add(lblPasswordUsuario);
            dlgUsuarioEditar.add(txtPasswordUsuario);
            txtPasswordUsuario.setEchoChar('*');
            dlgUsuarioEditar.add(lblPasswordUsuario2);
            dlgUsuarioEditar.add(txtPasswordUsuario2);
            txtPasswordUsuario2.setEchoChar('*');
            dlgUsuarioEditar.add(lblUsuarioAdmin);
            if (usuario.getTipoUsuario() == 0){
                c1.setState(true);
                c2.setState(false);
            } else if (usuario.getTipoUsuario() == 1) {
                c1.setState(false);
                c2.setState(true);
            }
            dlgUsuarioEditar.add(c1);
            dlgUsuarioEditar.add(c2);
            dlgUsuarioEditar.add(btnModificarUsuario);
            btnModificarUsuario.addActionListener(this);
            dlgUsuarioEditar.add(btnCancelarEditarUsuario);
            btnCancelarEditarUsuario.addActionListener(this);

            dlgUsuarioEditar.setLocationRelativeTo(null);
            dlgUsuarioEditar.setResizable(false);
            dlgUsuarioEditar.setVisible(true);

        } else if (e.getSource().equals(btnCancelarEditarUsuario)) {
            dlgUsuarioEditar.setVisible(false);
        } else if (e.getSource().equals(btnModificarUsuario)) {
            String nombre = txtNombreUsuario.getText();
            String correo = txtCorreoUsuario.getText();
            int tipoUsuario = 1;
            if (cbgAdmin.getSelectedCheckbox().equals(c1)){
                tipoUsuario = 0;
            }
            if ((txtPasswordUsuario.getText().length() == 0  || txtPasswordUsuario.getText().equals(txtPasswordUsuario2.getText()))
                    && nombre.length() > 0 && correo.length() > 0) {
                if (txtCorreoUsuario.getText().length() > 0){
                    if(conexion.updatePassword(txtPasswordUsuario.getText(), usuario.getIdUsuario(), usuLog) == 0){
                        pantallaError();
                    }
                }
                if (conexion.updateUsuario(txtNombreUsuario.getText(), txtCorreoUsuario.getText(), usuario.getIdUsuario(),tipoUsuario, usuLog) > 0){
                    pantallaExito();
                }
            }else{
                pantallaError();
            }
        }


    }
    @Override
    public void itemStateChanged(ItemEvent e) {

    }
    private void pantallaError(){
        dlgError.setSize(270, 100);
        dlgError.setLayout(new FlowLayout());
        dlgError.addWindowListener(this);
        lblError.setText("ERROR EN LOS DATOS INTRODUCIDOS");
        dlgError.add(lblError);
        dlgError.setResizable(false);
        dlgError.setLocationRelativeTo(null);
        dlgError.setVisible(true);
    }private void pantallaExito(){
        dlgUsuarioModificado.setSize(270, 100);
        dlgUsuarioModificado.setLayout(new FlowLayout());
        dlgUsuarioModificado.addWindowListener(this);
        dlgUsuarioModificado.add(lblUsuarioModiciado);
        choUsuarioMod.removeAll();
        conexion.rellenarChoiceUsuarios(choUsuarioMod);
        dlgUsuarioModificado.setResizable(false);
        dlgUsuarioModificado.setLocationRelativeTo(null);
        dlgUsuarioModificado.setVisible(true);
    }

}
