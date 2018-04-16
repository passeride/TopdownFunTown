package com.rominntrenger.main.objects;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.camera.OrtographicCamera;
import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.*;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.RomInntrenger;
import com.topdownfuntown.main.Topdownfuntown;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Player extends GameObject {

    AudioPlayer hitSound;
    protected Vector2 collisionDirection;
    private double speed = 800.0; // Gotta go fast
    private double baseSpeed = 300.0;
    private double speedBoostSpeed = 1000.0;
    private boolean speedBost = false;
    private Collider walkCollider;

    public RigidBody2D rb2;

    private int rayCastResolution = 0;
    private ArrayList<RayCast> raycasts = new ArrayList<>();

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
    public Player(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);


        ((RomInntrenger)GameApplication.getInstance()).player = this;

        setRenderLayer(RenderLayer.RenderLayerName.GUI);
        hitSound = new AudioPlayer("./assets/audio/lukasAuu.wav");
        hitSound.setSpital(this);

        collider = new BoxCollider(this);
        collider.setName("Player");
        collider.setTag("UnHittable");
        collider.addInteractionLayer("Hittable");

        // WalkCollider
        walkCollider = new CircleCollider(this, 20);
        walkCollider.setName("Player_Walk");
        walkCollider.setTag("Walk");
        walkCollider.addInteractionLayer("Block");
        walkCollider.setPadding(new Vector2(-20, -20));

        setUpRayCast();

        rb2 = new RigidBody2D(this);

        OrtographicCamera.main.follow(this);

    }

    private void setUpRayCast(){
        for(int i = 0; i < rayCastResolution; i++){
            double dir = (Math.PI * 2) * ((double) i / rayCastResolution);
            raycasts.add(new RayCast(dir, this));
        }
    }

    @Override
    public void update(double delta) {
        translate(Vector2.ZERO); // This is to update in case of intersection
    }

    /**
     * Will move the player object NORTH/UP by {@link Player#speed}
     */
    public void moveUp(double delta) {
        translate(Vector2.multiply(Vector2.UP, speed * delta));
    }

    /**
     * Will move the player object SOUTH/DOWN by {@link Player#speed}
     */
    public void moveDown(double delta) {
        translate(Vector2.multiply(Vector2.DOWN, speed * delta));
    }

    /**
     * Will move the player object WEST/LEFT by {@link Player#speed}
     */
    public void moveLeft(double delta) {
        translate(Vector2.multiply(Vector2.LEFT, speed * delta));
    }

    /**
     * Will move the player object EAST/RIGHT by {@link Player#speed}
     */
    public void moveRight(double delta) {
        translate(Vector2.multiply(Vector2.RIGHT, speed * delta));
    }

    /**
     * Override to create a 8 % margin for movement
     *
     * @param moveVector
     */
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

    /**
     * Used when player is hit to subtract health and check for death
     */
    public void hit() {
//        int hp = topdownfuntown.getHealth();
//        topdownfuntown.setHealth(--hp);
//        if (hp <= 0) {
//            die();
//            destroy();
//        }
        hitSound.playOnce();
    }

    private void die() {
        GameEngine.getInstance().Pause();
        destroy();
    }

    public void activateGottaGoFast() {
        speedBost = true;
        speed = baseSpeed + speedBoostSpeed;
    }

    public void deactivateGottaGoFast() {
        speedBost = false;
        speed = baseSpeed;
    }

    public void shoot() {
        rb2.addForce(Vector2.multiply(Vector2.Vector2FromAngleInDegrees(transform.getGlobalRotation().getAngleInDegrees() + 90),  300));
    }

}
