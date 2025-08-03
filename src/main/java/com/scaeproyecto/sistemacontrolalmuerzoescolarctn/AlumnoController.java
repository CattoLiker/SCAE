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
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class AlumnoController implements Initializable {

    @FXML
    private TextField TxtCodigo;
    @FXML
    private TextField TxtNombre;
    @FXML
    private TextField TxtApellido;
    @FXML
    private TextField TxtBuscar;
    @FXML
    private TableView<?> TablaClientes;
    @FXML
    private TableColumn<?, ?> ColumnId;
    @FXML
    private TableColumn<?, ?> ColumnNombre;
    @FXML
    private TableColumn<?, ?> ColumnApellido;
    @FXML
    private Button BtnNuevo;
    @FXML
    private Button BtnModificar;
    @FXML
    private Button BtnEliminar;
    @FXML
    private Button BtnGuardar;
    @FXML
    private Button BtnCancelar;
    @FXML
    private MenuItem menuCurso;
    @FXML
    private TableColumn<?, ?> columnCurso;
    @FXML
    private TableColumn<?, ?> columnSeccion;
    @FXML
    private TableColumn<?, ?> columnEspecialidad;
    @FXML
    private SplitMenuButton dropmenuCurso;
    private SplitMenuButton dropmenuSección;
    @FXML
    private MenuItem menuCurso1;
    @FXML
    private SplitMenuButton dropmenuSeccion;
    @FXML
    private SplitMenuButton dropmenuEspe;
    @FXML
    private MenuItem menuCurso11;
    @FXML
    private MenuItem menuCurso111;
    @FXML
    private MenuItem menuCurso1111;
    @FXML
    private MenuItem menuCurso1112;
    @FXML
    private MenuItem menuCurso11121;
    @FXML
    private MenuItem menuCurso11122;

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
    private void buscar(KeyEvent event) {
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
    }

    @FXML
    private void nuevo(ActionEvent event) {
        TxtCodigo.setDisable(false);
        TxtNombre.setDisable(false);
        TxtApellido.setDisable(false);
        BtnNuevo.setDisable(true);
        BtnGuardar.setDisable(false);
        BtnCancelar.setDisable(false);
        TxtCodigo.requestFocus();
        dropmenuCurso.setDisable(false);
        dropmenuSeccion.setDisable(false);
        dropmenuEspe.setDisable(false);
    }

    @FXML
    private void modificar(ActionEvent event) {
    }

    @FXML
    private void eliminar(ActionEvent event) {
    }

    @FXML
    private void guardar(ActionEvent event) {
    }

    @FXML
    private void cancelar(ActionEvent event) {
        TxtCodigo.setText("");
        TxtNombre.setText("");
        TxtApellido.setText("");
        TxtCodigo.setDisable(true);
        TxtNombre.setDisable(true);
        TxtApellido.setDisable(true);
        BtnNuevo.setDisable(false);
        BtnGuardar.setDisable(true);
        BtnCancelar.setDisable(true);
        dropmenuCurso.setDisable(true);
        dropmenuCurso.setText("Seleccionar Curso");
        dropmenuSeccion.setDisable(true);
        dropmenuSeccion.setText("Seleccionar Sección");
        dropmenuEspe.setDisable(true);
        dropmenuEspe.setText("Seleccionar Especialidad");
    }

    @FXML
    private void abrirMenuPrincipal(ActionEvent event) {
        String menuprincipal = "menu.fxml";

        try {
            abrirMenuOtro(event, menuprincipal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
