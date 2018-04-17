package com.rominntrenger.main.FSM;

import java.lang.reflect.Method;

//FSM
public class FSM {
    public enum States{wander,dodge,escape,shootPlayer,chargePlayer}
    private States activeState;
    Method method = obj.getClass().getMethod("methodName" + activeState);
    public FSM(){
    }

    public void setState(States state){
        activeState = state;
    }

    public void update(){
        if(activeState != null){
            //call the state stored in activeState
            method.invoke();

        }else{
            activeState = States.wander;
        }

    }

}
