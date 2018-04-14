package com.bluebook.util;

import com.bluebook.camera.OrtographicCamera;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.Collider;
import com.bluebook.renderer.CanvasRenderer;
import com.bluebook.renderer.GraphicsRenderer;
import com.bluebook.renderer.RenderLayer;
import javafx.scene.canvas.GraphicsContext;

public abstract class GameObject {

    protected Vector2 position;
    protected Vector2 direction;
    protected Vector2 size;
    protected Vector2 scaledSize;

    protected Transform transform;

    protected Sprite sprite;

    protected boolean isAlive = true;

    protected Collider collider;

    OrtographicCamera camera;

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

        transform = new Transform();
        transform.setLocalPosition(position);
        transform.setLocalRotation(direction);
        transform.setLocalScale(new Vector2(1, 1));
        if(sprite != null)
        this.sprite.setOrigin(transform);


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
            sprite.draw(gc, transform.getGlobalPosition(), transform.getGlobalRotation());
        // legge til spillerID for å følge spiller
        //camera.follow();
    }

    public void draw(GraphicsRenderer gr){
        if(sprite != null)
            sprite.draw(gr);
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
        transform.translate(moveVector);
    }

    /**
     * Will change direction to point at given  position
     * @param lookPosition
     */
    public void lookAt(Vector2 lookPosition){
        transform.setLocalRotation(Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(transform.getLocalPosition(), lookPosition) + 90));
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
        return transform.getLocalPosition();
    }

    public void setPosition(Vector2 position) {
        transform.setLocalPosition(position);
    }

    public Vector2 getDirection() {
        return transform.getLocalRotation();
    }

    public void setDirection(Vector2 direction) {
        transform.setLocalRotation(direction);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.sprite.setOrigin(transform);
        this.size = new Vector2(sprite.getSquareWidth(),  sprite.getSquareHeight());
    }

    public Vector2 getScale() {
        return transform.getLocalScale();
    }

    public Vector2 getScaledSize() {
        return transform.getLocalScale();
    }

    /**
     * Will return a value from 0 -> 1.0 that is the relative X position on the screen
     * @return
     */
    public double getProcentageXPosition(){
        return  position.getX() / GameSettings.getInt("game_resolution_X");
    }

    public void setSize(Vector2 scale) {
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
