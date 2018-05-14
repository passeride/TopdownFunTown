package com.rominntrenger.objects.FSM;

import com.rominntrenger.objects.enemy.Enemy;

/**
 * interface Behaviour that is inherited by every state in the FSM
 */
public interface Behaviour {

    /**
     * NextBehaviour is an abstract class overridden by every behaviour in the FSM package
     * @param behaviourContext
     */
    void nextBehaviour(Enemy behaviourContext);
}
