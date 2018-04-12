package com.rominntrenger.main.maploader;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.objects.*;
import javafx.scene.canvas.GraphicsContext;

import java.awt.image.BufferedImage;

/**
 * Creating GameMap/Area with player, enemy and obstacle array lists.
 */
public class MapCreator extends GameObject {
    ID[][] currentLevel;
    GameObject[][] grid;
    BufferedImage newMap;
    GraphicsContext gc;
    Vector2 vector;

    private String name = "Space Adventures in Space";
    private AudioPlayer soundTrack;
    private ID tempID;

    public MapCreator(String spritePath, BufferedImage map) {
        super(Vector2.ZERO,Vector2.ZERO,new Sprite(spritePath));
        this.newMap = map;
        setRenderLayer(RenderLayer.RenderLayerName.BACKGROUND);
    }

    @Override
    public void draw(GraphicsContext gc) {
        //super.draw(gc);
        //this.gc = gc;
    }

    public void createLevel() {
        currentLevel = LevelImageLoader.loadLevel(newMap);
        int x = currentLevel.length;
        int y = currentLevel[0].length;
        grid = new GameObject[x][y];
        for(int i = 0; i < x; i++) {
            for(int j = 0; j < y; j++) {

                tempID = currentLevel[i][j];

                vector = new Vector2(i,j);
                vector = Vector2.multiply(vector, GameSettings.getScreenScale());
                new Tile(vector, Vector2.ZERO, new Sprite("../bg/tile_01_00"));
                switch (tempID) {

                    case Wall:
                        grid[i][j] = new Wall(vector,Vector2.ZERO,new Sprite("../bg/wall"));
                    break;

                    case SpawnPlayer:
                    break;

                    //KEYS AND DOORS
                    case Key:
                        grid[i][j] = new Key(vector,Vector2.ZERO,new Sprite("./items/key_gold00"));
                    break;

                    case KeyY:
                        grid[i][j] = new Key(vector,Vector2.ZERO,new Sprite("./items/key_gold00"));
                    break;

                    case KeyB:
                        grid[i][j] = new Key(vector,Vector2.ZERO,new Sprite("./items/key_gold00"));
                    break;

                    case KeyG:
                        grid[i][j] = new Key(vector,Vector2.ZERO,new Sprite("./items/key_gold00"));
                    break;

                    case KeyP:
                        grid[i][j] = new Key(vector,Vector2.ZERO,new Sprite("./items/key_gold00"));
                    break;

                    case KeyR:
                        grid[i][j] = new Key(vector,Vector2.ZERO,new Sprite("./items/key_gold00"));
                    break;

                    case Door:
                        grid[i][j] = new Door(vector, Vector2.ZERO, new Sprite("../bg/doorG"));
                    break;
                    case DoorY:
                        grid[i][j] = new Door(vector, Vector2.ZERO, new Sprite("../bg/doorG"));
                    break;
                    case DoorB:
                        grid[i][j] = new Door(vector, Vector2.ZERO, new Sprite("../bg/doorG"));
                    break;
                    case DoorG:
                        grid[i][j] = new Door(vector, Vector2.ZERO, new Sprite("../bg/doorG"));
                    break;
                    case DoorP:
                        grid[i][j] = new Door(vector, Vector2.ZERO, new Sprite("../bg/doorG"));
                    break;
                    case DoorR:
                        grid[i][j] = new Door(vector, Vector2.ZERO, new Sprite("../bg/doorG"));
                    break;
                    case Ship:
                        grid[i][j] = new Door(vector, Vector2.ZERO, new Sprite("../bg/doorG"));
                    break;

                    // BOXES AND SUCH
                    case StasisBox:
                        grid[i][j] = new StasisBox(vector,Vector2.ZERO, new Sprite("../bg/stasisBoxRed"));
                    break;
                    case Crate:
                        grid[i][j] = new Crate(vector, Vector2.ZERO, new Sprite("./items/crate_02"));
                    break;
                    case Bench:
                        grid[i][j] = new Bench(vector, Vector2.ZERO, new Sprite("./items/bench"));
                    break;
                    case Table:
                        grid[i][j] = new Table(vector, Vector2.ZERO, new Sprite("./items/table"));
                    break;
                    case Corpse:
                        grid[i][j] = new Corpse(vector, Vector2.ZERO, new Sprite("./items/corpse_00"));
                    break;
                    case Barrel:
                        grid[i][j] = new Barrel(vector, Vector2.ZERO, new Sprite("./items/barrel_blue"));
                    break;

                    //ENEMIES
                    case AlienGreen:
                        grid[i][j] = new AlienGreen(vector);
                    break;
                    case AlienPurple:
                        grid[i][j] = new AlienPurple(vector);
                    break;
                    case AlienTurret:
                        grid[i][j] = new AlienTurret(vector,Vector2.ZERO);
                    break;
                    case AlienExplode:
                    break;

                    default:
                        //grid[i][j] = new Tile(vector, Vector2.ZERO, new Sprite("../bg/tile"));
                    break;
                }

            }
        }
    }

}