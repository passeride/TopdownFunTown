package com.bluebook.util;

import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.Collider;
import com.bluebook.renderer.CanvasRenderer;
import com.bluebook.renderer.GraphicsRenderer;
import com.bluebook.renderer.RenderLayer;
import javafx.scene.canvas.GraphicsContext;

/**
 * {@link GameObject} is the abstract class to be used for everything that should be renderer
 *
 */
public abstract class GameObject {

    protected Vec2 position;
    protected Vec2 direction;
    protected Vec2 size;

    protected boolean allwaysOnScreen = false;

    protected Transform transform;

    protected Sprite sprite;

    boolean isAlive = true;

    protected Collider collider;

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public GameObject(Vec2 position, Vec2 direction, Sprite sprite) {
        this.position = position;
        this.direction = direction;
        this.sprite = sprite;

        transform = new Transform();
        transform.setLocalPosition(position);
        transform.setLocalRotation(direction);
        transform.setLocalScale(new Vec2(1, 1));
        if (sprite != null) {
            this.sprite.setOrigin(transform);
        }

        if (sprite != null) {
            this.size = new Vec2(sprite.getSquareWidth(), sprite.getSquareHeight());
        }

        CanvasRenderer.getInstance().addGameObject(this);
        GameEngine.getInstance().addGameObject(this);
    }

    /**
     * Used to draw GameObject on canvas
     */
    public void draw(GraphicsContext gc) {
        if (sprite != null) {
            sprite.draw(gc, transform.getGlobalPosition(), transform.getGlobalRotation());
        }
    }

    public void draw(GraphicsRenderer gr) {
        if (sprite != null) {
            sprite.draw(gr);
        }
    }


    public boolean isOnScreen() {
        if (allwaysOnScreen) {
            return true;
        }

        Vec2 screen = GameSettings.getScreen();
        Vec2 position = transform.getGlobalPosition();
        return (position.getX() < screen.getX() + 128 && position.getX() > -128
            && position.getY() < screen.getY() + 128 && position.getY() > -128);

    }

    /**
     * Used for objects that need to get the update tick, they can override this function
     */
    public void update(double delta) {

    }

    /**
     * Will move this vector relative to it's own (1,0) will move it one in x axis
     */
    public void translate(Vec2 moveVector) {
        transform.translate(moveVector);
    }

    /**
     * Will change direction to point at given  position
     */
    public void lookAt(Vec2 lookPosition) {
        transform.setLocalRotation(Vec2.Vector2FromAngleInDegrees(
            Vec2.getAngleBetweenInDegrees(transform.getLocalPosition(), lookPosition) + 90));
    }

    /**
     * Used to destroy gameobject, important to call this
     */
    public void destroy() {
        if (collider != null) {
            collider.destroy();
        }
        CanvasRenderer.getInstance().removeGameObject(this);
        GameEngine.getInstance().removeGameObject(this);

        isAlive = false;
    }

    /**
     * Will change the renderlayer of the object t
     *
     * @param layer {@link com.bluebook.renderer.RenderLayer.RenderLayerName} to be used
     */
    public void setRenderLayer(RenderLayer.RenderLayerName layer) {
        CanvasRenderer.getInstance().moveGameObjectToLayer(this, layer);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Vec2 getPosition() {
        return transform.getLocalPosition();
    }

    public void setPosition(Vec2 position) {
        transform.setLocalPosition(position);
    }

    public Vec2 getDirection() {
        return transform.getLocalRotation();
    }

    public void setDirection(Vec2 direction) {
        transform.setLocalRotation(direction.getNormalizedVector());
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.sprite.setOrigin(transform);
        this.size = new Vec2(sprite.getSquareWidth(), sprite.getSquareHeight());
    }

    public Vec2 getScale() {
        return transform.getLocalScale();
    }

    public Vec2 getScaledSize() {
        return transform.getLocalScale();
    }

    /**
     * Will return a value from 0 -> 1.0 that is the relative X position on the screen
     */
    public double getProcentageXPosition() {
        return position.getX() / GameSettings.getInt("game_resolution_X");
    }

    public void setSize(Vec2 scale) {
        transform.setLocalScale(scale);
    }

    public Collider getCollider() {
        return this.collider;
    }

    public void setCollider(Collider collider) {
        this.collider = collider;
        collider.attachToGameObject(this);
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

}
