package edu.ucdenver.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AdminApp.class.getResource("admin-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 920, 480);
        stage.setTitle("2022 World Cup Client - Admin");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
