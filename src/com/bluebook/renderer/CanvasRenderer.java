package com.bluebook.renderer;

import com.bluebook.engine.GameEngine;
import com.bluebook.physics.Collider;
import com.topdownfuntown.objects.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.bluebook.util.GameObject;
import javafx.scene.text.Font;

import java.util.ArrayList;

/**
 * This class is used to draw all {@link GameObject}
 */
public class CanvasRenderer {

    private ArrayList<GameObject> drawables = new ArrayList<>();
    private ArrayList<GameObject> drawablesInBuffer = new ArrayList<>();
    private ArrayList<GameObject> drawableOutBuffer = new ArrayList<>();
    private ArrayList<Collider> colliderDebugDrawables = new ArrayList<>();
    private ArrayList<Collider> colliderInBuffer = new ArrayList<>();
    private ArrayList<Collider> colliderOutBuffer = new ArrayList<>();



    private static CanvasRenderer singelton;

    private Canvas canvas;

    public void addCollider(Collider col){
        colliderInBuffer.add(col);
    }

    public void removeCollider(Collider col){
        colliderOutBuffer.add(col);
    }

    /**
     * This function will add the GameObject to the list of drawables to be drawn onto the canvas
     * @param in Object to be drawn on canvas
     */
    public void addGameObject(GameObject in){
        drawablesInBuffer.add(in);
    }

    public void removeGameObject(GameObject go){
        drawableOutBuffer.add(go);
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
    public void drawAll(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        clearCanvas(gc);
        for(GameObject go : drawables){
            go.draw(gc);
            if(go instanceof Player){
                gc.setFont(new Font(20.0));
                gc.setFill(Color.RED);
                gc.fillText("Health: " +  ((Player)go).getHealth(),10,50);
            }
        }

        if(GameEngine.DEBUG){
            for(Collider c : colliderDebugDrawables){
                c.debugDraw(gc);
            }
        }

;

        moveDrawablesBuffers();
        moveColliderBuffers();
    }

    private void moveColliderBuffers() {
        colliderDebugDrawables.addAll(colliderInBuffer);
        colliderInBuffer.clear();

        for(Collider col : colliderOutBuffer){
            if(colliderDebugDrawables.contains(col))
                colliderDebugDrawables.remove(col);
        }
        colliderOutBuffer.clear();
    }

    private void moveDrawablesBuffers() {
        drawables.addAll(drawablesInBuffer);
        drawablesInBuffer.clear();

        for(GameObject go : drawableOutBuffer){
            if(drawables.contains(go))
                drawables.remove(go);
        }
        drawableOutBuffer.clear();
    }

    /**
     * Draw all objects onto canvas
     * @param canvas canvas for objects to be drawn to
     */
    public void drawAll(Canvas canvas){
        setCanvas(canvas);
        drawAll();
    }

    private void clearCanvas(GraphicsContext gc){
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    public void setCanvas(Canvas canvas){
        this.canvas = canvas;
    }

}
