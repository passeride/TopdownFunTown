package com.rominntrenger.objects.FSM;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameApplication;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.objects.enemy.Enemy;
import com.rominntrenger.objects.player.Player;

public class Attack implements Behaviour {

    public static int position = 1;
    boolean hasFled = false;
    boolean hasScreamed = false;

    @Override
    public void nextBehaviour(Enemy behaviourContext) {

        if(!hasScreamed){
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
        if(player != null) {
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
