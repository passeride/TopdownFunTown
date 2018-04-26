package com.rominntrenger.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.enemy.AlienGreen;
import com.rominntrenger.objects.enemy.Enemy;

public class AlienHive extends GameObject {

    private double spawnRate = 0.1;
    private long previousSPawn = 0l;

    private int max_enemies = 25;

    private int currency = 150;

    public boolean isActive = false;


    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public AlienHive(Vec2 position) {
        super(position, Vec2.ZERO, new Sprite("projectiles/projectileRed"));

        setSize(new Vec2(3, 3));

        WaveManager.hives.add(this);

    }

    public void spawn(){
        if(System.currentTimeMillis() - previousSPawn > spawnRate * 1000.0 && Enemy.allEnemies.size() < max_enemies){
            new AlienGreen(getPosition());
            previousSPawn = System.currentTimeMillis();
        }
    }

    @Override
    public void update(double delta) {
        super.update(delta);

    }
}
