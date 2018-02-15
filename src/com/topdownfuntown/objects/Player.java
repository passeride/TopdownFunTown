package com.topdownfuntown.objects;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
import com.bluebook.util.GameObject;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.Vector2;
import com.sun.javafx.geom.Vec2d;

public class Player extends GameObject {


    private double speed = 100.0; // Gotta go fast
    private double baseSpeed = 100.0;
    private double speedBoostSpeed = 1000.0;
    private boolean speedBost = false;
    AudioPlayer hitSound;

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
    public Player(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
        hitSound = new AudioPlayer("./assets/audio/lukasAuu.wav");
    }

    /**
     * Will move the player object NORTH/UP by {@link Player#speed}
     */
    public void moveUp(double delta){
        translate(Vector2.multiply(Vector2.UP, speed * delta));
    }

    /**
     * Will move the player object SOUTH/DOWN by {@link Player#speed}
     */
    public void moveDown(double delta){
        translate(Vector2.multiply(Vector2.DOWN, speed * delta));
    }

    /**
     * Will move the player object WEST/LEFT by {@link Player#speed}
     */
    public void moveLeft(double delta){
        translate(Vector2.multiply(Vector2.LEFT, speed * delta));
    }

    /**
     * Will move the player object EAST/RIGHT by {@link Player#speed}
     */
    public void moveRight(double delta){
        translate(Vector2.multiply(Vector2.RIGHT, speed * delta));
    }

    /**
     * Override to create a 8 % margin for movement
     * @param moveVector
     */
    @Override
    public void translate(Vector2 moveVector){
        Vector2 newValue = Vector2.add(position, moveVector);

        double screenWidth = GameApplication.getInstance().getScreenWidth();
        double screenHeihgt = GameApplication.getInstance().getScreenHeight();
        double boudMarginX = screenWidth * GameApplication.getInstance().getDouble("map_movement_padding_X");
        double boudMarginY = screenHeihgt * GameApplication.getInstance().getDouble("map_movement_padding_Y");

        if(newValue.getX() <= screenWidth - boudMarginX
                && newValue.getX() > boudMarginX
                && newValue.getY() <= screenHeihgt - boudMarginY
                && newValue.getY() > boudMarginY){
            position = newValue;
        }
    }

    /**
     * Used when player is hit to subtract health and check for death
     */
    public void hit(){
        HealthElement.health --;
        if(HealthElement.health <= 0){
            die();
            destroy();
        }
        hitSound.playOnce();
    }

    private void die(){
        GameEngine.getInstance().Pause();
    }

    public void activateGottaGoFast(){
        speedBost = true;
        speed = baseSpeed + speedBoostSpeed;
    }

    public void deactivateGottaGoFast(){
        speedBost = false;
        speed = baseSpeed;
    }


}
