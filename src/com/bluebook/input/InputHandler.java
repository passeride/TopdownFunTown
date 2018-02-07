package com.bluebook.input;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import com.bluebook.engine.GameEngine;

public class InputHandler implements EventHandler<KeyEvent>{

    private GameEngine engine;


    public InputHandler(GameEngine engine){
        this.engine = engine;
    }

    /**
     * Will process an incomming keyevent
     * @param event
     */
    @Override
    public void handle(KeyEvent event){
        System.out.println(event.getCode());
        switch (event.getCode()){
            case W:
                engine.getPlayer().moveUp();
                break;
            case S:
                engine.getPlayer().moveDown();
                break;
            case D:
                engine.getPlayer().moveRight();
                break;
            case A:
                engine.getPlayer().moveLeft();
                break;
        }
    }

}
