package com.rominntrenger.objects.FSM;

import com.bluebook.engine.GameApplication;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.objects.enemy.Enemy;
import com.rominntrenger.objects.player.Player;

public class Flee implements Behaviour {

    public static int position = 2;

    @Override
    public void nextBehaviour(Enemy behaviourContext) {
        double speed;
        double delta;
        speed = behaviourContext.getSpeed();
        delta = behaviourContext.getDelta();

        speed*=1.2;
        Player player = ((RomInntrenger) GameApplication.getInstance()).getClosestPlayer(
            behaviourContext.getTransform().getGlobalPosition());
        double distance2Player = behaviourContext.getPosition().distance(player.getPosition());
        behaviourContext.setTarget(player);
        behaviourContext.setDirection(Vec2.subtract(
            behaviourContext.getPosition(), player.getPosition()));
        behaviourContext
            .translate(Vec2.multiply(behaviourContext.getDirection().getNormalizedVector(),speed * delta));

       if(distance2Player >= GameSettings.getDouble("Alien_flee_distance")){
           behaviourContext.setBehaviour(Wander.position);
       }



    }
}
