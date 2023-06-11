package es.studium.artistic;

import java.io.IOException;
import java.net.URISyntaxException;

public class Ayuda {

    Ayuda() {

        String dir = System.getProperty("user.dir").replaceAll("\\\\", "/");
        goToURL("file:///" + dir + "/ManualDeUsuario/Primerospasos.html");

    }

    public static void goToURL(String URL) {
        if (java.awt.Desktop.isDesktopSupported()) {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

            if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                try {
                    java.net.URI uri = new java.net.URI(URL);
                    desktop.browse(uri);
                } catch (URISyntaxException | IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
