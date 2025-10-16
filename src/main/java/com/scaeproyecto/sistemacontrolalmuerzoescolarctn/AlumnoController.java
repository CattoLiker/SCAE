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
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

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
    private TableView<Alumno> TablaClientes;
    @FXML
    private TableColumn<Alumno, Integer> ColumnId;
    @FXML
    private TableColumn<Alumno, String> ColumnNombre;
    @FXML
    private TableColumn<Alumno, String> ColumnApellido;
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
    private TableColumn<Alumno, String> columnCurso;
    @FXML
    private TableColumn<Alumno, String> columnSeccion;
    @FXML
    private TableColumn<Alumno, String> columnEspecialidad;
    @FXML
    private SplitMenuButton dropmenuCurso;
    @FXML
    private SplitMenuButton dropmenuSeccion;
    @FXML
    private SplitMenuButton dropmenuEspe;
    @FXML
    private MenuItem espeInfo;
    @FXML
    private MenuItem espeCC;
    @FXML
    private MenuItem espeEdad;
    @FXML
    private MenuItem espeEtronica;
    @FXML
    private MenuItem espeEtrocanica;
    @FXML
    private MenuItem espeMecGen;
    @FXML
    private MenuItem espeAuto;
    @FXML
    private MenuItem espeQca;
    @FXML
    private MenuItem curso1;
    @FXML
    private MenuItem curso2;
    @FXML
    private MenuItem curso3;
    @FXML
    private MenuItem seccion1;
    @FXML
    private MenuItem seccion2;
    @FXML
    private MenuItem seccion3;

    private ObservableList<Alumno> listaAlumnos = FXCollections.observableArrayList();
    @FXML
    private TableColumn<?, ?> columnEstado;
    @FXML
    private SplitMenuButton dropmenuEstado;
    @FXML
    private MenuItem estadoActivo;
    @FXML
    private MenuItem estadoDesActivo;
    @FXML
    private Label labelEstado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        espeInfo.setOnAction(e -> dropmenuEspe.setText(espeInfo.getText()));
        espeCC.setOnAction(e -> dropmenuEspe.setText(espeCC.getText()));
        espeEdad.setOnAction(e -> dropmenuEspe.setText(espeEdad.getText()));
        espeEtronica.setOnAction(e -> dropmenuEspe.setText(espeEtronica.getText()));
        espeEtrocanica.setOnAction(e -> dropmenuEspe.setText(espeEtrocanica.getText()));
        espeMecGen.setOnAction(e -> dropmenuEspe.setText(espeMecGen.getText()));
        espeAuto.setOnAction(e -> dropmenuEspe.setText(espeAuto.getText()));
        espeQca.setOnAction(e -> dropmenuEspe.setText(espeQca.getText()));
        
        estadoActivo.setOnAction(e -> dropmenuEstado.setText(estadoActivo.getText()));
        estadoDesActivo.setOnAction(e -> dropmenuEstado.setText(estadoDesActivo.getText()));

        curso1.setOnAction(e -> dropmenuCurso.setText(curso1.getText()));
        curso2.setOnAction(e -> dropmenuCurso.setText(curso2.getText()));
        curso3.setOnAction(e -> dropmenuCurso.setText(curso3.getText()));

        seccion1.setOnAction(e -> dropmenuSeccion.setText(seccion1.getText()));
        seccion2.setOnAction(e -> dropmenuSeccion.setText(seccion2.getText()));
        seccion3.setOnAction(e -> dropmenuSeccion.setText(seccion3.getText()));

        ColumnId.setCellValueFactory(new PropertyValueFactory<>("idEstudiante"));
        ColumnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        ColumnApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        columnCurso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        columnSeccion.setCellValueFactory(new PropertyValueFactory<>("seccion"));
        columnEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        columnEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        labelEstado.setVisible(false);
        dropmenuEstado.setVisible(false);

        cargarAlumnos();
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
        String filtro = TxtBuscar.getText().toLowerCase();

        if (filtro.isEmpty()) {
            TablaClientes.setItems(listaAlumnos);
            return;
        }

        ObservableList<Alumno> alumnosFiltrados = FXCollections.observableArrayList();

        for (Alumno alumno : listaAlumnos) {
            if (String.valueOf(alumno.getIdEstudiante()).contains(filtro) || alumno.getNombre().toLowerCase().contains(filtro) || alumno.getApellido().toLowerCase().contains(filtro) || alumno.getCurso().toLowerCase().contains(filtro) || alumno.getSeccion().toLowerCase().contains(filtro) || alumno.getEspecialidad().toLowerCase().contains(filtro)) {
                alumnosFiltrados.add(alumno);
            }
        }
        TablaClientes.setItems(alumnosFiltrados);

    }

    @FXML
    private void mostrarFila(MouseEvent event) {
        labelEstado.setVisible(false);
        dropmenuEstado.setVisible(false);

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

        dropmenuCurso.setDisable(true);
        dropmenuCurso.setText("Seleccionar Curso");
        dropmenuSeccion.setDisable(true);
        dropmenuSeccion.setText("Seleccionar Sección");
        dropmenuEspe.setDisable(true);
        dropmenuEspe.setText("Seleccionar Especialidad");
        
        Alumno alumnoSeleccionado = TablaClientes.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado != null) {
            TxtCodigo.setText(String.valueOf(alumnoSeleccionado.getIdEstudiante()));
            TxtNombre.setText(alumnoSeleccionado.getNombre());
            TxtApellido.setText(alumnoSeleccionado.getApellido());
            dropmenuCurso.setText(alumnoSeleccionado.getCurso());
            dropmenuSeccion.setText(alumnoSeleccionado.getSeccion());
            dropmenuEspe.setText(alumnoSeleccionado.getEspecialidad());
            dropmenuEstado.setText(alumnoSeleccionado.getEstado());

            BtnCancelar.setDisable(false);
            BtnModificar.setDisable(false);
            BtnEliminar.setDisable(false);
            BtnNuevo.setDisable(true);

            TxtNombre.setDisable(false);
            TxtApellido.setDisable(false);
            dropmenuCurso.setDisable(false);
            dropmenuSeccion.setDisable(false);
            dropmenuEspe.setDisable(false);

            labelEstado.setVisible(true);
            dropmenuEstado.setVisible(true);
            dropmenuEstado.setDisable(false);
        }
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
        Alumno alumnoSeleccionado = TablaClientes.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado == null) {
            Alert alerta2 = new Alert(Alert.AlertType.ERROR);
            alerta2.setTitle("Error");
            alerta2.setHeaderText(null);
            alerta2.setContentText("Selecciona un alumno para continuar");
            alerta2.show(); 
            return;
        }

        String nombre = TxtNombre.getText();
        String apellido = TxtApellido.getText();
        String curso = dropmenuCurso.getText();
        String seccion = dropmenuSeccion.getText();
        String especialidad = dropmenuEspe.getText();
        String estado = dropmenuEstado.getText();
        if ((!especialidad.equals("Construcciones Civiles") && !especialidad.equals("Química Industrial") && !especialidad.equals("Electrónica")) && seccion.equals("3ra")){
            Alert alerta2 = new Alert(AlertType.ERROR);
            alerta2.setTitle("Error");
            alerta2.setHeaderText(null);
            alerta2.setContentText("Seccion invalida");
            alerta2.show();
            return;
        }
        int estadoReal = 1;
        if (estado.equals("ACTIVO")) {
            estadoReal = 1;
        } else if (estado.equals("NO ACTIVO")) {
            estadoReal = 0;
        }
        int idEstudiante = alumnoSeleccionado.getIdEstudiante();

        Alert alerta = new Alert(AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación de Modificación");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Desea modificar al alumno?");
        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try (Connection conn = ConeccionDB.getConnection()) {
                String sql = "UPDATE estudiante SET Nombre=?, Apellido=?, Curso=?, Seccion=?, Especialidad=?, Estado=? WHERE idEstudiante=?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, nombre);
                    pstmt.setString(2, apellido);
                    pstmt.setString(3, curso);
                    pstmt.setString(4, seccion);
                    pstmt.setString(5, especialidad);
                    pstmt.setInt(6, estadoReal);
                    pstmt.setInt(7, idEstudiante);
                    

                    int filasAfectadas = pstmt.executeUpdate();
                    if (filasAfectadas > 0) {
                        Alert alerta2 = new Alert(AlertType.INFORMATION);
                        alerta2.setTitle("Confirmado");
                        alerta2.setHeaderText(null);
                        alerta2.setContentText("Alumno modificado correctamente");
                        alerta2.show();
                        cargarAlumnos();
                    } else {
                        Alert alerta2 = new Alert(AlertType.ERROR);
                        alerta2.setTitle("Error");
                        alerta2.setHeaderText(null);
                        alerta2.setContentText("No se pudo modificar al alumno.");
                        alerta2.show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alerta2 = new Alert(AlertType.INFORMATION);
            alerta2.setTitle("Cancelado");
            alerta2.setHeaderText(null);
            alerta2.setContentText("El alumno no ha sido modificado.");
            alerta2.show();
        }
        cancelar(event);
    }

    @FXML
    private void eliminar(ActionEvent event) {
        Alumno alumnoSeleccionado = TablaClientes.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado == null) {
            Alert alerta2 = new Alert(Alert.AlertType.ERROR);
            alerta2.setTitle("Error");
            alerta2.setHeaderText(null);
            alerta2.setContentText("Seleccione un alumno para continuar");
            alerta2.show(); //verificar q el ci sea numercio
            return;
        }

        // Popup de confirmación
        Alert alerta = new Alert(AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación de Eliminación");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Desea eliminar al alumno?");
        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            int idEstudiante = alumnoSeleccionado.getIdEstudiante();
            try (Connection conn = ConeccionDB.getConnection()) {
                String sql2 = "DELETE FROM registroconsumo WHERE Estudiante_idEstudiante = ?";
                try(PreparedStatement pstmt = conn.prepareStatement(sql2)) {
                    pstmt.setInt(1, idEstudiante);
                    pstmt.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String sql = "DELETE FROM estudiante WHERE idEstudiante=?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, idEstudiante);

                    int filasAfectadas = pstmt.executeUpdate();
                    if (filasAfectadas > 0) {
                        Alert alerta2 = new Alert(AlertType.INFORMATION);
                        alerta2.setTitle("Confirmado");
                        alerta2.setHeaderText(null);
                        alerta2.setContentText("Alumno eliminado correctamente");
                        alerta2.show();
                        cargarAlumnos();
                    } else {
                        Alert alerta2 = new Alert(AlertType.ERROR);
                        alerta2.setTitle("Error");
                        alerta2.setHeaderText(null);
                        alerta2.setContentText("No se pudo eliminar al alumno.");
                        alerta2.show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alerta2 = new Alert(AlertType.INFORMATION);
            alerta2.setTitle("Cancelado");
            alerta2.setHeaderText(null);
            alerta2.setContentText("El alumno no ha sido eliminado.");
            alerta2.show();
        }
        cancelar(event);
    }

    @FXML
    private void guardar(ActionEvent event) {
        int codigo=0;
        try {
            Integer.parseInt(TxtCodigo.getText());
            codigo = Integer.parseInt(TxtCodigo.getText());
        } catch (NumberFormatException e) {
            Alert alerta2 = new Alert(AlertType.ERROR);
            alerta2.setTitle("Error");
            alerta2.setHeaderText(null);
            alerta2.setContentText("El CI debe ser numerico");
            alerta2.show(); //verificar q el ci sea numercio

        }
        if(codigo<1000000){
            Alert alerta2 = new Alert(AlertType.ERROR);
            alerta2.setTitle("Error");
            alerta2.setHeaderText(null);
            alerta2.setContentText("CI incorrecto");
            alerta2.show();//verificar que el ci no sea un num cualquiera
            return;
        }
       
        String nombre = TxtNombre.getText();
        String apellido = TxtApellido.getText();
        String curso = dropmenuCurso.getText();
        String seccion = dropmenuSeccion.getText();
        String especialidad = dropmenuEspe.getText();
       if ((!especialidad.equals("Construcciones Civiles") && !especialidad.equals("Química Industrial") && !especialidad.equals("Electrónica")) && seccion.equals("3ra")){
            Alert alerta2 = new Alert(AlertType.ERROR);
            alerta2.setTitle("Error");
            alerta2.setHeaderText(null);
            alerta2.setContentText("Seccion invalida");
            alerta2.show();
            return;
        }

        // Validación básica
        if (TxtCodigo.getText().isEmpty() || nombre.isEmpty() || apellido.isEmpty() || curso.equals("Seleccionar Curso") || seccion.equals("Seleccionar Sección") || especialidad.equals("Seleccionar Especialidad")) {
            // Muestra un mensaje de error al usuario
            Alert alerta2 = new Alert(AlertType.ERROR);
            alerta2.setTitle("Error");
            alerta2.setHeaderText(null);
            alerta2.setContentText("Todos los campos son obligatorios.");
            alerta2.show();
            return;
        }
        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "INSERT INTO estudiante (idEstudiante, Estado, Nombre, Apellido, Curso, Seccion, Especialidad) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, codigo);
                pstmt.setInt(2, 1);
                pstmt.setString(3, nombre);
                pstmt.setString(4, apellido);
                pstmt.setString(5, curso);
                pstmt.setString(6, seccion);
                pstmt.setString(7, especialidad);

                int filasAfectadas = pstmt.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("Alumno guardado correctamente.");

                    Alert alerta2 = new Alert(AlertType.INFORMATION);
                    alerta2.setTitle("Éxito");
                    alerta2.setHeaderText(null);
                    alerta2.setContentText("Alumno guardado correctamente.");
                    alerta2.show();
                } else {
                    System.out.println("No se pudo guardar el alumno.");

                    Alert alerta2 = new Alert(AlertType.ERROR);
                    alerta2.setTitle("Error");
                    alerta2.setHeaderText(null);
                    alerta2.setContentText("No se pudo guardar al alumno.");
                    alerta2.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cargarAlumnos();
        cancelar(event);
    }

    @FXML
    private void cancelar(ActionEvent event) {
        labelEstado.setVisible(false);
        dropmenuEstado.setVisible(false);

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

    private void cargarAlumnos() {
        listaAlumnos.clear();

        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "SELECT idEstudiante, Nombre, Apellido, Curso, Seccion, Especialidad, Estado FROM estudiante";
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String estadito = "NO ACTIVO";
                    if (rs.getInt("Estado") == 1) {
                        estadito = "ACTIVO";
                    }
                    Alumno alumno = new Alumno(
                            rs.getInt("idEstudiante"),
                            rs.getString("Nombre"),
                            rs.getString("Apellido"),
                            String.valueOf(rs.getString("Curso")),
                            String.valueOf(rs.getString("Seccion")),
                            rs.getString("Especialidad"),
                            estadito
                    );
                    listaAlumnos.add(alumno);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TablaClientes.setItems(listaAlumnos);
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
