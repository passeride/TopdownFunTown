package com.bluebook.renderer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.bluebook.util.GameObject;

import java.util.ArrayList;

/**
 * This class is used to draw all {@link GameObject}
 */
public class CanvasRenderer {

    public ArrayList<GameObject> drawables = new ArrayList<>();

    private static CanvasRenderer singelton;

    private Canvas canvas;


    /**
     * This function will add the GameObject to the list of drawables to be drawn onto the canvas
     * @param in Object to be drawn on canvas
     */
    public void AddGameObject(GameObject in){
        drawables.add(in);
    }

    /**
     * Returns the instance
     */
    public static CanvasRenderer getInstance(){
        if(singelton == null)
            singelton = new CanvasRenderer();

        return singelton;
    }

    /**
     * Draw all objects onto canvas
     */
    public void DrawAll(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas(gc);
        for(GameObject go : drawables){
            go.draw(gc);
        }
    }

    /**
     * Draw all objects onto canvas
     * @param canvas canvas for objects to be drawn to
     */
    public void DrawAll(Canvas canvas){
        setCanvas(canvas);
        DrawAll();
    }

    private void clearCanvas(GraphicsContext gc){
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    public void setCanvas(Canvas canvas){
        this.canvas = canvas;
    }

}
