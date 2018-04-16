package com.rominntrenger.main.objects.enemy;


import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.main.objects.player.Player;
import com.rominntrenger.main.objects.Projectile;

import java.util.Random;

public class AlienGreen extends Enemy {
    private double shootInterval = 1.8;
    private long prevShot = 0;



    /*
    public AlienGreen(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
    } */

    public AlienGreen(Vector2 position) {
        super(position, Vector2.ZERO, new AnimationSprite("/enemies/enemyGreen",3));
        Random r = new Random();
        prevShot = System.currentTimeMillis() + r.nextInt((int) (shootInterval * 1000));
        // setTarget(((Topdownfuntown)GameApplication.getInstance()).getPlayer());
        setTarget(((RomInntrenger)GameApplication.getInstance()).player);

    }

    public void update(double delta) {
        super.update(delta);
        if ((System.currentTimeMillis() - prevShot) / 1000 >= shootInterval) {
            prevShot = System.currentTimeMillis();
            shoot();
        }
    }

    public void shoot() {
        Projectile p = new Projectile(transform.getLocalPosition(), transform.getGlobalRotation(), new Sprite("/projectiles/projectile_enemy_00"));
        p.getSprite().setSquareHeight(32);
        p.getSprite().setSquareWidth(32);
        p.setPeriod(1.2f);
        p.setAmplitude(3f);
        p.setPhase(200f);
        p.setSpeed(1600);
        p.setSine(true);

        // Adding colliders layers
        p.getCollider().addInteractionLayer("UnHittable");
        p.getCollider().addInteractionLayer("Block");

        p.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if (other.getGameObject() instanceof Player) {
                    Player pl = (Player) other.getGameObject();
                    pl.hit(bullet_dmg);
                    pl.rb2.addForce(Vector2.multiply(Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(getPosition(), pl.getPosition())), 300.0));

                }
                p.destroy();

            }
        });
    }
}
