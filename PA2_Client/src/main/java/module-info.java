module com.example.pa2_client {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pa2_client to javafx.fxml;
    exports com.example.pa2_client;
}