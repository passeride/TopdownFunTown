package com.bluebook.camera;

import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;

/**
 * OrthographicCamera is an object containing X and Y coordinates, that we use to translate/displace all objects drawn on screen to create the effect of a camera
 * We also have a follow function with {@link OrthographicCamera#follow(GameObject)} that corresponds to deadzones and will allow some movement withing bounds without camera movement
 */
public class OrthographicCamera {


    private static double x = 0, y = 0, dampening = 0.1;
    private GameObject gameobject;
    private double deadZoneX, deadZoneY;

    private double width, height;

    /**
     * Used to get the current camera, this should make it possible  to have more than one camera at the scene.
     * And toggle which to use for render
     */
    public static OrthographicCamera main;


    /**
     * Used to get offsett as a {@link Vec2}
     * @return
     */
    public static Vec2 getOffset() {
        if (main == null) {
            return Vec2.ZERO;
        } else {
            return new Vec2(x, y);
        }
    }

    /**
     * Used to jump to a follow object without transition
     */
    public void moveToFollow() {
        x = gameobject.getTransform().getLocalPosition().getX();
        y = gameobject.getTransform().getLocalPosition().getY();

    }

    /**
     * Constructor for OrthographicCamera
     */
    public OrthographicCamera() {
        OrthographicCamera.main = this;
        // Loading from settings file
        width = GameSettings.getDouble("game_resolution_X");
        height = GameSettings.getDouble("game_resolution_Y");
        deadZoneX = GameSettings.getDouble("OrthographicCamera_deadzone_X");
        deadZoneY = GameSettings.getDouble("OrthographicCamera_deadzone_Y");
        dampening = GameSettings.getDouble("OrthographicCamera_dampening");
    }

    //må fikse id før vi kan se hvordan vi ordner offsets.
    public void follow(GameObject target) {
        gameobject = target;
    }


    /**
     * Update will update the cameras position to follow player
     */
    public void update(double delta) {
        if (gameobject != null) {
            double deadzoneX = width * this.deadZoneX;
            double deadzoneY = height * this.deadZoneY;

            Vec2 playPoss = gameobject.getTransform().getLocalPosition();
            if (playPoss.getX() >= -x + width - deadzoneX) {
                double movePosX = -playPoss.getX() - x + width - deadzoneX;
                OrthographicCamera.x = OrthographicCamera.x + movePosX * dampening;
            }

            if (playPoss.getX() <= -x + deadzoneX) {
                double movePosX = -playPoss.getX() - x + deadzoneX;
                OrthographicCamera.x = OrthographicCamera.x + movePosX * dampening;
            }

            if (playPoss.getY() >= -y + height - deadzoneY) {
                double movePosY = -playPoss.getY() - y + height - deadzoneY;
                OrthographicCamera.y = OrthographicCamera.y + movePosY * dampening;
            }

            if (playPoss.getY() <= -y + deadzoneY) {
                double movePosY = -playPoss.getY() - y + deadzoneY;
                OrthographicCamera.y = OrthographicCamera.y + movePosY * dampening;
            }
        }
    }

    /**
     * Will return the gameobject to follow
     * @return
     */
    public GameObject getGameobject() {
        return gameobject;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        OrthographicCamera.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        OrthographicCamera.y = y;
    }


}
