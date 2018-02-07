package com.bluebook.util;

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
    }

    /**
     * Used to draw GameObject on canvas
     * @param gc
     */
    public void draw(GraphicsContext gc){
        sprite.draw(gc, position, direction);
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
