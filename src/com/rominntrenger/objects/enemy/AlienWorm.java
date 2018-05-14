package com.rominntrenger.objects.enemy;


import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.FSM.Attack;
import com.rominntrenger.objects.Projectile;
import com.rominntrenger.objects.player.Player;

import java.util.Random;

public class AlienWorm extends Enemy {

    private double shootInterval = 0.001;
    private long prevShot = 0;
    private int hit_dmg;

    /**
     * Creates an AlienWorm given position Vec2.
     * @param position
     */
    public AlienWorm(Vec2 position) {
        super(position, Vec2.ZERO, new AnimationSprite("enemies/alienWorm", 6));
        Random r = new Random();
        prevShot = System.currentTimeMillis() + r.nextInt((int) (shootInterval * 1000));
        speed = 200;

        max_health = GameSettings.getInt("Alien_worm_max_health");
        health = max_health;

        /**
         * If AlienWorm collides with the player, do damage.
         */
        hit_dmg = GameSettings.getInt("Alien_worm_hit_dmg");
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
     * Creates a new AlienWorm from existing AlienWorm.
     * @param pos
     * @return
     */
    @Override
    public AlienWorm createNew(Vec2 pos) {
        return new AlienWorm(pos);
    }

    /**
     * Update function, checks if they should go to the next behaviour.
     * @param delta
     */
    public void update(double delta) {
        super.update(delta);
        AlienWorm.super.nextBehaviour();
    }

}
