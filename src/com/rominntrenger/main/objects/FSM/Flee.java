package com.rominntrenger.main.objects.FSM;

import com.rominntrenger.main.objects.enemy.Enemy;

public class Flee implements Behaviour {

    @Override
    public void nextBehaviour(Enemy behaviourContext) {
        Enemy enemy = behaviourContext;
        double speed;
        double delta;

        speed = enemy.getSpeed();
        delta = enemy.getDelta();

        behaviourContext.setBehaviour(new Wander());
    }
}
