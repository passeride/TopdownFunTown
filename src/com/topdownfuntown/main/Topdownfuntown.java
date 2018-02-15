package com.topdownfuntown.main;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.Collider;
import com.bluebook.util.Vector2;
import com.topdownfuntown.maps.GameMap;
import com.topdownfuntown.maps.maploader.MapLoader;
import com.topdownfuntown.objects.*;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Random;

import static com.topdownfuntown.stateHandler.StateHandling.saveProgression;

public class Topdownfuntown extends GameApplication {

    private int score;
    private int health;
    private int level;
    private ScoreElement scoreObject;
    private HealthElement healthObject;

    private Player player;
    private AudioPlayer audioPlayer1 = new AudioPlayer(testFil1);

    private GameMap currentGameMap;

    ArrayList<Projectile> projectiles = new ArrayList<>();

    private Enemy[] enemies = new Enemy[3];

    private static String testFil1 = "./assets/audio/scifi002.wav";
    private static String lagringsFil = "./assets/progression/savedFile";

    public Topdownfuntown() {
        super();
    }

    @Override
    public void onLoad(){

        player = new Player(new Vector2(getScreenWidth(), getScreenHeight()), Vector2.ZERO, new Sprite("/friendlies/hilde"));
        player.setSize(new Vector2(128, 128));

        setScore(1000);
        scoreObject = new ScoreElement(new Vector2(getScreenWidth() - 200, 200));
        healthObject = new HealthElement(new Vector2(100,  100));

        Collider c = new Collider(player);
        c.setName("Player");
        c.setTag("UnHittable");
        c.attachToGameObject(player);


        currentGameMap = MapLoader.loadMapJson("Default");
    }

    @Override
    public void update(double delta) {

        if(input.isKeyDown(KeyCode.S)){
            player.moveDown(delta);
        }

        if(input.isKeyDown(KeyCode.W)){
            player.moveUp(delta);
        }

        if(input.isKeyDown(KeyCode.D)){
            player.moveRight(delta);
        }

        if(input.isKeyDown(KeyCode.A)){
            player.moveLeft(delta);
        }

        if(input.isKeyDown(KeyCode.SPACE)){
            player.activateGottaGoFast();
            /* debug for saving file
            try {
                System.out.println(loadProgression(lagringsFil));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println();
            */
        }else{
            player.deactivateGottaGoFast();
        }

        if(input.isMouseButton0Pressed()){
            shoot();
            try {
                saveProgression(lagringsFil);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        player.lookAt(input.getMousePosition());
        scoreObject.setPosition(new Vector2(getScreenWidth() - 700,  100.0));
        checkEnemies();
    }

    private void checkEnemies(){
        Random r = new Random();
        for(int i = 0; i < enemies.length;  i++){
            if(enemies[i] != null) {
                if (!enemies[i].isAlive()) {

                    enemies[i] = new GreenAlien(new Vector2(r.nextInt((int) getScreenWidth()), r.nextInt((int) getScreenHeight())));
                    enemies[i].setTarget(player);
                }
            }else{
                enemies[i] = new GreenAlien(new Vector2(r.nextInt((int) getScreenWidth()), r.nextInt((int) getScreenHeight())));
                enemies[i].setTarget(player);
            }
        }
    }

    public void shoot(){
        audioPlayer1.playOnce();
        score -= 50;
        Projectile p = new Projectile(player.getPosition(), player.getDirection(), new Sprite("/projectiles/bullet"));
        p.setOnCollisionListener(other -> {
            //System.out.println("HIT:  "  + other.getName() + "  :  " + other.getTag());
            if(other.getTag() == "Hittable") {
                if(other.getGameObject() instanceof Player) {
                    Player player = (Player) other.getGameObject();
                    player.hit();
                    if (GameEngine.DEBUG)
                        System.out.println("Bullet Hit " + other.getName());
                    Topdownfuntown.this.player.destroy();
                }else{
                    score += (int)(0.2 * p.getLengthTraveled());
                    other.getGameObject().destroy();
                    p.destroy();
                }
            }
        });

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

/*DUMMY FUNCTION
    public void setLevelNumber(){
        if(LevelLoading.loadLevel()){
            level++;
        }
    }
    */

    public static void main(String[] args){
        launch(args);
    }

}