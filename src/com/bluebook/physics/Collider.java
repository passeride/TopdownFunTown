package com.bluebook.physics;

import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.CanvasRenderer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.sun.javafx.geom.Line2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The Collider class will be the superclass for different types of colliders, mainly {@link
 * CircleCollider}  and {@link BoxCollider}
 */
public abstract class Collider {

    String name;
    String tag;
    List<String> interactionLayer = new ArrayList<>();

    Vec2 position = Vec2.ZERO;

    Path intersection;
    Vec2 intersectionCenter;
    Vec2 padding = Vec2.ZERO;
    Collider intersectionCollider;

    GameObject gameObject;
    OnCollisionListener listener;

    /**
     * Constructor for Collider
     *
     * @param go {@link GameObject} collider is Attached to
     */
    public Collider(GameObject go) {
        this.gameObject = go;
        CanvasRenderer.getInstance().addCollider(this);
        HitDetectionHandler.getInstance().addCollider(this);
    }

    protected abstract void updatePosition();

    protected abstract void updateCenterPoint();

    /**
     * If {@link com.bluebook.engine.GameEngine#DEBUG} is true this will draw the collider for
     * debugging
     */
    public abstract void debugDraw(GraphicsContext gc);

    abstract void setIntersection(Path intersection);

    public Vec2 getFurthestPointOfIntersection() {
        Vec2 ret = new Vec2(0, 0);
        double minDist = 0;

        for (PathElement pe : intersection.getElements()) {
            if (pe instanceof MoveTo) {
                Vec2 pathPosition = new Vec2(((MoveTo) pe).getX(), ((MoveTo) pe).getY());
                if (gameObject.getPosition().distance(pathPosition) > minDist) {
                    ret = pathPosition;
                }
            }
        }

        return ret;
    }

    public abstract Rectangle getBoudningBox();

    public Vec2 getClosestPointOfIntersection() {
        Vec2 ret = new Vec2(0, 0);
        double maxDist = Double.MAX_VALUE;

        for (PathElement pe : intersection.getElements()) {
            if (pe instanceof MoveTo) {
                Vec2 pathPosition = new Vec2(((MoveTo) pe).getX(), ((MoveTo) pe).getY());
                if (gameObject.getPosition().distance(pathPosition) < maxDist) {
                    ret = pathPosition;
                }
            }
        }

        return ret;
    }

    public abstract boolean instersects(Collider other);

    abstract Line2D[] getLines();

    public void destroy() {
        CanvasRenderer.getInstance().removeCollider(this);
        HitDetectionHandler.getInstance().removeCollider(this);
    }

    public abstract Shape getShape();

    public boolean isAttached() {
        return gameObject != null;
    }

    public void attachToGameObject(GameObject go) {
        this.gameObject = go;
    }

    public void dettactchGameObject() {
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

    public void setOnCollisionListener(OnCollisionListener listener) {
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


    public Vec2 getIntersectionCenter() {
        return intersectionCenter;
    }

    public void setIntersectionCenter(Vec2 intersectionCenter) {
        this.intersectionCenter = intersectionCenter;
    }

    public Vec2 getPadding() {
        return padding;
    }

    public void setPadding(Vec2 padding) {
        this.padding = padding;
    }

    public List<String> getInteractionLayer() {
        return this.interactionLayer;
    }

    /**
     * This will remove an interactcionLayer
     */
    public void removeInteractcionLayer(String tagName) {
        interactionLayer.remove(tagName);
    }

    /**
     * Will add a string corresponding to a TAG, and interactions between this collider and colliders
     * with this tag will be marked
     */
    public void addInteractionLayer(String tagName) {
        interactionLayer.add(tagName);
    }

    /**
     * If there was a collision on last {@link CollisionThread} update, the collided collider will be
     * retrivable here. If this returns null, no collision has occured.
     */
    public Collider getIntersectionCollider() {
        return intersectionCollider;
    }

    /**
     * Used to set the collided collider
     */
    public void setIntersectionCollider(Collider intersectionCollider) {
        this.intersectionCollider = intersectionCollider;
    }

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }
}
