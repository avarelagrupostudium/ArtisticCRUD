package es.studium.artistic;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;


public class Conexion {
    //Elementos de conexión a la base de datos
    String driver = "com.mysql.cj.jdbc.Driver";//Driver nativo 4 de de MySQL
    String url = "jdbc:mysql://localhost:3306/artisticcrud";//Ubicación y nombre de la base de datos
    String user = "adminArtisticCRUD";//Usuario
    String password = "Studium2023;";
    Connection connection = null;//Objeto para conectar
    Statement statement = null;//Objeto para lanzar sentencial SQL
    ResultSet resultSet = null;//Objeto que recoge la información de la BD
    ResultSet resultSet2 = null;
    Statement statement2 = null;

    Conexion() {
        connection = this.conexion();
    }
    //Usuario Logado.
    String usu = "";

    public Connection conexion() {
        try {
            //Cargar los controladores para el acceso a la BD
            Class.forName(driver);
            //EStablecer la conexión con la BD empresa
            return (DriverManager.getConnection(url, user, password));

        } catch (Exception e) {
            apunteLog(usu ,"Error: " + e.getMessage());
        }
        return null;
    }

    public boolean comprobarCredenciales(String usuario, String password) {
        String sentencia = "SELECT * FROM usuarios WHERE alias = '" + usuario + "' AND `password` = SHA2('" + password + "', 256);";
        usu = usuario;
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            if (resultSet.next()) {
                apunteLog(usu,"Se ha conectado");
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            apunteLog(usu ,"Error: " + e.getMessage());
            return false;
        }
    }

    public String[] obtenerUsuario(String usuario) {
        String sentencia = "SELECT * FROM usuarios WHERE alias = '" + usuario + "';";
        String[] resultado = new String[2];
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            resultSet.next();
            resultado[0] = Integer.toString(resultSet.getInt("tipoUsuario"));
            resultado[1] = resultSet.getString("alias");
            return resultado;
        } catch (SQLException e) {
            apunteLog(usu ,"Error: " + e.getMessage());
        }
        return resultado;
    }

    public boolean insertarUsuario(String nombre, String password, String email, int tipoUsuario,String usuLog) {
        String sentencia = "INSERT INTO usuarios VALUES(null,'" + tipoUsuario + "','"+email+"','"+nombre+"',SHA2('" + password + "', 256));";
        try {
            //Crear una sentencia
            statement = connection.prepareStatement(sentencia);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            statement.executeUpdate(sentencia);
            apunteLog(usuLog,"[Alta de Usuario "+sentencia+"]");
            return true;

        } catch (SQLException e) {
            apunteLog(usuLog ,"Error: " + e.getMessage());
            return false;
        }
    }

    public void rellenarChoiceUsuarios(Choice choUsuario) {
        choUsuario.removeAll();
        choUsuario.add("----------Elegir un usuario...----------");
        String sentencia = "SELECT idUsuario,alias FROM usuarios ORDER BY idUsuario;";
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            while (resultSet.next()) {
                choUsuario.add(resultSet.getInt("idUsuario") + "-" + resultSet.getString("alias"));
            }
        } catch (SQLException e) {
            apunteLog(usu ,"Error: " + e.getMessage());
        }
    }

    public boolean eliminarUsuario(String id ,String usuLog) {
        String sentencia = "DELETE FROM usuarios WHERE idUsuario="+id+";";
        try {
            //Crear una sentencia
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            int del = statement.executeUpdate(sentencia);
            apunteLog(usuLog,"[Eleminar usuario: "+sentencia+"]");
            if (del != 0){
                return true;
            }else {
                return false;
            }

        } catch (SQLException e) {
            apunteLog(usuLog ,"Error: " + e.getMessage());
            return false;
        }
    }

    public Usuario rellenarDatosEditarUsuario(String part) {
        String sentencia = "SELECT idUsuario,alias,correo,tipoUsuario FROM usuarios WHERE idUsuario = " + Integer.parseInt(part) + ";";
        Usuario u = new Usuario();
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            resultSet.next();
            u.setIdUsuario(resultSet.getInt("idUsuario"));
            u.setTipoUsuario(resultSet.getInt("tipoUsuario"));
            u.setNombre(resultSet.getString("alias"));
            u.setEmail(resultSet.getString("correo"));
            return u;
        } catch (SQLException e) {
            apunteLog(usu ,"Error: " + e.getMessage());
            return u;
        }
    }

    public int updatePassword(String text, int idUsuario, String usuLog) {
        String sentencia = "UPDATE usuarios SET password = SHA2('"+text+"', 256) WHERE idUsuario = "+idUsuario+";";
        int r = 0;
        try {
            //Crear una sentencia
            statement = connection.prepareStatement(sentencia);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            r =statement.executeUpdate(sentencia);
            apunteLog(usuLog,"[Modificar contraseña: "+sentencia+"]");
            return r;


        } catch (SQLException e) {
            apunteLog(usuLog ,"Error: " + e.getMessage());
            return r;
        }
    }

    public int updateUsuario(String nombre, String correo, int idUsuario ,int tipo, String usuLog) {
        String sentencia = "UPDATE usuarios SET alias ='"+nombre+"',correo ='"+correo+"', tipoUsuario = "+tipo+" WHERE idUsuario = "+idUsuario+";";
        int r = 0;
        //System.out.println(sentencia);
        try {
            //Crear una sentencia
            statement = connection.prepareStatement(sentencia);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            r = statement.executeUpdate(sentencia);
            apunteLog(usuLog,"[Modificar usuario: "+sentencia+"]");
            return r;

        } catch (SQLException e) {
            apunteLog(usuLog ,"Error: " + e.getMessage());
            return r;
        }
    }

    public String listadoUsuario(String usuLog) {
        String sentencia = "SELECT idUsuario,tipoUsuario,alias,correo FROM usuarios ORDER BY idUsuario;";
        String tipoU = "";
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            apunteLog(usuLog,"[Listado de usuarios: "+sentencia+"]");
            String resultado ="";
            while (resultSet.next()) {

                if(resultSet.getInt("tipoUsuario") == 0){
                    tipoU = "Admin";
                } else if (resultSet.getInt("tipoUsuario") == 1) {
                    tipoU = "Normal";
                }

                if (resultado.length()==0) {
                    resultado = (resultSet.getInt("idUsuario") + "\t"  + tipoU + "\t" + resultSet.getString("alias") +
                            "\t\t" + resultSet.getString("correo"));
                } else {
                    resultado = resultado + "\n" + (resultSet.getInt("idUsuario") + "\t" + tipoU + "\t" +  resultSet.getString("alias") +
                            "\t\t" + resultSet.getString("correo"));
                }


            }
            return resultado;

        } catch (SQLException e) {
            apunteLog(usuLog ,"Error: " + e.getMessage());
        }
        return null;
    }

    public boolean insertarEstilo(String estilo, String usuLog) {
        String sentencia = "INSERT INTO estilos VALUES(null,'"+estilo+"');";

        try {
            //Crear una sentencia
            statement = connection.prepareStatement(sentencia);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            statement.executeUpdate(sentencia);
            apunteLog(usuLog,"[Alta estilo: "+sentencia+"]");
            return true;

        } catch (SQLException e) {
            apunteLog(usuLog ,
                    "Error: " + e.getMessage());
            return false;
        }
    }

    public void rellenarChoiceEstilos(Choice choEstilo) {
        choEstilo.removeAll();
        choEstilo.add("----------Elegir un estilo...----------");
        String sentencia = "SELECT idEstilos,nombre FROM estilos ORDER BY idEstilos;";
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            while (resultSet.next()) {
                choEstilo.add(resultSet.getInt("idEstilos") + "-" + resultSet.getString("nombre"));
            }
        } catch (SQLException e) {
            apunteLog(usu ,"Error: " + e.getMessage());
        }
    }

    public boolean eliminarEstilo(String id ,String usuLog) {
        String sentencia = "DELETE FROM estilos WHERE idEstilos="+id+";";
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            int del = statement.executeUpdate(sentencia);
            apunteLog(usuLog,"[Eliminar estilo: "+sentencia+"]");
            if (del != 0){
                return true;
            }else {
                return false;
            }

        } catch (SQLException e) {
            apunteLog(usuLog ,"Error: " + e.getMessage());
            return false;
        }
    }

    public String[] rellenarDatosEditarEstilo(String part) {
        String sentencia = "SELECT idEstilos,nombre FROM estilos WHERE idEstilos = " + Integer.parseInt(part) + ";";
        String[] datosEstilo = new String[2];
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            resultSet.next();
            datosEstilo[0] = Integer.toString(resultSet.getInt("idEstilos"));
            datosEstilo[1] = resultSet.getString("nombre");

            return datosEstilo;
        } catch (SQLException e) {
            apunteLog(usu ,"Error: " + e.getMessage());
            return datosEstilo;
        }
    }

    public int updateEstilo(String[] datosEstilo, String usuLog) {
        String sentencia = "UPDATE estilos SET nombre ='"+datosEstilo[1]+"' WHERE idEstilos = "+datosEstilo[0]+";";
        int r = 0;
        //System.out.println(sentencia);
        try {
            //Crear una sentencia
            statement = connection.prepareStatement(sentencia);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            r = statement.executeUpdate(sentencia);
            apunteLog(usuLog,"[Modificar estilo: "+sentencia+"]");
            return r;

        } catch (SQLException e) {
            apunteLog(usuLog ,"Error: " + e.getMessage());
            return r;
        }

    }

    public String listadoEstilos(String usuLog) {
        String sentencia = "SELECT idEstilos,nombre FROM estilos ORDER BY idEStilos;";
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            apunteLog(usuLog,"[Listado estilos: "+sentencia+"]");
            String resultado ="";
            while (resultSet.next()) {

                if (resultado.length()==0) {
                    resultado = (resultSet.getInt("idEstilos") + "\t" + resultSet.getString("nombre"));
                } else {
                    resultado = resultado + "\n" + (resultSet.getInt("idEstilos") + "\t" +  resultSet.getString("nombre"));
                }
            }
            return resultado;

        } catch (SQLException e) {
            apunteLog(usuLog ,"Error: " + e.getMessage());
        }
        return null;
    }
    public  void apunteLog(String u, String mensaje) {
        try {
            //Abrir el fichero para añadir
            FileWriter fw = new FileWriter("fichero.log", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            //Sacamos fecha y hora del sistema
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String fecha = dtf.format(LocalDateTime.now());
            //Añadimos el apunte
            pw.println("[ " + fecha + " ] (" + u + ") " + mensaje);
            //Cerrar el fichero
            pw.close();
            bw.close();
            fw.close();
        }catch (IOException ioe){
            System.out.println("Error en el fichero");
        }
    }
    public void usuarioListadoPDF(String usuLog){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH_mm_ss");
        String fecha = dtf.format(LocalDateTime.now());
        String dest = fecha + "Listado Usuarios.pdf";
        try {
            //Inicializamos PDF writes
            PdfWriter writer = new PdfWriter(dest);
            //Inicializamos ODF document
            PdfDocument pdf = new PdfDocument(writer);
            //Inicialize document
            Document document = new Document(pdf);
            document.setMargins(50,25,25,25);
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2, 3, 5})).useAllAvailableWidth();
            table = rellenarTablaPdfUsuario(table);
            document.add(table);
            document.close();
            Desktop.getDesktop().open(new File(dest));
            apunteLog(usuLog,"[Listado usuarios exportados a PDF]");
        } catch (IOException ei) {
            apunteLog(usu ,"Error: " + ei.getMessage());
        }

    }

    private Table rellenarTablaPdfUsuario(Table table)  {
        String sentencia = "SELECT idUsuario,tipoUsuario,alias,correo FROM usuarios ORDER BY idUsuario;";
        String tipoU = "";
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            table.addHeaderCell("Id");
            table.addHeaderCell("Rol");
            table.addHeaderCell("Nombre");
            table.addHeaderCell("Email");
            while (resultSet.next()) {

                if(resultSet.getInt("tipoUsuario") == 0){
                    tipoU = "Admin";
                } else if (resultSet.getInt("tipoUsuario") == 1) {
                    tipoU = "Normal";
                }
                table.addCell(Integer.toString(resultSet.getInt("idUsuario")));
                table.addCell(tipoU);
                table.addCell(resultSet.getString("alias"));
                table.addCell(resultSet.getString("correo"));
            }
            return table;

        } catch (SQLException e) {
            apunteLog(usu ,"Error: " + e.getMessage());
        }
        return null;
    }
    public void usuarioListadoExcel(String usuLog){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH_mm_ss");
        String fecha = dtf.format(LocalDateTime.now());
        String nombreArchivo = fecha + "Listado Usuarios.xlsx";
        String hoja = "Listado Usuarios";
        String rutaArchivo= System.getProperty("user.dir")+"\\FicherosExcel\\"+nombreArchivo;
        XSSFWorkbook libro = new XSSFWorkbook();
        XSSFSheet hoja1 = libro.createSheet(hoja);
        //Cabeceras hoja de Excel


        //Contenido hoja de excel
        hoja1 = rellenarExcelUsuario(hoja1,libro);
        apunteLog(usuLog,"[Listado usuarios exportados a XLSX]");
        File file;
        file = new File(rutaArchivo);
        try (FileOutputStream fileOuS = new FileOutputStream(file))
        {
            if (file.exists())
            { // Si el archivo ya existe, se elimina
                file.delete();
                System.out.println("Archivo eliminado");
            }
            // Se guarda la información en el fichero
            libro.write(fileOuS);
            fileOuS.flush();
            fileOuS.close();
            System.out.println("Archivo Creado");
            libro.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("El archivo no se encuentra o está en uso...");
        }
        catch (IOException e)
        {

            e.printStackTrace();
        }

    }



    private XSSFSheet rellenarExcelUsuario(XSSFSheet hoja, XSSFWorkbook libro) {
        String sentencia = "SELECT idUsuario,tipoUsuario,alias,correo FROM usuarios ORDER BY idUsuario;";
        String tipoU = "";
        String[] cabecera = {"Id","Rol","Nombre","Correo"};
        // Poner en negrita la cabecera
        CellStyle style = libro.createCellStyle();
        XSSFFont font = libro.createFont();
        font.setBold(true);
        style.setFont(font);
        int fila = 1;
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            // Se crean las filas
            XSSFRow row = hoja.createRow(0);

            for (int j = 0; j<cabecera.length; j++) {
                XSSFCell celdaCabecera = row.createCell(j);
                celdaCabecera.setCellStyle(style);
                celdaCabecera.setCellValue(cabecera[j]);
            }

            while (resultSet.next()) {
                row = hoja.createRow(fila);
                fila++;
                XSSFCell id = row.createCell(0);
                id.setCellValue(Integer.toString(resultSet.getInt("idUsuario")));
                if(resultSet.getInt("tipoUsuario") == 0){
                    tipoU = "Admin";
                } else if (resultSet.getInt("tipoUsuario") == 1) {
                    tipoU = "Normal";
                }
                XSSFCell rol = row.createCell(1);
                rol.setCellValue(tipoU);
                XSSFCell nombre = row.createCell(2);
                nombre.setCellValue(resultSet.getString("alias"));
                XSSFCell email = row.createCell(3);
                email.setCellValue(resultSet.getString("correo"));
            }
            return hoja;

        } catch (SQLException e) {
            apunteLog(usu ,"Error: " + e.getMessage());
        }
        return null;
    }


    public boolean insertarArtista(String nombre, String tlf, String instagram, String web, String bio, String usuLog) {
        String sentencia = "INSERT INTO artistas VALUES((SELECT idUsuario FROM usuarios WHERE alias = '"+nombre+"'),'"+tlf+"','"+instagram+"','"+web+"','"+bio+"');";

        try {
            //Crear una sentencia
            statement = connection.prepareStatement(sentencia);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            statement.executeUpdate(sentencia);
            apunteLog(usuLog,"[Alta artista: "+sentencia+"]");
            return true;

        } catch (SQLException e) {
            apunteLog(usuLog ,"Error: " + e.getMessage());
            return false;
        }
    }

    public void insertarEstilosArtistas(String nombre, int idEstilo) {
        String sentencia = "INSERT INTO estilos_artistas VALUES("+idEstilo+",(SELECT idUsuario FROM usuarios WHERE alias = '"+nombre+"'));";

        try {
            //Crear una sentencia
            statement = connection.prepareStatement(sentencia);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            statement.executeUpdate(sentencia);

        } catch (SQLException e) {
            apunteLog(usu,"Error: " + e.getMessage());
        }
    }

    public String listadoArtista(String usuLog) {
        int idUsuActual = 0;
        String sentencia = "SELECT idUsuarioFK,alias,correo,tlf,instagram,web,biografia FROM artistas INNER JOIN usuarios ON artistas.idUsuarioFK = usuarios.idUsuario;";
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            String resultado ="";
            String estilos = "";
            while (resultSet.next()) {
                idUsuActual = (resultSet.getInt("idUsuarioFK"));
                String sentenciaEstilos = "SELECT artistas.idUsuarioFK, estilos.nombre FROM artistas JOIN estilos_artistas ON artistas.idUsuarioFK = estilos_artistas.idArtistasFK JOIN estilos ON estilos.idEstilos = estilos_artistas.idEstilosFK WHERE artistas.idUsuarioFK = "+idUsuActual+";";

                if (resultado.length()==0) {
                    resultado = (resultSet.getInt("idUsuarioFK") + "\t" + resultSet.getString("alias")+"\t\t" + resultSet.getString("correo")+"\t" +resultSet.getString("tlf")
                            +"\t" +resultSet.getString("instagram")+"\t\t" +resultSet.getString("web")+"\t" +resultSet.getString("biografia"));
                } else {
                    resultado = resultado + "\n" + (resultSet.getInt("idUsuarioFK") + "\t" + resultSet.getString("alias")+"\t" + resultSet.getString("correo")+"\t" +resultSet.getString("tlf")
                            +"\t" +resultSet.getString("instagram")+"\t" +resultSet.getString("web")+"\t" +resultSet.getString("biografia")+"\t\t");;
                }
                resultSet2 = statement2.executeQuery(sentenciaEstilos);
                while (resultSet2.next()){
                    if (estilos.length()==0){
                        estilos = resultSet2.getString("nombre");
                    }else {
                        estilos = estilos + ", " + resultSet2.getString("nombre");
                    }
                }
                resultado = resultado + "\t" +estilos;
            }
            apunteLog(usuLog,"[Listado de artistas: "+sentencia);
            return resultado;

        } catch (SQLException e) {
            apunteLog(usuLog ,"Error: " + e.getMessage());
        }
        return null;
    }

    public void artistaListadoPDF(String usuLog) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH_mm_ss");
        String fecha = dtf.format(LocalDateTime.now());
        String dest = fecha + "Listado Artista.pdf";
        try {
            //Inicializamos PDF writes
            PdfWriter writer = new PdfWriter(dest);
            //Inicializamos ODF document
            PdfDocument pdf = new PdfDocument(writer);
            //Inicialize document
            Document document = new Document(pdf, PageSize.A4.rotate());
            document.setMargins(50,25,25,25);
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2, 2, 2,2,2,3,3})).useAllAvailableWidth();
            table = rellenarTablaPdfArtista(table);
            document.add(table);
            document.close();
            Desktop.getDesktop().open(new File(dest));
            apunteLog(usuLog,"[Listado artistas exportados a PDF]");
        } catch (IOException ei) {
            apunteLog(usu ,"Error: " + ei.getMessage());
        }
    }

    private Table rellenarTablaPdfArtista(Table table) {
        int idUsuActual = 0;
        String sentencia = "SELECT idUsuarioFK,alias,correo,tlf,instagram,web,biografia FROM artistas INNER JOIN usuarios ON artistas.idUsuarioFK = usuarios.idUsuario;";
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            String estilos = "";
            table.addHeaderCell("Id");
            table.addHeaderCell("Nombre");
            table.addHeaderCell("Correo");
            table.addHeaderCell("Tlf");
            table.addHeaderCell("Instagram");
            table.addHeaderCell("Web");
            table.addHeaderCell("Biofrafía");
            table.addHeaderCell("Estilos");
            while (resultSet.next()) {
                idUsuActual = (resultSet.getInt("idUsuarioFK"));
                String sentenciaEstilos = "SELECT artistas.idUsuarioFK, estilos.nombre FROM artistas JOIN estilos_artistas ON artistas.idUsuarioFK = estilos_artistas.idArtistasFK JOIN estilos ON estilos.idEstilos = estilos_artistas.idEstilosFK WHERE artistas.idUsuarioFK = "+idUsuActual+";";

                table.addCell(Integer.toString(resultSet.getInt("idUsuarioFK")));
                table.addCell(resultSet.getString("alias"));
                table.addCell(resultSet.getString("correo"));
                table.addCell(resultSet.getString("tlf"));
                table.addCell(resultSet.getString("instagram"));
                table.addCell(resultSet.getString("web"));
                table.addCell(resultSet.getString("biografia"));
                resultSet2 = statement2.executeQuery(sentenciaEstilos);
                while (resultSet2.next()){
                    if (estilos.length()==0){
                        estilos = resultSet2.getString("nombre");
                    }else {
                        estilos = estilos + ", " + resultSet2.getString("nombre");
                    }
                }
                table.addCell(estilos);
            }
            return table;

        } catch (SQLException e) {
            apunteLog(usu ,"Error: " + e.getMessage());
        }
        return null;
    }

    public void artistaListadoExcel(String usuLog) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH_mm_ss");
        String fecha = dtf.format(LocalDateTime.now());
        String nombreArchivo = fecha + "Listado Artistas.xlsx";
        String hoja = "Listado Artistas";
        String rutaArchivo= System.getProperty("user.dir")+"\\FicherosExcel\\"+nombreArchivo;
        XSSFWorkbook libro = new XSSFWorkbook();
        XSSFSheet hoja1 = libro.createSheet(hoja);
        //Cabeceras hoja de Excel


        //Contenido hoja de excel
        hoja1 = rellenarExcelArtista(hoja1,libro);
        apunteLog(usuLog,"[Listado Artistas exportados a XLSX]");
        File file;
        file = new File(rutaArchivo);
        try (FileOutputStream fileOuS = new FileOutputStream(file))
        {
            if (file.exists())
            { // Si el archivo ya existe, se elimina
                file.delete();
                System.out.println("Archivo eliminado");
            }
            // Se guarda la información en el fichero
            libro.write(fileOuS);
            fileOuS.flush();
            fileOuS.close();
            System.out.println("Archivo Creado");
            libro.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("El archivo no se encuentra o está en uso...");
        }
        catch (IOException e)
        {

            e.printStackTrace();
        }
    }

    private XSSFSheet rellenarExcelArtista(XSSFSheet hoja, XSSFWorkbook libro) {
        int idUsuActual = 0;
        String sentencia = "SELECT idUsuarioFK,alias,correo,tlf,instagram,web,biografia FROM artistas INNER JOIN usuarios ON artistas.idUsuarioFK = usuarios.idUsuario;";
        String[] cabecera = {"Id","Nombre","Correo","Tlf","Instagram","Web","Biografía","Estilos"};
        // Poner en negrita la cabecera
        CellStyle style = libro.createCellStyle();
        XSSFFont font = libro.createFont();
        font.setBold(true);
        style.setFont(font);
        int fila = 1;
        String estilos = "";
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            // Se crean las filas
            XSSFRow row = hoja.createRow(0);

            for (int j = 0; j<cabecera.length; j++) {
                XSSFCell celdaCabecera = row.createCell(j);
                celdaCabecera.setCellStyle(style);
                celdaCabecera.setCellValue(cabecera[j]);
            }
            while (resultSet.next()) {
                row = hoja.createRow(fila);
                fila++;
                idUsuActual = (resultSet.getInt("idUsuarioFK"));
                String sentenciaEstilos = "SELECT artistas.idUsuarioFK, estilos.nombre FROM artistas JOIN estilos_artistas ON artistas.idUsuarioFK = estilos_artistas.idArtistasFK JOIN estilos ON estilos.idEstilos = estilos_artistas.idEstilosFK WHERE artistas.idUsuarioFK = "+idUsuActual+";";
                XSSFCell id = row.createCell(0);
                id.setCellValue(Integer.toString(resultSet.getInt("idUsuarioFK")));
                XSSFCell nombre = row.createCell(1);
                nombre.setCellValue(resultSet.getString("alias"));
                XSSFCell correo = row.createCell(2);
                correo.setCellValue(resultSet.getString("correo"));
                XSSFCell tlf = row.createCell(3);
                tlf.setCellValue(resultSet.getString("tlf"));
                XSSFCell insta = row.createCell(4);
                insta.setCellValue(resultSet.getString("instagram"));
                XSSFCell web = row.createCell(5);
                web.setCellValue(resultSet.getString("web"));
                XSSFCell bio = row.createCell(6);
                bio.setCellValue(resultSet.getString("biografia"));
                resultSet2 = statement2.executeQuery(sentenciaEstilos);
                while (resultSet2.next()){
                    if (estilos.length()==0){
                        estilos = resultSet2.getString("nombre");
                    }else {
                        estilos = estilos + ", " + resultSet2.getString("nombre");
                    }
                }
                XSSFCell est = row.createCell(7);
                est.setCellValue(estilos);
            }
            return hoja;

        } catch (SQLException e) {
            apunteLog(usu ,"Error: " + e.getMessage());
        }
        return null;
    }

    public void rellenarChoiceArtistas(Choice choArtista) {
        choArtista.removeAll();
        choArtista.add(" ---------- Elegir un Artista... ---------- ");
        String sentencia = "SELECT idUsuarioFK,alias FROM artistas INNER JOIN usuarios ON artistas.idUsuarioFK = usuarios.idUsuario;";
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            while (resultSet.next()) {
                choArtista.add(resultSet.getInt("idUsuarioFK") + "-" + resultSet.getString("alias"));
            }
        } catch (SQLException e) {
            apunteLog(usu ,"Error: " + e.getMessage());
        }
    }

    public boolean eliminarArtista(String id, String usuLog) {
        String sentencia = "DELETE FROM estilos_artistas WHERE idArtistasFK="+id+";";
        String sentencia2 = "DELETE FROM artistas WHERE idUsuarioFK="+id+";";
        String sentencia3 = "DELETE FROM usuarios WHERE idUsuario="+id+";";
        try {
            //Crear una sentencia
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            int del = statement.executeUpdate(sentencia);
            int del2 = statement.executeUpdate(sentencia2);
            int del3 = statement.executeUpdate(sentencia3);
            apunteLog(usuLog,"[Eleminar Artista: "+sentencia+"\\|"+sentencia2+"\\|"+sentencia3+"]");
            if (del > 0 && del2 > 0 && del3 > 0){
                return true;
            }else return false;

        } catch (SQLException e) {
            apunteLog(usuLog ,"Error: " + e.getMessage());
            return false;
        }
    }

    public Artista rellenarDatosEditarArtista(Artista artista) {
        String sentencia = "SELECT alias,correo,tlf,instagram,web,biografia FROM artistas INNER JOIN usuarios ON artistas.idUsuarioFK = usuarios.idUsuario WHERE idUsuarioFK = "+artista.getIdUsuario()+";";
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            resultSet.next();
            artista.setNombre(resultSet.getString("alias"));
            artista.setEmail(resultSet.getString("correo"));
            artista.setTlf(resultSet.getString("tlf"));
            artista.setInstagram(resultSet.getString("instagram"));
            artista.setWeb(resultSet.getString("web"));
            artista.setBiografia(resultSet.getString("biografia"));

            return artista;
        } catch (SQLException e) {
            apunteLog(usu ,"Error: " + e.getMessage());
            return artista;
        }
    }
    public int updateArtista(Artista artista, String usuLog) {
        String sentencia = "UPDATE usuarios SET alias ='"+artista.getNombre()+"',correo ='"+artista.getEmail()+"' WHERE idUsuario = "+artista.getIdUsuario()+";";
        String sentencia2 ="UPDATE artistas SET tlf = '"+artista.getTlf()+"',instagram = '"+artista.getInstagram()+"',web = '"+artista.getWeb()+"',biografia = '"+artista.getBiografia()+"' WHERE idUsuarioFK = "+artista.getIdUsuario()+";";
        int r = 0;
        try {
            //Crear una sentencia
            statement = connection.prepareStatement(sentencia);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            r =statement.executeUpdate(sentencia);
            r = r + statement.executeUpdate(sentencia2);
            apunteLog(usuLog,"[Modificar Artista: "+sentencia+"\\|"+sentencia2+"]");
            return r;


        } catch (SQLException e) {
            apunteLog(usuLog ,"Error: " + e.getMessage());
            return r;
        }

    }
    public String rellenarDatosEditarEstilosArtista(int idUsuario) {
        String sentencia = "SELECT estilos.idEstilos, estilos.nombre FROM artistas JOIN estilos_artistas ON artistas.idUsuarioFK = estilos_artistas.idArtistasFK JOIN estilos ON estilos.idEstilos = estilos_artistas.idEstilosFK WHERE artistas.idUsuarioFK = "+idUsuario+";";
        String resultado = "";
        try {
            //Crear una sentencia
            statement = connection.prepareStatement(sentencia);
            resultSet = statement.executeQuery(sentencia);
            while (resultSet.next()){
                if (resultado.length()==0){
                    resultado = resultSet.getInt("estilos.idEstilos") + "-" + resultSet.getString("estilos.nombre") + "-\n";
                }else {
                    resultado = resultado + resultSet.getInt("estilos.idEstilos") + "-" + resultSet.getString("estilos.nombre") + "-\n";
                }
            }
            return resultado;
        }catch (SQLException e){
            apunteLog(usu ,"Error: " + e.getMessage());
            return null;
        }

    }
    public int updateEstilosArtista(int idArt, String[] partsEstilos, String usuLog) {
        String sentencia = "DELETE FROM estilos_artistas WHERE idArtistasFK = "+idArt+";";
        int idEstilo = 0;
        int r = 0;
        try {
            //Crear una sentencia
            statement = connection.prepareStatement(sentencia);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            r = statement.executeUpdate(sentencia);
            for (int i = 0; i < partsEstilos.length; i +=2) {
                idEstilo = Integer.parseInt(partsEstilos[i]);
                String sentencia2 = "INSERT INTO estilos_artistas VALUES("+idEstilo+","+idArt+");";
                r = r + statement.executeUpdate(sentencia2);
                apunteLog(usuLog,"[Modificar estilo: "+sentencia+"\\|"+sentencia2+"]");
            }
            return r;
        } catch (SQLException e) {
            apunteLog(usuLog,"Error: " + e.getMessage());
        }
        return r;
    }

    public boolean insertarPublicacion(String idArtista, String idEstilo, String fecha, String text, String usuLog) {
        String sentencia = "INSERT INTO publicaciones VALUES(null,"+idArtista+","+idEstilo+",'"+fecha+"','"+text+"');";

        try {
            //Crear una sentencia
            statement = connection.prepareStatement(sentencia);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            statement.executeUpdate(sentencia);
            return true;

        } catch (SQLException e) {
            apunteLog(usuLog,"Error: " + e.getMessage());
            return false;
        }
    }

    public String listadoPublicacion(String usuLog) {
        String sentencia = "SELECT \n" +
                "\tpublicaciones.idPublicacion,\n" +
                "\tusuarios.alias,\n" +
                "    estilos.nombre, \n" +
                "    DATE_FORMAT(publicaciones.fecha, '%d/%m/%Y') AS 'fecha', \n" +
                "    publicaciones.texto\n" +
                "FROM\n" +
                "\tpublicaciones\n" +
                "\t\tINNER JOIN\n" +
                "\t\t\tartistas ON publicaciones.idArtistaFK = artistas.idUsuarioFK\n" +
                "\t\t\t\tINNER JOIN\n" +
                "\t\t\t\t\tusuarios ON artistas.idUsuarioFK = usuarios.idUsuario\n" +
                "\t\t\t\t\t\tINNER JOIN\n" +
                "\t\t\t\t\t\t\testilos ON publicaciones.idEstiloFK = estilos.idEstilos;";
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            apunteLog(usuLog,"[Listado de publicaciones: "+sentencia+"]");
            String resultado ="";
            while (resultSet.next()) {


                if (resultado.length()==0) {
                    resultado = (resultSet.getInt("publicaciones.idPublicacion") + "\t" + resultSet.getString("usuarios.alias") +
                            "\t\t" + resultSet.getString("estilos.nombre") + "\t"+ resultSet.getString("fecha") + "\t" + resultSet.getString("publicaciones.texto"));
                } else {
                    resultado = resultado + "\n" + (resultSet.getInt("publicaciones.idPublicacion") + "\t" + resultSet.getString("usuarios.alias") +
                            "\t\t" + resultSet.getString("estilos.nombre") + "\t"+ resultSet.getString("fecha") + "\t" + resultSet.getString("publicaciones.texto"));
                }


            }
            return resultado;

        } catch (SQLException e) {
            apunteLog(usuLog ,"Error: " + e.getMessage());
        }
        return null;
    }

    public void publicacionListadoPDF(String usuLog) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH_mm_ss");
        String fecha = dtf.format(LocalDateTime.now());
        String dest = fecha + "Listado Publicaciones.pdf";
        try {
            //Inicializamos PDF writes
            PdfWriter writer = new PdfWriter(dest);
            //Inicializamos ODF document
            PdfDocument pdf = new PdfDocument(writer);
            //Inicialize document
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(50,25,25,25);
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 4, 4, 4,6})).useAllAvailableWidth();
            table = rellenarTablaPdfPublicacion(table);
            document.add(table);
            document.close();
            Desktop.getDesktop().open(new File(dest));
            apunteLog(usuLog,"[Listado publicaciones exportados a PDF]");
        } catch (IOException ei) {
            apunteLog(usu ,"Error: " + ei.getMessage());
        }
    }

    private Table rellenarTablaPdfPublicacion(Table table) {
        String sentencia = "SELECT \n" +
                "\tpublicaciones.idPublicacion,\n" +
                "\tusuarios.alias,\n" +
                "    estilos.nombre, \n" +
                "    DATE_FORMAT(publicaciones.fecha, '%d/%m/%Y') AS 'fecha', \n" +
                "    publicaciones.texto\n" +
                "FROM\n" +
                "\tpublicaciones\n" +
                "\t\tINNER JOIN\n" +
                "\t\t\tartistas ON publicaciones.idArtistaFK = artistas.idUsuarioFK\n" +
                "\t\t\t\tINNER JOIN\n" +
                "\t\t\t\t\tusuarios ON artistas.idUsuarioFK = usuarios.idUsuario\n" +
                "\t\t\t\t\t\tINNER JOIN\n" +
                "\t\t\t\t\t\t\testilos ON publicaciones.idEstiloFK = estilos.idEstilos;";
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            table.addHeaderCell("Id");
            table.addHeaderCell("Nombre_artista");
            table.addHeaderCell("Estilo");
            table.addHeaderCell("Fecha");
            table.addHeaderCell("Texto");

            while (resultSet.next()) {
                table.addCell(Integer.toString(resultSet.getInt("publicaciones.idPublicacion")));
                table.addCell(resultSet.getString("usuarios.alias"));
                table.addCell(resultSet.getString("estilos.nombre"));
                table.addCell(resultSet.getString("fecha"));
                table.addCell(resultSet.getString("publicaciones.texto"));
            }
            return table;

        } catch (SQLException e) {
            apunteLog(usu ,"Error: " + e.getMessage());
        }
        return null;
    }

    public void publicacionListadoExcel(String usuLog) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH_mm_ss");
        String fecha = dtf.format(LocalDateTime.now());
        String nombreArchivo = fecha + "Listado Publicaciones.xlsx";
        String hoja = "Listado Publicaciones";
        String rutaArchivo= System.getProperty("user.dir")+"\\FicherosExcel\\"+nombreArchivo;
        XSSFWorkbook libro = new XSSFWorkbook();
        XSSFSheet hoja1 = libro.createSheet(hoja);
        //Cabeceras hoja de Excel


        //Contenido hoja de excel
        hoja1 = rellenarExcelPublicacion(hoja1,libro);
        apunteLog(usuLog,"[Listado Publicaciones exportados a XLSX]");
        File file;
        file = new File(rutaArchivo);
        try (FileOutputStream fileOuS = new FileOutputStream(file))
        {
            if (file.exists())
            { // Si el archivo ya existe, se elimina
                file.delete();
                System.out.println("Archivo eliminado");
            }
            // Se guarda la información en el fichero
            libro.write(fileOuS);
            fileOuS.flush();
            fileOuS.close();
            System.out.println("Archivo Creado");
            libro.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("El archivo no se encuentra o está en uso...");
        }
        catch (IOException e)
        {

            e.printStackTrace();
        }
    }

    private XSSFSheet rellenarExcelPublicacion(XSSFSheet hoja, XSSFWorkbook libro) {
        String sentencia = "SELECT \n" +
                "\tpublicaciones.idPublicacion,\n" +
                "\tusuarios.alias,\n" +
                "    estilos.nombre, \n" +
                "    DATE_FORMAT(publicaciones.fecha, '%d/%m/%Y') AS 'fecha', \n" +
                "    publicaciones.texto\n" +
                "FROM\n" +
                "\tpublicaciones\n" +
                "\t\tINNER JOIN\n" +
                "\t\t\tartistas ON publicaciones.idArtistaFK = artistas.idUsuarioFK\n" +
                "\t\t\t\tINNER JOIN\n" +
                "\t\t\t\t\tusuarios ON artistas.idUsuarioFK = usuarios.idUsuario\n" +
                "\t\t\t\t\t\tINNER JOIN\n" +
                "\t\t\t\t\t\t\testilos ON publicaciones.idEstiloFK = estilos.idEstilos;";
        String[] cabecera = {"Id","Nombre","Estilo","Fecha","Texto"};
        // Poner en negrita la cabecera
        CellStyle style = libro.createCellStyle();
        XSSFFont font = libro.createFont();
        font.setBold(true);
        style.setFont(font);
        int fila = 1;
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            // Se crean las filas
            XSSFRow row = hoja.createRow(0);

            for (int j = 0; j<cabecera.length; j++) {
                XSSFCell celdaCabecera = row.createCell(j);
                celdaCabecera.setCellStyle(style);
                celdaCabecera.setCellValue(cabecera[j]);
            }
            while (resultSet.next()) {
                row = hoja.createRow(fila);
                fila++;
                XSSFCell id = row.createCell(0);
                id.setCellValue(Integer.toString(resultSet.getInt("publicaciones.idPublicacion")));
                XSSFCell nombre = row.createCell(1);
                nombre.setCellValue(resultSet.getString("usuarios.alias"));
                XSSFCell estilo = row.createCell(2);
                estilo.setCellValue(resultSet.getString("estilos.nombre"));
                XSSFCell fecha = row.createCell(3);
                fecha.setCellValue(resultSet.getString("fecha"));
                XSSFCell texto = row.createCell(4);
                texto.setCellValue(resultSet.getString("texto"));
            }
            return hoja;

        } catch (SQLException e) {
            apunteLog(usu ,"Error: " + e.getMessage());
        }
        return null;
    }

    public void estiloListadoPDF(String usuLog) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH_mm_ss");
        String fecha = dtf.format(LocalDateTime.now());
        String dest = fecha + "Listado Estilos.pdf";
        try {
            //Inicializamos PDF writes
            PdfWriter writer = new PdfWriter(dest);
            //Inicializamos ODF document
            PdfDocument pdf = new PdfDocument(writer);
            //Inicialize document
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(50,250,25,25);
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 4})).useAllAvailableWidth();
            table = rellenarTablaPdfEstilo(table);
            document.add(table);
            document.close();
            Desktop.getDesktop().open(new File(dest));
            apunteLog(usuLog,"[Listado estilos exportados a PDF]");
        } catch (IOException ei) {
            apunteLog(usu ,"Error: " + ei.getMessage());
        }

    }

    private Table rellenarTablaPdfEstilo(Table table) {
        String sentencia = "SELECT idEstilos, nombre FROM estilos;";
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            table.addHeaderCell("Id");
            table.addHeaderCell("Estilo");
            while (resultSet.next()) {
                table.addCell(Integer.toString(resultSet.getInt("idEstilos")));
                table.addCell(resultSet.getString("nombre"));
            }
            return table;
        } catch (SQLException e) {
            apunteLog(usu ,"Error: " + e.getMessage());
        }
        return null;

    }

    public void estiloListadoExcel(String usuLog) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH_mm_ss");
        String fecha = dtf.format(LocalDateTime.now());
        String nombreArchivo = fecha + "Listado Estilos.xlsx";
        String hoja = "Listado Estilos";
        String rutaArchivo= System.getProperty("user.dir")+"\\FicherosExcel\\"+nombreArchivo;
        XSSFWorkbook libro = new XSSFWorkbook();
        XSSFSheet hoja1 = libro.createSheet(hoja);
        //Cabeceras hoja de Excel


        //Contenido hoja de excel
        hoja1 = rellenarExcelEstilo(hoja1,libro);
        apunteLog(usuLog,"[Listado estilos exportados a XLSX]");
        File file;
        file = new File(rutaArchivo);
        try (FileOutputStream fileOuS = new FileOutputStream(file))
        {
            if (file.exists())
            { // Si el archivo ya existe, se elimina
                file.delete();
                System.out.println("Archivo eliminado");
            }
            // Se guarda la información en el fichero
            libro.write(fileOuS);
            fileOuS.flush();
            fileOuS.close();
            System.out.println("Archivo Creado");
            libro.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("El archivo no se encuentra o está en uso...");
        }
        catch (IOException e)
        {

            e.printStackTrace();
        }
    }

    private XSSFSheet rellenarExcelEstilo(XSSFSheet hoja, XSSFWorkbook libro) {
        String sentencia = "SELECT idEstilos, nombre FROM estilos;";
        String[] cabecera = {"Id","Estilos"};
        // Poner en negrita la cabecera
        CellStyle style = libro.createCellStyle();
        XSSFFont font = libro.createFont();
        font.setBold(true);
        style.setFont(font);
        int fila = 1;
        try {
            //Crear una sentencia
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //Crear un objeto ResultSet para guardar lo obtenido
            //y ejecutar la sentencia SQL
            resultSet = statement.executeQuery(sentencia);
            // Se crean las filas
            XSSFRow row = hoja.createRow(0);

            for (int j = 0; j<cabecera.length; j++) {
                XSSFCell celdaCabecera = row.createCell(j);
                celdaCabecera.setCellStyle(style);
                celdaCabecera.setCellValue(cabecera[j]);
            }
            while (resultSet.next()) {
                row = hoja.createRow(fila);
                fila++;
                XSSFCell id = row.createCell(0);
                id.setCellValue(Integer.toString(resultSet.getInt("idEstilos")));
                XSSFCell nombre = row.createCell(1);
                nombre.setCellValue(resultSet.getString("nombre"));
            }
            return hoja;

        } catch (SQLException e) {
            apunteLog(usu ,"Error: " + e.getMessage());
        }
        return null;
    }
}



