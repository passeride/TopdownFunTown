package com.rominntrenger.objects;

import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.objects.enemy.AlienWorm;
import com.rominntrenger.objects.enemy.Enemy;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;

public class AlienHive extends Enemy {
    private double spawnRate = GameSettings.getDouble("hiveSpawnRate");
    private int max_enemies = GameSettings.getInt("maxAliensfromHive");
    private long previousSpawn = 0l;
    private boolean isMotherHive = false;
    private boolean isActive = true;
    private int enemyNum = 0;

    public AlienHive(Vec2 position, boolean isMotherHive){
        super(position, Vec2.ZERO, new Sprite("enemies/nestBlueBig_0"));

        this.isMotherHive = isMotherHive;
    }

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public AlienHive(Vec2 position) {

        super(position, Vec2.ZERO, new Sprite("enemies/nestBlueSmall_0"));

        setSize(new Vec2(3, 3));

        WaveManager.hives.add(this);

        max_health = 1000;
        isActive = false;
        health = 0;

        collider.setOnCollisionListener(null);

    }

    public void spawn(int i){
        if(isActive) {
            //TODO: enemyNum-workarounden er weird men ser ut til å funke? Må somehow sette enemyNum til -1 når enemy = destroyed.
            if (System.currentTimeMillis() - previousSpawn > spawnRate * 1000.0
                && enemyNum < max_enemies) {
                // && Enemy.allEnemies.size() < max_enemies) {
                //TODO: This is trainwreck.
                ((RomInntrenger)GameApplication.getInstance()).addRandomEnemy.randomElement().createNew(this.getPosition());
                previousSpawn = System.currentTimeMillis();
                enemyNum++;
            }
        }
    }

    @Override
    public AlienWorm createNew(Vec2 pos) {
        return null;
    }

    @Override
    public void destroy() {
        isActive = false;
        if (isMotherHive)
            setSprite(new Sprite("enemies/nestBlueBig_1"));
        else
            setSprite(new Sprite("enemies/nestBlueSmall_1"));
//        super.destroy();
    }

    public void reset(){
        isActive = true;
        health = max_health;
        if(isMotherHive)
            setSprite(new Sprite("enemies/nestBlueBig_0"));
        else
            setSprite(new Sprite("enemies/nestBlueSmall_0"));
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

    public int getEnemyNum() {
        return enemyNum;
    }

    public void setEnemyNum(int enemyNum) {
        this.enemyNum = enemyNum;
    }
}
