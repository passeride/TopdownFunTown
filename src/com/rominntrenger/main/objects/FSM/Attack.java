package com.rominntrenger.main.objects.FSM;

import com.bluebook.engine.GameApplication;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.main.objects.enemy.Enemy;
import com.rominntrenger.main.objects.player.Player;

public class Attack implements Behaviour {

    @Override
    public void nextBehaviour(Enemy behaviourContext) {
        Enemy enemy = behaviourContext;
        double speed;
        double delta;

        speed = enemy.getSpeed();
        delta = enemy.getDelta();
        Player player = ((RomInntrenger) GameApplication.getInstance()).player;
        double distance2Player = enemy.getPosition().distance(player.getPosition());
        if (behaviourContext != null) {
            enemy.setTarget(((RomInntrenger) GameApplication.getInstance()).player);
            enemy.setDirection(Vec2.subtract(player.getPosition(), enemy.getPosition()));
            enemy.setSpeed(450);
            enemy.translate(Vec2.multiply(enemy.getDirection().getNormalizedVector(),speed * delta));

        }
        if(distance2Player >= 750){
            behaviourContext.setBehaviour(new Wander());
        }

//        if(enemy.health <= (enemy.health / 2)){
//            behaviourContext.setBehaviour(new Flee());
//        }

    }
}
