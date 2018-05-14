package com.bluebook.input;

import com.bluebook.engine.GameEngine;
import com.bluebook.util.Vec2;
import net.java.games.input.*;

import java.util.ArrayList;

/**
 * GamepadInput will use JInput to get input from gamepad such as PS4 controller or XBox kontroller
 */
public class GamepadInput {

    private Vec2[] leftJoistick;
    private Vec2[] rightJoistick;
    private Boolean[] shoot;
    private Boolean[] reload;
    private double deadZone = 0.1;

    private ArrayList<Controller> controllers = new ArrayList<>();

    /**
     * This function will initialize the gamepads, and selecting only the gamepads we have configured
     */
    public GamepadInput() {

        Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();

        for (int i = 0; i < ca.length; i++) {


            if (ca[i].getName().equals("Wireless Controller") ||
                ca[i].getName().equals("Sony Interactive Entertainment Wireless Controller") ||
                ca[i].getName().equals("Gamepad") ||
                ca[i].getName().equals("Controller (XBOX 360 For Windows)")) {
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
        reload = new Boolean[numOfControllers];

        for (int i = 0; i < numOfControllers; i++) {

            leftJoistick[i] = new Vec2(0, 0);
            rightJoistick[i] = new Vec2(0, 0);
            shoot[i] = new Boolean(false);
            reload[i] = new Boolean(false);
        }
    }

    public int getNumberOfControllers() {
        return controllers.size();
    }

    /**
     * This functions will poll events from the gamepads selected and format the input into  format used by the game
     */
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
                    case "Y Axis":
                        double valueY = event.getValue();
                        leftJoistick[i].setY(valueY);
                        break;
                    case "X Axis":
                        double valueX = event.getValue();
                        leftJoistick[i].setX(valueX);
                        break;
                    case "X Rotation":
                        rightJoistick[i].setX(event.getValue());
                        break;
                    case "Y Rotation":
                        rightJoistick[i].setY(event.getValue());
                        break;
                    case "rx":
                        rightJoistick[i].setX(event.getValue());
                        break;
                    case "ry":
                        rightJoistick[i].setY(event.getValue());
                        break;
                    case "Z Axis":
                        float value = event.getValue();
                        shoot[i] = value < -0.8;
                        break;
                    case "rz":
                        value = event.getValue();
                        shoot[i] = value > -0.8;
                        break;
                    case "Button 2":
                        value = event.getValue();
                        reload[i] = value > 0.8;
                        break;
                    case "B":
                        value = event.getValue();
                        reload[i] = value > 0.8;
                        break;
                }
                if (GameEngine.DEBUG) {

                    System.out.println(comp.getName());

                    StringBuffer buffer = new StringBuffer(con.getName());
                    buffer.append(" NUM: " + i + " ");
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

    /**
     * Will return the {@link Vec2} where  the LeftJoistick
     * @param id Number of controller you want vector from
     * @return
     */
    public Vec2 getLeftJoistick(int id) {
        if (id < leftJoistick.length)
            return leftJoistick[id];
        else
            return Vec2.ZERO;
    }

    /**
     * Will return the {@link Vec2} where  the RightJoystick
     * @param id Number of controller you want vector from
     * @return
     */
    public Vec2 getRightJoystick(int id) {
        if (id < leftJoistick.length)
            return rightJoistick[id];
        else
            return Vec2.ZERO;
    }

    /**
     * Will return weather  the  shoot button for gamepadID is down
     * @param id Number of controller you want input from
     * @return
     */
    public boolean isShoot(int id) {
        if (id < shoot.length)
            return shoot[id];
        else
            return false;
    }

    /**
     * Will return weather  the  reload button for gamepadID is down
     * @param id Number of controller you want input from
     * @return
     */
    public boolean isReload(int id) {
        if (id < reload.length)
            return reload[id];
        else
            return false;
    }

}
