package com.rominntrenger.objects.enemy;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.CircleCollider;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.FSM.Behaviour;
import com.rominntrenger.objects.FSM.Wander;
import com.rominntrenger.objects.blocks.Blood;
import com.rominntrenger.objects.player.Player;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Enemy extends GameObject {

    public static List<Enemy> allEnemies = new ArrayList<>();

    protected double speed = 300;
    protected int max_health = 1000;
    protected int health = 1000;
    GameObject target;
    double angularDampening = 0.05;
    int bullet_dmg = 10;
    Behaviour behaviour;
    public double delta;

    private Behaviour[] behaviours = new Behaviour[12];

    private boolean[] isSeenByPlayer;


    public Enemy(Vec2 position, Vec2 direction, Sprite sprite) {
        super(position, direction, sprite);
        // setSeenByPlayer
        isSeenByPlayer = new boolean[Math.max(1, ((RomInntrenger) GameApplication.getInstance()).players.size() + 2)];

        allEnemies.add(this);
        setRenderLayer(RenderLayer.RenderLayerName.ENEMIES);
        collider = new CircleCollider(this, 20);

        collider.setName("enemy");
        collider.setTag("Hittable");
        collider.attachToGameObject(this);
        collider.addInteractionLayer("UnHittable");
        collider.addInteractionLayer("Block");
        collider.addInteractionLayer("Walk");

        collider.setOnCollisionListener((Collider other) -> {
            if (other.getGameObject() instanceof Player) {
                Player p = (Player) other.getGameObject();
                p.hit(bullet_dmg);
                destroy();
            }
        });


        behaviours[Wander.position] = new Wander();
        behaviours[Attack.position] = new Attack();
        behaviours[Flee.position] = new Flee();


        behaviour = behaviours[Wander.position];
    }

    /** Sets the current behaviour to input.
     * @param behaviourPosition
     */
    public void setBehaviour(int behaviourPosition) {
        this.behaviour = behaviours[behaviourPosition];
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

        Collider hit = collider.getIntersectionCollider();

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

    public void hit(int dmg){
        health -= dmg;
        if(health <= 0){
            destroy();
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
        Vec2 pos = transform.getGlobalPosition();
        gc.setStroke(Color.BLACK);
        gc.strokeRect(pos.getX() - 50, pos.getY() - 50, 100, 20);
        gc.setFill(Color.GREEN);
        gc.fillRect(pos.getX() - 50, pos.getY() - 50,  ((double)health / (double)max_health) * 100.0, 20);

        if(GameEngine.DEBUG && isSeenByPlayer()) {
            gc.setFill(Color.RED);
            gc.fillArc(pos.getX() - 30, pos.getY() - 30, 60, 60, 0, 360, ArcType.CHORD);
        }
    }

    @Override
    public void destroy() {
//        walkCollider.destroy();
        allEnemies.remove(this);
        new Blood(getPosition());
//        new WeaponClipUpgrade(getPosition(), new WeaponClip());
        collider.destroy();
        super.destroy();
//        if(isKeyHolder)
//            ((Topdownfuntown) GameApplication.getInstance()).hasKey = true;
    }

    @Override
    public void update(double delta) {
        this.delta = delta;
    }

    public double getDelta() {
        return delta;
    }

    public GameObject getTarget() {
        return target;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setIsSeenByPlayer(int playerID, boolean state){
        isSeenByPlayer[playerID] = state;
    }

    public boolean isSeenByPlayer(){
        boolean ret = false;
        for(Boolean bol : isSeenByPlayer){
            if(bol)
                ret = true;
        }
        return ret;
    }

    public int getHealth() {
        return health;
    }

    public int getMax_health() {
        return max_health;
    }
}
