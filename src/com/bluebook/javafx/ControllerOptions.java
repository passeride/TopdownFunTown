package com.bluebook.javafx;

import com.bluebook.engine.GameApplication;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class ControllerOptions {

    @FXML
    private Slider slider;

    boolean b = true;
    GameApplication gameApplication = GameApplication.getInstance();

    @FXML
    private void toggleLyd(ActionEvent event) {
        // TODO: 2/27/18
        //sette lyd fra audioplayer sin setVolume function
        if (b) {
            slider.setDisable(true);
            b = false;
        } else {
            slider.setDisable(false);
            b = true;
            int vol = (int) slider.getValue();
        }

    }

    @FXML
    private void setPlayerName(ActionEvent event) {
        // TODO: 2/27/18
        //sette navn på gui til det satt her
    }

    @FXML
    private void setGore(ActionEvent event) {
        // TODO: 2/27/18
        //set/clear gore from gamePane
    }

    @FXML
    private void returnToMenu(ActionEvent event) throws IOException{
        FXMLLoader fxml = new FXMLLoader();
        Parent root = fxml.load(getClass().getResource("menu.fxml").openStream());
        Stage stageOptions = gameApplication.getStage();
        stageOptions.setScene(new Scene(root));
        stageOptions.show();
    }
}
