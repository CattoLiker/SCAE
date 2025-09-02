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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class MenuController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
    private void abrirMenuProfe(ActionEvent event) {
        String menuprofe = "Profesor.fxml";

        try {
            abrirMenuOtro(event, menuprofe);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirMenuAlumnos(ActionEvent event) {
        String menualumnos = "Alumno.fxml";

        try {
            abrirMenuOtro(event, menualumnos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirMenuComida(ActionEvent event) {
        String menucomida = "Comida.fxml";

        try {
            abrirMenuOtro(event, menucomida);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void formularioProfe(ActionEvent event) {
    }

    @FXML
    private void formularioAlumno(ActionEvent event) {
    }

    @FXML
    private void formularioComida(ActionEvent event) {
    }

    @FXML
    private void comidaHoy(ActionEvent event) {
    }

    @FXML
    private void abrirMenuSemana(ActionEvent event) {
        String menusemana = "ComidaSemana.fxml";

        try {
            abrirMenuOtro(event, menusemana);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
