package com.rominntrenger.main.objects;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.*;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
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
    Topdownfuntown topdownfuntown;
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
        setRenderLayer(RenderLayer.RenderLayerName.PLAYER);
        topdownfuntown = (Topdownfuntown) GameApplication.getInstance();
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
    }

    private void setUpRayCast(){
        for(int i = 0; i < rayCastResolution; i++){
            double dir = (Math.PI * 2) * ((double) i / rayCastResolution);
            raycasts.add(new RayCast(dir, this));
        }
    }

    @Override
    public void update(double delta) {
        rb2.update(delta);
//        translate(Vector2.multiply(rb2.getLinearVelocity(), delta));
    }

    @Override
    public void draw(GraphicsContext gc){

        double[][] polygon = getPolygon();

        gc.save();

        //gc.applyEffect(new ColorAdjust(0, 0, -0.3, 0));

//        gc.setFill( new RadialGradient(0, 0, 0.5, 0.5, 0., true,
//                CycleMethod.NO_CYCLE,
//                new Stop(0.0, new Color(1, 1, 1, 0.3)),
//                new Stop(1.0, Color.TRANSPARENT)));
//        gc.setGlobalBlendMode(BlendMode.OVERLAY);
//        gc.fillPolygon(polygon[0], polygon[1], polygon[0].length);
        //gc.setFill(Color.GREEN);
        //gc.strokeLine(getPosition().getX(), getPosition().getY(), getPosition().getX() + rb2.getLinearVelocity().getX(), getPosition().getY() + rb2.getLinearVelocity().getY());

        gc.restore();
//        gc.fillText("X:" + rb2.getLinearVelocity().getX() + "-Y:"+ rb2.getLinearVelocity().getY(), getPosition().getX(), getPosition().getY());

        super.draw(gc);

    }

    /**
     * Goes over {@link #raycasts} and returns a double array with positions first array is X cooridnates
     * second is Y coordinates
     * @return [x/y][position]
     */
    private double[][] getPolygon(){
        double[][] ret = new double[2][];
        double[] xs = new double[raycasts.size()];
        double[] ys = new double[raycasts.size()];

        for(int i = 0; i < raycasts.size(); i++){
            RayCastHit rch = raycasts.get(i).getHit();
            if(rch != null) {
                if (rch.isHit) {
                    xs[i] = rch.ray.x2;
                    ys[i] = rch.ray.y2;
                } else {
                    xs[i] = rch.ray.x2;
                    ys[i] = rch.ray.y2;
                }
            }
        }
        ret[0] = xs;
        ret[1] = ys;
        return ret;
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

//        Vector2 newValue = Vector2.add(transform.getGlobalPosition(), moveVector);
//        if(walkCollider.getIntersectionCenter() != null) {
//            //newValue = Vector2.add(transform.getGlobalPosition(), Vector2.subtract(walkCollider.getIntersectionCenter().getNormalizedVector(), moveVector));
//            rb2.addForce(Vector2.multiply(Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(walkCollider.getIntersectionCenter(), getPosition())), 15));
//        }
//
//
//        double screenWidth = GameSettings.getInt("game_resolution_X");
//        double screenHeihgt = GameSettings.getInt("game_resolution_Y");
//        double boudMarginX = screenWidth * GameSettings.getDouble("map_movement_padding_X");
//        double boudMarginY = screenHeihgt * GameSettings.getDouble("map_movement_padding_Y");
//
//        if (newValue.getX() <= screenWidth - boudMarginX
//                && newValue.getX() > boudMarginX
//                && newValue.getY() <= screenHeihgt - boudMarginY
//                && newValue.getY() > boudMarginY) {
//            transform.setLocalPosition(newValue);
//        }
        transform.setLocalPosition(Vector2.add(getPosition(), moveVector));
    }

    /**
     * Used when player is hit to subtract health and check for death
     */
    public void hit() {
        int hp = topdownfuntown.getHealth();
        topdownfuntown.setHealth(--hp);
        if (hp <= 0) {
            die();
            destroy();
        }
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
