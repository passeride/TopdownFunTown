package com.bluebook.input;

import com.bluebook.util.Vector2;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import com.bluebook.engine.GameEngine;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Singelton class to be used  for handeling input on the logic thread
 */
public class Input {

    private static Input singelton;

    public ArrayList<KeyCode> input = new ArrayList<KeyCode>();
    boolean mouseButton0 = false;
    boolean mouseButton1 = false;
    boolean mouseOnScreen = false;
    double mouse_X = 0.0, mouse_y = 0.0;

    public Input(Stage stage){
        singelton = this;
        setEventHandlers(stage);
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
                if(GameEngine.DEBUG){
                    System.out.println("KEY PRESSED " + event.getCode());
                }
                keyPressed(event);
            }
        });

        stage.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(GameEngine.DEBUG){
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
                if(GameEngine.DEBUG){
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
                if(GameEngine.DEBUG){
                    System.out.println("MOUSE Released " + event.getButton());
                }
                if(event.isPrimaryButtonDown()){
                    setMouseButton0State(false);
                }else if(event.isSecondaryButtonDown()){
                    setMouseButton1State(false);
                }
            }
        });

        stage.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("MOUSE ENTERED");
                mouseEnter();
            }
        });

        stage.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("MOUSE EXITED");
                mouseExit();
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
    }

    private void keyReleased(KeyEvent event){
        KeyCode released = event.getCode();
        if(input.contains(released)){
            input.remove(released);
        }
    }

    /**
     * Will return the pressed state of keycode
     * @param code Key to check
     * @return is pressed
     */
    public boolean isPressed(KeyCode code){
        if(input.contains(code)){
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
        return mouseButton0;
    }

    /**
     * This will return the pressed state of the secondary mouse button
     * @return
     */
    public boolean isMouseButton1Pressed(){
        return mouseButton1;
    }

}
