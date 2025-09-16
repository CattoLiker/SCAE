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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ComidaSemanaController implements Initializable {

    @FXML
    private TextField TxtBuscar;
    @FXML
    private TableView<SemanaMenuComidas> TablaComidas;
    @FXML
    private TableColumn<SemanaMenuComidas, String> ColumnDia;
    @FXML
    private TableColumn<SemanaMenuComidas, Integer> ColumnSemana;
    @FXML
    private TableColumn<SemanaMenuComidas, String> ColumnComida;
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
    private SplitMenuButton Semana;
    @FXML
    private MenuItem semana1;
    @FXML
    private MenuItem semana2;
    @FXML
    private MenuItem semana3;
    @FXML
    private MenuItem semana4;
    @FXML
    private TextField TxtNombre; // Para mostrar el nombre de la comida seleccionada
    @FXML
    private Button btnMenuComida;

    private ObservableList<SemanaMenuComidas> listaSemanaMenuComidas = FXCollections.observableArrayList();

    // Para selección
    private int idComidasSeleccionada = -1;
    private String nombreComidaSeleccionada = "";
    private int diaSemanaSeleccionado = 0;      // 1=lunes ... 5=viernes
    private int idSemanaMenuSeleccionada = 0;   // 1..4

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ColumnSemana.setCellValueFactory(new PropertyValueFactory<>("idSemanaMenu"));
        ColumnDia.setCellValueFactory(new PropertyValueFactory<>("diaSemana"));
        ColumnComida.setCellValueFactory(new PropertyValueFactory<>("nombreComida"));
        cargarSemanaMenuComidas();

        // Listeners para menú de día
        diaLun.setOnAction(e -> setDiaSemana(1, "Lunes"));
        diaMar.setOnAction(e -> setDiaSemana(2, "Martes"));
        diaMier.setOnAction(e -> setDiaSemana(3, "Miércoles"));
        diaJue.setOnAction(e -> setDiaSemana(4, "Jueves"));
        diaVier.setOnAction(e -> setDiaSemana(5, "Viernes"));

        // Listeners para menú de semana
        semana1.setOnAction(e -> setSemanaMenu(1, "Semana 1"));
        semana2.setOnAction(e -> setSemanaMenu(2, "Semana 2"));
        semana3.setOnAction(e -> setSemanaMenu(3, "Semana 3"));
        semana4.setOnAction(e -> setSemanaMenu(4, "Semana 4"));

        // Estado inicial de botones
        BtnNuevo.setDisable(false);
        BtnGuardar.setDisable(true);
        BtnCancelar.setDisable(true);
        BtnModificar.setDisable(true);
        BtnEliminar.setDisable(true);

        ColumnDia.setCellValueFactory(cellData -> {
            int dia = cellData.getValue().getDiaSemana();
            return new javafx.beans.property.SimpleStringProperty(diaNombre(dia));
        });
    }

    private void setDiaSemana(int numero, String texto) {
        diaSemanaSeleccionado = numero;
        Día.setText(texto);
    }

    private void setSemanaMenu(int numero, String texto) {
        idSemanaMenuSeleccionada = numero;
        Semana.setText(texto);
    }

    private void cargarSemanaMenuComidas() {
        listaSemanaMenuComidas.clear();
        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "SELECT smc.DiaSemana, smc.Comidas_idComidas, smc.SemanaMenu_idSemanaMenu, c.Nombre AS nombreComida "
                    + "FROM SemanaMenuComidas smc JOIN Comidas c ON smc.Comidas_idComidas = c.idComidas";
            try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SemanaMenuComidas smc = new SemanaMenuComidas(
                            rs.getInt("DiaSemana"),
                            rs.getInt("Comidas_idComidas"),
                            rs.getInt("SemanaMenu_idSemanaMenu"),
                            rs.getString("nombreComida")
                    );
                    listaSemanaMenuComidas.add(smc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TablaComidas.setItems(listaSemanaMenuComidas);
    }

    @FXML
    private void guardar(ActionEvent event) {
        if (idComidasSeleccionada == -1 || idSemanaMenuSeleccionada == 0 || diaSemanaSeleccionado == 0) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Seleccione los datos correspondientes", ButtonType.OK);
            alerta.showAndWait();
            return;
        }
        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "INSERT INTO SemanaMenuComidas (DiaSemana, Comidas_idComidas, SemanaMenu_idSemanaMenu) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, diaSemanaSeleccionado);
                pstmt.setInt(2, idComidasSeleccionada);
                pstmt.setInt(3, idSemanaMenuSeleccionada);

                int filas = pstmt.executeUpdate();
                if (filas > 0) {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Registro guardado correctamente", ButtonType.OK);
                    alerta.showAndWait();
                    cargarSemanaMenuComidas();
                    return;
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR, "No se pudo guardar los datos", ButtonType.OK);
            alerta.showAndWait();
            
        }
        cancelar(event);
    }

    @FXML
    private void modificar(ActionEvent event) {
        SemanaMenuComidas seleccion = TablaComidas.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            Alert alerta = new Alert(Alert.AlertType.ERROR, "Selecciona un registro para modificar.", ButtonType.OK);
            alerta.showAndWait();
            return;
        }
        try (Connection conn = ConeccionDB.getConnection()) {
            String sql = "UPDATE SemanaMenuComidas SET Comidas_idComidas=?, SemanaMenu_idSemanaMenu=? WHERE DiaSemana=? AND Comidas_idComidas=? AND SemanaMenu_idSemanaMenu=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idComidasSeleccionada);
                pstmt.setInt(2, idSemanaMenuSeleccionada);
                pstmt.setInt(3, seleccion.getDiaSemana());
                pstmt.setInt(4, seleccion.getIdComidas());
                pstmt.setInt(5, seleccion.getIdSemanaMenu());

                int filas = pstmt.executeUpdate();
                if (filas > 0) {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Registro modificado correctamente.", ButtonType.OK);
                    alerta.showAndWait();
                    cargarSemanaMenuComidas();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR, "No se pudo modificar los datos", ButtonType.OK);
            alerta.showAndWait();
        }
        cancelar(event);
    }

    @FXML
    private void eliminar(ActionEvent event) {
        SemanaMenuComidas seleccion = TablaComidas.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Selecciona una regstro para eliminar", ButtonType.OK);
            alerta.showAndWait();
            return;
        }
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea eliminar el registro?", ButtonType.YES, ButtonType.NO);
        alerta.setHeaderText(null);
        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.YES) {
            try (Connection conn = ConeccionDB.getConnection()) {
                String sql = "DELETE FROM SemanaMenuComidas WHERE DiaSemana=? AND Comidas_idComidas=? AND SemanaMenu_idSemanaMenu=?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, seleccion.getDiaSemana());
                    pstmt.setInt(2, seleccion.getIdComidas());
                    pstmt.setInt(3, seleccion.getIdSemanaMenu());
                    int filas = pstmt.executeUpdate();
                    if (filas > 0) {
                        System.out.println("Registro eliminado correctamente.");
                        cargarSemanaMenuComidas();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alert alerta1 = new Alert(Alert.AlertType.ERROR, "No se pudo eliminar los datos", ButtonType.OK);
                alerta.showAndWait();
            }
        }
        cancelar(event);
    }

    @FXML
    private void buscar(KeyEvent event) {
        String filtro = TxtBuscar.getText().toLowerCase();

        if (filtro.isEmpty()) {
            TablaComidas.setItems(listaSemanaMenuComidas);
            return;
        }

        ObservableList<SemanaMenuComidas> filtrados = FXCollections.observableArrayList();
        for (SemanaMenuComidas smc : listaSemanaMenuComidas) {
            if (String.valueOf(smc.getIdSemanaMenu()).contains(filtro)
                    || String.valueOf(smc.getDiaSemana()).contains(filtro)
                    || smc.getNombreComida().toLowerCase().contains(filtro)) {
                filtrados.add(smc);
            }
        }
        TablaComidas.setItems(filtrados);
    }

    @FXML
    private void mostrarFila(MouseEvent event) {
        TxtNombre.setText("Seleccione nombre de la comida");
        idComidasSeleccionada = -1;
        nombreComidaSeleccionada = "";
        idSemanaMenuSeleccionada = 0;
        diaSemanaSeleccionado = 0;
        BtnNuevo.setDisable(false);
        BtnGuardar.setDisable(true);
        BtnCancelar.setDisable(true);
        BtnModificar.setDisable(true);
        BtnEliminar.setDisable(true);
        btnMenuComida.setDisable(true);
        Semana.setDisable(true);
        Día.setDisable(true);
        Semana.setText("Seleccionar Semana");
        Día.setText("Seleccionar Día");

        SemanaMenuComidas seleccion = TablaComidas.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            idSemanaMenuSeleccionada = seleccion.getIdSemanaMenu();
            diaSemanaSeleccionado = seleccion.getDiaSemana();
            idComidasSeleccionada = seleccion.getIdComidas();
            nombreComidaSeleccionada = seleccion.getNombreComida();
            TxtNombre.setText(nombreComidaSeleccionada);
            Semana.setText("Semana " + idSemanaMenuSeleccionada);
            Día.setText(diaNombre(diaSemanaSeleccionado));

            Semana.setDisable(false);
            btnMenuComida.setDisable(false);
            BtnModificar.setDisable(false);
            BtnEliminar.setDisable(false);
            BtnNuevo.setDisable(true);
        }
    }

    private String diaNombre(int n) {
        switch (n) {
            case 1:
                return "Lunes";
            case 2:
                return "Martes";
            case 3:
                return "Miércoles";
            case 4:
                return "Jueves";
            case 5:
                return "Viernes";
            default:
                return "";
        }
    }

    @FXML
    private void desplegarMenuComida(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SeleccionarComida.fxml"));
            Parent root = loader.load();
            SeleccionarComidaController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Seleccionar Comida");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            Comida comidaSeleccionada = controller.getComidaSeleccionada();
            if (comidaSeleccionada != null) {
                idComidasSeleccionada = comidaSeleccionada.getIdComidas();
                nombreComidaSeleccionada = comidaSeleccionada.getNombre();
                TxtNombre.setText(nombreComidaSeleccionada);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        TxtNombre.setText("Seleccione nombre de la comida");
        idComidasSeleccionada = -1;
        nombreComidaSeleccionada = "";
        idSemanaMenuSeleccionada = 0;
        diaSemanaSeleccionado = 0;
        BtnNuevo.setDisable(false);
        BtnGuardar.setDisable(true);
        BtnCancelar.setDisable(true);
        BtnModificar.setDisable(true);
        BtnEliminar.setDisable(true);
        btnMenuComida.setDisable(true);
        Semana.setDisable(true);
        Día.setDisable(true);
        Semana.setText("Seleccionar Semana");
        Día.setText("Seleccionar Día");
    }

    @FXML
    private void nuevo(ActionEvent event) {
        BtnNuevo.setDisable(true);
        BtnGuardar.setDisable(false);
        BtnCancelar.setDisable(false);
        BtnModificar.setDisable(true);
        BtnEliminar.setDisable(true);

        Día.setDisable(false);
        Semana.setDisable(false);
        btnMenuComida.setDisable(false);
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
}
