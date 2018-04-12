package com.rominntrenger.main;

import com.bluebook.camera.OrtographicCamera;
import com.bluebook.engine.GameApplication;
import com.rominntrenger.main.maploader.ImageBuffering;
import com.rominntrenger.main.maploader.MapCreator;
import javafx.scene.input.KeyCode;

import java.awt.image.BufferedImage;

public class RomInntrenger extends GameApplication {

    OrtographicCamera cam;

    double camSpeed = 15;

    @Override
    protected void onLoad() {
        super.onLoad();
        cam = new OrtographicCamera();
        ImageBuffering loader = new ImageBuffering();
        BufferedImage thisMap;
        thisMap = loader.loadImage("mapStart",32,32);
        MapCreator level = new MapCreator("../bg/backgroundGradient_01", thisMap);
        level.createLevel();
    }

    @Override
    public void update(double delta) {
        if(input.isKeyDown(KeyCode.W)){
            cam.setY(cam.getY() + camSpeed);
        }

        if(input.isKeyDown(KeyCode.S)){
            cam.setY(cam.getY() - camSpeed);
        }

        if(input.isKeyDown(KeyCode.A)){
            cam.setX(cam.getX() + camSpeed);
        }

        if(input.isKeyDown(KeyCode.D)){
            cam.setX(cam.getX() - camSpeed);
        }
    }
}
