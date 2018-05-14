package com.rominntrenger.objects.enemy;


import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
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

    /**
     * Creates a new AlienZombie given Vec2 Position.
     * @param position
     *
     * Gives it a CollisionListener and sets the damage of the collision.
     */
    public AlienZombie(Vec2 position) {
        super(position, Vec2.ZERO, new AnimationSprite("enemies/alienZombie", 4));
        Random r = new Random();
        prevShot = System.currentTimeMillis() + r.nextInt((int) (shootInterval * 1000));
        speed = 100;

        max_health = GameSettings.getInt("Alien_zombie_max_health");
        health = max_health;

        hit_dmg = GameSettings.getInt("Alien_zombie_hit_dmg");

        collider.setOnCollisionListener(other -> {
            if (other.getGameObject() instanceof Player) {
                Player pl = (Player) other.getGameObject();
                pl.hit(hit_dmg);
                destroy();
                collider.destroy();
            }
        });
    }

    /**
     * Update function, checks if they should go to the next behaviour.
     * @param delta
     */
    public void update(double delta) {
        super.update(delta);
        AlienZombie.super.nextBehaviour();
    }

    /**
     * Creates a new AlienZombie from existing AlienZombie.
     * @param pos
     * @return
     */
    @Override
    public AlienZombie createNew(Vec2 pos) {
        return new AlienZombie(pos);
    }

}
