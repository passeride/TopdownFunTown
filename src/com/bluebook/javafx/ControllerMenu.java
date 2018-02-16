package com.bluebook.javafx;


import com.bluebook.engine.GameApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMenu implements Initializable{

    GameApplication gameApplication;


    @FXML
    private ImageView imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        gameApplication = GameApplication.getInstance();
    }

    @FXML
    private Label label;


    @FXML
    private void handleButtonAction(ActionEvent event) {
        Stage stage = gameApplication.getStage();
        gameApplication.callGame(stage);
    }


}
