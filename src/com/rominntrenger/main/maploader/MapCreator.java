package com.rominntrenger.main.maploader;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.objects.Key;
import com.topdownfuntown.objects.*;
import javafx.scene.canvas.GraphicsContext;

import java.awt.image.BufferedImage;

/**
 * Creating GameMap/Area with player, enemy and obstacle array lists.
 */
public class MapCreator extends GameObject {
    ID[][] currentLevel;
    BufferedImage newMap;
    GraphicsContext gc;
    Vector2 vector;

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

    public MapCreator(String spritePath, BufferedImage map) {
        super(Vector2.ZERO,Vector2.ZERO,new Sprite(spritePath));
        this.newMap = map;
    }

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
        this.gc = gc;
    }

    public void createLevel() {
        currentLevel = LevelImageLoader.loadLevel(newMap);
        int x = currentLevel.length;
        int y = currentLevel[0].length;
        vector = new Vector2(x,y);

        for(int i = 0; i < x; i++) {
            for(int j = 0; j < y; j++) {
                tempID = currentLevel[i][j];

                switch (tempID) {
                    case Wall:
                    break;

                    case SpawnPlayer:
                    break;

                    //KEYS AND DOORS
                    case Key:
                        key = new Key(vector,Vector2.ZERO,new Sprite("../sprite/items/key_gold00.png"));
                    break;

                    case KeyY:

                    break;

                    case KeyB:

                    break;

                    case KeyG:

                    break;

                    case KeyP:

                    break;

                    case KeyR:

                    break;

                    case Door:

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