package com.bluebook.physics;

import com.bluebook.engine.GameEngine;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.CanvasRenderer;
import com.bluebook.util.GameObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 * Collider is the phycisc class that handle collision
 *
 */
public class Collider {

    private CollisionBox collisionBox;
    private String name;
    private String tag;

    private GameObject gameObject;
    protected OnCollisionListener listener;
    private Rectangle rect;


    public Collider(GameObject go){
        this.rect = new Rectangle(go.getPosition().getX() - go.getSize().getX(), go.getPosition().getY() - go.getSize().getY() * 2.0, go.getSize().getX(), go.getSize().getY());

        HitDetectionHandler.getInstance().addCollider(this);
        CanvasRenderer.getInstance().addCollider(this);
    }

    protected void updatePosition(){
        if(gameObject != null){
            rect.setX(gameObject.getPosition().getX() - gameObject.getSize().getX() / 2.0);
            rect.setY(gameObject.getPosition().getY() - gameObject.getSize().getY() / 2.0);
            rect.setRotate(gameObject.getDirection().getAngleInDegrees());
        }
    }

    public void debugDraw(GraphicsContext gc){
        gc.setStroke(Color.GREEN);
        gc.save();
        rotateGraphicsContext(gc, rect);
        gc.strokeRect(rect.getX(), rect.getY(),  rect.getWidth(), rect.getHeight());
        gc.restore();
    }

    private GraphicsContext rotateGraphicsContext(GraphicsContext gc, Rectangle rect){
        Rotate r = new Rotate(rect.getRotate(), rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        return gc;
    }

    /**
     * Used to destroy gameobject, and remove it's references from other objects
     */
    public void destroy(){
        CanvasRenderer.getInstance().removeCollider(this);
        HitDetectionHandler.getInstance().removeCollider(this);
    }

    public boolean isAttached(){
        return gameObject != null;
    }

    public void attachToGameObject(GameObject go){
        this.gameObject = go;
    }

    public void dettactchGameObject(){
        this.gameObject = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setOnCollisionListener(OnCollisionListener listener){
        this.listener = listener;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

}
