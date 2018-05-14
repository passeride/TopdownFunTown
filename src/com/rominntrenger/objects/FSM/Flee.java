package com.rominntrenger.objects.FSM;

import com.bluebook.engine.GameApplication;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.objects.enemy.Enemy;
import com.rominntrenger.objects.player.Player;

/**
 * A class that implements the interface Behaviour and handles the AI state flee
 */
public class Flee implements Behaviour {

    /**
     * this static integer determines a position in an array that contains positions for behaviours
     */
    public static int position = 2;

    /**
     * nextBehaviour contains the logics for the AI`s flee state and with this logic decides if it will continue in it`s current behaviour or set another behaviour.
     * @param behaviourContext is the enemy that the FSM works on.
     */
    @Override
    public void nextBehaviour(Enemy behaviourContext) {
        double speed;
        double delta;
        speed = behaviourContext.getSpeed();
        delta = behaviourContext.getDelta();

        speed *= 1.2;
        Player player = ((RomInntrenger) GameApplication.getInstance()).getClosestPlayer(
            behaviourContext.getTransform().getGlobalPosition());
        if (player == null)
            return;
        double distance2Player = behaviourContext.getPosition().distance(player.getPosition());
        behaviourContext.setTarget(player);
        behaviourContext.setDirection(Vec2.subtract(
            behaviourContext.getPosition(), player.getPosition()));
        behaviourContext
            .translate(Vec2.multiply(behaviourContext.getDirection().getNormalizedVector(), speed * delta));

        //check distance
        if (distance2Player >= GameSettings.getDouble("Alien_flee_distance")) {
            behaviourContext.setBehaviour(Wander.position);
        }


    }
}
