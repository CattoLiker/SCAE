/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

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
    private void formularioProfe(ActionEvent event) throws SQLException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Generar Reporte");
        dialog.setHeaderText("Ingrese el CI del profesor");
        dialog.setContentText("CI:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().trim().isEmpty()) {
            try {
                Integer ci = Integer.parseInt(result.get().trim());

                String forProf = "/reportes/ReporteProfesReal.jasper";

                Map<String, Object> parametros = new HashMap<>();
                parametros.put("CI", ci);

                Connection conexion = ConeccionDB.getConnection();

                ReporteUtilities.abrirJasper(forProf, parametros, conexion);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error de formato");
                alert.setHeaderText(null);
                alert.setContentText("El CI debe ser un número válido.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atención");
            alert.setHeaderText(null);
            alert.setContentText("Debe ingresar un CI válido para generar el reporte.");
            alert.showAndWait();
        }
    }

    @FXML
    private void formularioAlumno(ActionEvent event) throws SQLException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Generar Reporte");
        dialog.setHeaderText("Ingrese el CI del alumno");
        dialog.setContentText("CI:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().trim().isEmpty()) {
            try {
                Integer ci = Integer.parseInt(result.get().trim());

                String forProf = "/reportes/ReporteAlumnoReal.jasper";

                Map<String, Object> parametros = new HashMap<>();
                parametros.put("CI", ci);

                Connection conexion = ConeccionDB.getConnection();

                ReporteUtilities.abrirJasper(forProf, parametros, conexion);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error de formato");
                alert.setHeaderText(null);
                alert.setContentText("El CI debe ser un número válido.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atención");
            alert.setHeaderText(null);
            alert.setContentText("Debe ingresar un CI válido para generar el reporte.");
            alert.showAndWait();
        }
    }

    @FXML
    private void formularioComida(ActionEvent event) throws SQLException {
        String forCom = "/reportes/ReporteComida.jasper";

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("TITULO", "Reporte de Comidas");

        Connection conexion = ConeccionDB.getConnection();

        ReporteUtilities.abrirJasper(forCom, parametros, conexion);
    }

    @FXML
    private void comidaHoy(ActionEvent event) throws SQLException {
        LocalDate fecha = LocalDate.now();
        DayOfWeek diaSemana = fecha.getDayOfWeek();

        int nroSemana = (fecha.get(WeekFields.ISO.weekOfWeekBasedYear())) % 4;
        
        int nroDiaSemana = diaSemana.getValue();

//        System.out.println(diaSemana + "<- Dia. NroSemana ->" + nroSemana);

        String forCom = "/reportes/ReporteComidaHoy.jasper";

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("Dia", nroDiaSemana);
        parametros.put("Semana", nroSemana);

        Connection conexion = ConeccionDB.getConnection();

        ReporteUtilities.abrirJasper(forCom, parametros, conexion);
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
