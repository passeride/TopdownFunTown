package com.rominntrenger.objects.enemy;


import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.FSM.Attack;
import com.rominntrenger.objects.Projectile;
import com.rominntrenger.objects.player.Player;
import java.util.Random;

public class AlienZombie extends Enemy {

    private double shootInterval = 0.001;
    private long prevShot = 0;
    private int hit_dmg;

    public AlienZombie(Vec2 position) {
        super(position, Vec2.ZERO, new AnimationSprite("/enemies/alienZombie", 4));
        Random r = new Random();
        prevShot = System.currentTimeMillis() + r.nextInt((int) (shootInterval * 1000));
        speed = 100;

        max_health = GameSettings.getInt("Alien_zombie_max_health");
        health = max_health;

        hit_dmg = GameSettings.getInt("Alien_zombie_hit_dmg");

        collider = new BoxCollider(this);
        collider.setTag("Block");
        this.collider.addInteractionLayer("Walk");

        collider.setOnCollisionListener(new OnCollisionListener() {
            @Override
            /**
             * Checks if the Player has the right key in their Inventory.
             */
            public void onCollision(Collider other) {
                if (other.getGameObject() instanceof Player) {
                    Player pl = (Player) other.getGameObject();
                    pl.hit(hit_dmg);
                    destroy();
                    collider.destroy();
                }
            }
        });
    }

    public void update(double delta) {
        super.update(delta);
        AlienZombie.super.nextBehaviour();

        if (super.behaviour instanceof Attack && (System.currentTimeMillis() - prevShot) / 1000 >= shootInterval) {
            prevShot = System.currentTimeMillis();
            //TODO: må endre speed så den ikke blir gottagofast når den ser player
            // shoot();
        }
    }

    public void shoot() {
        Projectile p = new Projectile(transform.getLocalPosition(), transform.getGlobalRotation(),
            new Sprite("/projectiles/projectile_enemy_00"));
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
            if(other.getGameObject() != p.getSource()) {
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

    public void wander(Vec2 position) {
        double x = position.getX();
        double y = position.getY();
    }

}
