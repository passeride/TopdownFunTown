package com.rominntrenger.objects.FSM;

import com.bluebook.engine.GameApplication;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.objects.enemy.Enemy;
import com.rominntrenger.objects.player.Player;


/**
 * A class that implements the interface Behaviour and handles the AI state wander
 */
public class Wander implements Behaviour {

    private long prevRandomMove = 0;
    /**
     * this static integer determines a position in an array that contains positions for behaviours
     */
    public static int position = 0;
    private int health = 30;

    /**
     * nextBehaviour contains the logics for the AI`s wander state and with this logic decides if it will continue in it`s current behaviour or set another behaviour.
     * @param behaviourContext is the enemy that the FSM works on.
     */
    @Override
    public void nextBehaviour(Enemy behaviourContext) {
        double speed = behaviourContext.getSpeed();
        double delta = behaviourContext.getDelta();


        double moveInterval = 0.7;
        if ((System.currentTimeMillis() - prevRandomMove) / 1000 >= moveInterval) {
            prevRandomMove = System.currentTimeMillis();
            behaviourContext.setSpeed(150);
            randomMove(behaviourContext);
        }

        behaviourContext.translate(Vec2.multiply(behaviourContext.getDirection(), speed * delta));
        Player player = ((RomInntrenger) GameApplication.
            getInstance()).getClosestPlayer(behaviourContext.getTransform().getGlobalPosition());
        if (player == null)
            return;
        double distance2Player = behaviourContext.getPosition().distance(player.getPosition());
        if (distance2Player <= GameSettings.getDouble("Alien_attack_distance")) {
            behaviourContext.setBehaviour(Attack.position);
        }

        if ((distance2Player >= GameSettings.getDouble("Alien_attack_distance")) && (
            behaviourContext.getHealth() != health)) {
            health = behaviourContext.getHealth();
            behaviourContext.setBehaviour(Attack.position);
        }


    }

    /**
     * randomMove moves the enemy randomly through vector2 and math to choose a direction
     * @param behaviourContext the enemy that is moved randomly
     */
    private void randomMove(Enemy behaviourContext) {
        double speed;
        double delta;
        speed = behaviourContext.getSpeed();
        delta = behaviourContext.getDelta();

        Vec2 direction = Vec2.Vector2FromAngleInDegrees(Math.random() * 360);
        behaviourContext.setDirection(direction);
        behaviourContext.translate(Vec2.multiply(direction, speed * delta));

    }
}
