package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class ConfigDBController {

    @FXML
    private TextField hostField;
    @FXML
    private TextField puertoField;
    @FXML
    private TextField nombreBDField;
    @FXML
    private TextField usuarioField;
    @FXML
    private PasswordField contrasenaField;

    @FXML
    private void initialize() {
        // Opcional: cargar valores previos si existen.
    }

    @FXML
    private void guardarConfiguracion() {
        String host = hostField.getText().trim();
        String puerto = puertoField.getText().trim();
        String nombreBD = nombreBDField.getText().trim();
        String usuario = usuarioField.getText().trim();
        String contrasena = contrasenaField.getText();

        if (host.isEmpty() || puerto.isEmpty() || nombreBD.isEmpty() || usuario.isEmpty()) {
            mostrarAlerta("Todos los campos son obligatorios (excepto contraseña).", Alert.AlertType.WARNING);
            return;
        }

        String url = String.format("jdbc:mysql://%s:%s/%s", host, puerto, nombreBD);

        ConeccionDB.guardarConfiguracion(url, usuario, contrasena);

        // Intentar conexión real
        try (Connection conn = ConeccionDB.getConnection()) {
            mostrarAlerta("¡Configuración guardada exitosamente!", Alert.AlertType.INFORMATION);
            // Cerrar la ventana de configuración
            Stage stage = (Stage) hostField.getScene().getWindow();
            stage.close();
        } catch (SQLException ex) {
            mostrarAlerta("No se logró la conexión a la base de datos.\nRevisa los datos e inténtalo de nuevo.", Alert.AlertType.ERROR);
            // No cerrar la ventana, permitir al usuario corregir los datos
        }
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Configuración");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}