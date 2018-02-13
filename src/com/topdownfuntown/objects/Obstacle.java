package com.topdownfuntown.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;

public class Obstacle extends GameObject {


    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
    public Obstacle(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
    }


}
