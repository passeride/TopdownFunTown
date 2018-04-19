package com.bluebook.graphics;

import com.bluebook.renderer.GraphicsRenderer;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Transform;
import com.bluebook.util.Vec2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

/**
 * Wrapper for image, used to draw images
 */
public class Sprite {

    Image img;
    private double rotateAngle;
    private double squareWidth = 64;
    private double squareHeight = 64;

    private Transform origin;

    private double scaledSquareWidth, scaledSquareHeight;

    private boolean isRotated = false;


    /**
     * Sprite object with required image
     *
     * @param name relative name to asset
     */
    public Sprite(String name) {
        loadImage(name);
    }

    public Sprite(String name, Transform origin) {
        loadImage(name);
        this.origin = origin;

    }

    private void loadImage(String name) {
        this.img = SpriteLoader.loadImage(name);
    }


    public void setOrigin(Transform origin) {
        this.origin = origin;
    }

    protected Sprite() {

    }

    /**
     * Draws the image on screen on given coordinates pivoted around given axis
     */
    public void draw(GraphicsContext gc, Vec2 position, Vec2 angle) {
        rotate(angle);
        draw(gc, position);
    }

    public void draw(GraphicsRenderer gr) {
        if (origin != null) {
            gr.drawImage(origin.getGlobalPosition(), origin.getGlobalRotation().getAngleInDegrees(),
                img);
        }
    }

    public void draw(GraphicsRenderer gc, Vec2 position, Vec2 angle) {
        rotate(angle);
        draw(gc, position);
    }

    public void draw(GraphicsRenderer gc, Vec2 position) {
        gc.save();

        scaledSquareWidth = squareWidth;
        scaledSquareHeight = squareHeight;

        if (isRotated) {
            gc.rotate(position, rotateAngle);
        }

        if (origin != null) {

            Vec2 pos = origin.getGlobalPosition();
            Vec2 scale = origin.getGlobalScale();
            rotate(origin.getGlobalRotation());
            gc.rotate(position, rotateAngle);

            Vec2 scaleVec = GameSettings.getSquareScale();
            scaledSquareHeight = scaleVec.getY() * scale.getY();
            scaledSquareWidth = scaleVec.getX() * scale.getY();

            gc.drawImage(img, pos.getX() - (scaledSquareWidth / 2f),
                pos.getY() - (scaledSquareHeight / 2f), scaledSquareWidth, scaledSquareHeight);
        } else {
            gc.drawImage(img, position.getX() - (scaledSquareWidth / 2f),
                position.getY() - (scaledSquareHeight / 2f), scaledSquareWidth, scaledSquareHeight);
        }

        gc.restore();
    }

    /**
     * Used to draw background
     */
    public void drawBackground(GraphicsContext gc) {
        gc.drawImage(img, 0, 0, GameSettings.getInt("game_resolution_X"),
            GameSettings.getInt("game_resolution_Y"));
    }

    /**
     * Will draw a sprite not relative to world, but on screen
     *
     * @param position is a prosentage of the screen
     * @param scale is width relative to normal square size
     */
    public void drawGUIProsentage(GraphicsContext gc, Vec2 position, Vec2 scale) {
        Vec2 scaleVec = GameSettings.getSquareScale();
        scaledSquareHeight = scaleVec.getY() * scale.getY();
        scaledSquareWidth = scaleVec.getX() * scale.getX();
        Vec2 pos = Vec2.multiply(position,
            new Vec2(GameSettings.getDouble("game_resolution_X"),
                GameSettings.getDouble("game_resolution_Y")));
        gc.drawImage(img, pos.getX() - (scaledSquareWidth / 2f),
            pos.getY() - (scaledSquareHeight / 2f), scaledSquareWidth, scaledSquareHeight);
    }

    /**
     * Will draw sprite on screen not relative to world
     *
     * @param position Will be the prosentage of screen
     */
    public void drawGUIProsentage(GraphicsContext gc, Vec2 position) {
        Vec2 scale = origin.getGlobalScale();

        Vec2 scaleVec = GameSettings.getSquareScale();
        scaledSquareHeight = scaleVec.getY() * scale.getY();
        scaledSquareWidth = scaleVec.getX() * scale.getX();
        Vec2 pos = Vec2.multiply(position,
            new Vec2(GameSettings.getDouble("game_resolution_X"),
                GameSettings.getDouble("game_resolution_Y")));
        gc.drawImage(img, pos.getX() - (scaledSquareWidth / 2f),
            pos.getY() - (scaledSquareHeight / 2f), scaledSquareWidth, scaledSquareHeight);

    }

    /**
     * Will draw a sprite not relative to world, but on screen It will draw from TOP LEFT Courner
     *
     * @param position Will be the pixelPosition of gameScreen (1920x1080)
     * @param scale is width relative to normal square size
     */
    public void drawGUI(GraphicsContext gc, Vec2 position, Vec2 scale) {
        Vec2 scaleVec = GameSettings.getSquareScale();
        scaledSquareHeight = scaleVec.getY() * scale.getY();
        scaledSquareWidth = scaleVec.getX() * scale.getX();
//        Vec2 pos = Vec2.multiply(position, GameSettings.getSquareScale());
        Vec2 pos = position;
        gc.drawImage(img, pos.getX(), pos.getY(), scaledSquareWidth, scaledSquareHeight);
    }

    /**
     * Will draw sprite on screen not relative to world It will draw from TOP LEFT Courner
     *
     * @param position Will be the pixelPosition of gameScreen (1920x1080)
     */
    public void drawGUI(GraphicsContext gc, Vec2 position) {
        Vec2 scale = origin.getGlobalScale();

        Vec2 scaleVec = GameSettings.getSquareScale();
        scaledSquareHeight = scaleVec.getY() * scale.getY();
        scaledSquareWidth = scaleVec.getX() * scale.getX();
//        Vec2 pos = Vec2.multiply(position, GameSettings.getSquareScale());
        Vec2 pos = position;
        gc.drawImage(img, pos.getX(), pos.getY(), scaledSquareWidth, scaledSquareHeight);

    }

    public void drawGUI(GraphicsContext gc, Vec2 position, int WIDTH, int HEIGHT) {
        Vec2 pos = position;
        gc.drawImage(img, pos.getX(), pos.getY(), WIDTH, HEIGHT);
    }

    /**
     * Draws the image on given coordinates
     */
    public void draw(GraphicsContext gc, Vec2 position) {
        gc.save();

        scaledSquareWidth = squareWidth;
        scaledSquareHeight = squareHeight;

        if (isRotated) {
            gc = rotateGraphicsContext(gc, position);
        }

        if (origin != null) {

            Vec2 pos = origin.getGlobalPosition();
            Vec2 scale = origin.getGlobalScale();
            rotate(origin.getGlobalRotation());
            gc = rotateGraphicsContext(gc, pos);

            Vec2 scaleVec = GameSettings.getSquareScale();
            scaledSquareHeight = scaleVec.getY() * scale.getY();
            scaledSquareWidth = scaleVec.getX() * scale.getX();

            gc.drawImage(img, pos.getX() - (scaledSquareWidth / 2f),
                pos.getY() - (scaledSquareHeight / 2f), scaledSquareWidth, scaledSquareHeight);
        } else {
            gc.drawImage(img, position.getX() - (scaledSquareWidth / 2f),
                position.getY() - (scaledSquareHeight / 2f), scaledSquareWidth, scaledSquareHeight);
        }

        gc.restore();
    }

    protected GraphicsContext rotateGraphicsContext(GraphicsContext gc, Vec2 position) {
        Rotate r = new Rotate(rotateAngle, position.getX(), position.getY());
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        return gc;
    }

    /**
     * Used to rotate sprite around center axis
     */
    public void rotate(double angle) {
        this.rotateAngle = angle;
        isRotated = true;
    }

    /**
     * Used to rotate sprite around center axis
     */
    public void rotate(Vec2 angle) {
        rotateAngle = angle.getAngleInDegrees();
        isRotated = true;
    }

    /**
     * Used to reset the rotation
     */
    public void clearRotation() {
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

    public Image getImg() {
        return img;
    }
}
