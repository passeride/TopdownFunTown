package com.topdownfuntown.objects;

import com.bluebook.util.GameObject;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.Vector2;

public class Projectile extends GameObject{

    private double speed = 100.0;


    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
    public Projectile(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
    }


    @Override
    public void update(double delta){
        translate(Vector2.multiply(direction, speed * delta));
    }

}
