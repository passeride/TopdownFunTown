package com.topdownfuntown.objects;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.ConvexHull;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.HitDetectionHandler;
import com.bluebook.physics.RayCast;
import com.bluebook.physics.RayCastHit;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;
import com.sun.javafx.geom.Line2D;
import com.topdownfuntown.main.Topdownfuntown;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {
    AudioPlayer hitSound;
    private double speed = 100.0; // Gotta go fast
    private double baseSpeed = 100.0;
    private double speedBoostSpeed = 1000.0;
    private boolean speedBost = false;
    private StarterWeapon currentWeapon;
    Topdownfuntown topdownfuntown;

    private int rayCastResolution = 720;
    private ArrayList<RayCast> raycasts = new ArrayList<>();

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
    public Player(Vector2 position, Vector2 direction, Sprite sprite, StarterWeapon weapon) {
        super(position, direction, sprite);
        topdownfuntown = (Topdownfuntown) GameApplication.getInstance();
        hitSound = new AudioPlayer("./assets/audio/lukasAuu.wav");
        hitSound.setSpital(this);
        currentWeapon = weapon;
        setUpRayCast();
    }

    private void setUpRayCast(){
        for(int i = 0; i < rayCastResolution; i++){
            double dir = (Math.PI * 2) * ((double) i / rayCastResolution);
            raycasts.add(new RayCast(dir, this));
        }
    }

    private void updatePositionRaycast(){
        for(RayCast r : raycasts){
            r.updatePosition();
        }
    }

    @Override
    public void update(double delta) {
        updatePositionRaycast();
        currentWeapon.setPosition(position);
        currentWeapon.setDirection(direction);
    }

    @Override
    public void draw(GraphicsContext gc){


        gc.setFill(Color.RED);

        double[] xs = new double[raycasts.size()];
        double[] ys = new double[raycasts.size()];


        for(int i = 0; i < raycasts.size(); i++){
            RayCastHit rch = raycasts.get(i).hit;
            if(rch != null) {
                if (rch.isHit) {
                    //gc.setStroke(Color.RED);
                    //gc.strokeLine(rch.ray.x1, rch.ray.y1, rch.ray.x2, rch.ray.y2);
                    xs[i] = rch.ray.x2;
                    ys[i] = rch.ray.y2;
                    //gc.strokeLine(rch.originalRay.x1, rch.originalRay.y1, rch.originalRay.x2, rch.originalRay.y2);

                } else {
                    //gc.setStroke(Color.GREEN);
                    xs[i] = rch.ray.x2;
                    ys[i] = rch.ray.y2;
                    //xs[i] = ((int)rch.originalRay.x2);
                    //ys[i] = ((int)rch.originalRay.y2);
                    //gc.strokeLine(rch.originalRay.x1, rch.originalRay.y1, rch.originalRay.x2, rch.originalRay.y2);
                }
            }
        }

        gc.save();
        gc.setFill(Color.WHITE);
        gc.applyEffect(new ColorAdjust(0, 0, -0.7, 0));
        gc.setGlobalBlendMode(BlendMode.OVERLAY);
        gc.fillPolygon(xs, ys, xs.length);
        gc.restore();

        super.draw(gc);

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
        Vector2 newValue = Vector2.add(position, moveVector);

        double screenWidth = GameSettings.getInt("game_resolution_X");
        double screenHeihgt = GameSettings.getInt("game_resolution_Y");
        double boudMarginX = screenWidth * GameSettings.getDouble("map_movement_padding_X");
        double boudMarginY = screenHeihgt * GameSettings.getDouble("map_movement_padding_Y");

        if (newValue.getX() <= screenWidth - boudMarginX
                && newValue.getX() > boudMarginX
                && newValue.getY() <= screenHeihgt - boudMarginY
                && newValue.getY() > boudMarginY) {
            position = newValue;
        }
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
        currentWeapon.shoot();
    }
}
