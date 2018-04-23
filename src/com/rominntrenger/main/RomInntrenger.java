package com.rominntrenger.main;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.camera.OrthographicCamera;
import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.util.Vec2;
import com.rominntrenger.gui.HealthElement;
import com.rominntrenger.gui.Inventory;
import com.rominntrenger.maploader.MapCreator;
import com.rominntrenger.messageHandling.MessageHandler;
import com.rominntrenger.objects.player.Player;
import com.rominntrenger.objects.player.StarterWeapon;
import com.rominntrenger.objects.player.Weapon;
import javafx.scene.input.KeyCode;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class RomInntrenger extends GameApplication {

    OrthographicCamera cam;
    public Inventory inventory;
    double camSpeed = 15;
    public Player player;
    public HealthElement healthElement;
    public Weapon currentWeapon;

    public AudioPlayer bgMusic;
    public Clip clip;
    public FloatControl floatControl;

    MessageHandler msh;

    @Override
    protected void onLoad() {
        super.onLoad();
        currentWeapon = new StarterWeapon(Vec2.ZERO,
            new AnimationSprite("/friendlies/arms", 2), Vec2.ZERO);
        cam = new OrthographicCamera();

        MapCreator level = new MapCreator("FSMTEST");
        level.createLevel();
        inventory = new Inventory(6);
        healthElement = new HealthElement(new Vec2(0, 0));

        bgMusic = new AudioPlayer("./assets/audio/MoodyLoop.wav");
        clip = bgMusic.getClip();
        bgMusic.playLoop();

        player.setCurrentWeapon(currentWeapon);

        msh = MessageHandler.getInstance();
    }

    @Override
    public void update(double delta) {
        cam.update(delta);

        if (input.isKeyDown(KeyCode.S) || input.isKeyDown(KeyCode.W) || input.isKeyDown(KeyCode.A)
            || input.isKeyDown(KeyCode.D)) {
            ((AnimationSprite) player.getSprite()).setPlaying(true);
        } else {
            ((AnimationSprite) player.getSprite()).setPlaying(false);
        }

        if (input.isKeyDown(KeyCode.S)) {
            player.moveDown(delta);
        }

        if (input.isKeyDown(KeyCode.W)) {
            player.moveUp(delta);
        }

        if (input.isKeyDown(KeyCode.D)) {
            player.moveRight(delta);
        }

        if (input.isKeyDown(KeyCode.A)) {
            player.moveLeft(delta);
        }
        if(input.isKeyPressed(KeyCode.ESCAPE)){
            callMenu();
        }

        if(player.getCurrentWeapon() != null) {
            if (input.isMouseButton0Pressed()) {
                // TODO: Fix trainwreck
                ((AnimationSprite) player.getCurrentWeapon().getSprite()).setPlaying(true);

                player.shoot();
            } else {
                ((AnimationSprite) player.getCurrentWeapon().getSprite()).setPlaying(false);

            }
        }

        // Lookat
        if (OrthographicCamera.main != null) {
            player.lookAt(Vec2.subtract(input.getMousePosition(),
                new Vec2(OrthographicCamera.main.getX(), OrthographicCamera.main.getY())));
        } else {
            player.lookAt(input.getMousePosition());
        }

    }

    public Clip getClip() {
        return clip;
    }

    public FloatControl getFloatControl() {
        return floatControl;
    }
}
