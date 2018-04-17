package com.rominntrenger.main;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.camera.OrtographicCamera;
import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.gui.HealthElement;
import com.rominntrenger.main.gui.Inventory;
import com.rominntrenger.main.maploader.ImageBuffering;
import com.rominntrenger.main.maploader.MapCreator;
import com.rominntrenger.main.messageHandling.MessageHandler;
import com.rominntrenger.main.objects.player.Player;
import com.rominntrenger.main.objects.player.StarterWeapon;
import com.rominntrenger.main.objects.player.Weapon;
import javafx.scene.input.KeyCode;

import java.awt.image.BufferedImage;

public class RomInntrenger extends GameApplication {
    OrtographicCamera cam;
    Inventory inventory;
    double camSpeed = 15;
    public Player player;
    public HealthElement healthElement;
    public Weapon currentWeapon;

    public AudioPlayer bgMusic;

    MessageHandler msh;

    @Override
    protected void onLoad() {
        super.onLoad();
        currentWeapon = new StarterWeapon(new Vector2(0,23), Vector2.ZERO, new AnimationSprite("/friendlies/arms",2), Vector2.ZERO);
        cam = new OrtographicCamera();
        ImageBuffering loader = new ImageBuffering();
        BufferedImage thisMap;
        thisMap = loader.loadImage("mapStart04",32,32);
        MapCreator level = new MapCreator("../bg/backgroundGradient_01", thisMap);
        level.createLevel();
        inventory = new Inventory(6);
        healthElement = new HealthElement(new Vector2(0, 0));

        bgMusic = new AudioPlayer("./assets/audio/MoodyLoop.wav");
        bgMusic.playLoop();


        player.setCurrentWeapon(currentWeapon);

        msh = MessageHandler.getInstance();
    }

    @Override
    public void update(double delta) {
        cam.update();

        if(input.isKeyDown(KeyCode.S) || input.isKeyDown(KeyCode.W) || input.isKeyDown(KeyCode.A) || input.isKeyDown(KeyCode.D)){
            ((AnimationSprite)player.getSprite()).setPlaying(true);
        }else{
            ((AnimationSprite)player.getSprite()).setPlaying(false);
        }

        if(input.isKeyDown(KeyCode.S)){
            player.moveDown(delta);
        }

        if(input.isKeyDown(KeyCode.W)){
            player.moveUp(delta);
        }

        if(input.isKeyDown(KeyCode.D)){
            player.moveRight(delta);
//            msgH.writeMessage("Du trykket på knapp d.\n Fuck yeah, u hit dat shit boi\n oh shit! dat boi");

        }

        if(input.isKeyDown(KeyCode.A)){
            player.moveLeft(delta);
//           msgH.writeMessage("Du trykket på knapp a.\n Fuck yeah, u hit dat shit boi\n oh shit! dat boi", new Sprite("items/crate"));

        }

        if(input.isMouseButton0Pressed()){
            // TODO: Fix trainwreck
            ((AnimationSprite)((GameObject)player.getCurrentWeapon()).getSprite()).setPlaying(true);

            player.shoot();
        }else{
            ((AnimationSprite)((GameObject)player.getCurrentWeapon()).getSprite()).setPlaying(false);

        }

        // Lookat
        if(OrtographicCamera.main != null)
            player.lookAt(Vector2.subtract(input.getMousePosition(), new Vector2(OrtographicCamera.main.getX(), OrtographicCamera.main.getY())));
        else
            player.lookAt(input.getMousePosition());

    }
}
