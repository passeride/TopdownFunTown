package com.rominntrenger.main.objects.enemy;


import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.main.objects.Explotion;

import java.util.Random;

/**
 * AlienExplode is a small and fast alien that goes for you and goes boom when collider
 */
public class AlienExplode extends Enemy {
    private double shootInterval = 1.8;
    private long prevShot = 0;

    /**
     * Gives the spawn coordinate for AlienExplode
     * @param position
     */
    public AlienExplode(Vector2 position) {
        super(position, Vector2.ZERO, new AnimationSprite("/enemies/alienExplode",6));
        Random r = new Random();
        prevShot = System.currentTimeMillis() + r.nextInt((int) (shootInterval * 1000));
        speed = 500;
    }


    @Override
    public void destroy() {
        new Explotion(getPosition());
        super.destroy();
    }

    public void update(double delta) {
        super.update(delta);
        setTarget(((RomInntrenger)GameApplication.getInstance()).player);
    }
}
