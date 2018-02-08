package com.topdownfuntown.objects;

import com.bluebook.util.GameObject;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.Vector2;

public class Player extends GameObject {


    private double speed = 100.0; // Gotta go fast
    private double baseSpeed = 100.0;
    private double speedBoostSpeed = 1000.0;
    private boolean speedBost = false;

    private int health = 100;

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
    public Player(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
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

    public void hit(){
        health --;
    }

    public int getHealth(){
        return health;
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
