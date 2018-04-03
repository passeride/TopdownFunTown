package com.bluebook.camera;

import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;

public class OrtographicCamera {



    private double x, y;
    private double size = 300d;
    private GameObject gameobject;
    //private deadzone rect;
    //private dampening dampening;

    public double width, height;

    public static OrtographicCamera main;


    public OrtographicCamera(){
        OrtographicCamera.main = this;
        width = GameSettings.getDouble("game_resolution_X");
        height = GameSettings.getDouble("game_resolution_Y");

    }

    //må fikse id før vi kan se hvordan vi ordner offsets.
    public void follow(GameObject target){
        gameobject = target;
        x = gameobject.getPosition().getX();
        y = gameobject.getPosition().getY();
    }

    public void update(){
        if(gameobject != null){
            x = -gameobject.getPosition().getX() + width / 2;
            y = -gameobject.getPosition().getY() + height / 2;
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


}
