package com.rominntrenger.main.FSM;

//FSM
public class FSM {

    public enum States {wander, dodge, escape, shootPlayer, chargePlayer}

    private States activeState;

    // TODO: Pål, fix this error \|/
    //Method method = obj.getClass().getMethod("methodName" + activeState);
    public FSM() {
    }

    public void setState(States state) {
        activeState = state;
    }

    public void update() {
        if (activeState != null) {
            //call the state stored in activeState
//            method.invoke(); // TODO: This is also failed

        } else {
            activeState = States.wander;
        }

    }

}
