package com.rominntrenger.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;

public class Obstacle extends GameObject {


    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public Obstacle(Vec2 position, Vec2 direction, Sprite sprite) {
        super(position, direction, sprite);
    }


}
