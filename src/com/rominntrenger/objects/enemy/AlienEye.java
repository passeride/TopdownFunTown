package com.rominntrenger.objects.enemy;


import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.Projectile;
import com.rominntrenger.objects.player.Player;

import java.util.Random;

public class AlienEye extends Enemy {

    private double shootInterval;
    private long prevShot;

    /**
     * Constructor for AlienEye given position.
     * @param position
     */
    public AlienEye(Vec2 position) {
        super(position, Vec2.ZERO, new Sprite("enemies/alienEye_0"));
        Random r = new Random();
        shootInterval = 0.2;
        prevShot = 0;
        prevShot = System.currentTimeMillis() + r.nextInt((int) (shootInterval * 1000));
        speed = 100;

        max_health = GameSettings.getInt("Alien_eye_max_health");
        health = max_health;

        bullet_dmg = GameSettings.getInt("Alien_eye_hit_dmg");
    }

    /**
     * Update function, checks if they should go to the next behaviour.
     * @param delta time from last update to next update
     */
    public void update(double delta) {
        super.update(delta);
        AlienEye.super.nextBehaviour();

        if ((System.currentTimeMillis() - prevShot) / 1000 >= shootInterval){
            prevShot = System.currentTimeMillis();
            shoot();
        }



        if (health < ((max_health * 20) / 100)) {
            this.setSprite(new AnimationSprite("enemies/alienEyeFlame", 2));
            ((AnimationSprite) getSprite()).setPlaying(true);
        }
    }

    /**
     * Creates projectiles given AlienEye's position and rotation.
     */
    private void shoot() {
        Projectile p = new Projectile(transform.getLocalPosition(), transform.getGlobalRotation(),
            new Sprite("projectiles/alienProjectileLaser"));
        p.setSize(new Vec2(0.1, 0.1));
        p.getCollider().addInteractionLayer("UnHittable");
        p.getCollider().addInteractionLayer("Hittable");

        p.getCollider().addInteractionLayer("Block");
        p.getSprite().setSquareHeight(32);
        p.getSprite().setSquareWidth(32);
        p.setPeriod(1.2f);
        p.setAmplitude(3f);
        p.setPhase(200f);
        p.setSpeed(1600);
        p.setSine(true);
        p.setSource(this);
        p.setOnCollisionListener(other -> {
            if (other.getGameObject() != p.getSource()) {
                if (other.getGameObject() instanceof Player) {
                    Player pl = (Player) other.getGameObject();
                    pl.hit(bullet_dmg);
                    pl.rb2.addForce(Vec2.multiply(Vec2.Vector2FromAngleInDegrees(
                        Vec2.getAngleBetweenInDegrees(getPosition(), pl.getPosition())),
                        3000.0));
                } else if (other.getGameObject() instanceof Enemy) {
                    ((Enemy) other.getGameObject()).hit(bullet_dmg);
                }
                p.destroy();
            }
        });
    }

    /**
     * Creates a new AlienEye from existing AlienEye.
     * @param pos is the given position for the new alien
     * @return AlienEye(pos)
     */
    @Override
    public AlienEye createNew(Vec2 pos) {
        return new AlienEye(pos);
    }
}
