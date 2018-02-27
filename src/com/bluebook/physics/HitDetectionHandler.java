package com.bluebook.physics;

import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class is called from {@link CollisionThread} and handles every instance of {@link Collider}
 */
public class HitDetectionHandler {

    private static HitDetectionHandler singelton;

    ArrayList<Collider> colliders = new ArrayList<>();
    ArrayList<RayCast> raycasts = new ArrayList<>();

    // And out buffers needed for thread safe operation
    ArrayList<Collider> colliderInBuffer = new ArrayList<>();
    ArrayList<Collider> colliderOutBuffer = new ArrayList<>();

    private HitDetectionHandler(){

    }

    protected void updatePositions(){
        for(Collider collider : colliders){
            collider.getGameObject().setSize(collider.getGameObject().getSize());
            collider.updatePosition();
        }
    }

    /**
     * This will go over  all collision and raytracing looking for intersections
     */
    protected void lookForCollision(){
        synchronized (this) {
            for (Collider base : colliders) {
                Rectangle cbBase = base.getRect();
                Boolean notCollided = true;
                for (Collider dest : colliders) {
                    if (base.getName() != dest.getName()) {
                        if (base.getInteractionLayer().contains(dest.getTag())) {
                            Rectangle cbDest = dest.getRect();
                            if (cbBase.getBoundsInParent().intersects(cbDest.getBoundsInParent())) {
                                base.setIntersection((Path) Shape.intersect(cbBase, cbDest));
                                notCollided = false;
                                if (base.listener != null)
                                    base.listener.onCollision(dest);
                            }
                        }
                    }
                }
                if (notCollided)
                    base.setIntersection(null);
            }

            // Raycasting
            for (RayCast r : raycasts) {
                r.Cast();
            }
            moveBuffer();
        }
    }

    public void moveBuffer(){
        colliders.addAll(colliderInBuffer);
        colliderInBuffer.clear();

        // Remove
        for(Collider c : colliderOutBuffer){
            if(colliders.contains(c))
                colliders.remove(c);
        }
        colliderOutBuffer.clear();
    }

    public static HitDetectionHandler getInstance(){
        if(singelton == null)
            singelton = new HitDetectionHandler();
        return singelton;
    }

    protected void addCollider(Collider collider){
        synchronized (this) {
            colliderInBuffer.add(collider);
        }
    }

    protected void removeCollider(Collider collider){
        synchronized (this) {
            colliderOutBuffer.add(collider);
        }
    }



}
