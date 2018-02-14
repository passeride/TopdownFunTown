package com.topdownfuntown.objects;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;

public abstract class Enemy extends GameObject{


    double speed = 100;
    GameObject target;




    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
    public Enemy(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
        setRenderLayer(RenderLayer.RenderLayerName.ENEMIES);
        setSize(new Vector2(64, 64));
        collider = new Collider(this);

        collider.setName("Enemy");
        collider.setTag("Hittable");
        collider.attachToGameObject(this);
        collider.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if(other.getGameObject() instanceof Player){
                    Player p = (Player)other.getGameObject();
                    p.hit();
                    destroy();
                }
            }
        });
    }

    public void setTarget(GameObject target){
        this.target = target;
    }

    @Override
    public void update(double detla){
        if(target != null) {
            translate(Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(position, target.getPosition())));
            direction = Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(position, target.getPosition()));
        }
    }


}
