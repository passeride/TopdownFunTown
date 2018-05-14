package com.rominntrenger.objects.FSM;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameApplication;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.objects.enemy.Enemy;
import com.rominntrenger.objects.player.Player;

/**
 * A class that implements the interface Behaviour and handles the AI state attack
 */
public class Attack implements Behaviour {

    /**
     * this static integer determines a position in an array that contains positions for behaviours
     */
    public static int position = 1;
    private boolean hasFled = false;
    private boolean hasScreamed = false;


    /**
     * nextBehaviour contains the logics for the AI`s attack state and with this logic decides if it will continue in it`s current behaviour or set another behaviour.
     * @param behaviourContext is the enemy that the FSM works on.
     */
    @Override
    public void nextBehaviour(Enemy behaviourContext) {

        if (!hasScreamed) {
            // scream
            AudioPlayer ap = new AudioPlayer("audio/Alien_scream.wav");
            ap.setVolume(0.2f);
            ap.playOnce();
            hasScreamed = true;
        }

        double speed;
        double delta;
        speed = behaviourContext.getSpeed();
        delta = behaviourContext.getDelta();

        Player player = ((RomInntrenger) GameApplication.getInstance()).getClosestPlayer(
            behaviourContext.getTransform().getGlobalPosition());
        if (player != null) {
            double distance2Player = behaviourContext.getPosition().distance(player.getPosition());
            behaviourContext.setTarget(player);
            behaviourContext
                .setDirection(Vec2.subtract(player.getPosition(), behaviourContext.getPosition()));
            behaviourContext.setSpeed(GameSettings.getDouble("Alien_green_speed"));
            behaviourContext
                .translate(Vec2.multiply(behaviourContext.getDirection().getNormalizedVector(),
                    speed * delta));

            if (distance2Player >= GameSettings.getDouble("Alien_attack_distance")) {
                behaviourContext.setBehaviour(Wander.position);
            }

            if (behaviourContext.getHealth() <= (behaviourContext.getMax_health() / 2)) {
                hasScreamed = false;
                if (!hasFled) {
                    hasFled = true;
                    behaviourContext.setBehaviour(Flee.position);

                } else {
                    behaviourContext.setBehaviour(Attack.position);
                }
            }
        }

    }


}
