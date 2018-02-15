package com.topdownfuntown.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.graphics.SpriteLoader;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;

public class Tile extends GameObject {
    public ArrayList<Image> tiles = new ArrayList<Image>();

    int grid_X = 15, grid_Y = 8;
    int tileWidth = 64, tileHeight = 64;

    public Image[][] gridTile = new Image[grid_Y][grid_X];

    public Tile() {
        super(Vector2.ZERO, Vector2.ZERO, null);
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_00"));
        tiles.add(SpriteLoader.loadBackground("tile_00_01"));
        tiles.add(SpriteLoader.loadBackground("tile_00_02"));
        setupGrid();
    }

    @Override
    public void update(double detla) {
        super.update(detla);

    }

    void setupGrid(){
        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 15; col++) {
                int num = randomTile(0,19);
                Image tile = tiles.get(num);
                gridTile[row][col] = tile;
            }
        }
    }

    @Override
    public void draw(GraphicsContext gc){

        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 15; col++) {
                gc.drawImage(gridTile[row][col], col * tileWidth, row * tileHeight, tileWidth, tileHeight);
            }
        }

    }

    public int randomTile(int min, int max) {
        Random r = new Random();
        return r.ints(min, (max + 1)).limit(1).findFirst().getAsInt();
    }
}