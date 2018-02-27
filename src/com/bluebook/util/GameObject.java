package com.bluebook.util;

import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.Collider;
import com.bluebook.renderer.RenderLayer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import com.bluebook.renderer.CanvasRenderer;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public abstract class GameObject {

    protected Vector2 position;
    protected Vector2 direction;
    protected Vector2 size;
    protected Vector2 scaledSize;

    protected Sprite sprite;

    protected boolean isAlive = true;

    protected Collider collider;

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
        if(sprite != null)
            this.size = new Vector2(sprite.getSquareWidth(),  sprite.getSquareHeight());

        CanvasRenderer.getInstance().addGameObject(this);
        GameEngine.getInstance().addGameObject(this);
    }


    /**
     * Used to draw GameObject on canvas
     * @param gc
     */
    public void draw(GraphicsContext gc){
        if(sprite != null)
            sprite.draw(gc, position, direction);
    }

    /**
     * Used for objects that need to get the update tick, they can override this function
     * @param detla
     */
    public void update(double detla){

    }

    /**
     * Will move this vector relative to it's own
     * (1,0) will move it one in x axis
     * @param moveVector
     */
    public void translate(Vector2 moveVector){
        position = Vector2.add(position, moveVector);
    }

    /**
     * Will change direction to point at given  position
     * @param lookPosition
     */
    public void lookAt(Vector2 lookPosition){
        direction =  Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(position, lookPosition));
    }

    /**
     * Used to destroy gameobject, important to call this
     */
    public void destroy(){
        if(collider != null)
            collider.destroy();
        CanvasRenderer.getInstance().removeGameObject(this);
        isAlive = false;
    }

    /**
     * Will change the renderlayer of the object t
     * @param layer {@link com.bluebook.renderer.RenderLayer.RenderLayerName} to be used
     */
    public void setRenderLayer(RenderLayer.RenderLayerName layer){
        CanvasRenderer.getInstance().moveGameObjectToLayer(this, layer);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
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

    public Vector2 getSize() {
        return size;
    }

    public Vector2 getScaledSize() {
        if(scaledSize != null)
            return scaledSize;
        else
            return size;
    }

    /**
     * Will return a value from 0 -> 1.0 that is the relative X position on the screen
     * @return
     */
    public double getProcentageXPosition(){
        return  position.getX() / GameSettings.getInt("game_resolution_X");
    }

    public void setSize(Vector2 size) {
        this.size = size;
        this.scaledSize = new Vector2(size.getX() * GameApplication.X_scale.get(), size.getY() * GameApplication.Y_scale.get());
    }

    public Collider getCollider() {
        return collider;
    }

    public void setCollider(Collider collider) {
        this.collider = collider;
        collider.attachToGameObject(this);
    }

}
