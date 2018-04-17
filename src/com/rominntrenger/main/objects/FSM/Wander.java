package com.rominntrenger.main.objects.FSM;

import com.bluebook.engine.GameApplication;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.main.objects.enemy.Enemy;
import com.rominntrenger.main.objects.player.Player;

public class Wander implements Behaviour{

    @Override
    public void nextBehaviour(Enemy behaviourContext) {
        Enemy enemy = behaviourContext;
        double speed;
        double delta;

        speed = enemy.getSpeed();
        delta = enemy.getDelta();
        if(behaviourContext != null){
            Vector2 direction = Vector2.Vector2FromAngleInDegrees(Math.random() * 360);
            enemy.setDirection(direction);
            enemy.translate(Vector2.multiply(direction, speed * delta));
            //make sure enemy is pointing to something
        }
        Player player = ((RomInntrenger)GameApplication.getInstance()).player;
        double distance2Player = enemy.getPosition().distance(player.getPosition());

        if(distance2Player<= 500){
            behaviourContext.setBehaviour(new Attack());
        }

    }
}
