/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author abiga
 */
public class AccederComidaController implements Initializable {

    @FXML
    private Button btnOk;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void ok(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MenuInicioUser.fxml"));
            Parent root = fxmlLoader.load();
            // Obtener el Stage actual desde el botón o cualquier nodo que disparó el evento
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Reemplazar la escena
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
