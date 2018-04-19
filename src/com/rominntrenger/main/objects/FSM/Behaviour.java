package com.rominntrenger.main.objects.FSM;

import com.rominntrenger.main.objects.enemy.Enemy;

/**
 * nextBehaviour updates what behaviour said gameobject is currently at in the update function by using behaviourContext.setBehaviour
 * @param Enemy behaviourContext
 */
public interface Behaviour {

    void nextBehaviour(Enemy behaviourContext);
}
