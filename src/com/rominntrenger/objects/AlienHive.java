package com.rominntrenger.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.enemy.AlienExplode;
import com.rominntrenger.objects.enemy.AlienGreen;
import com.rominntrenger.objects.enemy.AlienPurple;
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
        isActive = false;
        health = 0;

        collider.setOnCollisionListener(null);

    }



    public void spawn(int i){
        if(isActive) {
            if (System.currentTimeMillis() - previousSPawn > spawnRate * 1000.0
                && Enemy.allEnemies.size() < max_enemies) {
                if(i <= 2) {
                    new AlienGreen(getPosition());
                }else  if(i <= 5){
                    if(Math.random() >= 0.5){
                        new AlienGreen(getPosition());
                    }else{
                        new AlienPurple(getPosition());
                    }
                }else{
                    new AlienGreen(getPosition());
                    new AlienPurple(getPosition());
                    new AlienExplode(getPosition());
                }
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

    public void reset(){
        isActive = true;
        health = max_health;
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

}
