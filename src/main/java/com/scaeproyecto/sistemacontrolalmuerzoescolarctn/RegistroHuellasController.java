/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

import java.io.File;
import java.io.FileInputStream;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
/**
 * FXML Controller class
 *
 * @author abiga
 */
public class RegistroHuellasController implements Initializable {


    @FXML
    private TextField TxtIdAlumno;
    @FXML
    private TextField TxtBuscar;
    @FXML
    private TableView<?> TablaProfesor;
    @FXML
    private TableColumn<?, ?> ColumnId;
    @FXML
    private TableColumn<?, ?> ColumnNombre;
    @FXML
    private Button BtnNuevaHuella;
    @FXML
    private Button BtnModificar;
    @FXML
    private Button BtnEliminar;
    @FXML
    private Button BtnGuardar;
    @FXML
    private Button BtnCancelar;
    @FXML
    private Button Back;
    @FXML
    private ImageView ImagenHuella;
    @FXML
    private Button BtnGuardarHuella;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         BtnGuardarHuella.setDisable(true);
         
    }    
    
    @FXML
    private void buscar(KeyEvent event) {
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
    }

    @FXML
    private void nuevoRegistro(ActionEvent event) {
        BtnNuevaHuella.setDisable(true);
        BtnGuardarHuella.setDisable(false);
        BtnCancelar.setDisable(false);
        BtnGuardar.setDisable(false);
        TxtIdAlumno.setDisable(false);
   
    }

    @FXML
    private void modificarHuella(ActionEvent event) {
    }

    @FXML
    private void eliminarHuella(ActionEvent event) {
    }

    @FXML
    private void guardar(ActionEvent event) {
    }

    @FXML
    private void cancelar(ActionEvent event) {
    }

    @FXML
    private void abrirMenuPrincipal(ActionEvent event) {
    }

    @FXML
    private void subirHuella(ActionEvent event) {
        String TxtId = TxtIdAlumno.getText();
        if(TxtId.isBlank()){
            JOptionPane.showMessageDialog(
                    null, "No se encontro Id", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean existe=false;
        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "SELECT idEstudiante FROM estudiante WHERE idEstudiante = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, TxtId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        existe = true; // si existe id
                    }
                }
            }
        } catch (SQLException ex) {
            System.getLogger(RegistroHuellasController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        if(!existe){
            JOptionPane.showMessageDialog(
                    null, "No se encontro Id", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imagenes", "jpg", "png", "tif", "tiff");//filtro
        JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); //nueva vista de archivos
        j.setFileFilter(filtro);
        int r = j.showOpenDialog(null); //abrir la vista

        if (r == JFileChooser.APPROVE_OPTION) { //si guarda
            //guardar el archivo seleccionado
            File archivo = j.getSelectedFile();
            try (Connection conn = ConeccionDB.getConnection();
            FileInputStream fis = new FileInputStream(archivo);
            ) {
            String sql = "UPDATE estudiante SET InformacionHuella FROM ESTUDIANTE WHERE idEstudiante = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setBinaryStream(1, fis, (int) archivo.length());
                stmt.setString(2, TxtId); // identificando por id
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(
                    null, "Se guardo exitosamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            
        }
        
        
    }
}
