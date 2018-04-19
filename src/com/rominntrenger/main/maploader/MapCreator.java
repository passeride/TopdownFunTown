package com.rominntrenger.main.maploader;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.camera.OrthographicCamera;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.objects.Door;
import com.rominntrenger.main.objects.Key;
import com.rominntrenger.main.objects.Ship;
import com.rominntrenger.main.objects.blocks.Barrel;
import com.rominntrenger.main.objects.blocks.Bench;
import com.rominntrenger.main.objects.blocks.Blood;
import com.rominntrenger.main.objects.blocks.Corpse;
import com.rominntrenger.main.objects.blocks.Crate;
import com.rominntrenger.main.objects.blocks.PickupWeapon;
import com.rominntrenger.main.objects.blocks.StasisBox;
import com.rominntrenger.main.objects.blocks.Table;
import com.rominntrenger.main.objects.blocks.Wall;
import com.rominntrenger.main.objects.enemy.AlienExplode;
import com.rominntrenger.main.objects.enemy.AlienGreen;
import com.rominntrenger.main.objects.enemy.AlienPurple;
import com.rominntrenger.main.objects.enemy.AlienTurret;
import com.rominntrenger.main.objects.player.Player;
import java.awt.image.BufferedImage;
import javafx.scene.canvas.GraphicsContext;

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

    public MapCreator(BufferedImage map) {
        super(Vector2.ZERO, Vector2.ZERO, new Sprite("../bg/tilebg02"));
        allwaysOnScreen = true;
        this.newMap = map;
        setRenderLayer(RenderLayer.RenderLayerName.BACKGROUND);
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Will draw groundtiles only where camera sees
        // To avoid some tearing issues

        Vector2 offset = OrthographicCamera.getOffset();
        Vector2 squareSize = GameSettings.getSquareScale();
        Vector2 screen = GameSettings.getScreen();
        int xNum = (int) (-offset.getX() / squareSize
            .getX()); // Squares to not draw (Left of camera)
        int yNum = (int) (-offset.getY() / squareSize.getY());
        int squaresScreenX = (int) (screen.getX() / squareSize
            .getX()); // Square to draw across camera
        int squaresScreenY = (int) (screen.getY() / squareSize.getY());
        for (int i = xNum - 1; i < xNum + squaresScreenX + 10; i++) {
            for (int j = yNum - 4; j < yNum + squaresScreenY + 10; j++) {
                gc.drawImage(sprite.getImg(),
                    squareSize.getX() * i + offset.getX() + squareSize.getX() / 2,
                    squareSize.getY() * j + offset.getY() + squareSize.getY() / 2,
                    squareSize.getX(), squareSize.getY());
            }
        }
    }

    public void createLevel() {
        currentLevel = LevelImageLoader.loadLevel(newMap);
        int x = currentLevel.length;
        int y = currentLevel[0].length;
        grid = new GameObject[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {

                tempID = currentLevel[i][j];

                vector = new Vector2(i, j);
                vector = Vector2.multiply(vector, GameSettings.getSquareScale());
                switch (tempID) {

                    case Wall:
                        grid[i][j] = new Wall(vector, Vector2.ZERO, new Sprite("../bg/wall"));
                        break;

                    case Blood:
                        grid[i][j] = new Blood(vector);
                        break;

                    case SpawnPlayer:
                        grid[i][j] = new Player(vector, Vector2.ZERO,
                            new AnimationSprite("/friendlies/character", 4));
                        break;

                    //KEYS AND DOORS
                    case Key:
                        grid[i][j] = new Key(vector, Vector2.ZERO, new Sprite("./items/key_gold00"),
                            0);
                        break;

                    case KeyY:
                        grid[i][j] = new Key(vector, Vector2.ZERO, new Sprite("./items/key_gold00"),
                            1);
                        break;

                    case KeyB:
                        grid[i][j] = new Key(vector, Vector2.ZERO, new Sprite("./items/key_gold00"),
                            2);
                        break;

                    case KeyG:
                        grid[i][j] = new Key(vector, Vector2.ZERO, new Sprite("./items/key_gold00"),
                            3);
                        break;

                    case KeyP:
                        grid[i][j] = new Key(vector, Vector2.ZERO, new Sprite("./items/key_gold00"),
                            4);
                        break;

                    case KeyR:
                        grid[i][j] = new Key(vector, Vector2.ZERO, new Sprite("./items/key_gold00"),
                            5);
                        break;

                    case KeyShip:
                        grid[i][j] = new Key(vector, Vector2.ZERO, new Sprite("./items/key_gold00"),
                            6);
                        break;

                    case Door:
                        grid[i][j] = new Door(vector, Vector2.ZERO, new Sprite("../bg/doorG"), 0);
                        System.out.println("The door ID is 0 this is where it constructs");
                        break;
                    case DoorY:
                        grid[i][j] = new Door(vector, Vector2.ZERO, new Sprite("../bg/doorG"), 1);
                        break;
                    case DoorB:
                        grid[i][j] = new Door(vector, Vector2.ZERO, new Sprite("../bg/doorG"), 2);
                        break;
                    case DoorG:
                        grid[i][j] = new Door(vector, Vector2.ZERO, new Sprite("../bg/doorG"), 3);
                        // System.out.println("This door is also made");
                        break;
                    case DoorP:
                        grid[i][j] = new Door(vector, Vector2.ZERO, new Sprite("../bg/doorG"), 4);
                        break;
                    case DoorR:
                        grid[i][j] = new Door(vector, Vector2.ZERO, new Sprite("../bg/doorG"), 5);
                        break;
                    case Ship:
                        new Door(vector, Vector2.ZERO, new Sprite("../bg/doorG"), 6);
                        grid[i][j] = new Ship(vector, Vector2.ZERO, new Sprite("../bg/ship"));
                        break;

                    // BOXES AND SUCH
                    case StasisBox:
                        grid[i][j] = new StasisBox(vector, Vector2.ZERO,
                            new Sprite("./items/stasisBoxRed"));
                        break;
                    case Crate:
                        grid[i][j] = new Crate(vector, Vector2.ZERO,
                            new Sprite("./items/crate_02"));
                        break;
                    case Bench:
                        grid[i][j] = new Bench(vector, Vector2.ZERO, new Sprite("./items/bench"));
                        break;
                    case Table:
                        grid[i][j] = new Table(vector, Vector2.ZERO, new Sprite("./items/table"));
                        break;
                    case Corpse:
                        grid[i][j] = new Corpse(vector, Vector2.ZERO,
                            new Sprite("./items/corpse_00"));
                        break;
                    case Barrel:
                        grid[i][j] = new Barrel(vector, Vector2.ZERO,
                            new Sprite("./items/barrel_blue"));
                        break;

                    //ENEMIES
                    case AlienGreen:
                        grid[i][j] = new AlienGreen(vector);
                        break;
                    case AlienPurple:
                        grid[i][j] = new AlienPurple(vector);
                        break;
                    case AlienTurret:
                        grid[i][j] = new AlienTurret(vector, Vector2.ZERO);
                        break;
                    case AlienExplode:
                        grid[i][j] = new AlienExplode(vector);
                        break;

                    //WEAPONS
                    case PickupWeaponS:
                        grid[i][j] = new PickupWeapon(vector, Vector2.ZERO,
                            new Sprite("./items/weaponS"), 0);
                        //TODO: Fix sprite so it is the starter weapon
                        break;

                    case PickupWeaponR:
                        grid[i][j] = new PickupWeapon(vector, Vector2.ZERO,
                            new Sprite("./items/weaponR_03"), 1);
                        break;

                    case PickupWeaponY:
                        grid[i][j] = new PickupWeapon(vector, Vector2.ZERO,
                            new Sprite("./items/weaponY"), 2);
                        break;

                    default:
                        //grid[i][j] = new Tile(vector, Vector2.ZERO, new Sprite("../bg/tile"));
                        break;
                }

            }
        }
    }

}