package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class RegistroController implements Initializable {

    @FXML
    private Button registro;
    @FXML
    private PasswordField Codigo;
    @FXML
    private TextField nombreUsuario;
    @FXML
    private PasswordField Contra1;
    @FXML
    private PasswordField Contra2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        registro.setOnAction(event -> registrarUsuario());
    }

    private void registrarUsuario() {
        String codigo = Codigo.getText();
        String nombre = nombreUsuario.getText();
        String contrasena1 = Contra1.getText();
        String contrasena2 = Contra2.getText();

        if (codigo.isEmpty() || nombre.isEmpty() || contrasena1.isEmpty() || contrasena2.isEmpty()) {
            mostrarAlerta("Campos vacíos", "Por favor complete todos los campos.");
            return;
        }
        if (!contrasena1.equals(contrasena2)) {
            mostrarAlerta("Contraseña", "Las contraseñas no coinciden.");
            return;
        }
        if (codigo.equals("sUp3rSeCrEt")) {
            try (Connection conn = ConeccionDB.getConnection()) {
                String sql = "INSERT INTO usuario (TipoUsuario, Contrasena, Username) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, 1); // Cambia el tipo de usuario según tu lógica (1 = Estudiante, por ejemplo)
                stmt.setString(2, contrasena1);
                stmt.setString(3, nombre);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    mostrarAlerta("Éxito", "Usuario registrado correctamente.");
                    limpiarCampos();
                } else {
                    mostrarAlerta("Error", "No se pudo registrar el usuario.");
                }
            } catch (SQLException e) {
                mostrarAlerta("Error SQL", e.getMessage());
            }
        } else {
            mostrarAlerta("Error", "El codigo de verificación no es correcto.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        Codigo.clear();
        nombreUsuario.clear();
        Contra1.clear();
        Contra2.clear();
    }

    public void abrirMenuOtro(ActionEvent event, String recurso) throws IOException {
        // Cargar el nuevo FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(recurso));
        Parent root = fxmlLoader.load();

        // Obtener el Stage actual desde el botón o cualquier nodo que disparó el evento
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Reemplazar la escena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void volver(ActionEvent event) throws IOException {
        String inicio = "MenuInicio.fxml";

        try {
            abrirMenuOtro(event, inicio);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void abrirDoc(KeyEvent event) {
        if (event.getCode() == KeyCode.CONTROL) {
            try {
                String docu = "/Documentacion/manual.chm";
                InputStream inputStream = getClass().getResourceAsStream(docu);

                if (inputStream == null) {
                    System.err.println("No se pudo encontrar el archivo: " + docu);
                    mostrarError("Archivo no encontrado", "No se pudo encontrar el manual de ayuda.");
                    return;
                }

                // Crear archivo temporal
                File tempFile = File.createTempFile("manual", ".chm");
                tempFile.deleteOnExit(); // Se eliminará al cerrar la aplicación

                // Copiar el contenido del InputStream al archivo temporal
                try (FileOutputStream out = new FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }

                // Abrir el archivo con la aplicación predeterminada
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(tempFile);
                } else {
                    System.err.println("La acción OPEN no está soportada en este sistema");
                    mostrarError("Error", "No se puede abrir el archivo en este sistema.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                mostrarError("Error", "No se pudo abrir el manual: " + e.getMessage());
            }
        }
    }

    private void mostrarError(String titulo, String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR
        );
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
