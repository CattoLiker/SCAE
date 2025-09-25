package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SeleccionarComidaController {

    @FXML
    private TableView<Comida> tablaComidas;
    @FXML
    private TableColumn<Comida, Integer> colId;
    @FXML
    private TableColumn<Comida, String> colNombre;
    @FXML
    private TableColumn<Comida, String> colDescripcion;
    @FXML
    private Button btnSeleccionar;

    private ObservableList<Comida> listaComidas = FXCollections.observableArrayList();

    private Comida comidaSeleccionada = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdComidas()).asObject());
        colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        colDescripcion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescripcion()));
        cargarComidas();
    }

    private void cargarComidas() {
        listaComidas.clear();
        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "SELECT idComidas, Nombre, Descripcion FROM comidas";
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Comida comida = new Comida(
                            rs.getInt("idComidas"),
                            rs.getString("Nombre"),
                            rs.getString("Descripcion")
                    );
                    listaComidas.add(comida);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tablaComidas.setItems(listaComidas);
    }

    @FXML
    private void seleccionarComida() {
        comidaSeleccionada = tablaComidas.getSelectionModel().getSelectedItem();
        if (comidaSeleccionada == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Por favor seleccione una comida de la lista.", ButtonType.OK);
            alerta.showAndWait();
            return;
        }
        // Cierra la ventana
        Stage stage = (Stage) tablaComidas.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelar() {
        comidaSeleccionada = null;
        Stage stage = (Stage) tablaComidas.getScene().getWindow();
        stage.close();
    }

    public Comida getComidaSeleccionada() {
        return comidaSeleccionada;
    }
}