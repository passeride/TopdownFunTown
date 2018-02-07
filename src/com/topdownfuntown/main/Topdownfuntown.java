package com.topdownfuntown.main;

import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.Sprite;
import com.bluebook.graphics.SpriteLoader;
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
        player = new Player(new Vector2(5, 5), Vector2.ZERO, new Sprite(SpriteLoader.loadImage("senik")));
    }

    @Override
    public void update(double delta) {
        if(input.isPressed(KeyCode.S)){
            player.moveDown(delta);
        }
        if(input.isPressed(KeyCode.W)){
            player.moveUp(delta);
        }

        if(input.isPressed(KeyCode.D)){
            player.moveRight(delta);
        }
        if(input.isPressed(KeyCode.A)){
            player.moveLeft(delta);
        }

        if(input.isPressed(KeyCode.SPACE)){
            player.activateGottaGoFast();
        }else{
            player.deactivateGottaGoFast();
        }

        if(input.isPressed(KeyCode.E)){
            shoot();
        }

        player.lookAt(input.getMousePosition());
    }

    public void shoot(){
        projectiles.add(new Projectile(player.getPosition(), player.getDirection(), new Sprite(SpriteLoader.loadImage("bullet"))));

    }

    public static void main(String[] args){
        launch(args);
    }

}
