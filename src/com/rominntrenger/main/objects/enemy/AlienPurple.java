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

public class AlienPurple extends Enemy {


    private double shootInterval = 1.8;
    private long prevShot = 0;

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     */
    public AlienPurple(Vector2 position) {
        super(position, Vector2.ZERO, new AnimationSprite("/enemies/enemyPurple",3));
        Random r = new Random();
        prevShot = System.currentTimeMillis() + r.nextInt((int) (shootInterval * 1000));
        //setTarget(((Topdownfuntown)GameApplication.getInstance()).getPlayer());
        speed = 100;
    }


    @Override
    public void update(double delta) {
        super.update(delta);
        setTarget(((RomInntrenger)GameApplication.getInstance()).player);

        if ((System.currentTimeMillis() - prevShot) / 1000 >= shootInterval) {
            prevShot = System.currentTimeMillis();
            shoot();
        }
    }

    public void shoot() {
        Projectile p = new Projectile(transform.getLocalPosition(), transform.getGlobalRotation(), new Sprite("/projectiles/projectile_enemy_00"));
        Projectile p2 = new Projectile(transform.getLocalPosition(), transform.getGlobalRotation(), new Sprite("/projectiles/projectile_enemy_00"));

        p.getSprite().setSquareHeight(32);
        p.getSprite().setSquareWidth(32);
        p.setPeriod(1.2f);
        p.setAmplitude(3f);
        p.setPhase(200f);
        p.setSpeed(1000);
        p.setSine(true);

        p2.getSprite().setSquareHeight(32);
        p2.getSprite().setSquareWidth(32);
        p2.setPeriod(2.2f);
        p2.setAmplitude(3f);
        p2.setPhase(200f);
        p2.setSpeed(800);
        p2.setSine(true);

        // Adding colliders layers
        p2.getCollider().addInteractionLayer("UnHittable");
        p2.getCollider().addInteractionLayer("Block");

        p2.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if (other.getGameObject() instanceof Player) {
                    Player pl = (Player) other.getGameObject();
                    pl.hit(bullet_dmg);
                    pl.rb2.addForce(Vector2.multiply(Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(getPosition(), pl.getPosition())), 3000.0));
                }
                p2.destroy();
            }
        });

        // Adding colliders layers
        p.getCollider().addInteractionLayer("UnHittable");
        p.getCollider().addInteractionLayer("Block");

        p.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if (other.getGameObject() instanceof Player) {
                    Player pl = (Player) other.getGameObject();
                    pl.hit(bullet_dmg);
                    pl.rb2.addForce(Vector2.multiply(Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(getPosition(), pl.getPosition())), 3000.0));
                }
                p.destroy();
            }
        });
    }
}
