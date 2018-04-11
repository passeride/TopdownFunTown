package com.bluebook.camera;

import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;

public class OrtographicCamera {



    private static double x = 0, y = 0, dampening = 0.03;
    private double size = 300d;
    private GameObject gameobject;
    private double deadzoneX, deadzoneY;

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
//        x = gameobject.getPosition().getX();
//        y = gameobject.getPosition().getY();
    }

    public void update(){
        if(gameobject != null){
            deadzoneX = width * 0.3d;
            deadzoneY = height * 0.3d;

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

//    public void draw(GraphicsContext gc){
//
//
//        gc.setFill(Color.BLUE);
//        int widthWDeadzone = (int)(width - deadzone * 2);
//        int heightWDeadzone = (int)(height - deadzone);
//        int deadzoneXStart = (int)(width / 2 - widthWDeadzone / 2);
//        int deadzoneYStart = (int)(height / 2 - heightWDeadzone / 2);
//        System.out.println("DEADZONE: " + heightWDeadzone);
//        gc.fillRect(deadzoneXStart, deadzoneYStart, widthWDeadzone, heightWDeadzone);
//
//    }

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
