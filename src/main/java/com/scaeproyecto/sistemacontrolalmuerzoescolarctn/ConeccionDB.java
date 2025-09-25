package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase para gestionar la conexión a la base de datos.
 * Permite la configuración dinámica de los parámetros de conexión mediante un archivo config.properties.
 */
public class ConeccionDB {

    private static final String CONFIG_FILE = "config.properties";
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    // Bloque estático: carga la configuración al iniciar la clase
    static {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
            URL = props.getProperty("url", "jdbc:mysql://localhost:3306/scae");
            USER = props.getProperty("usuario", "root");
            PASSWORD = props.getProperty("contrasena", "");
        } catch (IOException e) {
            // Si no existe el archivo, usar valores por defecto
            URL = "jdbc:mysql://localhost:3306/scae";
            USER = "root";
            PASSWORD = "";
        }
    }

    /**
     * Obtiene una nueva conexión a la base de datos usando los parámetros actuales.
     * @return Connection activa a la base de datos
     * @throws SQLException si ocurre un error de conexión
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Guarda la configuración de la conexión a la base de datos en config.properties.
     * También actualiza los valores estáticos para futuras conexiones en la misma ejecución.
     *
     * @param url        URL completa JDBC de la base de datos
     * @param usuario    Usuario de la base de datos
     * @param contrasena Contraseña de la base de datos
     */
    public static void guardarConfiguracion(String url, String usuario, String contrasena) {
        Properties props = new Properties();
        props.setProperty("url", url);
        props.setProperty("usuario", usuario);
        props.setProperty("contrasena", contrasena != null ? contrasena : "");

        try (OutputStream os = new FileOutputStream(CONFIG_FILE)) {
            props.store(os, "Configuración de la conexión a la base de datos");
            // Actualiza los valores en memoria
            URL = url;
            USER = usuario;
            PASSWORD = contrasena;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}