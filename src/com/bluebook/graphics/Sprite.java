package com.bluebook.graphics;

import com.bluebook.engine.GameApplication;
import com.bluebook.util.Vector2;
import com.sun.corba.se.impl.orbutil.graph.Graph;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

/**
 * Wrapper for image, used to draw images
 */
public class Sprite {

    private Image img;
    private double rotateAngle;
    protected double squareWidth = 64 * GameApplication.X_scale;
    protected double squareHeight = 64 * GameApplication.Y_scale;

    protected boolean isRotated = false;


    /**
     * Sprite object with required image
     * @param name relative name to asset
     */
    public Sprite(String name){
        this.img = SpriteLoader.loadImage(name);
    }

    protected Sprite(){

    }

    /**
     * Draws the image on screen on given coordinates pivoted around given axis
     * @param gc
     * @param position
     * @param angle
     */
    public void draw(GraphicsContext gc, Vector2 position, Vector2 angle){
        rotate(angle);
        draw(gc, position);
    }

    /**
     * Used to draw background
     * @param gc
     */
    public void drawBackground(GraphicsContext gc){
        gc.drawImage(img, 0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    /**
     * Draws the image on given coordinates
     * @param gc
     * @param position
     */
    public void draw(GraphicsContext gc, Vector2 position){
        gc.save();

        if(isRotated)
            gc = rotateGraphicsContext(gc, position);

        gc.drawImage(img, position.getX() - (squareWidth / 2f), position.getY() - (squareHeight / 2f), squareWidth, squareHeight);

        gc.restore();
    }

    protected GraphicsContext rotateGraphicsContext(GraphicsContext gc, Vector2 position){
        Rotate r = new Rotate(rotateAngle, position.getX(), position.getY());
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        return gc;
    }

    /**
     * Used to rotate sprite around center axis
     * @param angle
     */
    public void rotate(double angle){
        this.rotateAngle = angle;
        isRotated = true;
    }

    /**
     * Used to rotate sprite around center axis
     * @param angle
     */
    public void rotate(Vector2 angle){
        rotateAngle = angle.getAngleInDegrees();
        isRotated = true;
    }

    /**
     * Used to reset the rotation
     */
    public void clearRotation(){
        isRotated = false;
    }

    public double getSquareWidth() {
        return squareWidth;
    }

    public void setSquareWidth(double squareWidth) {
        this.squareWidth = squareWidth;
    }

    public double getSquareHeight() {
        return squareHeight;
    }

    public void setSquareHeight(double squareHeight) {
        this.squareHeight = squareHeight;
    }
}
