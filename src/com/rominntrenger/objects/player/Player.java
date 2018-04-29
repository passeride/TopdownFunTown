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
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.gui.DeathOverlay;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.messageHandling.Describable;
import com.rominntrenger.objects.PlayerGuiElement;
import com.rominntrenger.objects.enemy.Enemy;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class Player extends GameObject {


    int PlayerID = 0;
    Color playerColor = Color.RED;
    int maxPlayerHealth = 100;
    int playerHealth = 100;

    private PlayerGuiElement gui;


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

    private double angularDampening = 0.1;

    private RomInntrenger romInntrenger;
    private AudioPlayer audioPlayer;

    private boolean isMultiplayer = false;
    private int multiPlayerCircleRadius = 75;
    private boolean showLaser = false;

    private Vec2 lookDirection = new Vec2(0,1);

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

        maxPlayerHealth = GameSettings.getInt("player_health");
        angularDampening = GameSettings.getDouble("player_angular_dampening");
        playerHealth = maxPlayerHealth;


        gui = new PlayerGuiElement(this);
    }

    @Override
    public void draw(GraphicsContext gc) {
        Vec2 pos = transform.getGlobalPosition();

        if(true) {
            if (light2D.polygon != null) {
                double[][] polygon = light2D.polygon;
//                gc.save();
//                gc.setGlobalBlendMode(BlendMode.OVERLAY);
                gc.setFill(new Color(1, 0.7, 0.8, 0.2));
                gc.fillPolygon(polygon[0], polygon[1], polygon[0].length);
//                gc.restore();
            }
        }


        if(uses_controller && showLaser) {
            double dir = getDirection().getAngleInRadians() - Math.PI / 2;
            gc.setStroke( new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), 0.3));
            gc.setLineDashes(8, 10, 8, 10);
            gc.setLineWidth(5);
            gc.strokeLine(pos.getX(), pos.getY(),
                pos.getX() + Math.cos(dir) * 2000,
                pos.getY() + Math.sin(dir) * 2000);
//            gc.restore();

        }

        if(getPlayerID()!= 0){
            gc.setFill( new Color(playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(), 0.3));
            gc.fillArc(pos.getX() - multiPlayerCircleRadius / 2, pos.getY() - multiPlayerCircleRadius /  2,
                multiPlayerCircleRadius,  multiPlayerCircleRadius,  0, 360, ArcType.CHORD);

            gc.setStroke(playerColor);
            gc.setLineWidth(3);
            gc.setLineDashes(3, 1, 3, 2);
            gc.strokeArc(pos.getX() - multiPlayerCircleRadius / 2, pos.getY() - multiPlayerCircleRadius /  2,
                multiPlayerCircleRadius,  multiPlayerCircleRadius,  0, 360, ArcType.CHORD);
        }

        super.draw(gc);


    }

    @Override
    public void update(double delta) {
        rb2.update(delta);

        translate(Vec2.multiply(rb2.getVelocity(), delta));
        translate(Vec2.ZERO); // This is to update in case of intersection



        int playerInLight = 0;
        for(Enemy e : Enemy.allEnemies){
            if(light2D.polygon != null && light2D.pointInPoly(e.getTransform().getGlobalPosition())) {
                e.setIsSeenByPlayer(getPlayerID(), true);
                playerInLight++;
            }else {
                e.setIsSeenByPlayer(getPlayerID(), false);
            }
        }
    }

    /**
     * Will return the speed with possible modifications from pickups
     * @return
     */
    public double getModifiedSpeed(){
        double modSpeed = speed;
        if(currentWeapon != null && currentWeapon.getWeaponBase() != null)
            modSpeed *= currentWeapon.getWeaponBase().speedMultiplier;

        return modSpeed;
    }

    /**
     * Used by controller input to set a laser guide for shooting direction
     * @param lookDirection
     */
    public void lookInDirection(Vec2 lookDirection){
        this.lookDirection = lookDirection.getNormalizedVector();
        if(lookDirection.getMagnitude() > 0.8){
            showLaser = true;
        }else{
            showLaser = false;
        }

        // TODO: FIX SHIT
        if (lookDirection.getMagnitude() >  0.2) {
//            translate(Vec2.multiply(Vec2.Vector2FromAngleInDegrees(Vec2.getAngleBetweenInDegrees(getPosition(), target.getPosition())), speed * delta));
            // Getting modified angular dampening
            double angularDampeningWModifiers = angularDampening;

            if(currentWeapon != null && currentWeapon.getWeaponBase() != null)
                angularDampeningWModifiers *= currentWeapon.getWeaponBase().angularDampeningMultiplier;

            setDirection(
                Vec2.add(
                    getDirection(),
                    Vec2.multiply(
                        Vec2.Vector2FromAngleInDegrees(
                            Vec2.getAngleBetweenInDegrees(getPosition(),
                                Vec2.add(getPosition(), lookDirection)
                            ) + 90
                        )
                        ,angularDampeningWModifiers)
                )
            );
            getDirection().normalize();
        }
    }

    /**
     * Will move the player object NORTH/UP by {@link Player#speed}
     */
    public void moveUp(double delta) {
        translate(Vec2.multiply(Vec2.UP, getModifiedSpeed() * delta));
    }

    /**
     * Will move the player object SOUTH/DOWN by {@link Player#speed}
     */
    public void moveDown(double delta) {
        translate(Vec2.multiply(Vec2.DOWN, getModifiedSpeed() * delta));
    }

    /**
     * Will move the player object WEST/LEFT by {@link Player#speed}
     */
    public void moveLeft(double delta) {
        translate(Vec2.multiply(Vec2.LEFT, getModifiedSpeed() * delta));
    }

    /**
     * Will move the player object EAST/RIGHT by {@link Player#speed}
     */
    public void moveRight(double delta) {
        translate(Vec2.multiply(Vec2.RIGHT, getModifiedSpeed() * delta));
    }

    public void move(Vec2 direction, double delta){
        translate(Vec2.multiply(direction, getModifiedSpeed() * delta));
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
        playerHealth = Math.max(0, playerHealth - dmg);

        if (playerHealth <= 0) {
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

    /**
     * Heals player, but will not surpass {@link Player#maxPlayerHealth}
     * @param health
     */
    public void heal(int health) {
        playerHealth = Math.min(playerHealth + health, maxPlayerHealth);
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
//                        .Vec2FromAngleInDegrees(
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

    @Override
    public void destroy() {
        if(currentWeapon != null)
            currentWeapon.destroy();
        if(walkCollider != null)
            walkCollider.destroy();
        if(gui != null)
            gui.destroy();
        super.destroy();
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

    public int getPlayerID() {
        return PlayerID;
    }

    public void setPlayerID(int playerID) {
        PlayerID = playerID;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

    public int getMaxPlayerHealth() {
        return maxPlayerHealth;
    }

    public void setMaxPlayerHealth(int maxPlayerHealth) {
        this.maxPlayerHealth = maxPlayerHealth;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public PlayerGuiElement getGui() {
        return gui;
    }

    public void setGui(PlayerGuiElement gui) {
        this.gui = gui;
    }
}
