package com.rominntrenger.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.enemy.AlienGreen;
import com.rominntrenger.objects.enemy.Enemy;
import javafx.scene.canvas.GraphicsContext;

public class AlienHive extends Enemy {

    private double spawnRate = 0.1;
    private long previousSPawn = 0l;

    private int max_enemies = 25;

    private int currency = 150;


    private boolean isActive = true;

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public AlienHive(Vec2 position) {
        super(position, Vec2.ZERO, new Sprite("projectiles/projectileRed"));

        setSize(new Vec2(3, 3));

        WaveManager.hives.add(this);

        max_health = 1000;
        health = 1000;

    }



    public void spawn(){
        if(isActive) {
            if (System.currentTimeMillis() - previousSPawn > spawnRate * 1000.0
                && Enemy.allEnemies.size() < max_enemies) {
                new AlienGreen(getPosition());
                previousSPawn = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void destroy() {
        isActive = false;
        setSprite(new Sprite("projectiles/projectileGreen"));
//        super.destroy();
    }

    public void resett(){
        setSprite(new Sprite("projectiles/projectileRed"));
    }

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
    }

    @Override
    public void update(double delta) {
//        super.update(delta);

    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
