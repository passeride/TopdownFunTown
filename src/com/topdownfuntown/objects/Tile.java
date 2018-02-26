package com.topdownfuntown.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.graphics.SpriteLoader;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;

public class Tile extends GameObject {
    public ArrayList<Image> tiles = new ArrayList<Image>();

    int grid_X = 15, grid_Y = 8;
    int tileWidth = 64, tileHeight = 64;
    double marginX, marginY;

    public Image[][] gridTile;

    public Tile() {
        super(Vector2.ZERO, Vector2.ZERO, null);

        grid_X = GameSettings.getInt("grid_X");
        grid_Y = GameSettings.getInt("grid_Y");

        int gameResolutionX = GameSettings.getInt("game_resolution_X");
        int gameResolutionY = GameSettings.getInt("game_resolution_Y");

        marginX = gameResolutionX * GameSettings.getDouble("map_movement_padding_X");
        marginY = gameResolutionY * GameSettings.getDouble("map_movement_padding_Y");

        gameResolutionX = (int)(gameResolutionX - marginX * 2);
        gameResolutionY = (int)(gameResolutionY - marginY * 2);

        tileWidth = gameResolutionX / grid_X;
        tileHeight = gameResolutionY /  grid_Y;

        gridTile = new Image[grid_X][grid_Y];

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

        setRenderLayer(RenderLayer.RenderLayerName.TILES);

    }

    @Override
    public void update(double detla) {
        super.update(detla);

    }


    public void setupGrid(){

        for(int i = 0; i < grid_X; i++){
            for(int j = 0; j < grid_Y; j++){
                int num = randomTile(0,tiles.size()-1);
                Image tile = tiles.get(num);
                gridTile[i][j] = tile;
            }
        }
    }

    @Override
    public void draw(GraphicsContext gc){

        for(int i = 0; i < grid_X; i++){
            for(int j = 0; j < grid_Y; j++){
                gc.drawImage(gridTile[i][j], i * tileWidth + marginX, j * tileHeight + marginY, tileWidth, tileHeight);
            }
        }

    }

    public int randomTile(int min, int max) {
        Random r = new Random();
        return r.ints(min, (max + 1)).limit(1).findFirst().getAsInt();
    }
}