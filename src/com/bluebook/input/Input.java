package com.bluebook.input;

import com.bluebook.engine.GameApplication;
import com.bluebook.util.Vector2;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import com.bluebook.engine.GameEngine;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.security.Key;
import java.util.ArrayList;

/**
 * Singelton class to be used  for handeling input on the logic thread
 */
public class Input {

    private static Input singelton;

    public ArrayList<KeyCode> input = new ArrayList<KeyCode>();
    public ArrayList<KeyCode> input_once = new ArrayList<>();
    boolean mouseButton0 = false;
    boolean mouseButton1 = false;
    boolean mouseOnScreen = false;
    double mouse_X = 0.0, mouse_y = 0.0;

    public static boolean DEBUG = false;

    public Input(Stage stage){
        singelton = this;
        setEventHandlers(stage);
    }

    /**
     * Will return the pressed state of keycode
     * @param code Key to check
     * @return is pressed
     */
    public boolean isKeyDown(KeyCode code){
        if(input.contains(code)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Will return if a key is pressed, and then remove it to avoid multiple presses
     * @param code Key To check
     * @return
     */
    public boolean isKeyPressed(KeyCode code){
        if(input_once.contains(code)){
            input_once.remove(code);
            return true;
        }else{
            return false;
        }
    }
    /**
     * This will return the pressed state of the primary mouse button
     * @return
     */
    public boolean isMouseButton0Pressed(){
        if(mouseButton0){
            mouseButton0 = false;
            return true;
        }
        return false;
    }

    /**
     * This will return the pressed state of the secondary mouse button
     * @return
     */
    public boolean isMouseButton1Pressed(){
        if(mouseButton1){
            mouseButton1 = false;
            return true;
        }
        return false;
    }

    /**
     * Singelton Getter
     * @return
     */
    public static Input getInstance(){
        return singelton;
    }

    private void setEventHandlers(Stage stage){
        stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if(Input.DEBUG){
                    System.out.println("KEY PRESSED " + event.getCode());
                }
                keyPressed(event);
                if(event.getCode() == KeyCode.F1){
                    GameEngine.DEBUG = !GameEngine.DEBUG;
                } else if(event.getCode() == KeyCode.ESCAPE){
                    try {
                        GameApplication.getInstance().stop();
                        Platform.exit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Freeze stuff
                if(event.getCode() ==  KeyCode.F2){
                    GameEngine engine = GameEngine.getInstance();
                    if(engine.isPaused()){
                        engine.unPause();
                    }else {
                        engine.Pause();
                    }
                }
            }
        });

        stage.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(Input.DEBUG){
                    System.out.println("KEY RELEASED " + event.getCode());
                }
                keyReleased(event);
            }
        });

        stage.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setMousePosition(event.getX(), event.getY());
            }
        });

        stage.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(Input.DEBUG){
                    System.out.println("MOUSE PRESSED " + event.getButton());
                }
                if(event.isPrimaryButtonDown()){
                    setMouseButton0State(true);
                }else if(event.isSecondaryButtonDown()){
                    setMouseButton1State(true);
                }
            }
        });

        stage.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(Input.DEBUG){
                    System.out.println("MOUSE Released " + event.getButton());
                }
                if(event.isPrimaryButtonDown()){
                    setMouseButton0State(false);
                }else if(event.isSecondaryButtonDown()){
                    setMouseButton1State(false);
                }
            }
        });
    }

    private void mouseEnter(){
        mouseOnScreen = true;
    }

    private void mouseExit(){
        mouseOnScreen = false;
    }

    private void setMouseButton0State(boolean state){
        mouseButton0 = state;
    }

    private void setMouseButton1State(boolean state){
        mouseButton1 = state;
    }

    private void setMousePosition(double X, double Y){
        this.mouse_X = X;
        this.mouse_y = Y;
    }

    private boolean isMouseOnScreen(){
        return mouseOnScreen;
    }

    /**
     * This will return the position of the mouse on screen
     * @return
     */
    public Vector2 getMousePosition(){
        return new Vector2(mouse_X, mouse_y);
    }

    private void keyPressed(KeyEvent event){
        KeyCode pressed = event.getCode();
        if(!input.contains(pressed)){
            input.add(pressed);
        }

        if(!input_once.contains(pressed)){
            input_once.add(pressed);
        }
    }

    private void keyReleased(KeyEvent event){
        KeyCode released = event.getCode();
        if(input.contains(released)){
            input.remove(released);
        }

        if(input_once.contains(released)){
            input_once.remove(released);
        }

    }



}
