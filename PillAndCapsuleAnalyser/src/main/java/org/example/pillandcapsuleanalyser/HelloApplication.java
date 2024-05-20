package org.example.pillandcapsuleanalyser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    public static Scene mainS,viewS,twoToneS;
    public static Stage mainStage;

    public static Scene changeScene(String file) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(file));
        return new Scene(fxmlLoader.load(), 1000, 700);
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        mainS = new Scene(fxmlLoader.load(), 1000, 700);
        viewS = changeScene("view-view.fxml");
        twoToneS = changeScene("two-tone-view.fxml");
        stage.setTitle("Pill and Capsule Analyser");
        stage.setScene(mainS);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}