package com.topdownfuntown.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.Collider;
import com.bluebook.util.Vector2;

public class Crate extends Obstacle {
    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     */
    public Crate(Vector2 position) {
        super(position, Vector2.ZERO, new Sprite("items/crate"));
        setSize(new Vector2(sprite.getSquareWidth(), sprite.getSquareHeight()));
        this.collider = new Collider(this);
        collider.setTag("Block");
        collider.setName("Crate");

    }
}
