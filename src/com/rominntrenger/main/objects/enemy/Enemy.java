package com.rominntrenger.main.objects.enemy;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.physics.CircleCollider;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.objects.FSM.Behaviour;
import com.rominntrenger.main.objects.FSM.Wander;
import com.rominntrenger.main.objects.blocks.Blood;
import com.rominntrenger.main.objects.player.Player;

public abstract class Enemy extends GameObject {

    double speed = 300;
    GameObject target;
    double angularDampening = 0.05;
    int bullet_dmg = 10;
    Behaviour behaviour;
    public double delta;
    private Collider walkCollider;

    public boolean isKeyHolder = false;

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public Enemy(Vec2 position, Vec2 direction, Sprite sprite) {
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
        walkCollider.setPadding(new Vec2(-20, -20));

        this.behaviour = new Wander();
    }

    public void setBehaviour(Behaviour behaviour) {
        this.behaviour = behaviour;
    }

    public void nextBehaviour() {
        this.behaviour.nextBehaviour(this);
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public void translate(Vec2 moveVector) {
        Vec2 newPoss = Vec2.add(getPosition(), moveVector);

        Collider hit = walkCollider.getIntersectionCollider();

        if (hit == null) {
            transform.setLocalPosition(newPoss);
        } else {
            transform.setLocalPosition(
                Vec2.add(
                    transform.getLocalPosition(),
                    Vec2.multiply(Vec2.subtract(getTransform().getGlobalPosition(),
                        hit.getGameObject().getTransform().getGlobalPosition())
                        .getNormalizedVector(), 0.5)));

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
    public void update(double delta) {
        if (target != null) {
            translate(Vec2.multiply(Vec2.Vector2FromAngleInDegrees(
                Vec2.getAngleBetweenInDegrees(getPosition(), target.getPosition())),
                speed * delta));
            setDirection(Vec2.add(getDirection(), Vec2.multiply(Vec2
                    .Vector2FromAngleInDegrees(
                        Vec2.getAngleBetweenInDegrees(getPosition(), target.getPosition())),
                angularDampening)));
            getDirection().normalize();
        }
        this.delta = delta;
    }

    public double getDelta() {
        return delta;
    }
}
