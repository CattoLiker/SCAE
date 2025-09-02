package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javafx.stage.Stage;

public class MenuCambioController implements Initializable {

    @FXML
    private PasswordField TxtCodigo;
    @FXML
    private PasswordField TxtContra1;
    @FXML
    private PasswordField TxtContra2;
    @FXML
    private Button BtnCambiar;
    @FXML
    private TextField TxtUsername;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        BtnCambiar.setOnAction(event -> cambiarContrasena());
    }

    private void cambiarContrasena() {
        String codigo = TxtCodigo.getText();
        String username = TxtUsername.getText();
        String nuevaContra1 = TxtContra1.getText();
        String nuevaContra2 = TxtContra2.getText();

        if (codigo.isEmpty() || username.isEmpty() || nuevaContra1.isEmpty() || nuevaContra2.isEmpty()) {
            mostrarAlerta("Campos vacíos", "Por favor complete todos los campos.");
            return;
        }
        if (!nuevaContra1.equals(nuevaContra2)) {
            mostrarAlerta("Contraseña", "Las contraseñas no coinciden.");
            return;
        }
        if(codigo.equals("sUp3rSeCrEt")) {
            try (Connection conn = ConeccionDB.getConnection()) {
                // Verifica que el usuario exista
                String sqlVerifica = "SELECT idUsuario FROM Usuario WHERE Username = ?";
                PreparedStatement stmtVerifica = conn.prepareStatement(sqlVerifica);
                stmtVerifica.setString(1, username);
                ResultSet rs = stmtVerifica.executeQuery();

                if (!rs.next()) {
                    mostrarAlerta("Usuario no encontrado", "El nombre de usuario no corresponde a ningún usuario registrado.");
                    return;
                }

                // Actualiza la contraseña
                String sqlUpdate = "UPDATE Usuario SET Contrasena = ? WHERE Username = ?";
                PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
                stmtUpdate.setString(1, nuevaContra1);
                stmtUpdate.setString(2, username);

                int rows = stmtUpdate.executeUpdate();
                if (rows > 0) {
                    mostrarAlerta("Éxito", "La contraseña ha sido cambiada correctamente.");
                    limpiarCampos();
                } else {
                    mostrarAlerta("Error", "No se pudo cambiar la contraseña.");
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
        TxtCodigo.clear();
        TxtUsername.clear();
        TxtContra1.clear();
        TxtContra2.clear();
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
    
}