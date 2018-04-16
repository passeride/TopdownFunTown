package com.bluebook.javafx;

import com.bluebook.util.GameSettings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import com.bluebook.engine.GameEngine;
import com.bluebook.util.Vector2;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    public Canvas canvas;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GameSettings.SCREEN_HEIGHT = (int)canvas.getHeight();
        GameSettings.SCREEN_WIDTH = (int)canvas.getWidth();
        new GameEngine(canvas);
    }

    /**
     * Is called when stage is resized
     * @param width
     */
    public void setCanvasWidth(double width){
        canvas.setWidth(width);
    }

    /**
     * Is called when stage is resized
     * @param height
     */
    public void setCanvasHeight(double height){
        canvas.setHeight(height);
    }


}
