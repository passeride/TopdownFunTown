package com.rominntrenger.objects.enemy;


import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.objects.Explotion;
import com.rominntrenger.objects.FSM.Attack;
import java.util.Random;

/**
 * AlienExplode is a small and fast alien that goes for you and goes boom when collider
 */
public class AlienExplode extends Enemy {

    private double shootInterval = 1.8;
    private long prevShot = 0;

    /**
     * Gives the spawn coordinate for AlienExplode
     */
    public AlienExplode(Vec2 position) {
        super(position, Vec2.ZERO, new AnimationSprite("/enemies/alienExplode", 6));
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
        super.nextBehaviour();

        if (super.behaviour instanceof Attack && (System.currentTimeMillis() - prevShot) / 1000 >= shootInterval) {
            prevShot = System.currentTimeMillis();
        }
    }
}
