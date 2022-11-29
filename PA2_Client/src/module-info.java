module edu.ucdenver.pa2_client{
    requires javafx.controls;
    requires javafx.fxml;


    exports edu.ucdenver.app;
    opens edu.ucdenver.app to javafx.fxml;
}