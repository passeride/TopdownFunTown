package com.bluebook.physics;

import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.CanvasRenderer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.sun.javafx.geom.Line2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The Collider class will be the superclass for different types of colliders, mainly {@link CircleCollider}  and {@link BoxCollider}
 */
public abstract class Collider {

    protected String name;
    protected String tag;

    protected List<String> interactionLayer = new ArrayList<>();

    protected Path intersection;
    protected Vector2 intersectionCenter;
    protected Vector2 padding = Vector2.ZERO;
    protected Collider intersectionCollider;

    protected GameObject gameObject;
    protected OnCollisionListener listener;

    public Collider(GameObject go){
        this.gameObject = go;
        CanvasRenderer.getInstance().addCollider(this);
        HitDetectionHandler.getInstance().addCollider(this);

    }

    protected abstract void updatePosition();

    protected abstract void updateCenterPoint();

    public abstract void debugDraw(GraphicsContext gc);

    abstract void setIntersection(Path intersection);

    public Vector2 getFurthestPointOfIntersection(){
        Vector2 ret = new Vector2(0, 0);
        double minDist = 0;

        for(PathElement pe : intersection.getElements()){
            if(pe instanceof MoveTo){
                Vector2 pathPosition = new Vector2(((MoveTo) pe).getX(), ((MoveTo) pe).getY());
                if(gameObject.getPosition().distance(pathPosition) > minDist){
                    ret = pathPosition;
                }
            }
        }

        return ret;
    }

    public abstract Rectangle getBoudningBox();

    public Vector2 getClosestPointOfIntersection(){
        Vector2 ret = new Vector2(0, 0);
        double maxDist = Double.MAX_VALUE;

        for(PathElement pe : intersection.getElements()){
            if(pe instanceof MoveTo){
                Vector2 pathPosition = new Vector2(((MoveTo) pe).getX(), ((MoveTo) pe).getY());
                if(gameObject.getPosition().distance(pathPosition) < maxDist){
                    ret = pathPosition;
                }
            }
        }

        return ret;
    }

    public abstract boolean instersects(Collider other);

    abstract Line2D[] getLines();

    public void destroy(){
        CanvasRenderer.getInstance().removeCollider(this);
        HitDetectionHandler.getInstance().removeCollider(this);
    }

    public abstract Shape getShape();

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

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public Shape getIntersection() {
        return intersection;
    }


    public Vector2 getIntersectionCenter() {
        return intersectionCenter;
    }

    public void setIntersectionCenter(Vector2 intersectionCenter) {
        this.intersectionCenter = intersectionCenter;
    }

    public Vector2 getPadding() {
        return padding;
    }

    public void setPadding(Vector2 padding) {
        this.padding = padding;
    }

    public List<String> getInteractionLayer(){
        return this.interactionLayer;
    }

    /**
     * This will remove an interactcionLayer
     * @param tagName
     */
    public void removeInteractcionLayer(String tagName){
        if(interactionLayer.contains(tagName)){
            interactionLayer.remove(tagName);
        }
    }

    /**
     * Will add a string corresponding to a TAG, and interactions between this collider and colliders with this tag will be marked
     * @param tagName
     */
    public void addInteractionLayer(String tagName) {
        interactionLayer.add(tagName);
    }

    public Collider getIntersectionCollider() {
        return intersectionCollider;
    }

    public void setIntersectionCollider(Collider intersectionCollider) {
        this.intersectionCollider = intersectionCollider;
    }
}
