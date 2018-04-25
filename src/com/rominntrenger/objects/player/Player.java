package com.rominntrenger.objects.player;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.camera.OrthographicCamera;
import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.CircleCollider;
import com.bluebook.physics.Collider;
import com.bluebook.physics.Light2D;
import com.bluebook.physics.RigidBody2D;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.gui.DeathOverlay;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.messageHandling.Describable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player extends GameObject {

    AudioPlayer hitSound;
    private double speed = 800.0; // Gotta go fast
    private double baseSpeed = 300.0;
    private double speedBoostSpeed = 1000.0;
    private boolean speedBost = false;
    private Collider walkCollider;
    private Weapon currentWeapon;
    private int playerKey = 9;

    private int enemiesKilled = 0;

    private boolean uses_controller = false;

    public RigidBody2D rb2;
    public Light2D light2D;

    private double angularDampening = 0.2;

    private RomInntrenger romInntrenger;
    private AudioPlayer audioPlayer;

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public Player(Vec2 position, Vec2 direction, Sprite sprite) {
        super(position, direction, sprite);

        ((RomInntrenger) GameApplication.getInstance()).players.add(this);

        setRenderLayer(RenderLayer.RenderLayerName.PLAYER);
        hitSound = new AudioPlayer("./assets/audio/lukasAuu.wav");
        hitSound.setSpatial(this);

        collider = new CircleCollider(this, 30);
        collider.setName("player");
        collider.setTag("UnHittable");
        collider.addInteractionLayer("Hittable");


        light2D = new Light2D(this);

        // WalkCollider
        walkCollider = new CircleCollider(this, 20);
        walkCollider.setName("Player_Walk");
        walkCollider.setTag("Walk");
        walkCollider.addInteractionLayer("Item");
        walkCollider.addInteractionLayer("Block");
        walkCollider.setPadding(new Vec2(-20, -20));

        walkCollider.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if (other.getGameObject() instanceof Describable) {
                    ((Describable) other.getGameObject()).showMessage();
                }
            }
        });

        rb2 = new RigidBody2D(this);

        OrthographicCamera.main.follow(this);

        romInntrenger = ((RomInntrenger) GameApplication.getInstance());

    }

    @Override
    public void draw(GraphicsContext gc) {

        if(true) {
            if (light2D.polygon != null) {
                double[][] polygon = light2D.polygon;
//                gc.save();
//                gc.setGlobalBlendMode(BlendMode.OVERLAY);
                gc.setFill(new Color(1, 1, 1, 0.1));
                gc.fillPolygon(polygon[0], polygon[1], polygon[0].length);
//                gc.restore();
            }
        }


        if(uses_controller) {
//            gc.save();
//            gc.setGlobalBlendMode(BlendMode.OVERLAY);

            Vec2 pos = transform.getGlobalPosition();
            double dir = getDirection().getAngleInRadians() - Math.PI / 2;
            gc.setStroke(new Color(1, 0, 0, 0.3));
            gc.setLineDashes(8, 10, 8, 10);
            gc.setLineWidth(5);
            gc.strokeLine(pos.getX(), pos.getY(),
                pos.getX() + Math.cos(dir) * 2000,
                pos.getY() + Math.sin(dir) * 2000);
//            gc.restore();

        }
//        gc.setGlobalBlendMode(BlendMode.);
        super.draw(gc);

    }

    @Override
    public void update(double delta) {
        rb2.update(delta);

        translate(Vec2.multiply(rb2.getVelocity(), delta));
        translate(Vec2.ZERO); // This is to update in case of intersection

    }


    /**
     * Will move the player object NORTH/UP by {@link Player#speed}
     */
    public void moveUp(double delta) {
        translate(Vec2.multiply(Vec2.UP, speed * delta));
    }

    /**
     * Will move the player object SOUTH/DOWN by {@link Player#speed}
     */
    public void moveDown(double delta) {
        translate(Vec2.multiply(Vec2.DOWN, speed * delta));
    }

    /**
     * Will move the player object WEST/LEFT by {@link Player#speed}
     */
    public void moveLeft(double delta) {
        translate(Vec2.multiply(Vec2.LEFT, speed * delta));
    }

    /**
     * Will move the player object EAST/RIGHT by {@link Player#speed}
     */
    public void moveRight(double delta) {
        translate(Vec2.multiply(Vec2.RIGHT, speed * delta));
    }

    public void move(Vec2 direction, double delta){
        translate(Vec2.multiply(direction, speed * delta));
    }

    /**
     * Override to create a 8 % margin for movement
     */
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

    /**
     * Used when player is hit to subtract health and check for death
     */
    public void hit(int dmg) {
        int hp = romInntrenger.healthElement.getHp();
        hp -= dmg;
        romInntrenger.healthElement.setHp(hp);
        if (hp <= 0) {
            new DeathOverlay();
            die();
            audioPlayer = new AudioPlayer("./assets/audio/Evil_Laugh.wav");
            audioPlayer.playOnce();
            romInntrenger.bgMusic.stop();
            romInntrenger.bgMusic.close();
            destroy();



        }
        hitSound.playOnce();
    }

    public void heal(int health) {
        int hp = romInntrenger.healthElement.getHp();
        hp += health;
        romInntrenger.healthElement.setHp(hp);
        // TODO: if health == max : don't heal
    }

    private void die() {
        if(currentWeapon != null)
            currentWeapon.destroy();
        GameEngine.getInstance().pauseGame();

        // This is for fun, to mess with Hilde's old computer
        /*
        try {
            Runtime.getRuntime().exec("eject");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

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

        if (currentWeapon != null) {
            if(currentWeapon.shoot()) {
//                rb2.addForce(Vec2.multiply(Vec2
//                        .Vector2FromAngleInDegrees(
//                            transform.getGlobalRotation().getAngleInDegrees() + 90),
//                    30000));
            }
        }

    }

    public int getEnemiesKilled(){
        return enemiesKilled;
    }

    public void killedEnemy(){
        enemiesKilled++;
    }


    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public boolean hasWeapon(){
        if(currentWeapon != null)
            return true;
        else
            return false;
    }

    public void setCurrentWeapon(Weapon currentWeapon) {
        if(this.currentWeapon != null)
        this.currentWeapon.destroy();
        this.currentWeapon = currentWeapon;
        this.currentWeapon.setHolder(this);
        this.currentWeapon.getTransform().setParent(transform);
    }

    public int getPlayerKey() {
        return playerKey;
    }

    public void setPlayerKey(int playerKey) {
        this.playerKey = playerKey;
    }

    public boolean isUses_controller() {
        return uses_controller;
    }

    public void setUses_controller(boolean uses_controller) {
        this.uses_controller = uses_controller;
    }
}
