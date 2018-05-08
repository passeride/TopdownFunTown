package com.bluebook.input;

import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import java.util.ArrayList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Singelton class to be used  for handeling input on the logic thread
 */
public class Input {

    private static Input singleton;

    private ArrayList<KeyCode> input;
    private ArrayList<KeyCode> input_once;
    private boolean mouseButton0 = false;
    private boolean mouseButton1 = false;
    private boolean mouseButtonDown0 = false;
    private boolean mouseButtonDown1 = false;
    private double mouse_X = 0.0, mouse_y = 0.0;

    public Input(Stage stage) {
        singleton = this;
        setEventHandlers(stage);
        input = new ArrayList<>();
        input_once = new ArrayList<>();
    }



    /**
     * Will return the pressed state of keycode
     *
     * @param code Key to check
     * @return is pressed
     */
    public boolean isKeyDown(KeyCode code) {
        return input.contains(code);
    }

    /**
     * Will return if a key is pressed, and then remove it to avoid multiple presses
     *
     * @param code Key To check
     */
    public boolean isKeyPressed(KeyCode code) {
        if (input_once.contains(code)) {
            input_once.remove(code);
            return true;
        } else {
            return false;
        }
    }



    public boolean isMouseButton0(){
        return mouseButtonDown0;
    }

    /**
     * This will return the pressed state of the primary mouse button
     */
    public boolean isMouseButton0Pressed() {
        if (mouseButton0) {
            mouseButton0 = false;
            return true;
        }
        return false;
    }

    /**
     * This will return the pressed state of the secondary mouse button
     */
    public boolean isMouseButton1Pressed() {
        if (mouseButton1) {
            mouseButton1 = false;
            return true;
        }
        return false;
    }

    /**
     * Singelton Getter
     */
    public static Input getInstance() {
        return singleton;
    }

    private void setEventHandlers(Stage stage) {
        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (GameEngine.DEBUG) {
                System.out.println("KEY PRESSED " + event.getCode());
            }
            keyPressed(event);
            if (event.getCode() == KeyCode.F1) {
                GameEngine.DEBUG = !GameEngine.DEBUG;
            }
            // Freeze stuff
            if (event.getCode() == KeyCode.F2) {
                GameEngine engine = GameEngine.getInstance();
                if (engine.isPaused()) {
                    engine.resumeGame();
                } else {
                    engine.pauseGame();
                }
            }
        });

        stage.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (GameEngine.DEBUG) {
                System.out.println("KEY RELEASED " + event.getCode());
            }
            keyReleased(event);
        });

        stage.addEventHandler(MouseEvent.ANY, event -> {
            setMousePosition(event.getX(), event.getY());
            if(event.getEventType() == MouseEvent.MOUSE_PRESSED){
                if (GameEngine.DEBUG) {
                    System.out.println("MOUSE PRESSED " + event.getButton());
                }
                if (event.isPrimaryButtonDown()) {
                    setMouseButton0State(true);
                } else if (event.isSecondaryButtonDown()) {
                    setMouseButton1State(true);
                }
            }else if(event.getEventType() == MouseEvent.MOUSE_RELEASED){
                if (GameEngine.DEBUG) {
                    System.out.println("MOUSE Released " + event.getButton());
                }
                if (!event.isPrimaryButtonDown()) {
                    setMouseButton0State(false);
                } else if (!event.isSecondaryButtonDown()) {
                    setMouseButton1State(false);
                }
            }
        });

    }

    private void setMouseButton0State(boolean state) {
        mouseButtonDown0 = state;
        mouseButton0 = state;
    }

    private void setMouseButton1State(boolean state) {
        mouseButtonDown0 = state;
        mouseButton1 = state;
    }

    private void setMousePosition(double X, double Y) {
        this.mouse_X = mapMouseToCanvas(
            GameSettings.getInt("game_resolution_X"),
            GameApplication.getInstance().getScreenWidth(), X);
        this.mouse_y = mapMouseToCanvas(GameSettings.getInt("game_resolution_Y"),
            GameApplication.getInstance().getScreenHeight(), Y);
    }

    private double mapMouseToCanvas(double output_end,
        double input_end, double input) {
        return (double) 0 + ((output_end - (double) 0) / (input_end - (double) 0)) * (input
            - (double) 0);
    }

    /**
     * This will return the position of the mouse on screen
     */
    public Vec2 getMousePosition() {
        return new Vec2(mouse_X, mouse_y);
    }

    private void keyPressed(KeyEvent event) {
        KeyCode pressed = event.getCode();
        if (!input.contains(pressed)) {
            input.add(pressed);
        }

        if (!input_once.contains(pressed)) {
            input_once.add(pressed);
        }
    }

    private void keyReleased(KeyEvent event) {
        KeyCode released = event.getCode();
        if (input.contains(released)) {
            input.remove(released);
        }

        if (input_once.contains(released)) {
            input_once.remove(released);
        }

    }


}
