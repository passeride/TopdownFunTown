package com.rominntrenger.objects.FSM;

import com.bluebook.engine.GameApplication;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.objects.enemy.Enemy;
import com.rominntrenger.objects.player.Player;

public class Wander implements Behaviour {

    @Override
    public void nextBehaviour(Enemy behaviourContext) {
        Enemy enemy = behaviourContext;
        double speed;
        double delta;

        speed = enemy.getSpeed();
        delta = enemy.getDelta();
        if (behaviourContext != null) {
            Vec2 direction = Vec2.Vector2FromAngleInDegrees(Math.random() * 360);
            enemy.setDirection(direction);
            enemy.translate(Vec2.multiply(direction, speed * delta));
            //make sure enemy is pointing to something
        }
        Player player = ((RomInntrenger) GameApplication.getInstance()).player;
        double distance2Player = enemy.getPosition().distance(player.getPosition());

        if (distance2Player <= 500) {
            behaviourContext.setBehaviour(new Attack());
        }

    }
}
