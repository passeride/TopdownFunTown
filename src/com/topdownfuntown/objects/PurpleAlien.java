package com.topdownfuntown.objects;


import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.util.Vector2;
import com.topdownfuntown.main.Topdownfuntown;

import java.util.Random;

public class PurpleAlien extends Enemy {


    private double shootInterval = 1.8;
    private long prevShot = 0;

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     */
    public PurpleAlien(Vector2 position) {
        super(position, Vector2.ZERO, new AnimationSprite("/enemies/enemyPurple",3));
        Random r = new Random();
        prevShot = System.currentTimeMillis() + r.nextInt((int) (shootInterval * 1000));
        setTarget(((Topdownfuntown)GameApplication.getInstance()).getPlayer());
    }


    @Override
    public void update(double delta) {
        super.update(delta);
        if ((System.currentTimeMillis() - prevShot) / 1000 >= shootInterval) {
            prevShot = System.currentTimeMillis();
            shoot();
        }
    }

    public void shoot() {
        Projectile p = new Projectile(position, direction, new Sprite("/projectiles/projectile_enemy_00"));
       Projectile p2 = new Projectile(position, direction, new Sprite("/projectiles/projectile_enemy_00"));

        p.setSize(new Vector2(32, 32));

        p.getSprite().setSquareHeight(32);
        p.getSprite().setSquareWidth(32);
        p.setPeriod(1.2f);
        p.setAmplitude(3f);
        p.setPhase(200f);
        p.setSpeed(1000);
        p.setSine(true);

       p2.setSize(new Vector2(32, 32));

        p2.getSprite().setSquareHeight(32);
        p2.getSprite().setSquareWidth(32);
        p2.setPeriod(2.2f);
        p2.setAmplitude(3f);
        p2.setPhase(200f);
        p2.setSpeed(800);
        p2.setSine(true);


        // Adding colliders layers
        p.getCollider().addInteractionLayer("UnHittable");
        p.getCollider().addInteractionLayer("Block");

        p.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if (other.getGameObject() instanceof Player) {
                    Player pl = (Player) other.getGameObject();
                    pl.hit();

                }
                p.destroy();

            }
        });
    }
}
