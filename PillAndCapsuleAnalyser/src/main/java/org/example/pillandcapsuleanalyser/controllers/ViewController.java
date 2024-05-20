package org.example.pillandcapsuleanalyser.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.example.pillandcapsuleanalyser.HelloApplication;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable {
    public static ViewController viewController;

    @FXML
    public ImageView mainView, bAndWView,colorView,randColorView;

    @FXML
    public Label viewTitle;

    @FXML
    public void openFileMenu(){
        HelloApplication.mainStage.setScene(HelloApplication.mainS);
        MainController.mainController.openFileMenu();
    }

    @FXML
    public void bAndWViewOption(){
        MainController.mainController.bAndWViewOption();
    }
    @FXML
    public void colorOption(){
        MainController.mainController.colorOption();
    }
    @FXML
    public void randomColorOption(){
        MainController.mainController.randomColorOption();
    }
    @FXML
    public void allViewsOption(){
        MainController.mainController.allViewsOption();
    }
    @FXML
    public void back(){
        HelloApplication.mainStage.setScene(HelloApplication.mainS);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewController=this;
    }
}
