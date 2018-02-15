package com.topdownfuntown.objects;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;
import com.topdownfuntown.main.Topdownfuntown;

public class Player extends GameObject {


    AudioPlayer hitSound;
    private double speed = 100.0; // Gotta go fast
    private double baseSpeed = 100.0;
    private double speedBoostSpeed = 1000.0;
    private boolean speedBost = false;
    Topdownfuntown topdownfuntown;

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
    public void moveUp(double delta) {
        translate(Vector2.multiply(Vector2.UP, speed * delta));
    }

    /**
     * Will move the player object SOUTH/DOWN by {@link Player#speed}
     */
    public void moveDown(double delta) {
        translate(Vector2.multiply(Vector2.DOWN, speed * delta));
    }

    /**
     * Will move the player object WEST/LEFT by {@link Player#speed}
     */
    public void moveLeft(double delta) {
        translate(Vector2.multiply(Vector2.LEFT, speed * delta));
    }

    /**
     * Will move the player object EAST/RIGHT by {@link Player#speed}
     */
    public void moveRight(double delta) {
        translate(Vector2.multiply(Vector2.RIGHT, speed * delta));
    }

    /**
     * Override to create a 8 % margin for movement
     *
     * @param moveVector
     */
    @Override
    public void translate(Vector2 moveVector) {
        Vector2 newValue = Vector2.add(position, moveVector);

        double screenWidth = GameSettings.getInt("game_resolution_X");
        double screenHeihgt = GameSettings.getInt("game_resolution_Y");
        double boudMarginX = screenWidth * GameSettings.getDouble("map_movement_padding_X");
        double boudMarginY = screenHeihgt * GameSettings.getDouble("map_movement_padding_Y");

        if (newValue.getX() <= screenWidth - boudMarginX
                && newValue.getX() > boudMarginX
                && newValue.getY() <= screenHeihgt - boudMarginY
                && newValue.getY() > boudMarginY) {
            position = newValue;
        }
    }

    /**
     * Used when player is hit to subtract health and check for death
     */
    public void hit() {
        int hp = topdownfuntown.getHealth();
        topdownfuntown.setHealth(hp--);
        if (hp <= 0) {
            die();
            destroy();
        }
        hitSound.playOnce();
    }

    private void die() {
        GameEngine.getInstance().Pause();
    }

    public void activateGottaGoFast() {
        speedBost = true;
        speed = baseSpeed + speedBoostSpeed;
    }

    public void deactivateGottaGoFast() {
        speedBost = false;
        speed = baseSpeed;
    }


}
