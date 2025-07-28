module com.scaeproyecto.sistemacontrolalmuerzoescolarctn {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    opens com.scaeproyecto.sistemacontrolalmuerzoescolarctn to javafx.fxml;
    exports com.scaeproyecto.sistemacontrolalmuerzoescolarctn;
}
