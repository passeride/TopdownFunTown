package com.bluebook.javafx;

import com.bluebook.engine.GameApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerOptions {



    @FXML
    private CheckBox lydCheckBox;

    @FXML
    private Slider slider;

    boolean b = true;
    GameApplication gameApplication = GameApplication.getInstance();
    //AudioPlayer audioPlayer = new AudioPlayer();

    @FXML
    private void toggleLyd(ActionEvent event){
        // TODO: 2/27/18
        //sette lyd fra audioplayer sin setVolume function
        if(b){
            slider.setDisable(true);
            b = false;
            //audioPlayer.setVolume(-100);
        }else {
            slider.setDisable(false);
            b = true;
            int vol = (int) slider.getValue();
            //audioPlayer.setVolume(vol);
        }

    }

    @FXML
    private void setPlayerName(ActionEvent event){
        // TODO: 2/27/18
        //sette navn på gui til det satt her
    }

    @FXML
    private void setGore(ActionEvent event){
        // TODO: 2/27/18
        //set/clear gore from game
    }

    @FXML
    private void returnToMenu(ActionEvent event){
        FXMLLoader fxml = new FXMLLoader();
        Parent root = null;
        try {
            root = fxml.load(getClass().getResource("menu.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stageOptions = gameApplication.getStage();
        stageOptions.setScene(new Scene(root));
        stageOptions.show();
    }
}