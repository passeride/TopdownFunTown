package com.rominntrenger.main;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.camera.OrthographicCamera;
import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.input.GamepadInput;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.Randomizer;
import com.rominntrenger.gui.DeathOverlay;
import com.rominntrenger.gui.Menu;
import com.rominntrenger.maploader.MapCreator;
import com.rominntrenger.messageHandling.MessageHandler;
import com.rominntrenger.objects.Explotion;
import com.rominntrenger.objects.PlayerSpawn;
import com.rominntrenger.objects.Projectile;
import com.rominntrenger.objects.WaveManager;
import com.rominntrenger.objects.blocks.Blood;
import com.rominntrenger.objects.blocks.Soot;
import com.rominntrenger.objects.enemy.EnemyRandomizerToken;
import com.rominntrenger.objects.enemy.EnemyRandomizerToken.EnemyType;
import com.rominntrenger.objects.item.Item;
import com.rominntrenger.objects.item.ItemRandomizerToken;
import com.rominntrenger.objects.item.ItemRandomizerToken.ItemType;
import com.rominntrenger.objects.player.Player;
import com.rominntrenger.objects.weapon.*;
import com.rominntrenger.stateHandling.SaveStateLoader;
import com.rominntrenger.stateHandling.SaveStateSaver;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Main game class that handles the game
 */
public class RomInntrenger extends GameApplication {
    private OrthographicCamera cam;
    public CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();

    private int prevWaveNumber = 0;

    private AudioPlayer bgMusic;
    private AudioPlayer evilLaughAP;

    public Randomizer<ItemRandomizerToken> addRandomItem;
    public Randomizer<EnemyRandomizerToken> addRandomEnemy;
    private Menu menu;

    /**
     * Will give players colors corresponding to their PlayerID
     */
    private Color[] playerColor = {
        Color.PINK, Color.GREENYELLOW, Color.CYAN, Color.ORANGE
    };

    /**
     * Gives players different sprite colors.
     */
    public String[] playerSprites = {
        "friendlies/character",
        "friendlies/characterCute",
        "friendlies/characterGreen",
        "friendlies/wahracter"
    };

    private DeathOverlay deathOverlay;
    private GamepadInput gi;

    @Override
    public void start(Stage primaryStage) {
        loadSettings();
        this.primaryStage = primaryStage;

        Pane root = new Pane();
        root.setPrefSize(1920, 1080);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        if (GameSettings.getBoolean("start_menu")) {
            menu = new Menu(primaryStage);
            menu.callMenu();
        }
        primaryStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.F));
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        setUp();
        spawnPlayers();
        WaveManager.getInstance();

    }

    private void callMenu() {
        GameEngine.getInstance().pauseGame();
        GameApplication.getInstance().getStage().getScene().setRoot(menu.getRoot());
    }

    /**
     * Will do some one time configuring
     */
    private void setUp() {
        cam = new OrthographicCamera();

        MapCreator level = new MapCreator("map_0");
        level.createLevel();

        bgMusic = new AudioPlayer("audio/MoodyLoop.wav");
        bgMusic.playLoop();

        MessageHandler msh = MessageHandler.getInstance();

        gi = new GamepadInput();

        createEnemyRandomizer();
        createItemRandomizer();
    }

    /**
     * Will spawn one player if 1 or no gamepads is connected
     * And multiple if multiple gamepads are connectetd! o.0
     */
    private void spawnPlayers() {
        if (gi.getNumberOfControllers() > 0) {
            for (int i = 0; i < gi.getNumberOfControllers(); i++) {
                Player p = new Player(PlayerSpawn.position, Vec2.ZERO,
                    new AnimationSprite(playerSprites[i], 4), i + 1);
                p.setCurrentWeapon(new Weapon(Vec2.ZERO,
                    new AnimationSprite("friendlies/weaponR", 2), Vec2.ZERO));
                p.setPlayerColor(playerColor[i]);

            }
        } else {
            Player p = new Player(PlayerSpawn.position, Vec2.ZERO,
                new AnimationSprite("friendlies/character", 4), 0);
            p.setCurrentWeapon(new Weapon(Vec2.ZERO,
                new AnimationSprite("friendlies/weaponR", 2), Vec2.ZERO));
            p.setPlayerColor(playerColor[0]);

        }
    }

    /**
     * Used when restoring from file
     */
    public void clearGameState() {
        for (Player p : players) {
            p.destroy();
        }
        players.clear();
        Projectile.clearAllProjectiles();
        Explotion.clearAllExplotions();
        Item.removeAllItems();
        Soot.clearAll();
        WaveManager.getInstance().clearGamestate();
        Blood.clearAll();
        cam.setX(0);
        cam.setY(0);
        if (evilLaughAP != null) {
            evilLaughAP.stop();
            evilLaughAP = null;
        }

    }

    /**
     * Updates for the screen
     * @param delta seconds since last frame update
     */
    @Override
    public void update(double delta) {
        cam.update(delta);
        gi.pullEvents();

        /**
         * Checks to see if the Camera Holding player is still alive.
         */
        if (!cam.getGameobject().isAlive() && players.size() > 0) {
            cam.follow(players.get(0));
        }

        /**
         * Checks to see if one or more controllers are connected.
         * Connects movement to controller(s) if they are.
         */
        for (Player player : players) {
            AnimationSprite animSprite = ((AnimationSprite) player.getSprite());
            if (gi.getNumberOfControllers() > 0) {
                player.setUses_controller(true);

                int playerID = player.getPlayerID() - 1;
                if (gi.getLeftJoistick(playerID).getMagnitude() > 0.01) {
                    animSprite.setPlaying(true);
                } else {
                    animSprite.setPlaying(false);
                }
                player.move(gi.getLeftJoistick(playerID), delta);

                if (gi.getRightJoystick(playerID).getMagnitude() > 0.01) {
                    player.lookInDirection(gi.getRightJoystick(playerID));
                } else if (gi.getLeftJoistick(playerID).getMagnitude() > 0.01) {
                }

                if (player.hasWeapon()) {
                    AnimationSprite weaponAnim = ((AnimationSprite) player.getCurrentWeapon().getSprite());
                    if (gi.isShoot(playerID)) {
                        weaponAnim.setPlaying(true);

                        player.shoot();
                    } else if (gi.isReload(playerID)) {
                        player.reloadCurrentWeapon();
                        weaponAnim.setPlaying(false);

                    } else {
                        weaponAnim.setPlaying(false);
                    }
                }

            /**
             * Maps player movement to the keyboard if there are no controllers.
             */
            } else if (players.indexOf(player) == 0) {
                AnimationSprite animationSprite = ((AnimationSprite) player.getCurrentWeapon().getSprite());

                if (player.isAlive()) {
                    if (WaveManager.getInstance().getWaveNumber() != prevWaveNumber) {
                        prevWaveNumber = WaveManager.getInstance().getWaveNumber();
                        if(prevWaveNumber == 0 || prevWaveNumber == 1 ){

                        }else{
                            SaveStateSaver.save(this);
                        }
                    }

                    if (input.isKeyDown(KeyCode.S) || input.isKeyDown(KeyCode.W) || input
                        .isKeyDown(KeyCode.A)
                        || input.isKeyDown(KeyCode.D)) {
                        animSprite.setPlaying(true);
                    } else {
                        animSprite.setPlaying(false);
                    }

                    if (input.isKeyDown(KeyCode.S)) {
                        player.moveDown(delta);
                    }

                    if (input.isKeyDown(KeyCode.W)) {
                        player.moveUp(delta);
                    }

                    if (input.isKeyDown(KeyCode.D)) {
                        player.moveRight(delta);
                    }
                    if (input.isKeyDown(KeyCode.A)) {
                        player.moveLeft(delta);
                    }
                    if (input.isKeyPressed(KeyCode.ESCAPE)) {
                        callMenu();
                    }

                    if (player.hasWeapon()) {
                        if (input.isMouseButton0()) {
                            animationSprite.setPlaying(true);
                            if (player.getCurrentWeapon() != null) {
                                if (input.isMouseButton0Pressed()) {
                                    animationSprite.setPlaying(true);

                                    player.shoot();
                                } else {
                                    animationSprite.setPlaying(false);
                                }
                            }
                            player.shoot();
                        } else if (input.isMouseButton1Pressed()) {
                            player.reloadCurrentWeapon();
                        }

                    }

                    /**
                     * Turns the Player Character around to where they look.
                     */
                    if (OrthographicCamera.main != null) {
                        player.lookAt(Vec2.subtract(input.getMousePosition(),
                            new Vec2(OrthographicCamera.main.getX(),
                                OrthographicCamera.main.getY())));
                    } else {
                        player.lookAt(input.getMousePosition());
                    }
                } else if (!player.isAlive()) {

                }
            }

        }


        if (input.isKeyPressed(KeyCode.UP)) {
            SaveStateSaver.save(this);
        }

        if (input.isKeyPressed(KeyCode.DOWN)) {
            SaveStateLoader.loadPreviousSave(this);
        }

        /**
         * Opens the Death Overlay if all players are dead.
         */
        if (players.size() == 0) {

            if (deathOverlay == null) {
                deathOverlay = new DeathOverlay();
                if (evilLaughAP == null) {
                    evilLaughAP = new AudioPlayer("audio/Evil_Laugh.wav");
                    evilLaughAP.playOnce();
                    bgMusic.stop();
                }
            }
            if (input.isKeyPressed(KeyCode.P)) {
                SaveStateLoader.loadPreviousSave(this);
                deathOverlay.destroy();
                deathOverlay = null;
                bgMusic.playLoop();


            } else if (input.isKeyPressed(KeyCode.ESCAPE)) {
                Platform.runLater(() -> primaryStage.close());

            }
        }

    }

    public CopyOnWriteArrayList<Player> getPlayers() {
        return players;
    }


    public Player getClosestPlayer(Vec2 relativePoint) {

        if (players.size() == 0) {
            return null;
        }

        Player ret = players.get(0);
        double minDistance = ret.getTransform().getGlobalPosition().distance(relativePoint);

        for (Player p : players) {
            double newDist = p.getTransform().getGlobalPosition().distance(relativePoint);
            if (newDist < minDistance) {
                ret = p;
                minDistance = newDist;
            }
        }

        return ret;
    }

    /**
     * Adds all enemies to the randomizer
     */
    public void createEnemyRandomizer() {
        addRandomEnemy = new Randomizer<>();
        addRandomEnemy.addElement(6, new EnemyRandomizerToken(EnemyType.ALIEN_GREEN));
        addRandomEnemy.addElement(4, new EnemyRandomizerToken(EnemyType.ALIEN_PURPLE));
        addRandomEnemy.addElement(8, new EnemyRandomizerToken(EnemyType.AlIEN_ZOMBIE));
        addRandomEnemy.addElement(1, new EnemyRandomizerToken(EnemyType.ALIEN_EXPLODE));
        addRandomEnemy.addElement(2, new EnemyRandomizerToken(EnemyType.ALIEN_EYE));
        addRandomEnemy.addElement(1, new EnemyRandomizerToken(EnemyType.ALIEN_GLOW));
        addRandomEnemy.addElement(3, new EnemyRandomizerToken(EnemyType.ALIEN_WORM));
    }

    /**
     * Adds all items to the randomizer.
     */
    public void createItemRandomizer() {
        WeaponComponentHolderDAO stuff = WeaponComponentGSONHandler.loadTest();
        List<WeaponClip> clips = stuff.clips;

        addRandomItem = new Randomizer<>();


        addRandomItem.addElement(5, new ItemRandomizerToken(ItemType.HEAL_BIG));
        addRandomItem.addElement(1, new ItemRandomizerToken(ItemType.HEAL_SMALL));

        for (WeaponClip clip : stuff.clips) {
            addRandomItem.addElement(4, new ItemRandomizerToken(clip));
        }

        for (WeaponBase base : stuff.bases) {
            addRandomItem.addElement(4, new ItemRandomizerToken(base));
        }

        for (WeaponBarrel barrel : stuff.barrels) {
            addRandomItem.addElement(4, new ItemRandomizerToken(barrel));
        }


    }

}