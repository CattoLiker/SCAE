module com.scaeproyecto.sistemacontrolalmuerzoescolarctn {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.scaeproyecto.sistemacontrolalmuerzoescolarctn to javafx.fxml;
    exports com.scaeproyecto.sistemacontrolalmuerzoescolarctn;
}
