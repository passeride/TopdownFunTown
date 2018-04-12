package com.rominntrenger.main.objects;

import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.topdownfuntown.main.Topdownfuntown;

public abstract class Enemy extends GameObject {


    double speed = 300;
    GameObject target;
    double angularDampening = 0.05;

    public boolean isKeyHolder = false;


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
        collider = new BoxCollider(this);

        collider.setName("Enemy");
        collider.setTag("Hittable");
        collider.attachToGameObject(this);
        collider.addInteractionLayer("UnHittable");
        collider.addInteractionLayer("Block");
        collider.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if (other.getGameObject() instanceof Player) {
                    Player p = (Player) other.getGameObject();
                    p.hit();
                    destroy();
                }
            }
        });
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }

    @Override
    public void destroy() {
        super.destroy();
        if(isKeyHolder)
            ((Topdownfuntown) GameApplication.getInstance()).hasKey = true;
    }

    @Override
    public void update(double detla) {
        if (target != null) {
            translate(Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(getPosition(), target.getPosition())));
            setDirection(Vector2.add(getDirection(), Vector2.multiply(Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(getPosition(), target.getPosition())), angularDampening)));
            getDirection().normalize();
        }
    }


}
