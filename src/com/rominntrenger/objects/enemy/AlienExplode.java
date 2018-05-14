package com.rominntrenger.objects.enemy;


import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.objects.Explotion;

import java.util.Random;

/**
 * AlienExplode is a small and fast alien that goes for you and goes boom when collider
 */
public class AlienExplode extends Enemy {

    private double shootInterval = 1.8;
    private long prevShot = 0;
    private long startTime = 0;
    private double timeToExplotion = 3.6;
    private double timePassed = 0.0;
    private double maxSize = 2.0;

    /**
     * Gives the spawn coordinate for AlienExplode
     */
    public AlienExplode(Vec2 position) {
        super(position, Vec2.ZERO, new AnimationSprite("enemies/alienExplode", 6));
        Random r = new Random();
        prevShot = System.currentTimeMillis() + r.nextInt((int) (shootInterval * 1000));
        speed = 500;
        startTime = System.currentTimeMillis();
        dropsBlood = false;
    }

    /**
     * Upon its destruction, it creates an explosion.
     */
    @Override
    public void destroy() {
        new Explotion(getPosition(), getDirection(), getScale(), getDropRate());
        super.destroy();
    }

    public void update(double delta) {
        /**
         * Counts down the time and explodes. Modifies the size with the explosion.
         */
        timePassed += delta;
        if (timePassed >= timeToExplotion)
            destroy();
        double sizeModifier = timePassed / timeToExplotion;
        setSize(new Vec2(sizeModifier * maxSize, sizeModifier * maxSize));

        /**
         * If AlienExplode sees a player, it will run towards it.
         */
        target = ((RomInntrenger) GameApplication.getInstance()).getClosestPlayer(getPosition());
        if (target != null) {
            translate(Vec2.multiply(Vec2.Vector2FromAngleInDegrees(Vec2.getAngleBetweenInDegrees(getPosition(), target.getPosition())), speed * delta));
            setDirection(Vec2.add(getDirection(), Vec2.multiply(Vec2.Vector2FromAngleInDegrees(Vec2.getAngleBetweenInDegrees(getPosition(), target.getPosition())), angularDampening)));
            getDirection().normalize();
        }

    }

    public AlienEye createNew(Vec2 pos) {
        return new AlienEye(pos);
    }

}
