package com.topdownfuntown.objects;


import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.graphics.SpriteLoader;
import com.bluebook.util.Vector2;

public class GreenAlien extends Enemy {


    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
    public GreenAlien(Vector2 position) {
        super(position, Vector2.ZERO, new AnimationSprite(SpriteLoader.loadAnimationImage("/enemies/enemyGreen")));
    }
}
