package com.topdownfuntown.objects;


import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.graphics.SpriteLoader;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.util.Vector2;

import java.util.Random;

public class GreenAlien extends Enemy {


    private double shootInterval = 1.8;
    private long prevShot = 0;

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     */
    public GreenAlien(Vector2 position) {
        super(position, Vector2.ZERO, new AnimationSprite("/enemies/enemyGreen"));
        Random r = new Random();
        prevShot = System.currentTimeMillis() + r.nextInt((int)(shootInterval * 1000));
    }


    @Override
    public void update(double delta){
        super.update(delta);
        if((System.currentTimeMillis() - prevShot) / 1000 >= shootInterval){
            prevShot = System.currentTimeMillis();;
            shoot();
        }
    }

    public void shoot(){
        Projectile p = new Projectile(position, direction, new Sprite("/projectiles/balltest"));
        p.setSize(new Vector2(32, 32));

        p.getSprite().setSquareHeight(32);
        p.getSprite().setSquareWidth(32);
        p.setPeriod(1.2f);
        p.setAmplitude(3f);
        p.setPhase(200f);
        p.setSpeed(600);
        p.setSine(true);
        p.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if(other.getGameObject() instanceof Player){
                    Player pl = (Player)other.getGameObject();
                    pl.hit();
                    p.destroy();
                }

            }
        });
    }
}
