module com.example.ingibitor2026 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;

    opens com.example.ingibitor2026 to javafx.fxml;
    exports com.example.ingibitor2026;
}