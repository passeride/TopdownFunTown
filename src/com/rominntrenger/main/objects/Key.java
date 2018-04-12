package com.rominntrenger.main.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.Vector2;

public class Key extends Item {
    private Door door;

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
    public Key(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
    }

    public Key(Vector2 position, Vector2 direction, Sprite sprite, Door door) {
        super(position, direction, sprite);
        this.door = door;
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    public Door getDoor() {
        return door;
    }
}
