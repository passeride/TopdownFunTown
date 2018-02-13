package com.topdownfuntown.maps;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.topdownfuntown.objects.Enemy;
import com.topdownfuntown.objects.Obstacle;
import com.topdownfuntown.objects.Player;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

/**
 * Creating GameMap/Area with player, enemy and obstacle array lists.
 */
abstract class GameMap extends GameObject {
    ArrayList<Player> players = new ArrayList<Player>();
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
    public GameMap(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
    }

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
    }

    @Override
    public void update(double detla) {
        super.update(detla);
    }
}