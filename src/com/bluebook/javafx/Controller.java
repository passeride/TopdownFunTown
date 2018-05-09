package com.bluebook.javafx;

import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
import com.bluebook.util.GameSettings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public Canvas canvas;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GameSettings.SCREEN_HEIGHT = (int) canvas.getHeight();
        GameSettings.SCREEN_WIDTH = (int) canvas.getWidth();
        if (GameEngine.getInstance() == null)
            new GameEngine(canvas);

        GameApplication ga = GameApplication.getInstance();
        GameApplication.X_scale.set(GameSettings.getInt("game_resolution_X") / ga.getScreenWidth());
        //Y_scale = getScreenHeight() / Integer.parseInt(loadedSettings.get("game_resolution_Y"));
        GameApplication.Y_scale.set(GameSettings.getInt("game_resolution_Y") / ga.getScreenHeight());


        canvas.scaleXProperty().bindBidirectional(GameApplication.X_scale);
        canvas.scaleYProperty().bindBidirectional(GameApplication.Y_scale);
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
