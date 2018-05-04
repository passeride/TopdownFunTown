package com.rominntrenger.main;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.camera.OrthographicCamera;
import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.input.GamepadInput;
import com.bluebook.util.Vec2;
import com.rominntrenger.Randomizer;
import com.rominntrenger.gui.DeathOverlay;
import com.rominntrenger.maploader.MapCreator;
import com.rominntrenger.messageHandling.MessageHandler;
import com.rominntrenger.objects.Explotion;
import com.rominntrenger.objects.PlayerSpawn;
import com.rominntrenger.objects.Projectile;
import com.rominntrenger.objects.WaveManager;
import com.rominntrenger.objects.blocks.Blood;
import com.rominntrenger.objects.blocks.Item;
import com.rominntrenger.objects.enemy.AlienExplode;
import com.rominntrenger.objects.enemy.AlienEye;
import com.rominntrenger.objects.enemy.AlienGlow;
import com.rominntrenger.objects.enemy.AlienGreen;
import com.rominntrenger.objects.enemy.AlienPurple;
import com.rominntrenger.objects.enemy.AlienWorm;
import com.rominntrenger.objects.enemy.AlienZombie;
import com.rominntrenger.objects.enemy.Enemy;
import com.rominntrenger.objects.health.HealingItem;
import com.rominntrenger.objects.player.Player;
import com.rominntrenger.objects.player.RedRifle;
import com.rominntrenger.objects.weapon.Weapon;
import com.rominntrenger.objects.weapon.WeaponClip;
import com.rominntrenger.objects.weapon.WeaponComponentGSONHandler;
import com.rominntrenger.objects.weapon.WeaponComponentHolderDAO;
import com.rominntrenger.stateHandling.SaveStateLoader;
import com.rominntrenger.stateHandling.SaveStateSaver;
import com.rominntrenger.stateHandling.StateHandling;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class RomInntrenger extends GameApplication {

    OrthographicCamera cam;
    public CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();

    private int prevWaveNumber = 0;

    public AudioPlayer bgMusic;
    public AudioPlayer evilLaughAP;

    public Randomizer<Item> addRandomItem;
    public Randomizer<Enemy> addRandomEnemy;

    /**
     * Will give players colors corresponding to their PlayerID
     */
    public Color[] playerColor = {
        Color.RED,Color.GREEN, Color.BLUE, Color.YELLOW
    };

    public String[] playerSprites = {
        "friendlies/character",
        "friendlies/characterCute",
        "friendlies/characterGreen",
        "friendlies/wahracter"
    };

    MessageHandler msh;
    DeathOverlay deathOverlay;
    GamepadInput gi;
    StateHandling stateHandling;

    @Override
    protected void onLoad() {
        super.onLoad();
        setUp();
        spawnPlayers();

        // testing
        WeaponComponentHolderDAO stuff = WeaponComponentGSONHandler.loadTest();
        List<WeaponClip> clips = stuff.clips;

        WaveManager.getInstance();

    }

    /**
     * Will do some one time configuring
     */
    void setUp(){
        cam = new OrthographicCamera();

        MapCreator level = new MapCreator("startMap");
        level.createLevel();

        bgMusic = new AudioPlayer("audio/MoodyLoop.wav");
        bgMusic.playLoop();

        msh = MessageHandler.getInstance();

        gi = new GamepadInput();

        createEnemyRandomizer();
        createItemRandomizer();
}

    /**
     * Will spawn one player if 1 or no gamepads is connected
     * And multiple if multiple gamepads are connectetd! o.0
     */
    void spawnPlayers(){
        if(gi.getNumberOfControllers() > 0) {
            for (int i = 0; i < gi.getNumberOfControllers(); i++) {
                Player p = new Player(PlayerSpawn.position, Vec2.ZERO,
                    new AnimationSprite(playerSprites[i], 4),  i + 1);
                p.setCurrentWeapon(new Weapon(Vec2.ZERO,
                    new AnimationSprite("friendlies/weaponR", 2), Vec2.ZERO));
                p.setPlayerColor(playerColor[i]);

                stateHandling = new StateHandling();
            }
        }else{
            Player p = new Player(PlayerSpawn.position, Vec2.ZERO,
                new AnimationSprite("friendlies/character", 4), 0);
            p.setCurrentWeapon(new Weapon(Vec2.ZERO,
                new AnimationSprite("friendlies/weaponR", 2), Vec2.ZERO));
            p.setPlayerColor(playerColor[0]);

            stateHandling = new StateHandling();
        }
    }

    /**
     * Used when restoring from file
     */
    public void clearGamestate(){
        for(Player p : players){
            p.destroy();
        }
        players.clear();
        Projectile.clearAllProjectiles();
        Explotion.clearAllExplotions();
        WaveManager.getInstance().clearGamestate();
        Blood.clearAll();
        cam.setX(0);
        cam.setY(0);
        if(evilLaughAP != null) {
            evilLaughAP.stop();
            evilLaughAP = null;
        }

    }

    @Override
    public void update(double delta) {
        cam.update(delta);
        gi.pullEvents();
        for (Player player : players) {
            AnimationSprite animSprite = ((AnimationSprite) player.getSprite());
            if (gi.getNumberOfControllers() > 0) {
                player.setUses_controller(true);

                int playerID = players.indexOf(player);
                if (gi.getLeftJoistick(playerID).getMagnitude() > 0.01) {
                    animSprite.setPlaying(true);
                } else {
                    animSprite.setPlaying(false);
                }
                player.move(gi.getLeftJoistick(playerID), delta);

                if (gi.getRightJoystick(playerID).getMagnitude() > 0.01) {
                    player.lookInDirection(gi.getRightJoystick(playerID));
                } else if (gi.getLeftJoistick(playerID).getMagnitude() > 0.01) {
//                    player.lookInDirection(gi.getLeftJoistick(playerID));
                }

                if (player.hasWeapon()) {
                    if (gi.isShoot(playerID)) {
                        animSprite.setPlaying(true);

                        player.shoot();
                    } else if(gi.isReload(playerID)) {
                        player.reloadCurrentWeapon();
                    }else{
                        animSprite.setPlaying(false);
                    }
                }



            } else if (players.indexOf(player) == 0) {
                AnimationSprite animationSprite = ((AnimationSprite) player.getCurrentWeapon().getSprite());

                if (player.isAlive()) {
                    if (WaveManager.getInstance().getWaveNumber() != prevWaveNumber) {
                        prevWaveNumber = WaveManager.getInstance().getWaveNumber();
                        SaveStateSaver.save(this);
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
                        if (input.isMouseButton0Pressed()) {
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
                        }else if(input.isMouseButton1Pressed()){
                                player.reloadCurrentWeapon();
                        }

                    }

                    // Lookat
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
            System.out.println("Players is now (ROM) "  + players.size());
            SaveStateSaver.save(this);
        }

        if (input.isKeyPressed(KeyCode.DOWN)) {
            System.out.println("Players is now (ROM) "  + players.size());
            SaveStateLoader.loadPreviousSave(this);
        }

        if(players.size() == 0) {
            //Player player = players.get(0);
//            System.out.println("ZERO PLAYERS");
            if (deathOverlay == null) {
                deathOverlay = new DeathOverlay();
                if (evilLaughAP == null) {
                    evilLaughAP = new AudioPlayer("audio/Evil_Laugh.wav");
                    evilLaughAP.playOnce();
//                    evilLaughAP.close();
                    bgMusic.stop();
//                    bgMusic.close();
                }
            }
            if (input.isKeyPressed(KeyCode.P)) {
//                stateHandling.setAllLoadData(stateHandling);
                SaveStateLoader.loadPreviousSave(this);
                System.out.println("P trykket");
                //player.setAlive(true);
                deathOverlay.destroy();
                deathOverlay = null;
                bgMusic.playLoop();


            } else if (input.isKeyPressed(KeyCode.R)) {

                clearGamestate();
                spawnPlayers();
                deathOverlay.destroy();
                deathOverlay = null;
                bgMusic.playLoop();

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
        addRandomEnemy.addElement(6, new AlienGreen(Vec2.ZERO));
        addRandomEnemy.addElement(4, new AlienPurple(Vec2.ZERO));
        addRandomEnemy.addElement(8, new AlienZombie(Vec2.ZERO));
        addRandomEnemy.addElement(1, new AlienExplode(Vec2.ZERO));
        addRandomEnemy.addElement(2, new AlienEye(Vec2.ZERO));
        addRandomEnemy.addElement(1, new AlienGlow(Vec2.ZERO));
        addRandomEnemy.addElement(3, new AlienWorm(Vec2.ZERO));
    }

    public void createItemRandomizer() {
        addRandomItem = new Randomizer<>();
        addRandomItem.addElement(5,new HealingItem(Vec2.ZERO,Vec2.ZERO,new Sprite("items/healSmall"),true));
        addRandomItem.addElement(1,new HealingItem(Vec2.ZERO,Vec2.ZERO,new Sprite("items/healBig"),false));
    }

}