/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

/**
 *
 * @author abiga
 */
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class InicioSesionController {

    @FXML
    private TextField user;
    @FXML
    private Button btnIniciarSesion;
    @FXML
    private Button btnContrasena;
    @FXML
    private PasswordField password;
    @FXML
    private Button btnRegistro;

    @FXML
    private void iniciarSesion(ActionEvent event) {
        String Txtuser;
        Txtuser = user.getText();
        String Txtpassword;
        Txtpassword = password.getText();
        if (Txtuser.isBlank() || Txtpassword.isBlank()) { //verifica si no esta vacioa
            JOptionPane.showMessageDialog(
                    null, "Informacion Incompleta", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean correctuser = false;
        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "SELECT idUsuario FROM USUARIO WHERE Username = '" + Txtuser;
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    correctuser = true; //si existe el usuario
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean correctpassword = false;
        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "SELECT Contrasena FROM USUARIO WHERE idUsuario= '" + Txtuser + "'";
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {    
                    correctpassword = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (correctuser = false || !correctuser && !correctpassword) {
            JOptionPane.showMessageDialog(null, "Usuario incorrecto", "Error", JOptionPane.ERROR_MESSAGE);  //si no existe es incorrecto
            user.clear();
            password.clear();
            return;
        }
        if (correctpassword = false) {
            JOptionPane.showMessageDialog(null, "Contrasena incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            password.clear();
            return;
        }

        int usertype = 2;
        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "SELECT TipoUsuario FROM USUARIO WHERE Username = '" + Txtuser + "'";
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                usertype = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (usertype == 1) {
            try {
                //si es admin
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show(); //cargar menu 
            } catch (IOException ex) {
                System.getLogger(InicioSesionController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }

    }

    @FXML
    private void contrasena(ActionEvent event) {

    }

    @FXML
    private void registro(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Registro.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.getLogger(RegistroController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

// Caused by: java.lang.IllegalArgumentException: Can not set javafx.scene.control.TextField field
//  com.scaeproyecto.sistemacontrolalmuerzoescolarctn.InicioSesionController.user to javafx.scene.control.Label
}
