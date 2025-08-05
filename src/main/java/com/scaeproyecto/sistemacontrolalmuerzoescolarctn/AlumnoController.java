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
        int codigo = Integer.parseInt(TxtCodigo.getText());
        String nombre = TxtNombre.getText();
        String apellido = TxtApellido.getText();
        String curso = dropmenuCurso.getText();
        String seccion = dropmenuSeccion.getText();
        String especialidad = dropmenuEspe.getText();

        // Validación básica
        if (TxtCodigo.getText().isEmpty() || nombre.isEmpty() || apellido.isEmpty()
                || curso.equals("Seleccionar Curso") || seccion.equals("Seleccionar Sección") || especialidad.equals("Seleccionar Especialidad")) {
            // Muestra un mensaje de error al usuario
            System.out.println("Todos los campos son obligatorios.");
            return;
        }

        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "INSERT INTO Estudiante (idEstudiante, Estado, Nombre, Apellido, Curso, Seccion, Especialidad) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
                    // Limpia los campos o actualiza la tabla
                } else {
                    System.out.println("No se pudo guardar el alumno.");
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

    private void cargarAlumnos() {
        listaAlumnos.clear();

        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "SELECT idEstudiante, Nombre, Apellido, Curso, Seccion, Especialidad FROM Estudiante";
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Alumno alumno = new Alumno(
                            rs.getInt("idEstudiante"),
                            rs.getString("Nombre"),
                            rs.getString("Apellido"),
                            String.valueOf(rs.getString("Curso")),
                            String.valueOf(rs.getString("Seccion")),
                            rs.getString("Especialidad")
                    );
                    listaAlumnos.add(alumno);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TablaClientes.setItems(listaAlumnos);
    }

}
