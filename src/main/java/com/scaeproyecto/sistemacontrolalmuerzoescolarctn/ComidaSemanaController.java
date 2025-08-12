/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class ComidaSemanaController implements Initializable {

    @FXML
    private TextField TxtBuscar;
    @FXML
    private TableView<?> TablaComidas;
    @FXML
    private TableColumn<?, ?> ColumnDia;
    @FXML
    private TableColumn<?, ?> ColumnSemana;
    @FXML
    private TableColumn<?, ?> ColumnComida;
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
    private SplitMenuButton Día;
    @FXML
    private MenuItem diaLun;
    @FXML
    private MenuItem diaMar;
    @FXML
    private MenuItem diaMier;
    @FXML
    private MenuItem diaJue;
    @FXML
    private MenuItem diaVier;
    @FXML
    private SplitMenuButton Día1;
    @FXML
    private MenuItem semana1;
    @FXML
    private MenuItem semana2;
    @FXML
    private MenuItem semana3;
    @FXML
    private MenuItem semana4;
    @FXML
    private TextField TxtNombre;
    @FXML
    private Button btnMenuComida;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void buscar(KeyEvent event) {
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
    }

    @FXML
    private void abrirMenuPrincipal(ActionEvent event) {
    }

    @FXML
    private void nuevo(ActionEvent event) {
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
    }

    @FXML
    private void desplegarMenuComida(ActionEvent event) {
    }
    
}
