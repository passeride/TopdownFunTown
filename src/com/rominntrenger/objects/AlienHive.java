package com.rominntrenger.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.enemy.AlienEye;
import com.rominntrenger.objects.enemy.AlienGlow;
import com.rominntrenger.objects.enemy.AlienGreen;
import com.rominntrenger.objects.enemy.AlienMother;
import com.rominntrenger.objects.enemy.AlienWorm;
import com.rominntrenger.objects.enemy.AlienZombie;
import com.rominntrenger.objects.enemy.Enemy;

public class AlienHive extends GameObject {

    private double spawnRate = 0.1;
    private long previousSPawn = 0l;

    private int max_enemies = 25;



    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public AlienHive(Vec2 position) {
        super(position, Vec2.ZERO, new Sprite("projectiles/projectileRed"));

        setSize(new Vec2(3, 3));
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        if(System.currentTimeMillis() - previousSPawn > spawnRate * 1000.0 && Enemy.allEnemies.size() < max_enemies){
            //new AlienGreen(getPosition());
            //new AlienWorm(getPosition());
            //new AlienZombie(getPosition());
            //new AlienEye(getPosition());
            // new AlienGlow(getPosition());
            //new AlienMother(getPosition());
            previousSPawn = System.currentTimeMillis();
        }
    }
}
