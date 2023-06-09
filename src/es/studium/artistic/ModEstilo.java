package es.studium.artistic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class
ModEstilo implements WindowListener, ActionListener {
    //Elementos ventana ModEstilos
    Frame ventanaModEstilos = new Frame("Modificación estilo");
    Label lblModEstilo = new Label("MODIFICACIÓN DE ESTILO");
    Choice choEstilo = new Choice();
    Button btnMod = new Button(" Editar ");
    Button btnCancelar = new Button("Cancelar");

    //Elementos dlg modificacion
    Dialog dlgEditarEstilo = new Dialog(ventanaModEstilos,"EDITAR ESTILO");
    Label lblEditarEstilo = new Label();
    Label lblNombreEstilo = new Label("Nombre: ");
    TextField txtNombreEstilo = new TextField(30);
    Button btnEditarEstilo = new Button("Editar");
    Button btnCancelarEditarEstilo = new Button("Cancelar");
    Dialog dlgExito = new Dialog(dlgEditarEstilo,"Modificación realizada");
    Label lblExito = new Label("Se ha modificado el estilo");
    Button btnExito = new Button("Aceptar");
    Dialog dlgError = new Dialog(dlgEditarEstilo,"ERROR");
    Label lblError = new Label("Introduce los datos correctamente");

    //Fuente
    Font myFont = new Font("Verdana",Font.BOLD, 18);
    //Conexion
    Conexion conexion = new Conexion();
    String[] datosEstilo = new String[2];
    String usuLog;
    ModEstilo(String usuarioPropagado){
        this.usuLog = usuarioPropagado;
        ventanaModEstilos.setSize(275,150);
        ventanaModEstilos.setLayout(new FlowLayout());
        ventanaModEstilos.addWindowListener(this);

        lblModEstilo.setFont(myFont);
        ventanaModEstilos.add(lblModEstilo);
        //Rellenar el Choice con los elementos de la tabla estilos
        conexion.rellenarChoiceEstilos(choEstilo);
        ventanaModEstilos.add(choEstilo);
        btnMod.addActionListener(this);
        ventanaModEstilos.add(btnMod);
        btnCancelar.addActionListener(this);
        ventanaModEstilos.add(btnCancelar);

        ventanaModEstilos.setResizable(false);
        ventanaModEstilos.setLocationRelativeTo(null);
        ventanaModEstilos.setVisible(true);
    }
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {
       if (ventanaModEstilos.isActive()){
           ventanaModEstilos.setVisible(false);
       } else if (dlgError.isActive()) {
           dlgError.setVisible(false);
       } else if (dlgExito.isActive()) {
           dlgExito.setVisible(false);
           dlgEditarEstilo.setVisible(false);
           conexion.rellenarChoiceEstilos(choEstilo);
       } else if (dlgEditarEstilo.isActive()) {
           dlgEditarEstilo.setVisible(false);
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
            ventanaModEstilos.setVisible(false);
        } else if (e.getSource().equals(btnMod)) {
            String[] parts = choEstilo.getSelectedItem().split("-");
            datosEstilo = conexion.rellenarDatosEditarEstilo(parts[0]);
            dlgEditarEstilo.setLayout(new FlowLayout());
            dlgEditarEstilo.setSize(280, 200);
            dlgEditarEstilo.addWindowListener(this);

            lblEditarEstilo.setFont(myFont);
            lblEditarEstilo.setText(" Modificar estilo          id: " + parts[0]);
            dlgEditarEstilo.add(lblEditarEstilo);
            dlgEditarEstilo.add(lblNombreEstilo);
            txtNombreEstilo.setText(datosEstilo[1]);
            dlgEditarEstilo.add(txtNombreEstilo);
            btnEditarEstilo.addActionListener(this);
            dlgEditarEstilo.add(btnEditarEstilo);
            btnCancelarEditarEstilo.addActionListener(this);
            dlgEditarEstilo.add(btnCancelarEditarEstilo);

            dlgEditarEstilo.setResizable(false);
            dlgEditarEstilo.setLocationRelativeTo(null);
            dlgEditarEstilo.setVisible(true);


        } else if (e.getSource().equals(btnCancelarEditarEstilo)) {
            dlgEditarEstilo.setVisible(false);
        } else if (e.getSource().equals(btnEditarEstilo)) {
            if (txtNombreEstilo.getText().length()>0){
                datosEstilo[1] = txtNombreEstilo.getText();
                if(conexion.updateEstilo(datosEstilo, usuLog) > 0){
                    dlgExito.setSize(230,100);
                    dlgExito.setLayout(new FlowLayout());
                    dlgExito.addWindowListener(this);

                    dlgExito.add(lblExito);
                    btnExito.addActionListener(this);
                    dlgExito.add(btnExito);

                    dlgExito.setResizable(false);
                    dlgExito.setLocationRelativeTo(null);
                    dlgExito.setVisible(true);
                }else {
                    dlgError.setSize(280,200);
                    dlgError.setLayout(new FlowLayout());
                    dlgError.addWindowListener(this);

                    dlgError.add(lblError);

                    dlgError.setResizable(false);
                    dlgError.setLocationRelativeTo(null);
                    dlgError.setVisible(true);
                }
            }else{
                dlgError.setSize(280,200);
                dlgError.setLayout(new FlowLayout());
                dlgError.addWindowListener(this);

                dlgError.add(lblError);

                dlgError.setResizable(false);
                dlgError.setLocationRelativeTo(null);
                dlgError.setVisible(true);
            }

        } else if (e.getSource().equals(btnExito)) {
            dlgExito.setVisible(false);
            dlgEditarEstilo.setVisible(false);
            conexion.rellenarChoiceEstilos(choEstilo);
        }

    }
}
