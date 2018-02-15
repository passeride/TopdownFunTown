package com.topdownfuntown.tilerenderer;

import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.Sprite;
import com.topdownfuntown.objects.Tile;
import javafx.application.Application;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class TileRenderer extends GameApplication {
    // protected Sprite[] tiles = new Sprite[10];
    private ArrayList<Sprite> tiles = new ArrayList<Sprite>();

    @Override
    public void update(double delta) {

    }

    @Override
    public void onLoad() {
        Tile t = new Tile();



    }

}
