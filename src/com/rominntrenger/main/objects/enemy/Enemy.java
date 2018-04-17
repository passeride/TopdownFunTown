package com.rominntrenger.main.objects.enemy;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.physics.CircleCollider;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.objects.blocks.Blood;
import com.rominntrenger.main.objects.player.Player;

public abstract class Enemy extends GameObject {


    double speed = 300;
    GameObject target;
    double angularDampening = 0.05;
    int bullet_dmg = 10;

    private Collider walkCollider;

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

        collider.setName("enemy");
        collider.setTag("Hittable");
        collider.attachToGameObject(this);
        collider.addInteractionLayer("UnHittable");
        collider.addInteractionLayer("Block");
        collider.addInteractionLayer("Walk");

        collider.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {

                if (other.getGameObject() instanceof Player) {
                    Player p = (Player) other.getGameObject();
                    p.hit(bullet_dmg);
                    destroy();
                }
            }
        });

        // WalkCollider
        walkCollider = new CircleCollider(this, 20);
        walkCollider.setName("Enemy_Walk");
        walkCollider.setTag("Enemy_Walk");
        walkCollider.addInteractionLayer("Block");
        walkCollider.setPadding(new Vector2(-20, -20));

    }

    public void setTarget(GameObject target) {
        this.target = target;
    }

    @Override
    public void translate(Vector2 moveVector) {
        Vector2 newPoss = Vector2.add(getPosition(), moveVector);

        Collider hit = walkCollider.getIntersectionCollider();

        if(hit == null)
            transform.setLocalPosition(newPoss);
        else {
            transform.setLocalPosition(
                    Vector2.add(
                            transform.getLocalPosition(),
                            Vector2.multiply(Vector2.subtract(getTransform().getGlobalPosition(), hit.getGameObject().getTransform().getGlobalPosition()).getNormalizedVector(), 0.5)));

        }
    }

    @Override
    public void destroy() {
        walkCollider.destroy();
        new Blood(getPosition());
        super.destroy();
//        if(isKeyHolder)
//            ((Topdownfuntown) GameApplication.getInstance()).hasKey = true;
    }

    @Override
    public void update(double detla) {
        if (target != null) {
            translate(Vector2.multiply(Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(getPosition(), target.getPosition())), speed * detla));
            setDirection(Vector2.add(getDirection(), Vector2.multiply(Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(getPosition(), target.getPosition())), angularDampening)));
            getDirection().normalize();
        }
    }


}
