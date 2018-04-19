package com.rominntrenger.main.objects.FSM;

import com.bluebook.engine.GameApplication;
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
        if (behaviourContext != null) {
            Player player = ((RomInntrenger) GameApplication.getInstance()).player;
            double distance2Player = enemy.getPosition().distance(player.getPosition());
            enemy.setTarget(((RomInntrenger) GameApplication.getInstance()).player);
        }

        behaviourContext.setBehaviour(new Flee());
    }
}
