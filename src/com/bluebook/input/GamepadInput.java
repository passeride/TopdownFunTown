package com.bluebook.input;

import com.bluebook.util.Vec2;
import java.lang.reflect.Array;
import java.util.ArrayList;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class GamepadInput {

    private Vec2[] leftJoistick;
    private Vec2[] rightJoistick;
    private Boolean[] shoot;

    private ArrayList<Controller> controllers = new ArrayList<>();

    public GamepadInput() {

        Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();

        for (int i = 0; i < ca.length; i++) {

            // TODO: get a xbox controller and test with that
            if (ca[i].getName().equals("Wireless Controller") || ca[i].getName().equals("Stick")) {
                controllers.add(ca[i]);
            }
            /* Get the name of the controller */
            System.out.println(ca[i].getName());
            System.out.println(ca[i].getType().toString());
        }

        // setting up cache array
        int numOfControllers = controllers.size();
        System.out.println("Number of controllers " + numOfControllers);
        leftJoistick = new Vec2[numOfControllers];
        rightJoistick = new Vec2[numOfControllers];
        shoot = new Boolean[numOfControllers];  

        for(int i = 0; i < numOfControllers; i++){
            leftJoistick[i] = new Vec2(0, 0);
            rightJoistick[i] = new Vec2(0, 0);
            shoot[i] = new Boolean(false);
        }
    }

    public int getNumberOfControllers(){
        return controllers.size();
    }

    public void pullEvents() {

        for (int i = 0; i < controllers.size(); i++) {
//            if (!controllers[i].getName().equals("Wireless Controller Motion Sensors")) {

            Controller con = controllers.get(i);




            con.poll();
            EventQueue queue = con.getEventQueue();
            Event event = new Event();
            while (queue.getNextEvent(event)) {
                Component comp = event.getComponent();

                switch (comp.getName()) {
                    case "x":
                        leftJoistick[i].setX(event.getValue());
                        break;
                    case "y":
                        leftJoistick[i].setY(event.getValue());
                        break;
                    case "rx":
                        rightJoistick[i].setX(event.getValue());
                        break;
                    case "ry":
                        rightJoistick[i].setY(event.getValue());
                        break;
                    case "rz":
                        float value = event.getValue();
                        if (value > -0.8) {
                            shoot[i] = true;
//                            con.getRumblers()[0].rumble(1);
                        } else {
                            shoot[i] = false;
//                            con.getRumblers()[0].rumble(0);
                        }
                }

//                    System.out.println(comp.getName().toString());

//                StringBuffer buffer = new StringBuffer(con.getName());
//                buffer.append( " NUM: " + i + " ");
//                buffer.append(" at ");
//                buffer.append(event.getNanos()).append(", ");
//                buffer.append(comp.getName()).append(" changed to ");
//                float value = event.getValue();
//                if (comp.isAnalog()) {
//                    buffer.append(value);
//                } else {
//                    if (value == 1.0f) {
//                        buffer.append("On");
//                    } else {
//                        buffer.append("Off");
//                    }
//                }
//                System.out.println(buffer.toString());
            }

        }
    }

    public Vec2 getLeftJoistick(int id) {
        if(id < leftJoistick.length)
            return leftJoistick[id];
        else
            return Vec2.ZERO;
    }


    public Vec2 getRightJoistick(int id) {
        if(id < leftJoistick.length)
            return rightJoistick[id];
        else
            return Vec2.ZERO;
    }


    public boolean isShoot(int id) {
        if(id < shoot.length)
            return shoot[id];
        else
            return false;
    }

}
