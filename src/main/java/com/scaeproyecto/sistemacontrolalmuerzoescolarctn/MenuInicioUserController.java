package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author abiga
 */
public class MenuInicioUserController implements Initializable {

    LocalTime limite = LocalTime.of(13, 0);
    LocalTime inicio = LocalTime.of(11, 0);

    @FXML
    private Button btnHuella;
    @FXML
    private Button btnCI;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actualizarBotones();

        // Timeline para revisar la hora cada 1 segundo
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> actualizarBotones())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void actualizarBotones() {
        LocalTime ahora = LocalTime.now();
        boolean estaEnRango = !ahora.isBefore(inicio) && !ahora.isAfter(limite);
        btnHuella.setVisible(estaEnRango);
        btnCI.setVisible(estaEnRango);
        btnHuella.setDisable(!estaEnRango);
        btnCI.setDisable(!estaEnRango);
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
