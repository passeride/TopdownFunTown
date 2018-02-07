package com.bluebook.graphics;

import com.bluebook.util.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

/**
 * Wrapper for image, used to draw images
 */
public class Sprite {

    private Image img;
    private double rotateAngle;
    private double squareWidth = 128;
    private double squareHeight = 128;

    private boolean isRotated = false;


    /**
     * Sprite object with required image
     * @param img image to be drawn
     */
    public Sprite(Image img){
        this.img = img;
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

    private GraphicsContext rotateGraphicsContext(GraphicsContext gc, Vector2 position){
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

}
