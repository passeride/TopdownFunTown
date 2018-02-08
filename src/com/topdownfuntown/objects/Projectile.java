package com.topdownfuntown.objects;

import com.bluebook.engine.GameEngine;
import com.bluebook.physics.Collider;
import com.bluebook.physics.CollisionBox;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.util.GameObject;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.Vector2;

public class Projectile extends GameObject{

    private double speed = 100.0;


    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
    public Projectile(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
        size = new Vector2(128, 32);
        this.setCollider(new Collider(this));
        collider.setName("Bullet");
        collider.setTag("DMG");
        collider.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if(other.getGameObject() instanceof Player) {
                    Player p = (Player) other.getGameObject();
                    p.hit();
                    if (GameEngine.DEBUG)
                        System.out.println("Bullet Hit " + other.getName());
                    destroy();
                }
            }
        });
    }


    @Override
    public void update(double delta){
        translate(Vector2.multiply(direction, speed * delta));
    }

}
