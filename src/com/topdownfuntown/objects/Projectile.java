package com.topdownfuntown.objects;

import com.bluebook.engine.GameEngine;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.util.GameObject;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.MotionBlur;
import javafx.scene.paint.Color;

public class Projectile extends GameObject{

    private double speed = 800.0;


    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
    public Projectile(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
        size = new Vector2(100, 30);
        this.setCollider(new Collider(this));
        collider.setName("Bullet");
        collider.setTag("DMG");
        collider.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                System.out.println("HIT:  "  + other.getName() + "  :  " + other.getTag());
                if(other.getTag() == "Hittable") {
                    if(other.getGameObject() instanceof Player) {
                        Player p = (Player) other.getGameObject();
                        p.hit();
                        if (GameEngine.DEBUG)
                            System.out.println("Bullet Hit " + other.getName());
                        destroy();
                    }else{
                        other.getGameObject().destroy();
                        destroy();
                    }
                }
            }
        });
    }


    @Override
    public void update(double delta){
        translate(Vector2.multiply(direction, speed * delta));
    }

    @Override
    public void draw(GraphicsContext gc){

        sprite.draw(gc, position, direction);

    }

}
