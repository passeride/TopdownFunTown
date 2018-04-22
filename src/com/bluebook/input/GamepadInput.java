package com.bluebook.input;

import com.bluebook.util.Vec2;
import java.util.ArrayList;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class GamepadInput {

    public Vec2 leftJoistick = new Vec2(0, 0);
    public Vec2 rightJoistick = new Vec2(0, 0);
    public boolean shoot = false;

    private ArrayList<Controller> controllers = new ArrayList<>();

    public GamepadInput() {

        Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();

        for (int i = 0; i < ca.length; i++) {

            if (ca[i].getName().equals("Wireless Controller")) {
                controllers.add(ca[i]);
            }
            /* Get the name of the controller */
            System.out.println(ca[i].getName());
            System.out.println(ca[i].getType().toString());
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
                        leftJoistick.setX(event.getValue());
                        break;
                    case "y":
                        leftJoistick.setY(event.getValue());
                        break;
                    case "rx":
                        rightJoistick.setX(event.getValue());
                        break;
                    case "ry":
                        rightJoistick.setY(event.getValue());
                        break;
                    case "rz":
                        float value = event.getValue();
                        if (value > -0.8) {
                            shoot = true;
                            con.getRumblers()[0].rumble(1);
                        } else {
                            shoot = false;
                            con.getRumblers()[0].rumble(0);

                        }
                }

//                    System.out.println(comp.getName().toString());

                StringBuffer buffer = new StringBuffer(con.getName());
                buffer.append(" at ");
                buffer.append(event.getNanos()).append(", ");
                buffer.append(comp.getName()).append(" changed to ");
                float value = event.getValue();
                if (comp.isAnalog()) {
                    buffer.append(value);
                } else {
                    if (value == 1.0f) {
                        buffer.append("On");
                    } else {
                        buffer.append("Off");
                    }
                }
                System.out.println(buffer.toString());
            }

        }
    }
}
