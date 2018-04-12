package com.rominntrenger.main.maploader;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.objects.Key;
import com.topdownfuntown.objects.*;
import javafx.scene.canvas.GraphicsContext;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Creating GameMap/Area with player, enemy and obstacle array lists.
 */
public class NewLevel extends GameObject {
    ID[][] currentLevel;
    BufferedImage newMap;
    GraphicsContext gc;

    private String name = "Space Adventures in Space";
    private AudioPlayer soundTrack;
    private ID tempID;

    public Key key, keyY, keyG, keyP, keyB, keyR;

    public Door door, doorY, doorG, doorB, doorP, doorR;
    public Door ship; // Legger til exit level/skipet som en Door

    public Obstacle bench;
    public Obstacle table;
    public Obstacle stasisBox;
    public Obstacle corpse;
    public Obstacle crate;
    public Obstacle barrel;

    public NewLevel(Sprite sprite, BufferedImage map) {
        super(Vector2.ZERO,Vector2.ZERO,sprite);
        this.newMap = map;
    }

    /*
    public NewLevel(Sprite sprite, BufferedImage map) {
        super(Vector2.ZERO, Vector2.ZERO, sprite);
        this.newMap = map;
    }*/

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
        this.gc = gc;
    }

    public void createLevel() {
        currentLevel = LevelLoader.loadLevel(newMap);
        int w = currentLevel.length;
        int h = currentLevel[0].length;

        for(int x = 0; x < w; x++) {
            for(int y = 0; y < h; y++) {
                tempID = currentLevel[x][y];

                switch (tempID) {
                    case Wall:
                    break;

                    case SpawnPlayer:
                    break;

                    //KEYS AND DOORS
                    case Key:
                        key.draw(gc);
                    break;

                    case KeyY:
                        keyY.draw(gc);
                    break;

                    case KeyB:
                        keyB.draw(gc);
                    break;

                    case KeyG:
                        keyG.draw(gc);
                    break;

                    case KeyP:
                        keyP.draw(gc);
                    break;

                    case KeyR:
                        keyR.draw(gc);
                    break;

                    case Door:
                        door.draw(gc);
                    break;

                    case DoorY:
                    break;
                    case DoorB:
                    break;
                    case DoorG:
                    break;
                    case DoorP:
                    break;
                    case DoorR:
                    break;
                    case Ship:
                    break;

                    // BOXES AND SUCH
                    case StasisBox:
                    break;
                    case Crate:
                    break;
                    case Bench:
                    break;
                    case Table:
                    break;
                    case Corpse:
                    break;
                    case Barrel:
                    break;

                    //ENEMIES
                    case AlienGreen:
                    break;
                    case AlienPurple:
                    break;
                    case AlienTurret:
                    break;
                    case AlienExplode:
                    break;

                    default:
                        //add NOTHING
                    break;
                }

            }
        }
    }

}