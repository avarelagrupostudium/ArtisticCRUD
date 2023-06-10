package es.studium.artistic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Window;


public class MenuPrincipal implements WindowListener, ActionListener {
    //Elementos de la ventana menú principal
    Frame ventanaMenuPrincipal = new Frame("Menú principal");
    Label lblUsuarioLogueado = new Label();
    MenuBar barraPrincipal = new MenuBar();
    //Usuarios
    Menu usuarios = new Menu("Usuarios");
    MenuItem nuevoUsuario = new MenuItem("Nuevo");
    MenuItem bajaUsuario = new MenuItem("Baja");
    MenuItem modUsuario = new MenuItem("Modificación");
    MenuItem listadoUsuario = new MenuItem("Listado");
    //Artistas
    Menu artistas = new Menu("Artistas");
    MenuItem nuevoArtista = new MenuItem("Nuevo");
    MenuItem bajaArtista = new MenuItem("Baja");
    MenuItem modArtista = new MenuItem("Modificación");
    MenuItem listadoArtista = new MenuItem("Listado");
    //Publicaciones
    Menu publicaciones = new Menu("Publicaciones");
    MenuItem nuevoPublicacion = new MenuItem("Nuevo");
    MenuItem bajaPublicacion = new MenuItem("Baja");
    MenuItem modPublicacion = new MenuItem("Modificación");
    MenuItem listadoPublicacion = new MenuItem("Listado");
    //Estilos
    Menu estilos = new Menu("Estilos");
    MenuItem nuevoEstilo = new MenuItem("Nuevo");
    MenuItem bajaEstilo = new MenuItem("Baja");
    MenuItem modEstilo = new MenuItem("Modificación");
    MenuItem listadoEstilo = new MenuItem("Listado");
    //Conexion
    Conexion conexion = new Conexion();
    //FUENTE
    Font myFont = new Font("Verdana",Font.BOLD, 24);

    //Obtener nombre y tipo de Usuario
    int tipoUsuario = 1;
    String usuLog = "****";
    MenuPrincipal( int i, String u){
        this.usuLog = u;
        this.tipoUsuario = i;
        ventanaMenuPrincipal.setSize(500,450);
        ventanaMenuPrincipal.setLayout(new FlowLayout());
        ventanaMenuPrincipal.addWindowListener(this);
        ventanaMenuPrincipal.setMenuBar(barraPrincipal);
        lblUsuarioLogueado.setFont(myFont);
        lblUsuarioLogueado.setText("BIENVENIDO A ARTISTIC " + usuLog);
        ventanaMenuPrincipal.add(lblUsuarioLogueado);
        //Action Listeners Usuarios
        nuevoUsuario.addActionListener(this);
        bajaUsuario.addActionListener(this);
        modUsuario.addActionListener(this);
        listadoUsuario.addActionListener(this);
        //Elementos Usuarios
        barraPrincipal.add(usuarios);
        usuarios.add(nuevoUsuario);
        if (tipoUsuario == 0) {
            usuarios.add(bajaUsuario);
            usuarios.add(modUsuario);
            usuarios.add(listadoUsuario);
        }
        //Action Listeners Artistas
        nuevoArtista.addActionListener(this);
        bajaArtista.addActionListener(this);
        modArtista.addActionListener(this);
        listadoArtista.addActionListener(this);
        //Elementos Artistas
        barraPrincipal.add(artistas);
        artistas.add(nuevoArtista);
        if (tipoUsuario == 0) {
            artistas.add(bajaArtista);
            artistas.add(modArtista);
            artistas.add(listadoArtista);
        }
        //Action Listeners Publicaciones
        nuevoPublicacion.addActionListener(this);
        bajaPublicacion.addActionListener(this);
        modPublicacion.addActionListener(this);
        listadoPublicacion.addActionListener(this);
        //Elementos Publicaciones
        barraPrincipal.add(publicaciones);
        publicaciones.add(nuevoPublicacion);
        if (tipoUsuario == 0) {
            publicaciones.add(listadoPublicacion);
        }
        //Action Listeners Estilos
        nuevoEstilo.addActionListener(this);
        bajaEstilo.addActionListener(this);
        modEstilo.addActionListener(this);
        listadoEstilo.addActionListener(this);
        //Elementos Estilos
        barraPrincipal.add(estilos);
        estilos.add(nuevoEstilo);
        if (tipoUsuario == 0) {
            estilos.add(bajaEstilo);
            estilos.add(modEstilo);
            estilos.add(listadoEstilo);
        }

        ventanaMenuPrincipal.setLocationRelativeTo(null);
        ventanaMenuPrincipal.setResizable(false);
        ventanaMenuPrincipal.setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        conexion.apunteLog(usuLog, "Se ha desconectado");
        System.exit(0);

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
        if (e.getSource().equals(nuevoUsuario)){
            new NuevoUsuario(tipoUsuario, usuLog);
        } else if (e.getSource().equals(bajaUsuario)) {
            new BajaUsuario(usuLog);
        } else if (e.getSource().equals(modUsuario)) {
            new ModUsuario(usuLog);
        } else if (e.getSource().equals(listadoUsuario)) {
            new ListadoUsuario(usuLog);
        } else if (e.getSource().equals(nuevoArtista)) {
            new NuevoArtista(usuLog);
        } else if (e.getSource().equals(bajaArtista)) {
            new BajaArtista(usuLog);
        } else if (e.getSource().equals(modArtista)) {
            new ModArtista(usuLog);
        } else if (e.getSource().equals(listadoArtista)) {
            new ListadoArtista(usuLog);
        } else if (e.getSource().equals(nuevoPublicacion)) {
            new NuevoPublicacion(usuLog);
        } else if (e.getSource().equals(bajaPublicacion)) {
            //new BajaPublicacion(usuLog);
        } else if (e.getSource().equals(modPublicacion)) {
            //new ModPublicacion(usuLog);
        } else if (e.getSource().equals(listadoPublicacion)) {
            new ListadoPublicacion(usuLog);
        } else if (e.getSource().equals(nuevoEstilo)) {
            new NuevoEstilo(usuLog);
        } else if (e.getSource().equals(bajaEstilo)) {
            new BajaEstilo(usuLog);
        } else if (e.getSource().equals(modEstilo)) {
            new ModEstilo(usuLog);
        } else if (e.getSource().equals(listadoEstilo)) {
            new ListadoEstilo(usuLog);
        }
    }
}
