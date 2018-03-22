package com.bluebook.camera;

import com.bluebook.util.GameObject;

public class TopDownCamera {



    private double x, y;
    private double size = 300d;
    private GameObject gameobject;
    //private deadzone rect;
    //private dampening dampening;

    public static TopDownCamera main;


    public TopDownCamera(){
        TopDownCamera.main = this;

    }

    //må fikse id før vi kan se hvordan vi ordner offsets.
    public void follow(GameObject spiller){
        gameobject = spiller;
        x = gameobject.getPosition().getX();
        y = gameobject.getPosition().getY();
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
