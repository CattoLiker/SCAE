/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import java.time.*;
import java.time.temporal.WeekFields;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCombination;

import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author abiga
 */
public class RegistroCIController implements Initializable {
    @FXML
    private Button btnMenu;
   
    @FXML
    private TextField user;
    @FXML
    private Button btnRegistro;
    private LocalDate fecha;
    private int semanaDelAno;
    private int SemanaMenu;
    int diaSemana;
    java.sql.Date sqlDate;
    LocalTime limite = LocalTime.of(12, 30);
    LocalTime finServicio = LocalTime.of(13, 00);
    private int idComida;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fecha = LocalDate.now(); //fecha de hoy
        sqlDate = java.sql.Date.valueOf(fecha);
        semanaDelAno = semanaDelAno = fecha.get(WeekFields.ISO.weekOfYear());
        SemanaMenu = ((semanaDelAno - 1) % 4) + 1;
        diaSemana = fecha.getDayOfWeek().getValue();
        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "SELECT Comidas_idComidas FROM SemanaMenuComidas WHERE diaSemana = ? AND SemanaMenu_idSemanaMenu = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, diaSemana);
            stmt.setInt(2, SemanaMenu);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idComida = rs.getInt("Comidas_idComidas");
                    System.out.println("ID de la comida para hoy: " + idComida);
                } else {
                    Alert alerta = new Alert(Alert.AlertType.ERROR, "No se encontro comida para hoy", ButtonType.OK);
                    alerta.showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void aprobar(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AccederComida.fxml"));
            Parent root = fxmlLoader.load();
            // Obtener el Stage actual desde el botón o cualquier nodo que disparó el evento
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Reemplazar la escena
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    private void registroCI(ActionEvent event) {
        String CI = user.getText();

        if (CI.isBlank()) {
            JOptionPane.showMessageDialog(null, "ID vacio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int CInum;
        try {
            CInum = Integer.parseInt(CI);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID inválido, debe ser numérico", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean alumnoExiste = false;
        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = " SELECT idEstudiante FROM estudiante WHERE idEstudiante = ? AND Estado = 1"; //busca que exista y que el alumno este activo
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, CInum);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { //si existe el CI en la bd
                    alumnoExiste = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean docenteExiste = false;
        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = " SELECT idDocente FROM docente WHERE idDocente = ?"; //busca que exista
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, CInum);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) { //si existe el CI en la bd
                    docenteExiste = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!docenteExiste && !alumnoExiste) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("accesoDenegado.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setFullScreen(true);
                stage.setFullScreenExitHint("");
                stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                stage.show();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (alumnoExiste && LocalTime.now().isBefore(limite)) { //insertar en la base de datos
            try (Connection conn = ConeccionDB.getConnection()) {
                String sql = "SELECT 1 FROM registroconsumo WHERE Estudiante_idEstudiante = ? AND Fecha = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, CInum);
                stmt.setDate(2, java.sql.Date.valueOf(fecha));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) { //si el alumno ya comio 
                        JOptionPane.showMessageDialog(null, "No puedes repetir la comidaprevio a las 12.30", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        String insertcomer = "INSERT INTO registroconsumo (Fecha,Observaciones,Estudiante_idEstudiante,SemanaMenuComidas_Comidas_idComidas,SemanaMenuComidas_SemanaMenu_idSemanaMenu,HaComido) values (?,?,?,?,?,?)";
                        PreparedStatement insertstmt = conn.prepareStatement(insertcomer);
                        insertstmt.setDate(1, sqlDate);
                        insertstmt.setString(2, " ");
                        insertstmt.setInt(3, CInum);
                        insertstmt.setInt(4, idComida);
                        insertstmt.setInt(5, SemanaMenu);
                        insertstmt.setBoolean(6, true);
                        insertstmt.executeUpdate();
                        aprobar(event);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        if (alumnoExiste && LocalTime.now().isAfter(limite)) { //insertar en la base de datos   
            try (Connection conn = ConeccionDB.getConnection()) {
                String sql = "SELECT 1 FROM registroconsumo WHERE Estudiante_idEstudiante = ? AND Fecha = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, CInum);
                stmt.setDate(2, sqlDate);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) { //si el alumno ya comio 
                        String insertrepetir = "INSERT INTO registroconsumo (Fecha,Observaciones,Estudiante_idEstudiante,SemanaMenuComidas_Comidas_idComidas,SemanaMenuComidas_SemanaMenu_idSemanaMenu,HaComido) values (?,?,?,?,?,?)";
                        PreparedStatement insertstmt = conn.prepareStatement(insertrepetir);
                        insertstmt.setDate(1, sqlDate);
                        insertstmt.setString(2, "repitio");
                        insertstmt.setInt(3, CInum);
                        insertstmt.setInt(4, idComida);
                        insertstmt.setInt(5, SemanaMenu);
                        insertstmt.setBoolean(6, true);
                        insertstmt.executeUpdate();
                        aprobar(event);

                    } else {
                        String insertcomer = "INSERT INTO registroconsumo (Fecha,Observaciones,Estudiante_idEstudiante,SemanaMenuComidas_Comidas_idComidas,SemanaMenuComidas_SemanaMenu_idSemanaMenu,HaComido) values (?,?,?,?,?,?)";
                        PreparedStatement insertstmt = conn.prepareStatement(insertcomer);
                        insertstmt.setDate(1, sqlDate);
                        insertstmt.setString(2, " ");
                        insertstmt.setInt(3, CInum);
                        insertstmt.setInt(4, idComida);
                        insertstmt.setInt(5, SemanaMenu);
                        insertstmt.setBoolean(6, true);
                        insertstmt.executeUpdate();
                        aprobar(event);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        if (docenteExiste && LocalTime.now().isBefore(limite)) {
            JOptionPane.showMessageDialog(null, "Los docentes solo pueden consumir el almuerzo a partir de las 12.30", "Atencion", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (docenteExiste && LocalTime.now().isAfter(limite)) {
            try (Connection conn = ConeccionDB.getConnection()) {
                String insertrepetir = "INSERT INTO registrodocente(Docente_idDocente,SemanaMenuComidas_Comidas_idComidas,SemanaMenuComidas_SemanaMenu_idSemanaMenu) values (?,?,?,?,?,?)";
                PreparedStatement insertstmt = conn.prepareStatement(insertrepetir);
                insertstmt.setInt(1, CInum);
                insertstmt.setInt(2, idComida);
                insertstmt.setInt(3, SemanaMenu);
                insertstmt.executeUpdate();
                aprobar(event);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        if (LocalTime.now().isAfter(finServicio)) { //despues del servicio se registran los estudiantes que no comieron 
            String sql = "INSERT INTO registroconsumo (Estudiante_idEstudiante, SemanaMenuComidas_Comidas_idComidas, fecha, HaComido) "
                    + "SELECT e.idEstudiante, ?, CURRENT_DATE, false "
                    + "FROM estudiante e "
                    + "WHERE e.Estado = 1 "
                    + "AND e.idEstudiante NOT IN ("
                    + "   SELECT r.Estudiante_idEstudiante FROM registroconsumo r WHERE r.fecha = CURRENT_DATE"
                    + ")";

            try (Connection conn = ConeccionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idComida);
                int filasInsertadas = stmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    @FXML
    private void volverMenu(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MenuInicioUser.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    

}
