package com.topdownfuntown.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.graphics.SpriteLoader;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.Random;

public class Tile extends GameObject {
    public ArrayList<Image> tiles = new ArrayList<Image>();

    int grid_X = 15, grid_Y = 8;
    int tileWidth = 64, tileHeight = 64;
    double marginX, marginY;

    public Image[][] gridTile;
    public rotation[][] gridRotation;

    public enum rotation{
        NORTH, SOUTH, WEST, EAST;
    }

    int bloodied = 0;
    int cracked = 0;
    int num;

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
        gridRotation = new rotation[grid_X][grid_Y];

        tiles.add(SpriteLoader.loadBackground("tile_00_03"));
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

        Random r = new Random();
        for(int i = 0; i < grid_X; i++){
            for(int j = 0; j < grid_Y; j++){
                // Setting rotation
                int dir = r.nextInt(3);
                switch (dir){
                    case 1:
                        gridRotation[i][j] = rotation.NORTH;
                        break;
                    case 2:
                        gridRotation[i][j] = rotation.EAST;
                        break;
                    case 3:
                        gridRotation[i][j] = rotation.SOUTH;
                        break;
                    case 0:
                        gridRotation[i][j] = rotation.WEST;
                        break;
                }
                // setting tiles
               num = randomTile(0,(tiles.size()-1) * 5);
               if(num == 1){
                   gridTile[i][j] = tiles.get(num/5);
               }else if(num == 10) {
                   gridTile[i][j] = tiles.get(num / 5);
               }else if(num == 15){
                   gridTile[i][j] = tiles.get(num / 5);
               }else{
                   gridTile[i][j] = tiles.get(0);
               }
               /*
                if(num == 1) {
                    bloodied++;
                }
                if(num == 2) {
                    cracked++;
                }
                if(bloodied >= 3)
                {
                    num = 0;
                    Image tile = tiles.get(num);
                    gridTile[i][j] = tile;
                } else if (cracked >= 3) {
                    num = 0;
                    Image tile = tiles.get(num);
                    gridTile[i][j] = tile;
                } else {
                    Image tile = tiles.get(num);
                    gridTile[i][j] = tile;
                }*/
            }
        }


    }

    @Override
    public void draw(GraphicsContext gc){
        Random r = new Random();
        for(int i = 0; i < grid_X; i++){
            for(int j = 0; j < grid_Y; j++){
//                gc.save();
//                rotateGraphicsContext(gc, new Vector2(i * tileWidth + marginX + (tileWidth / 2.0), j * tileHeight + marginY + (tileHeight / 2.0)), r.nextDouble() * 360);

//                boolean swapWidthHeight = false;
//                switch(gridRotation[i][j]){
//                    case NORTH:
//                        rotateGraphicsContext(gc, new Vector2(i * tileWidth + marginX + (tileWidth / 2.0), j * tileHeight + marginY + (tileHeight / 2.0)), 0);
//                        break;
//                    case EAST:
//                        swapWidthHeight = true;
//                        rotateGraphicsContext(gc, new Vector2(i * tileWidth + marginX + (tileHeight / 2.0), j * tileHeight + marginY + (tileWidth / 2.0)), 45);
//                        break;
//                    case SOUTH:
//                        swapWidthHeight = true;
//                        rotateGraphicsContext(gc, new Vector2(i * tileWidth + marginX + (tileWidth / 2.0), j * tileHeight + marginY + (tileHeight / 2.0)), 180);
//                        break;
//                    case WEST:
//                        swapWidthHeight = true;
//                        rotateGraphicsContext(gc, new Vector2(i * tileWidth + marginX + (tileHeight / 2.0), j * tileHeight + marginY + (tileWidth / 2.0)), 270);
//                        break;
//                }
//                if(swapWidthHeight){
//                    gc.drawImage(gridTile[i][j], i * tileWidth + marginX, j * tileHeight + marginY, tileHeight, tileWidth);
//                }else {
                    gc.drawImage(gridTile[i][j], i * tileWidth + marginX, j * tileHeight + marginY, tileWidth, tileHeight);
//                }
//                gc.restore();
            }
        }

    }

    protected GraphicsContext rotateGraphicsContext(GraphicsContext gc, Vector2 position, double rotateAngle){
        Rotate r = new Rotate(rotateAngle, position.getX(), position.getY());
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        return gc;
    }

    public int randomTile(int min, int max) {
        Random r = new Random();
        return r.ints(min, (max + 1)).limit(1).findFirst().getAsInt();
    }
}