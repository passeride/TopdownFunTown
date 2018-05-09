package com.rominntrenger.objects.enemy;


import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.FSM.Attack;
import com.rominntrenger.objects.Projectile;
import com.rominntrenger.objects.player.Player;

import java.util.Random;

public class AlienGlow extends Enemy {

    private double shootInterval = 0.001;
    private long prevShot = 0;

    public AlienGlow(Vec2 position) {
        super(position, Vec2.ZERO, new AnimationSprite("enemies/alienGlow", 4));
        setSize(new Vec2(1.5, 1.5));
        Random r = new Random();
        prevShot = System.currentTimeMillis() + r.nextInt((int) (shootInterval * 1000));
        speed = 200;

        max_health = GameSettings.getInt("Alien_glow_max_health");
        health = max_health;

        bullet_dmg = GameSettings.getInt("Alien_glow_bullet_dmg");
    }

    public void update(double delta) {
        super.update(delta);
        AlienGlow.super.nextBehaviour();

        if (super.behaviour instanceof Attack && (System.currentTimeMillis() - prevShot) / 1000 >= shootInterval) {
            prevShot = System.currentTimeMillis();
            shoot();
        }
    }

    public void shoot() {
        //TODO: MAKE MULTIPLE BULLETS
        Projectile p = new Projectile(transform.getLocalPosition(), transform.getGlobalRotation(),
            new Sprite("projectiles/projectile_enemy_00"));
        //new Vec2(transform.getLocalPosition().getX()+10, transform.getLocalPosition().getY()+10)
        Projectile p2 = new Projectile(transform.getLocalPosition(), new Vec2(0, transform.getGlobalRotation().getAngleInDegrees() + 90),
            new Sprite("projectiles/projectile_enemy_00"));
        Projectile p3 = new Projectile(transform.getLocalPosition(), new Vec2(0, transform.getGlobalRotation().getAngleInDegrees() + 180),
            new Sprite("projectiles/projectile_enemy_00"));
        Projectile p4 = new Projectile(transform.getLocalPosition(), new Vec2(0, transform.getGlobalRotation().getAngleInDegrees() + 270),
            new Sprite("projectiles/projectile_enemy_00"));

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

        p3.getSprite().setSquareHeight(32);
        p3.getSprite().setSquareWidth(32);
        p3.setPeriod(2.2f);
        p3.setAmplitude(3f);
        p3.setPhase(200f);
        p3.setSpeed(800);
        p3.setSine(true);

        p4.getSprite().setSquareHeight(32);
        p4.getSprite().setSquareWidth(32);
        p4.setPeriod(2.2f);
        p4.setAmplitude(3f);
        p4.setPhase(200f);
        p4.setSpeed(800);
        p4.setSine(true);

        // Adding colliders layers
        p4.getCollider().addInteractionLayer("UnHittable");
        p4.getCollider().addInteractionLayer("Block");

        p4.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if (other.getGameObject() instanceof Player) {
                    Player pl = (Player) other.getGameObject();
                    pl.hit(bullet_dmg);
                    pl.rb2.addForce(Vec2.multiply(Vec2.Vector2FromAngleInDegrees(
                        Vec2.getAngleBetweenInDegrees(getPosition(), pl.getPosition())),
                        3000.0));
                }
                p4.destroy();
            }
        });

        p3.getCollider().addInteractionLayer("UnHittable");
        p3.getCollider().addInteractionLayer("Block");

        p3.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if (other.getGameObject() instanceof Player) {
                    Player pl = (Player) other.getGameObject();
                    pl.hit(bullet_dmg);
                    pl.rb2.addForce(Vec2.multiply(Vec2.Vector2FromAngleInDegrees(
                        Vec2.getAngleBetweenInDegrees(getPosition(), pl.getPosition())),
                        3000.0));
                }
                p3.destroy();
            }
        });

        p2.getCollider().addInteractionLayer("UnHittable");
        p2.getCollider().addInteractionLayer("Block");

        p2.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if (other.getGameObject() instanceof Player) {
                    Player pl = (Player) other.getGameObject();
                    pl.hit(bullet_dmg);
                    pl.rb2.addForce(Vec2.multiply(Vec2.Vector2FromAngleInDegrees(
                        Vec2.getAngleBetweenInDegrees(getPosition(), pl.getPosition())),
                        3000.0));
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
                    pl.rb2.addForce(Vec2.multiply(Vec2.Vector2FromAngleInDegrees(
                        Vec2.getAngleBetweenInDegrees(getPosition(), pl.getPosition())),
                        3000.0));
                }
                p.destroy();
            }
        });
    }

    public void wander(Vec2 position) {
        double x = position.getX();
        double y = position.getY();
    }

    public AlienGlow createNew(Vec2 pos) {
        return new AlienGlow(pos);
    }

}
