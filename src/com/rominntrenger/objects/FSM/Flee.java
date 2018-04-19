package com.rominntrenger.objects.FSM;

import com.rominntrenger.objects.enemy.Enemy;

public class Flee implements Behaviour {

    @Override
    public void nextBehaviour(Enemy behaviourContext) {
        double speed;
        double delta;

        speed = behaviourContext.getSpeed();
        delta = behaviourContext.getDelta();

        behaviourContext.setBehaviour(new Wander());
    }
}
