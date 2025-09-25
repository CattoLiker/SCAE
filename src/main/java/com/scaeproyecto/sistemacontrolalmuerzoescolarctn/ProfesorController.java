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
import javafx.scene.input.KeyCode;
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
            String sql = "SELECT idDocente, Nombre, Apellido FROM docente";
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
        int codigo = 0;
        String nombre = TxtNombre.getText();
        String apellido = TxtApellido.getText();

        if (TxtCodigo.getText().isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            Alert alerta2 = new Alert(Alert.AlertType.ERROR);
            alerta2.setTitle("Error");
            alerta2.setHeaderText(null);
            alerta2.setContentText("Todos los campos debe estar completos");
            alerta2.show(); //verificar q el ci sea numercio
            return;
        }
        try {
            Integer.parseInt(TxtCodigo.getText());
            codigo = Integer.parseInt(TxtCodigo.getText());
        } catch (NumberFormatException e) {
            Alert alerta2 = new Alert(Alert.AlertType.ERROR);
            alerta2.setTitle("Error");
            alerta2.setHeaderText(null);
            alerta2.setContentText("El CI debe ser numerico");
            alerta2.show(); //verificar q el ci sea numercio

        }
        if (codigo < 1000000) {
            Alert alerta2 = new Alert(Alert.AlertType.ERROR);
            alerta2.setTitle("Error");
            alerta2.setHeaderText(null);
            alerta2.setContentText("CI incorrecto");
            alerta2.show();//verificar que el ci no sea un num cualquiera
            return;
        }
        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "INSERT INTO docente (iddocente, nombre, apellido) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, codigo);
                pstmt.setString(2, nombre);
                pstmt.setString(3, apellido);

                int filasAfectadas = pstmt.executeUpdate();
                if (filasAfectadas > 0) {
                    Alert alerta2 = new Alert(Alert.AlertType.INFORMATION);
                    alerta2.setTitle("Exito");
                    alerta2.setHeaderText(null);
                    alerta2.setContentText("Docente guardado corectamente");
                    alerta2.show();//verificar que el ci no sea un num cualquiera

                    cargarProfesor();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cancelar(event);
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
                String sql = "UPDATE docente SET Nombre=?, Apellido=? WHERE iddocente=?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, nombre);
                    pstmt.setString(2, apellido);
                    pstmt.setInt(3, DocenteSeleccionado.getIdDocente());

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
        cancelar(event);
    }

    @FXML
    private void eliminar(ActionEvent event) {
        Profesor DocenteSeleccionado = TablaProfesor.getSelectionModel().getSelectedItem();
        if (DocenteSeleccionado == null) {
            Alert alerta2 = new Alert(Alert.AlertType.ERROR);
            alerta2.setTitle("Error");
            alerta2.setHeaderText(null);
            alerta2.setContentText("Seleccione un docente para continuar");
            alerta2.show(); //verificar q el ci sea numercio
            return;
        }
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación de Eliminación");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Desea eliminar al Docente?");
        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try (Connection conn = ConeccionDB.getConnection()) {
                String sql2 = "DELETE FROM registrodocente WHERE docente_iddocente = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql2)) {
                    pstmt.setInt(1, DocenteSeleccionado.getIdDocente());
                    pstmt.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String sql = "DELETE FROM docente WHERE idDocente=?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, DocenteSeleccionado.getIdDocente());

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
        cancelar(event);
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
            if (String.valueOf(profe.getIdDocente()).contains(filtro) || profe.getNombre().toLowerCase().contains(filtro) || profe.getApellido().toLowerCase().contains(filtro)) {
                ProfesorFiltrados.add(profe);
            }
        }
        TablaProfesor.setItems(ProfesorFiltrados);
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
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

        Profesor DocenteSeleccionado = TablaProfesor.getSelectionModel().getSelectedItem();
        if (DocenteSeleccionado != null) {
            TxtCodigo.setText(String.valueOf(DocenteSeleccionado.getIdDocente()));
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
            Alert alerta = new Alert(Alert.AlertType.ERROR, "No se pudo abrir el menu", ButtonType.OK);
            alerta.showAndWait();
        }
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
