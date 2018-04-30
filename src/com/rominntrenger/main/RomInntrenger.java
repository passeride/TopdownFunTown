package com.rominntrenger.main;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.camera.OrthographicCamera;
import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.input.GamepadInput;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.gui.DeathOverlay;
import com.rominntrenger.gui.HealthElement;
import com.rominntrenger.maploader.MapCreator;
import com.rominntrenger.messageHandling.MessageHandler;
import com.rominntrenger.objects.Explotion;
import com.rominntrenger.objects.PlayerGuiElement;
import com.rominntrenger.objects.PlayerSpawn;
import com.rominntrenger.objects.Projectile;
import com.rominntrenger.objects.WaveManager;
import com.rominntrenger.objects.blocks.Blood;
import com.rominntrenger.objects.player.Player;
import com.rominntrenger.objects.player.RedRifle;
import com.rominntrenger.objects.player.StarterWeapon;
import com.rominntrenger.objects.player.Weapon;
import com.rominntrenger.stateHandling.StateHandling;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class RomInntrenger extends GameApplication {

    public HealthElement healthElement;
    OrthographicCamera cam;
    public CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<>();

    public Weapon currentWeapon;
    private int prevWaveNumber = 0;

    public AudioPlayer bgMusic;
    public AudioPlayer evilLaughAP;
    public Clip clip;

    /**
     * Will give players colors corresponding to their PlayerID
     */
    public Color[] playerColor = {
        Color.RED,Color.GREEN, Color.BLUE, Color.YELLOW
    };

    public String[] playerSprites = {
        "/friendlies/character",
        "/friendlies/characterCute",
        "/friendlies/characterGreen",
        "/friendlies/wahracter"
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

    }

    /**
     * Will do some one time configuring
     */
    void setUp(){
        cam = new OrthographicCamera();

        MapCreator level = new MapCreator("startMap");
        level.createLevel();

        bgMusic = new AudioPlayer("./assets/audio/MoodyLoop.wav");
        clip = bgMusic.getClip();
        bgMusic.playLoop();

        msh = MessageHandler.getInstance();

        gi = new GamepadInput();

        WaveManager.getInstance();
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
                p.setCurrentWeapon(new RedRifle(Vec2.ZERO,
                    new AnimationSprite("/friendlies/weaponR", 2), Vec2.ZERO));
                p.setPlayerColor(playerColor[i]);

                stateHandling = new StateHandling();

            }
        }else{
            Player p = new Player(PlayerSpawn.position, Vec2.ZERO,
                new AnimationSprite("/friendlies/character", 4), 0);
            p.setPlayerColor(playerColor[0]);

            new PlayerGuiElement(p);
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

    }

    @Override
    public void update(double delta) {
        cam.update(delta);
        gi.pullEvents();
        for (Player player : players) {
            if (gi.getNumberOfControllers() > 0) {
                player.setUses_controller(true);

                int playerID = players.indexOf(player);
                if (gi.getLeftJoistick(playerID).getMagnitude() > 0.01) {
                    ((AnimationSprite) player.getSprite()).setPlaying(true);
                } else {
                    ((AnimationSprite) player.getSprite()).setPlaying(false);
                }
                player.move(gi.getLeftJoistick(playerID), delta);

                if (gi.getRightJoistick(playerID).getMagnitude() > 0.01) {
                    player.lookInDirection(gi.getRightJoistick(playerID));
                } else if (gi.getLeftJoistick(playerID).getMagnitude() > 0.01) {
//                    player.lookInDirection(gi.getLeftJoistick(playerID));
                }

                if (player.hasWeapon()) {
                    if (gi.isShoot(playerID)) {
                        ((AnimationSprite) player.getCurrentWeapon().getSprite()).setPlaying(true);

                        player.shoot();
                    } else {
                        ((AnimationSprite) player.getCurrentWeapon().getSprite()).setPlaying(false);
                    }
                }

            } else if (players.indexOf(player) == 0) {
                if (player.isAlive()) {
                    if (WaveManager.getInstance().getWaveNumber() != prevWaveNumber) {
                        System.out.println("lagrer spiller sin posisjon");
                        prevWaveNumber = WaveManager.getInstance().getWaveNumber();
                        double playerPosX = player.getPosition().getX();
                        double playerPosY = player.getPosition().getY();
                        double playerDegrees = player.getTransform().getGlobalRotation()
                            .getAngleInDegrees();
                        stateHandling.saveGame(prevWaveNumber, player.getPlayerHealth(), playerPosX,
                            playerPosY, playerDegrees);
                    }

                    if (input.isKeyDown(KeyCode.S) || input.isKeyDown(KeyCode.W) || input
                        .isKeyDown(KeyCode.A)
                        || input.isKeyDown(KeyCode.D)) {
                        ((AnimationSprite) player.getSprite()).setPlaying(true);
                    } else {
                        ((AnimationSprite) player.getSprite()).setPlaying(false);
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
                            ((AnimationSprite) player.getCurrentWeapon().getSprite())
                                .setPlaying(true);
                            if (player.getCurrentWeapon() != null) {
                                if (input.isMouseButton0Pressed()) {
                                    // TODO: Fix trainwreck
                                    ((AnimationSprite) player.getCurrentWeapon().getSprite())
                                        .setPlaying(true);

                                    player.shoot();
                                } else {
                                    ((AnimationSprite) player.getCurrentWeapon().getSprite())
                                        .setPlaying(false);
                                }
                            }
                            player.shoot();
                        } else {
                            ((AnimationSprite) player.getCurrentWeapon().getSprite())
                                .setPlaying(false);

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
            GameSettings.scale += 0.2;
        }

        if (input.isKeyPressed(KeyCode.DOWN)) {
            GameSettings.scale -= 0.2;
        }

        if(players.size() == 0) {
            //Player player = players.get(0);
            if (deathOverlay == null) {
                deathOverlay = new DeathOverlay();
                if (evilLaughAP == null) {
                    evilLaughAP = new AudioPlayer("./assets/audio/Evil_Laugh.wav");
                    evilLaughAP.playOnce();
                    evilLaughAP.close();
                    bgMusic.stop();
                    bgMusic.close();
                }
            }
            if (input.isKeyPressed(KeyCode.P)) {
                stateHandling.setAllLoadData(stateHandling);
                System.out.println("P trykket");
                //player.setAlive(true);
                deathOverlay.destroy();
                deathOverlay = null;

            } else if (input.isKeyPressed(KeyCode.R)) {

                clearGamestate();
                spawnPlayers();
                deathOverlay.destroy();
                deathOverlay = null;

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

}
