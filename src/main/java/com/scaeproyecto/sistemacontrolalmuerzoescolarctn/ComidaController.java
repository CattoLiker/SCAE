/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class ComidaController implements Initializable {

    @FXML
    private TextField TxtApellido;
    @FXML
    private TextField TxtBuscar;
    @FXML
    private TableView<?> TablaClientes;

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
    private TextField TxtCodigo;
    @FXML
    private TextField TxtNombre;
    @FXML
    private TextField TxtDescripcion;
    @FXML
    private TableView<Comida> TablaComidas;
    @FXML
    private TableColumn<Comida, Integer> ColumnId;
    @FXML
    private TableColumn<Comida, String> ColumnNombre;
    @FXML
    private TableColumn<Comida, String> ColumnDescripcion;
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

    private ObservableList<Comida> listaComidas = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ColumnId.setCellValueFactory(new PropertyValueFactory<>("idComidas"));
        ColumnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        ColumnDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

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
        TablaComidas.setItems(listaComidas);
    }

    @FXML
    private void guardar(ActionEvent event) {
        int codigo;
        try {
            codigo = Integer.parseInt(TxtCodigo.getText());
        } catch (NumberFormatException e) {
            Alert alerta2 = new Alert(Alert.AlertType.ERROR);
            alerta2.setTitle("Error");
            alerta2.setHeaderText(null);
            alerta2.setContentText("El codigo debe ser numerico");
            alerta2.show(); //verificar q el codigo sea numercio
            return;
        }
        
        String nombre = TxtNombre.getText();
        String descripcion = TxtDescripcion.getText();
        if(TxtCodigo.getText().isBlank() || nombre.isBlank() || descripcion.isBlank()){
             Alert alerta2 = new Alert(Alert.AlertType.ERROR);
            alerta2.setTitle("Error");
            alerta2.setHeaderText(null);
            alerta2.setContentText("Completar todos los campos");
            alerta2.show(); //verificar q el codigo sea numercio
            return;
        }
        if (nombre.isEmpty() || descripcion.isEmpty() || TxtCodigo.getText().isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Completar los campos", ButtonType.OK);
            alerta.showAndWait();
            return;
        }

        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "INSERT INTO comidas (idComidas, Nombre, Descripcion) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, codigo);
                pstmt.setString(2, nombre);
                pstmt.setString(3, descripcion);

                int filasAfectadas = pstmt.executeUpdate();
                if (filasAfectadas > 0) {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Comida guardada correctamente.", ButtonType.OK);
                    alerta.showAndWait();
                    cargarComidas();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
             Alert alerta = new Alert(Alert.AlertType.ERROR, "No se pudo guardar la comida", ButtonType.OK);
             alerta.showAndWait();
        }
        cancelar(event);
    }

    @FXML
    private void modificar(ActionEvent event) {
        Comida comidaSeleccionada = TablaComidas.getSelectionModel().getSelectedItem();
        if (comidaSeleccionada == null) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Selecciona una comida para modificar.", ButtonType.OK);
            alerta.showAndWait();
        
            return;
        }

        String nombre = TxtNombre.getText();
        String descripcion = TxtDescripcion.getText();

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación de Modificación");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Desea modificar la comida?");
        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try (Connection conn = ConeccionDB.getConnection()) {
                String sql = "UPDATE comidas SET Nombre=?, Descripcion=? WHERE idComidas=?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, nombre);
                    pstmt.setString(2, descripcion);
                    pstmt.setInt(3, comidaSeleccionada.getIdComidas());

                    int filasAfectadas = pstmt.executeUpdate();
                    if (filasAfectadas > 0) {
                        System.out.println("Comida modificada correctamente.");
                        
                        Alert alerta2 = new Alert(Alert.AlertType.INFORMATION);
                        alerta2.setTitle("Confirmado");
                        alerta2.setHeaderText(null);
                        alerta2.setContentText("Comida modificada correctamente");
                        alerta2.show();
                        cargarComidas();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alert alerta2 = new Alert(Alert.AlertType.ERROR, "No se pudo modificar la comida", ButtonType.OK);
                alerta.showAndWait();
            }
        }
        cancelar(event);
    }

    @FXML
    private void eliminar(ActionEvent event) {
        Comida comidaSeleccionada = TablaComidas.getSelectionModel().getSelectedItem();
        if (comidaSeleccionada == null) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Selecciona una comida para eliminar.", ButtonType.OK);
            alerta.showAndWait();
           
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación de Eliminación");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Desea eliminar la comida?");
        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try (Connection conn = ConeccionDB.getConnection()) {
                
                String sql = "DELETE FROM comidas WHERE idComidas=?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, comidaSeleccionada.getIdComidas());

                    int filasAfectadas = pstmt.executeUpdate();
                    if (filasAfectadas > 0) {
                       
                        
                        Alert alerta2 = new Alert(Alert.AlertType.INFORMATION);
                        alerta2.setTitle("Confirmado");
                        alerta2.setHeaderText(null);
                        alerta2.setContentText("Comida eliminada correctamente");
                        alerta2.show();
                        cargarComidas();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cancelar(event);
    }

    @FXML
    private void cancelar(ActionEvent event) {
        TxtCodigo.setText("");
        TxtNombre.setText("");
        TxtDescripcion.setText("");
        TxtCodigo.setDisable(true);
        TxtNombre.setDisable(true);
        TxtDescripcion.setDisable(true);

        BtnNuevo.setDisable(false);
        BtnGuardar.setDisable(true);
        BtnCancelar.setDisable(true);
        BtnModificar.setDisable(true);
        BtnEliminar.setDisable(true);
    }

    @FXML
    private void nuevo(ActionEvent event) {
        TxtCodigo.setDisable(false);
        TxtNombre.setDisable(false);
        TxtDescripcion.setDisable(false);
        BtnNuevo.setDisable(true);
        BtnGuardar.setDisable(false);
        BtnCancelar.setDisable(false);
        TxtCodigo.requestFocus();
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
        TxtCodigo.setText("");
        TxtNombre.setText("");
        TxtDescripcion.setText("");
        TxtCodigo.setDisable(true);
        TxtNombre.setDisable(true);
        TxtDescripcion.setDisable(true);

        BtnNuevo.setDisable(false);
        BtnGuardar.setDisable(true);
        BtnCancelar.setDisable(true);
        BtnModificar.setDisable(true);
        BtnEliminar.setDisable(true);
        
        Comida comidaSeleccionada = TablaComidas.getSelectionModel().getSelectedItem();
        if (comidaSeleccionada != null) {
            TxtCodigo.setText(String.valueOf(comidaSeleccionada.getIdComidas()));
            TxtNombre.setText(comidaSeleccionada.getNombre());
            TxtDescripcion.setText(comidaSeleccionada.getDescripcion());

            BtnCancelar.setDisable(false);
            BtnModificar.setDisable(false);
            BtnEliminar.setDisable(false);
            BtnNuevo.setDisable(true);

            TxtNombre.setDisable(false);
            TxtDescripcion.setDisable(false);
        }
    }

    @FXML
    private void abrirMenuPrincipal(ActionEvent event) {
        String menuprincipal = "menu.fxml";

        try {
            abrirMenuOtro(event, menuprincipal);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR, "No se pudo abrir el menu", ButtonType.OK);
            alerta.showAndWait();
        }
    }

    @FXML
    private void buscar(KeyEvent event) {
        String filtro = TxtBuscar.getText().toLowerCase();

        if (filtro.isEmpty()) {
            TablaComidas.setItems(listaComidas);
            return;
        }

        ObservableList<Comida> comidasFiltradas = FXCollections.observableArrayList();

        for (Comida comida : listaComidas) {
            if (String.valueOf(comida.getIdComidas()).contains(filtro) || comida.getNombre().toLowerCase().contains(filtro) || comida.getDescripcion().toLowerCase().contains(filtro)) {
                comidasFiltradas.add(comida);
            }
        }
        TablaComidas.setItems(comidasFiltradas);
    }

    @FXML
    private void abrirDoc(KeyEvent event) {
        if (event.getCode() == KeyCode.CONTROL) {
            try {
                String docu = "/Documentacion/manual.chm";
                InputStream inputStream = getClass().getResourceAsStream(docu);

                if (inputStream == null) {
                    System.err.println("No se pudo encontrar el archivo: " + docu);
                    mostrarError("Archivo no encontrado", "No se pudo encontrar el manual de ayuda.");
                    return;
                }

                // Crear archivo temporal
                File tempFile = File.createTempFile("manual", ".chm");
                tempFile.deleteOnExit(); // Se eliminará al cerrar la aplicación

                // Copiar el contenido del InputStream al archivo temporal
                try (FileOutputStream out = new FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }

                // Abrir el archivo con la aplicación predeterminada
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(tempFile);
                } else {
                    System.err.println("La acción OPEN no está soportada en este sistema");
                    mostrarError("Error", "No se puede abrir el archivo en este sistema.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                mostrarError("Error", "No se pudo abrir el manual: " + e.getMessage());
            }
        }
    }

    private void mostrarError(String titulo, String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR
        );
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
}
