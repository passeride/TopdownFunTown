package com.rominntrenger.main;

import com.bluebook.camera.OrtographicCamera;
import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.Sprite;
import com.rominntrenger.main.gui.Inventory;
import com.rominntrenger.main.maploader.ImageBuffering;
import com.rominntrenger.main.maploader.MapCreator;
import javafx.scene.input.KeyCode;

import java.awt.image.BufferedImage;

public class RomInntrenger extends GameApplication {
    OrtographicCamera cam;
    Inventory inventory;
    double camSpeed = 15;

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
    }

    @Override
    public void update(double delta) {
        if(input.isKeyPressed(KeyCode.W)){
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
        }
    }
}
