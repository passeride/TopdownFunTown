package com.topdownfuntown.main;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.camera.OrtographicCamera;
import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;
import com.topdownfuntown.maps.GameMap;
import com.topdownfuntown.maps.maploader.MapLoader;
import com.topdownfuntown.objects.*;
import javafx.scene.Camera;
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

    private Player[] player = new Player[2];
    private AudioPlayer audioPlayer1 = new AudioPlayer(testFil1);

    private GameMap currentGameMap;

    public boolean hasKey = false;

    OrtographicCamera cam;

    ArrayList<Projectile> projectiles = new ArrayList<>();

    private Tile tiles;

    private static String testFil1 = "./assets/audio/scifi002.wav";
    private static String lagringsFil = "./assets/progression/savedFile";

    public Topdownfuntown() {
        super();
    }

    @Override
    public void onLoad(){

        //player[1] = new Player(new Vector2(800, 800), Vector2.ZERO, new AnimationSprite("/friendlies/character",4), new StarterWeapon(new Vector2(600,600), Vector2.ZERO, new AnimationSprite("/friendlies/arms",2), Vector2.ZERO));



        tiles = new Tile();

        setScore(1000);
        scoreObject = new ScoreElement(new Vector2(GameSettings.getInt("game_resolution_X") - 200, 200));
        healthObject = new HealthElement(new Vector2(100,  100));

        health = GameSettings.getInt("player_health");

        currentGameMap = MapLoader.loadMapJson("TestMap");

        StarterWeapon w = new StarterWeapon(new Vector2(0,23), Vector2.ZERO, new AnimationSprite("/friendlies/arms",2), Vector2.ZERO);
        player[0] = new Player(new Vector2(currentGameMap.entry.getPosition().getX() + 50, currentGameMap.entry.getPosition().getY()), Vector2.ZERO,
                new AnimationSprite("/friendlies/character",4),w);

       Turret t = new Turret(new Vector2(800,500), Vector2.DOWN);
       cam = new OrtographicCamera();
       cam.follow(player[0]);
    }


    public void moveToNextRoom(){
        currentGameMap.destroy();
        tiles.setupGrid();
        Projectile.clearAllProjectiles();
        currentGameMap = MapLoader.loadMapJson("Room3");
        player[0].setPosition(new Vector2(currentGameMap.entry.getPosition().getX() + 50, currentGameMap.entry.getPosition().getY()));
        hasKey = false;
    }

    @Override
    public void update(double delta) {
        cam.update();
        if(input.isKeyDown(KeyCode.S) || input.isKeyDown(KeyCode.W) || input.isKeyDown(KeyCode.A) || input.isKeyDown(KeyCode.D)){
            ((AnimationSprite)player[0].getSprite()).setPlaying(true);
        }else{
            ((AnimationSprite)player[0].getSprite()).setPlaying(false);
        }

        if(input.isKeyDown(KeyCode.S)){
            player[0].moveDown(delta);
        }

        if(input.isKeyDown(KeyCode.W)){
            player[0].moveUp(delta);
        }

        if(input.isKeyDown(KeyCode.D)){
            player[0].moveRight(delta);
        }

        if(input.isKeyDown(KeyCode.A)){
            player[0].moveLeft(delta);
        }

        if(input.isKeyDown(KeyCode.LEFT)){
            cam.setX(cam.getX() + 10);
        }

        if(input.isKeyDown(KeyCode.RIGHT)){
            cam.setX(cam.getX() - 10);
        }

        if(input.isKeyDown(KeyCode.UP)){
            cam.setY(cam.getY() - 10);
        }

        if(input.isKeyDown(KeyCode.DOWN)){
            cam.setY(cam.getY() + 10);
        }

//        if(input.isKeyDown(KeyCode.DOWN)){
//            player[1].moveDown(delta);
//        }
//
//        if(input.isKeyDown(KeyCode.UP)){
//            player[1].moveUp(delta);
//        }
//
//        if(input.isKeyDown(KeyCode.RIGHT)){
//            player[1].moveRight(delta);
//        }
//
//        if(input.isKeyDown(KeyCode.LEFT)){
//            player[1].moveLeft(delta);
//        }

        if(input.isKeyPressed(KeyCode.L)){
            moveToNextRoom();
        }

        if(input.isKeyDown(KeyCode.G)){
            player[0].rb2.addForce(Vector2.multiply(Vector2.UP, 1.0));
        }

        if(input.isKeyDown(KeyCode.SPACE)){
            player[0].activateGottaGoFast();
            //player[1].activateGottaGoFast();
            /* debug for saving file
            try {
                System.out.println(loadProgression(lagringsFil));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println();
            */
        }else{
            //player[1].deactivateGottaGoFast();
            player[0].deactivateGottaGoFast();
        }

        if(input.isMouseButton0Pressed()){
            // TODO: Fix trainwreck
            ((AnimationSprite)((GameObject)player[0].getCurrentWeapon()).getSprite()).setPlaying(true);

            player[0].shoot();
            //player[1].shoot();
            try {
                //saveProgression(lagringsFil);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            ((AnimationSprite)((GameObject)player[0].getCurrentWeapon()).getSprite()).setPlaying(false);

        }

        if(OrtographicCamera.main != null)
            player[0].lookAt(Vector2.subtract(input.getMousePosition(), new Vector2(OrtographicCamera.main.getX(), OrtographicCamera.main.getY())));
        else
            player[0].lookAt(input.getMousePosition());

        //player[1].lookAt(input.getMousePosition());
        scoreObject.setPosition(new Vector2(getScreenWidth() - 700,  100.0));
        checkEnemies();
    }

    private void checkEnemies(){

//        boolean levelComplete = true;
//        for(int i = 0; i < enemies.length;  i++) {
//            if(enemies[i] != null && enemies[i].isAlive()){
//                levelComplete = false;
//            }
//        }
//        if(levelComplete){
//           // moveToNextRoom();
//        }
        /*for(int i = 0; i < enemies.length;  i++){
            if(enemies[i] != null) {
                if (!enemies[i].isAlive()) {

                    enemies[i] = new AlienGreen(new Vector2(r.nextInt((int) getScreenWidth()), r.nextInt((int) getScreenHeight())));
                    enemies[i].setTarget(player);
                }
            }else{
                enemies[i] = new AlienGreen(new Vector2(r.nextInt((int) getScreenWidth()), r.nextInt((int) getScreenHeight())));
                enemies[i].setTarget(player);
            }
        }*/
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
        healthObject.setHp(this.health);
    }

    public Player getPlayer() {
        return player[0];
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