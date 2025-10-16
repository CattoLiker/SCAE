package com.scaeproyecto.sistemacontrolalmuerzoescolarctn;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.temporal.WeekFields;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author abiga
 */
public class MenuInicioUserController implements Initializable {

    LocalTime limite = LocalTime.of(23, 0);
    LocalTime inicio = LocalTime.of(1, 0);
    LocalTime finServicio = LocalTime.of(23, 0);

    private LocalDate fecha = LocalDate.now();
    private int semanaDelAno = semanaDelAno = fecha.get(WeekFields.ISO.weekOfYear());
    private int SemanaMenu = ((semanaDelAno) % 4) + 1;

    private int idComida = conseguirComida(fecha, SemanaMenu);

    private String comidaDeHoy = conseguirNombreComida(idComida);
    
    private boolean yaRegistradoHoy = false; // Para evitar múltiples inserciones en el mismo día

    @FXML
    private Button btnHuella;
    @FXML
    private Button btnCI;
    @FXML
    private Label elija;
    @FXML
    private Label comidaHoy;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actualizarBotones();
        comidaHoy.setText("La comida de hoy es: " + comidaDeHoy);
        // Timeline para revisar la hora cada 1 segundo
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> actualizarBotones())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Timeline para revisar cada minuto si se debe registrar los que no comieron
        Timeline timelineRegistro = new Timeline(
                new KeyFrame(Duration.minutes(1), e -> registrarNoComieronSiCorresponde())
        );
        timelineRegistro.setCycleCount(Timeline.INDEFINITE);
        timelineRegistro.play();
    }

    private void actualizarBotones() {
        LocalTime ahora = LocalTime.now();
        boolean estaEnRango = !ahora.isBefore(inicio) && !ahora.isAfter(limite);
        btnHuella.setVisible(estaEnRango);
        btnCI.setVisible(estaEnRango);
        btnHuella.setDisable(!estaEnRango);
        btnCI.setDisable(!estaEnRango);
    }

    // ---------- MÉTODO NUEVO PARA REGISTRO AUTOMÁTICO ----------
    private void registrarNoComieronSiCorresponde() {
        LocalTime ahora = LocalTime.now();
        LocalDate hoy = LocalDate.now();

        // Si ya se registró hoy, no volver a hacerlo (reset si cambia el día)
        if (yaRegistradoHoy && !LocalDate.now().equals(ultimoRegistro)) {
            yaRegistradoHoy = false;
        }

        if (ahora.isAfter(finServicio) && !yaRegistradoHoy) {
            String sql = "INSERT INTO registroconsumo (Estudiante_idEstudiante, SemanaMenuComidas_Comidas_idComidas, fecha, HaComido) "
                    + "SELECT e.idEstudiante, ?, CURRENT_DATE, false "
                    + "FROM estudiante e "
                    + "WHERE e.Estado = 1 "
                    + "AND e.idEstudiante NOT IN ("
                    + "   SELECT r.Estudiante_idEstudiante FROM registroconsumo r WHERE r.fecha = CURRENT_DATE"
                    + ")";

            try (Connection conn = ConeccionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idComida);
                int filasInsertadas = stmt.executeUpdate();
                System.out.println("Estudiantes no comieron registrados: " + filasInsertadas);
                yaRegistradoHoy = true;
                ultimoRegistro = hoy;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private LocalDate ultimoRegistro = LocalDate.MIN;

    @FXML
    private void abrirMenuHuella(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("noHayHuella.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.show();
        } catch (IOException ex) {
            System.getLogger(MenuInicioController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    @FXML
    private void abrirMenuCI(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RegistroCI.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.show();
        } catch (IOException ex) {
            System.getLogger(MenuInicioController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    private int conseguirComida(LocalDate fecha, int SemanaMenu) {
        String sql = "SELECT comidas_idcomidas FROM semanamenucomidas WHERE DiaSemana = ? AND SemanaMenu_idSemanaMenu = ?";
        DayOfWeek hoy = fecha.getDayOfWeek();
        int nrodia = hoy.getValue();
        int idComid = -1; // valor por defecto si no encuentra nada
        try (Connection conn = ConeccionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nrodia);
            stmt.setInt(2, SemanaMenu);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idComid = rs.getInt("comidas_idcomidas");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idComid;
    }
    
    private String conseguirNombreComida(int IdComidaa) {
        String sql = "SELECT Nombre FROM comidas WHERE idComidas = ?";
        String comida = "";
        try (Connection conn = ConeccionDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, IdComidaa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    comida = rs.getString("Nombre");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comida;
    }
}
