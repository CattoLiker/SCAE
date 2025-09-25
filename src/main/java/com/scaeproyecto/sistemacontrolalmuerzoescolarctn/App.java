package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        while (true) {
            File configFile = new File("config.properties");
            boolean needsConfig = !configFile.exists();

            // Si no existe archivo o la conexión falla, pedir configuración
            if (needsConfig || !testConexion()) {
                FXMLLoader configLoader = new FXMLLoader(App.class.getResource("ConfigDB.fxml"));
                Parent configRoot = configLoader.load();

                Stage configStage = new Stage();
                configStage.setTitle("Configurar Base de Datos");
                configStage.setScene(new Scene(configRoot));
                configStage.initModality(Modality.APPLICATION_MODAL);
                configStage.setResizable(false);
                configStage.showAndWait();

                // Si sigue sin haber archivo, salir
                if (!configFile.exists()) {
                    System.exit(0);
                }
            } else {
                break; // conexión exitosa
            }
        }

        // Continúa con la carga normal de la aplicación
        scene = new Scene(loadFXML("MenuInicio"));
        stage.setScene(scene);
        stage.show();
    }

    private boolean testConexion() {
    try (Connection conn = ConeccionDB.getConnection()) {
        System.out.println("Conectado a la BD.");
        String sql = "SHOW TABLES";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("Tablas en la base de datos:");
            while (rs.next()) {
                // El primer campo de cada fila tiene el nombre de la tabla
                System.out.println(" - " + rs.getString(1));
            }
        }
        return true;
    } catch (SQLException ex) {
        System.out.println("Error al conectar o al listar tablas: " + ex.getMessage());
        return false;
    }
}

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
