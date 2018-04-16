package com.rominntrenger.main;

import com.bluebook.camera.OrtographicCamera;
import com.bluebook.engine.GameApplication;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.gui.HealthElement;
import com.rominntrenger.main.gui.Inventory;
import com.rominntrenger.main.maploader.ImageBuffering;
import com.rominntrenger.main.maploader.MapCreator;
import com.rominntrenger.main.objects.player.Player;
import javafx.scene.input.KeyCode;

import java.awt.image.BufferedImage;

public class RomInntrenger extends GameApplication {
    OrtographicCamera cam;
    Inventory inventory;
    double camSpeed = 15;
    public Player player;
    public HealthElement healthElement;

    @Override
    protected void onLoad() {
        super.onLoad();
        cam = new OrtographicCamera();
        ImageBuffering loader = new ImageBuffering();
        BufferedImage thisMap;
        thisMap = loader.loadImage("mapStart03",32,32);
        MapCreator level = new MapCreator("../bg/backgroundGradient_01", thisMap);
        level.createLevel();
        inventory = new Inventory(6);
        healthElement = new HealthElement(new Vector2(0, 0));
    }

    @Override
    public void update(double delta) {
        cam.update();
/*        if(input.isKeyPressed(KeyCode.W)){
            cam.setY(cam.getY() + camSpeed);
        }

        if(input.isKeyPressed(KeyCode.S)){
            cam.setY(cam.getY() - camSpeed);
            inventory.addItem(new Sprite("./items/bench"));
        }

        if(input.isKeyDown(KeyCode.A)){
            cam.setX(cam.getX() + camSpeed);
        }

        if(input.isKeyDown(KeyCode.D)){
            cam.setX(cam.getX() - camSpeed);
        }*/

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
//            msgH.writeMessage("Du trykket på knapp a.\n Fuck yeah, u hit dat shit boi\n oh shit! dat boi", new Sprite("items/crate"));

        }

        // Lookat
        if(OrtographicCamera.main != null)
            player.lookAt(Vector2.subtract(input.getMousePosition(), new Vector2(OrtographicCamera.main.getX(), OrtographicCamera.main.getY())));
        else
            player.lookAt(input.getMousePosition());

    }
}
