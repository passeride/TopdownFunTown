package com.rominntrenger.objects.FSM;

import com.rominntrenger.objects.enemy.Enemy;

public interface Behaviour {

    void nextBehaviour(Enemy behaviourContext);
}
