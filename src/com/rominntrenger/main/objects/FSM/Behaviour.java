package com.rominntrenger.main.objects.FSM;

import com.rominntrenger.main.objects.enemy.Enemy;

public interface Behaviour {

    void nextBehaviour(Enemy behaviourContext);
}
