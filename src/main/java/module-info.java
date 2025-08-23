module com.scaeproyecto.sistemacontrolalmuerzoescolarctn {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;
    requires java.desktop;
    opens com.scaeproyecto.sistemacontrolalmuerzoescolarctn to javafx.fxml;
    exports com.scaeproyecto.sistemacontrolalmuerzoescolarctn;
}
