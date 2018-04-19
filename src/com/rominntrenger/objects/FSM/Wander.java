package com.rominntrenger.objects.FSM;

import com.bluebook.engine.GameApplication;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.objects.enemy.Enemy;
import com.rominntrenger.objects.player.Player;

public class Wander implements Behaviour {

    private double moveInterval = 0.7;
    private long prevRandomMove = 0;


    @Override
    public void nextBehaviour(Enemy behaviourContext) {
        Enemy enemy = behaviourContext;
        double speed = enemy.getSpeed();
        double delta = enemy.getDelta();


        if (behaviourContext != null) {

            if ((System.currentTimeMillis() - prevRandomMove) / 1000 >= moveInterval) {
                prevRandomMove = System.currentTimeMillis();
                enemy.setSpeed(150);
                RandomMove(behaviourContext);
            }

            enemy.translate(Vec2.multiply(enemy.getDirection(), speed * delta));
            Player player = ((RomInntrenger) GameApplication.getInstance()).player;
        double distance2Player = enemy.getPosition().distance(player.getPosition());
        if (distance2Player <= 750) {
            behaviourContext.setBehaviour(new Attack());
        }

    }

    }
    public void RandomMove(Enemy behaviourContext){
        Enemy enemy = behaviourContext;
        double speed = enemy.getSpeed();
        double delta = enemy.getDelta();

        Vec2 direction = Vec2.Vector2FromAngleInDegrees(Math.random() * 360);
        enemy.setDirection(direction);
        enemy.translate(Vec2.multiply(direction, speed * delta));

    }
}
