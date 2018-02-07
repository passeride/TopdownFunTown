package com.bluebook.engine;

import com.bluebook.javafx.Main;
import javafx.application.Application;

public abstract class GameApplication {

    private static GameApplication singelton;

    public GameApplication(Main m){

        Application.launch(Main.class,  new String[0]);
        singelton = this;
    }

    public static GameApplication getSingelton() {
        return singelton;
    }

    public abstract void update(double delta);


    public void setWindowTitle(String s){

    }

}
