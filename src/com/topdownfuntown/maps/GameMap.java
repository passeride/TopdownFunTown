package com.topdownfuntown.maps;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.topdownfuntown.objects.Enemy;
import com.topdownfuntown.objects.Obstacle;
import com.topdownfuntown.objects.Player;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     *
     * @param sprite
     */
    public GameMap(Sprite sprite) {
        super(Vector2.ZERO, Vector2.ZERO, sprite);
        setRenderLayer(RenderLayer.RenderLayerName.BACKGROUND);

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