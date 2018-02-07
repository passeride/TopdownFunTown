package com.bluebook.objects;

import com.bluebook.util.GameObject;
import com.bluebook.util.Sprite;
import com.bluebook.util.Vector2;

public class Player extends GameObject {


    private double speed = 10.0;

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
    public void moveUp(){
        translate(Vector2.multiply(Vector2.UP, speed));
    }

    /**
     * Will move the player object SOUTH/DOWN by {@link Player#speed}
     */
    public void moveDown(){
        translate(Vector2.multiply(Vector2.DOWN, speed));
    }

    /**
     * Will move the player object WEST/LEFT by {@link Player#speed}
     */
    public void moveLeft(){
        translate(Vector2.multiply(Vector2.LEFT, speed));
    }

    /**
     * Will move the player object EAST/RIGHT by {@link Player#speed}
     */
    public void moveRight(){
        translate(Vector2.multiply(Vector2.RIGHT, speed));
    }


    public void translate(Vector2 moveVector){
        position = Vector2.add(position, moveVector);
    }

    public void lookAt(Vector2 lookPosition){
        direction =  Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(position, lookPosition));
    }


}
