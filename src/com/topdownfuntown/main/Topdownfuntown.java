package com.topdownfuntown.main;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.graphics.SpriteLoader;
import com.bluebook.physics.Collider;
import com.bluebook.util.Vector2;
import com.topdownfuntown.objects.Player;
import com.topdownfuntown.objects.Projectile;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;

public class Topdownfuntown extends GameApplication {

    Player player;
    AudioPlayer audioPlayer = new AudioPlayer(testFil);
    AudioPlayer audioPlayer1 = new AudioPlayer(testFil1);

    ArrayList<Projectile> projectiles = new ArrayList<>();

    private static String testFil = "./assets/audio/MoodyLoop.wav";
    private static String testFil1 = "./assets/audio/scifi002.wav";

    public Topdownfuntown() {
        super();
    }

    @Override
    public void onLoad(){
        player = new Player(new Vector2(5, 5), Vector2.ZERO, new AnimationSprite(SpriteLoader.loadAnimationImage("enemyGreen")));;
        player.setSize(new Vector2(128, 128));
        Collider c = new Collider(player);
        c.setName("Player");
        c.setTag("Hittable");
        c.attachToGameObject(player);
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
        }else{
            player.deactivateGottaGoFast();
        }

        if(input.isKeyPressed(KeyCode.J))
            audioPlayer.playOnce();


        if(input.isMouseButton0Pressed()){
            shoot();
            audioPlayer1.playOnce();
        }

        player.lookAt(input.getMousePosition());
    }

    public void shoot(){
        projectiles.add(new Projectile(Vector2.add(player.getPosition(), Vector2.multiply(player.getDirection(), player.getSize().getX() * 1.2)), player.getDirection(), new Sprite(SpriteLoader.loadImage("bullet"))));

    }

    public static void main(String[] args){
        launch(args);
    }

}