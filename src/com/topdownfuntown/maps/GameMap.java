package com.topdownfuntown.maps;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;
import com.topdownfuntown.main.Topdownfuntown;
import com.topdownfuntown.objects.Door;
import com.topdownfuntown.objects.Enemy;
import com.topdownfuntown.objects.Obstacle;
import com.topdownfuntown.objects.Player;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Creating GameMap/Area with player, enemy and obstacle array lists.
 */
public class GameMap extends GameObject {
    ArrayList<Player> players = new ArrayList<Player>();
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();

    private String name = "";
    private boolean isSafeRoom = false;
    private AudioPlayer soundTrack;
    public Door entry;
    public Door exit;

    boolean isExitTriggered = false;

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     *
     * @param sprite
     */
    public GameMap(Sprite sprite) {
        super(Vector2.ZERO, Vector2.ZERO, sprite);
        setRenderLayer(RenderLayer.RenderLayerName.BACKGROUND);

        //  TODO: Make smart
        entry = new Door(new Vector2(120, GameSettings.getInt("game_resolution_Y") / 2), Vector2.RIGHT);
        entry.getSprite().setSquareHeight(128);
        entry.getSprite().setSquareWidth(128);
        entry.setSize(new Vector2(512, 512));

        exit = new Door(new Vector2(GameSettings.getInt("game_resolution_X") - 128, GameSettings.getInt("game_resolution_Y") / 2), Vector2.LEFT);
        exit.getSprite().setSquareHeight(128);
        exit.getSprite().setSquareWidth(128);
        exit.setSize(new Vector2(128, 128));
        Collider doorCollider = new Collider(exit);
        exit.setCollider(doorCollider);
        doorCollider.addInteractionLayer("UnHittable");
        doorCollider.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                // TODO: make one time only, this is multithread issue
                if(!isExitTriggered) {
                    if(((Topdownfuntown) GameApplication.getInstance()).hasKey) {
                        isExitTriggered = true;
                        ((Topdownfuntown) GameApplication.getInstance()).moveToNextRoom();
                    }
                }
            }
        });


    }


    @Override
    public void destroy() {
        super.destroy();
        for(Enemy e : enemies)
            e.destroy();
        for(Obstacle o : obstacles)
            o.destroy();
    }

    /**
     * Used by {@link com.topdownfuntown.maps.maploader.MapLoader} to add enemies
     * @param e
     */
    public void addEnemy(Enemy e){
        enemies.add(e);
    }

    public void addObstale(Obstacle o){
        obstacles.add(o);
    }

    /**
     * Used by {@link com.topdownfuntown.maps.maploader.MapLoader} to add enemies
     * @param e
     */
    public void addEnemyList(List<Enemy> e){
        enemies.addAll(e);
    }

    @Override
    public void draw(GraphicsContext gc) {
        sprite.drawBackground(gc);
    }

    @Override
    public void update(double detla) {
        super.update(detla);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSafeRoom() {
        return isSafeRoom;
    }

    public void setSafeRoom(boolean safeRoom) {
        isSafeRoom = safeRoom;
    }

    public AudioPlayer getSoundTrack() {
        return soundTrack;
    }

    public void setSoundTrack(AudioPlayer soundTrack) {
        this.soundTrack = soundTrack;
        soundTrack.playLoop();
    }
}