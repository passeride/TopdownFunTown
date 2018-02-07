package com.bluebook.javafx;

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
    Canvas canvas;

    private GameEngine engine;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        engine = new GameEngine(canvas);

    }

    public GameEngine getEngine() {
        return engine;
    }

    /**
     * This is used when a change is detected in scaling
     * @param size
     */
    public void resizeCanvas(Vector2 size){

    }

    @FXML
    public void mouseHandler(MouseEvent event){
        engine.getPlayer().lookAt(new Vector2(event.getX(), event.getY()));
    }

    public void setCanvasWidth(double width){
        canvas.setWidth(width);
        //drawBackground();
    }

    public void setCanvasHeight(double height){
        canvas.setHeight(height);
        //drawBackground();
    }


}
