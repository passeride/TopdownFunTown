package com.bluebook.javafx;

import com.bluebook.engine.GameEngine;
import com.bluebook.util.GameSettings;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;

public class Controller implements Initializable {

    @FXML
    public Canvas canvas;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GameSettings.SCREEN_HEIGHT = (int) canvas.getHeight();
        GameSettings.SCREEN_WIDTH = (int) canvas.getWidth();
        new GameEngine(canvas);
    }

    /**
     * Is called when stage is resized
     */
    public void setCanvasWidth(double width) {
        canvas.setWidth(width);
    }

    /**
     * Is called when stage is resized
     */
    public void setCanvasHeight(double height) {
        canvas.setHeight(height);
    }


}
