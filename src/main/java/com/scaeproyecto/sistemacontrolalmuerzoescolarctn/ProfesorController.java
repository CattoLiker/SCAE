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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class ProfesorController implements Initializable {

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
    private TextField TxtApellido;
    @FXML
    private TextField TxtBuscar;
    @FXML
    private TableView<Profesor> TablaProfesor;
    @FXML
    private TableColumn<Profesor, Integer> ColumnId;
    @FXML
    private TableColumn<Profesor, String> ColumnNombre;
    @FXML
    private TableColumn<Profesor, String> ColumnApellido;
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

    /**
     * Initializes the controller class.
     */
    private ObservableList<Profesor> listaProfesor = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ColumnId.setCellValueFactory(new PropertyValueFactory<>("idDocente"));
        ColumnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        ColumnApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));

        cargarProfesor();
    }

    private void cargarProfesor() {
        listaProfesor.clear();
        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "SELECT idDocente, Nombre, Apellido FROM Docente";
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Profesor Docente = new Profesor(
                            rs.getInt("idDocente"),
                            rs.getString("Nombre"),
                            rs.getString("Apellido")
                    );
                    listaProfesor.add(Docente);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TablaProfesor.setItems(listaProfesor);
    }

    @FXML
    private void guardar(ActionEvent event) {
        int codigo = Integer.parseInt(TxtCodigo.getText());
        String nombre = TxtNombre.getText();
        String apellido = TxtApellido.getText();

        if (TxtCodigo.getText().isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            System.out.println("Todos los campos son obligatorios.");
            return;
        }

        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "INSERT INTO Docente (idDocente, Nombre, Apellido) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, codigo);
                pstmt.setString(2, nombre);
                pstmt.setString(3, apellido);

                int filasAfectadas = pstmt.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("Docente guardado correctamente.");
                    cargarProfesor();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modificar(ActionEvent event) {
        Profesor DocenteSeleccionado = TablaProfesor.getSelectionModel().getSelectedItem();
        if (DocenteSeleccionado == null) {
            System.out.println("Selecciona un Docente para modificar.");
            return;
        }
        String nombre = TxtNombre.getText();
        String apellido = TxtApellido.getText();

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación de Modificación");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Desea modificar al Docente?");
        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try (Connection conn = ConeccionDB.getConnection()) {
                String sql = "UPDATE Docente SET Nombre=?, Apellido=? WHERE idDocente=?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, nombre);
                    pstmt.setString(2, apellido);
                    pstmt.setInt(3, DocenteSeleccionado.getidDocente());

                    int filasAfectadas = pstmt.executeUpdate();
                    if (filasAfectadas > 0) {
                        Alert alerta2 = new Alert(Alert.AlertType.INFORMATION);
                        alerta2.setTitle("Confirmado");
                        alerta2.setHeaderText(null);
                        alerta2.setContentText("Docente modificado correctamente");
                        alerta2.show();
                        cargarProfesor();
                    } else {
                        Alert alerta2 = new Alert(Alert.AlertType.ERROR);
                        alerta2.setTitle("Error");
                        alerta2.setHeaderText(null);
                        alerta2.setContentText("No se pudo modificar al Docente.");
                        alerta2.show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        Profesor DocenteSeleccionado = TablaProfesor.getSelectionModel().getSelectedItem();
        if (DocenteSeleccionado == null) {
            System.out.println("Selecciona un Docente para eliminar.");
            return;
        }
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación de Eliminación");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Desea eliminar al Docente?");
        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try (Connection conn = ConeccionDB.getConnection()) {
                String sql = "DELETE FROM Docente WHERE idDocente=?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, DocenteSeleccionado.getidDocente());

                    int filasAfectadas = pstmt.executeUpdate();
                    if (filasAfectadas > 0) {
                        Alert alerta2 = new Alert(Alert.AlertType.INFORMATION);
                        alerta2.setTitle("Confirmado");
                        alerta2.setHeaderText(null);
                        alerta2.setContentText("Docente eliminado correctamente");
                        alerta2.show();
                        cargarProfesor();
                    } else {
                        Alert alerta2 = new Alert(Alert.AlertType.ERROR);
                        alerta2.setTitle("Error");
                        alerta2.setHeaderText(null);
                        alerta2.setContentText("No se pudo eliminar al Docente.");
                        alerta2.show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        BtnModificar.setDisable(true);
        BtnEliminar.setDisable(true);
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
    }

    @FXML
    private void buscar(KeyEvent event) {
        String filtro = TxtBuscar.getText().toLowerCase();

        if (filtro.isEmpty()) {
            TablaProfesor.setItems(listaProfesor);
            return;
        }

        ObservableList<Profesor> ProfesorFiltrados = FXCollections.observableArrayList();

        for (Profesor profe : listaProfesor) {
            if (String.valueOf(profe.getidDocente()).contains(filtro) || profe.getNombre().toLowerCase().contains(filtro) || profe.getApellido().toLowerCase().contains(filtro)) {
                ProfesorFiltrados.add(profe);
            }
        }
        TablaProfesor.setItems(ProfesorFiltrados);
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
        Profesor DocenteSeleccionado = TablaProfesor.getSelectionModel().getSelectedItem();
        if (DocenteSeleccionado != null) {
            TxtCodigo.setText(String.valueOf(DocenteSeleccionado.getidDocente()));
            TxtNombre.setText(DocenteSeleccionado.getNombre());
            TxtApellido.setText(DocenteSeleccionado.getApellido());

            BtnCancelar.setDisable(false);
            BtnModificar.setDisable(false);
            BtnEliminar.setDisable(false);
            BtnNuevo.setDisable(true);

            TxtNombre.setDisable(false);
            TxtApellido.setDisable(false);
        }
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
