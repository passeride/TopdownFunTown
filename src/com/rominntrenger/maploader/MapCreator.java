package com.rominntrenger.maploader;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.camera.OrthographicCamera;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.maploader.LevelImageLoader;
import com.rominntrenger.objects.Door;
import com.rominntrenger.objects.Key;
import com.rominntrenger.objects.Ship;
import com.rominntrenger.objects.blocks.Barrel;
import com.rominntrenger.objects.blocks.Bench;
import com.rominntrenger.objects.blocks.Blood;
import com.rominntrenger.objects.blocks.Corpse;
import com.rominntrenger.objects.blocks.Crate;
import com.rominntrenger.objects.blocks.PickupWeapon;
import com.rominntrenger.objects.blocks.StasisBox;
import com.rominntrenger.objects.blocks.Table;
import com.rominntrenger.objects.blocks.Wall;
import com.rominntrenger.objects.enemy.AlienExplode;
import com.rominntrenger.objects.enemy.AlienGreen;
import com.rominntrenger.objects.enemy.AlienGreenKey;
import com.rominntrenger.objects.enemy.AlienPurple;
import com.rominntrenger.objects.enemy.AlienTurret;
import com.rominntrenger.objects.health.HealingItem;
import com.rominntrenger.objects.player.Player;
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
    Vec2 vector;

    private String name = "Space Adventures in Space";
    private AudioPlayer soundTrack;
    private ID tempID;

    public MapCreator(String path) {
        super(Vec2.ZERO, Vec2.ZERO, new Sprite("../bg/tilebg02"));
        allwaysOnScreen = true;
        ImageBuffering loader = new ImageBuffering();
        this.newMap = loader.loadImage(path);
        setRenderLayer(RenderLayer.RenderLayerName.BACKGROUND);
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Will draw groundtiles only where camera sees
        // To avoid some tearing issues

        Vec2 offset = OrthographicCamera.getOffset();
        Vec2 squareSize = GameSettings.getSquareScale();
        Vec2 screen = GameSettings.getScreen();
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

    /**
     * Creates objects based on the IDs given in the {@link LevelImageLoader}
     * Also adds it to a new GameObject array.
     */
    public void createLevel() {
        currentLevel = LevelImageLoader.loadLevel(newMap);
        int x = currentLevel.length;
        int y = currentLevel[0].length;
        grid = new GameObject[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {

                tempID = currentLevel[i][j];

                vector = new Vec2(i, j);
                vector = Vec2.multiply(vector, GameSettings.getSquareScale());
                switch (tempID) {

                    case Wall:
                        grid[i][j] = new Wall(vector, Vec2.ZERO, new Sprite("../bg/wall"));
                        break;

                    case Blood:
                        grid[i][j] = new Blood(vector);
                        break;

                    case SpawnPlayer:
                        grid[i][j] = new Player(vector, Vec2.ZERO,
                            new AnimationSprite("/friendlies/character", 4));
                        break;

                    //KEYS AND DOORS
                    case Key:
                        grid[i][j] = new Key(vector, Vec2.ZERO, new Sprite("./items/key"),
                            1);
                    break;

                    case KeyY:
                        grid[i][j] = new Key(vector, Vec2.ZERO, new Sprite("./items/keyY"),
                            2);
                        grid[i][j] = new Crate(vector, Vec2.ZERO, new Sprite("./items/crate_0"));
                    break;

                    case KeyB:
                        grid[i][j] = new Key(vector, Vec2.ZERO, new Sprite("./items/keyB"),
                            3);
                    break;

                    case KeyG:
                        grid[i][j] = new Key(vector, Vec2.ZERO, new Sprite("./items/keyG"),
                            4);
                    break;

                    case KeyP:
                        grid[i][j] = new Key(vector, Vec2.ZERO, new Sprite("./items/keyP"),
                            5);
                    break;

                    case KeyR:
                        grid[i][j] = new Key(vector, Vec2.ZERO, new Sprite("./items/keyR"),
                            6);
                    break;

                    case KeyShip:
                        grid[i][j] = new Key(vector, Vec2.ZERO, new Sprite("./items/key_gold00"),
                            7);
                    break;

                    case Door:
                        if (currentLevel[i][j - 1] == ID.Wall)
                            grid[i][j] = new Door(vector, Vec2.UP, new Sprite("../bg/door"), 1);
                        else if (currentLevel[i][j + 1] == ID.Wall)
                            grid[i][j] = new Door(vector, Vec2.UP, new Sprite("../bg/door"), 1);
                        else
                            grid[i][j] = new Door(vector, Vec2.ZERO, new Sprite("../bg/door"),1);
                    break;
                    case DoorY:
                        if (currentLevel[i][j - 1] == ID.Wall)
                            grid[i][j] = new Door(vector, Vec2.UP, new Sprite("../bg/doorY"), 2);
                        else if (currentLevel[i][j + 1] == ID.Wall)
                            grid[i][j] = new Door(vector, Vec2.UP, new Sprite("../bg/doorY"), 2);
                        else
                            grid[i][j] = new Door(vector, Vec2.ZERO, new Sprite("../bg/doorY"),2);
                    break;
                    case DoorB:
                        if (currentLevel[i][j - 1] == ID.Wall)
                            grid[i][j] = new Door(vector, Vec2.UP, new Sprite("../bg/doorB"), 3);
                        else if (currentLevel[i][j + 1] == ID.Wall)
                            grid[i][j] = new Door(vector, Vec2.UP, new Sprite("../bg/doorB"), 3);
                        else
                            grid[i][j] = new Door(vector, Vec2.ZERO, new Sprite("../bg/doorB"),3);
                    break;
                    case DoorG:
                        if (currentLevel[i][j - 1] == ID.Wall)
                            grid[i][j] = new Door(vector, Vec2.UP, new Sprite("../bg/doorG"), 4);
                        else if (currentLevel[i][j + 1] == ID.Wall)
                            grid[i][j] = new Door(vector, Vec2.UP, new Sprite("../bg/doorG"), 4);
                        else
                            grid[i][j] = new Door(vector, Vec2.ZERO, new Sprite("../bg/doorG"),4);
                    break;
                    case DoorP:
                        if (currentLevel[i][j - 1] == ID.Wall)
                            grid[i][j] = new Door(vector, Vec2.UP, new Sprite("../bg/doorP"), 5);
                        else if (currentLevel[i][j + 1] == ID.Wall)
                            grid[i][j] = new Door(vector, Vec2.UP, new Sprite("../bg/doorP"), 5);
                        else
                            grid[i][j] = new Door(vector, Vec2.ZERO, new Sprite("../bg/doorP"),5);
                    break;
                    case DoorR:
                        if (currentLevel[i][j - 1] == ID.Wall)
                            grid[i][j] = new Door(vector, Vec2.UP, new Sprite("../bg/door"), 6);
                        else if (currentLevel[i][j + 1] == ID.Wall)
                            grid[i][j] = new Door(vector, Vec2.UP, new Sprite("../bg/door"), 6);
                        else
                            grid[i][j] = new Door(vector, Vec2.ZERO, new Sprite("../bg/door"),6);
                    break;
                    case Ship:
                        new Door(vector, Vec2.ZERO, new Sprite("../bg/doorG"), 7);
                        grid[i][j] = new Ship(vector, Vec2.ZERO, new Sprite("../bg/ship"));
                    break;

                    // BOXES AND SUCH
                    case StasisBox:
                        grid[i][j] = new StasisBox(vector, Vec2.ZERO,
                            new Sprite("./items/stasisBoxRed"));
                    break;
                    case Crate:
                        grid[i][j] = new Crate(vector, Vec2.ZERO,
                            new Sprite("./items/crate_0"));
                    break;
                    case Bench:
                        grid[i][j] = new Bench(vector, Vec2.ZERO, new Sprite("./items/bench"));
                    break;
                    case Table:
                        grid[i][j] = new Table(vector, Vec2.ZERO, new Sprite("./items/table"));
                    break;
                    case Corpse:
                        grid[i][j] = new Corpse(vector, Vec2.ZERO,
                            new Sprite("./items/corpse_0"));
                    break;
                    case Barrel:
                        grid[i][j] = new Barrel(vector, Vec2.ZERO,
                            new Sprite("./items/barrel_blue"));
                    break;

                    //ENEMIES
                    case AlienGreen:
                        grid[i][j] = new AlienGreen(vector);
                    break;
                    case AlienGreenKey:
                        grid[i][j] = new AlienGreenKey(vector);
                    break;
                    case AlienPurple:
                        grid[i][j] = new AlienPurple(vector);
                    break;
                    case AlienTurret:
                        grid[i][j] = new AlienTurret(vector, Vec2.ZERO);
                    break;
                    case AlienExplode:
                        grid[i][j] = new AlienExplode(vector);
                    break;

                    //WEAPONS
                    case PickupWeaponS:
                        grid[i][j] = new PickupWeapon(vector, Vec2.ZERO,
                            new Sprite("./items/weaponS"), 0);
                        //TODO: Fix sprite so it is the starter weapon
                    break;

                    case PickupWeaponR:
                        grid[i][j] = new PickupWeapon(vector, Vec2.ZERO,
                            new Sprite("./items/weaponR_03"), 1);
                    break;

                    case PickupWeaponY:
                        grid[i][j] = new PickupWeapon(vector, Vec2.ZERO,
                            new Sprite("./items/weaponY"), 2);
                    break;

                    case HealingItemSmall:
                        grid[i][j] = new HealingItem(vector, Vec2.ZERO, new Sprite("./items/healSmall"), true);
                        break;

                    case HealingItemBig:
                        grid[i][j] = new HealingItem(vector, Vec2.ZERO, new Sprite("./items/healBig"),false);
                    break;

                    default:
                        //grid[i][j] = new Tile(vector, Vec2.ZERO, new Sprite("../bg/tile"));
                    break;
                }

            }
        }
    }

}