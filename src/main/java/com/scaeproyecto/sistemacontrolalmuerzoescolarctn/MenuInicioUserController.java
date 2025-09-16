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
import java.time.*;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author abiga
 */
public class MenuInicioUserController implements Initializable {

    LocalTime limite = LocalTime.of(13, 00);
    @FXML
    private Button btnHuella;
    @FXML
    private Button btnCI;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(LocalTime.now().isAfter(limite)){
            btnHuella.setVisible(false);
            btnCI.setVisible(false);
        }
    }    
    
    @FXML
    private void abrirMenuHuella(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("noHayHuella.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.show();
        } catch (IOException ex) {
            System.getLogger(MenuInicioController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void abrirMenuCI(ActionEvent event) {
         try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RegistroCI.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.show();
        } catch (IOException ex) {
            System.getLogger(MenuInicioController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

}
