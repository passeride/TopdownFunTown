package com.rominntrenger.objects.enemy;

import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.CircleCollider;
import com.bluebook.physics.Collider;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.objects.FSM.Attack;
import com.rominntrenger.objects.FSM.Behaviour;
import com.rominntrenger.objects.FSM.Flee;
import com.rominntrenger.objects.FSM.Wander;
import com.rominntrenger.objects.blocks.Blood;
import com.rominntrenger.objects.player.Player;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 * Enemy - an abstract class that extends GameObject.
 * This class is the top-node for all enemies in-game.
 */
public abstract class Enemy extends GameObject {

    /**
     * A list containing all active enemies
     */
    public static CopyOnWriteArrayList<Enemy> allEnemies = new CopyOnWriteArrayList<>();
    protected double speed = 300;
    protected int max_health = 1000;
    protected int health = 1000;
    GameObject target;
    double angularDampening = 0.05;
    int bullet_dmg = 10;
    Behaviour behaviour;
    /**
     * delta is the update time used to get a smooth update frame
     */
    public double delta;

    boolean dropsBlood = true;

    private double dropRate = GameSettings.getDouble("itemDropRate");

    private Behaviour[] behaviours = new Behaviour[12];

    private boolean[] isSeenByPlayer = new boolean[12];

    /**
     * Constructor for the class enemy, extended by super(class gameObject)
     * @param position is the given position for the enemy
     * @param direction is the given direction for the enemy
     * @param sprite is the given sprite for the enemy
     */
    public Enemy(Vec2 position, Vec2 direction, Sprite sprite) {
        super(position, direction, sprite);


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

    /**
     * Sets the current behaviour to input.
     * @param behaviourPosition is the position sat in constructor
     */
    public void setBehaviour(int behaviourPosition) {
        this.behaviour = behaviours[behaviourPosition];
    }

    /**
     * sets next behaviour
     */
    public void nextBehaviour() {
        this.behaviour.nextBehaviour(this);
    }

    public void setTarget(GameObject target) {
        this.target = target;
    }

    public double getSpeed() {
        return speed;
    }

    /**
     * This translates a vector to another position
     * @param moveVector
     */
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

    /**
     * this damages whatever hit
     * @param dmg the amount in int
     */
    public void hit(int dmg) {
        health -= dmg;
        if (health <= 0) {
            setAlive(false);
            destroy();
        }
    }

    /**
     * Draws on screen
     * @param gc is the given graphicsContext to draw
     */
    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
        Vec2 pos = transform.getGlobalPosition();
        gc.setStroke(Color.BLACK);
        gc.strokeRect(pos.getX() - 50, pos.getY() - 50, 100, 20);
        gc.setFill(Color.GREEN);
        gc.fillRect(pos.getX() - 50, pos.getY() - 50, ((double) health / (double) max_health) * 100.0, 20);

        if (GameEngine.DEBUG && isSeenByPlayer()) {
            gc.setFill(Color.RED);
            gc.fillArc(pos.getX() - 30, pos.getY() - 30, 60, 60, 0, 360, ArcType.CHORD);
        }
    }

    /**
     * Destroys the given enemy and its given fields
     */
    @Override
    public void destroy() {
        allEnemies.remove(this);
        collider.destroy();
        if (Math.random() < dropRate) {
            ((RomInntrenger) GameApplication.getInstance()).addRandomItem.randomElement().spawn(getPosition());
        } else {
            if (dropsBlood)
                new Blood(getPosition());
        }
        super.destroy();

    }

    /**
     * Creates a new enemy from existing enemy.
     * @param pos position given the enemy
     * @return
     */
    public abstract Enemy createNew(Vec2 pos);

    @Override
    public void update(double delta) {
        this.delta = delta;
    }

    public double getDelta() {
        return delta;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setIsSeenByPlayer(int playerID, boolean state) {
        isSeenByPlayer[playerID] = state;
    }

    public boolean isSeenByPlayer() {
        boolean ret = false;
        for (Boolean bol : isSeenByPlayer) {
            if (bol)
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

    public double getDropRate() {
        return dropRate;
    }
}
