package com.topdownfuntown.main;

import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
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

    ArrayList<Projectile> projectiles = new ArrayList<>();

    public Topdownfuntown() {
        super();
    }

    @Override
    public void onLoad(){
        player = new Player(new Vector2(5, 5), Vector2.ZERO, new Sprite(SpriteLoader.loadImage("senik")));;
        player.setSize(new Vector2(128, 128));
        Collider c = new Collider(player);
        c.setName("Player");
        c.setTag("Hittable");
        c.attachToGameObject(player);
        GameEngine.DEBUG = true;
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

        if(input.isMouseButton0Pressed()){
            shoot();
        }

        player.lookAt(input.getMousePosition());
    }

    public void shoot(){
        projectiles.add(new Projectile(Vector2.add(player.getPosition(), Vector2.multiply(player.getDirection(), player.getSize().getX() * 2)), player.getDirection(), new Sprite(SpriteLoader.loadImage("bullet"))));

    }

    public static void main(String[] args){
        launch(args);
    }

}
