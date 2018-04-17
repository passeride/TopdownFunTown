package com.bluebook.camera;

import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;


public class OrtographicCamera {



    private static double x = 0, y = 0, dampening = 0.1;
    private double size = 300d;
    private GameObject gameobject;
    private double deadzoneX = 0.5, deadzoneY = 0.5;

    public double width, height;

    public static OrtographicCamera main;


    public static Vector2 getOffset(){
        if(main == null){
            return Vector2.ZERO;
        }else{
            return new Vector2(main.x, main.y);
        }
    }

    public OrtographicCamera(){
        OrtographicCamera.main = this;
        width = GameSettings.getDouble("game_resolution_X");
        height = GameSettings.getDouble("game_resolution_Y");

    }

    public Vector2 addCameraOffset(Vector2 pos){
        return Vector2.subtract(pos, OrtographicCamera.getOffset());
    }


    //må fikse id før vi kan se hvordan vi ordner offsets.
    public void follow(GameObject target){
        gameobject = target;
    }

    public void update(){
        if(gameobject != null){
            double deadzoneX = width * this.deadzoneX;
            double deadzoneY = height * this.deadzoneY;

            Vector2 playPoss = gameobject.getTransform().getLocalPosition();
            if(playPoss.getX() >= -x + width - deadzoneX){
                double movePosX = -playPoss.getX() - x + width - deadzoneX;
                OrtographicCamera.x = OrtographicCamera.x + movePosX * dampening;
            }

            if(playPoss.getX() <= -x + deadzoneX){
                double movePosX = -playPoss.getX() - x + deadzoneX;
                OrtographicCamera.x = OrtographicCamera.x + movePosX * dampening;
            }

            if(playPoss.getY() >= -y + height - deadzoneY){
                double movePosY = -playPoss.getY() - y + height - deadzoneY;
                OrtographicCamera.y = OrtographicCamera.y + movePosY * dampening;
            }

            if(playPoss.getY() <= -y + deadzoneY){
                double movePosY = -playPoss.getY() - y + deadzoneY;
                OrtographicCamera.y = OrtographicCamera.y + movePosY * dampening;
            }
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
