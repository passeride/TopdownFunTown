package com.bluebook.util;

import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import com.bluebook.renderer.CanvasRenderer;

public abstract class GameObject {

    protected Vector2 position;
    protected Vector2 direction;

    protected Sprite sprite;

    /**
     * Constructor for GameObject given position rotation and sprite
     * @param position
     * @param direction
     * @param sprite
     */
    public GameObject(Vector2 position, Vector2 direction, Sprite sprite){
        this.position = position;
        this.direction = direction;
        this.sprite = sprite;

        CanvasRenderer.getInstance().AddGameObject(this);
        GameEngine.getInstance().addGameObject(this);
    }

    /**
     * Used to draw GameObject on canvas
     * @param gc
     */
    public void draw(GraphicsContext gc){
        sprite.draw(gc, position, direction);
    }

    /**
     * Used for objects that need to get the update tick, they can override this function
     * @param detla
     */
    public void update(double detla){

    }

    public void translate(Vector2 moveVector){
        position = Vector2.add(position, moveVector);
    }

    public void lookAt(Vector2 lookPosition){
        direction =  Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(position, lookPosition));
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
