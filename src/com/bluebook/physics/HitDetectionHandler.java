package com.bluebook.physics;

import com.bluebook.camera.OrtographicCamera;
import com.bluebook.physics.quadtree.QuadTree;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * This class is called from {@link CollisionThread} and handles every instance of {@link BoxCollider}
 */
public class HitDetectionHandler {

    private boolean useQuadTree = true;

    private static HitDetectionHandler singelton;

    public double colliderQueryWidth = 200, colliderQueryHeight = 200;

    ArrayList<Collider> colliders = new ArrayList<>();
    ArrayList<RayCast> raycasts = new ArrayList<>();

    // And out buffers needed for thread safe operation
    ArrayList<Collider> colliderInBuffer = new ArrayList<>();
    ArrayList<Collider> colliderOutBuffer = new ArrayList<>();
    public QuadTree qtTree = new QuadTree(new Rectangle(0, 0, 1920, 1080), 5);

    private HitDetectionHandler(){

    }

    public void updatePositions(){
        for(Collider collider : colliders){
            collider.getGameObject().setSize(collider.getGameObject().getScale());
            collider.updatePosition();
        }
    }

    protected void buildQuadTree(){
        synchronized (this) {
            qtTree = new QuadTree(new Rectangle(-OrtographicCamera.main.getX(), -OrtographicCamera.main.getY(), 1920, 1080), 2);
            for (Collider c : colliders) {
                qtTree.insert(c);
            }
        }
    }

    public Collider isPositionCollided(Vector2 newPoss, Collider col){

        Rectangle queryRect = new Rectangle(newPoss.getX(), newPoss.getY(), 200, 200);
        ArrayList<Collider> queryCol = qtTree.query(queryRect);
        for (Collider dest : queryCol) {
            if (col.getName() != dest.getName()) {
                if (col.getInteractionLayer().contains(dest.getTag())) {

                    if (col.instersects(dest)) {
                        return dest;
                    }
                }
            }
        }

        return null;
    }

    /**
     * This will go over  all collision and raytracing looking for intersections
     */
    protected void lookForCollision(){
        synchronized (this) {
            if(useQuadTree) {
                buildQuadTree();
                for (Collider base : colliders) {
                    Vector2 goLocPos = base.getGameObject().getTransform().getLocalPosition();
                    ArrayList<Collider> queryCol = qtTree.query(
                            new Rectangle(goLocPos.getX() - colliderQueryWidth / 2, goLocPos.getY() - colliderQueryHeight / 2, colliderQueryWidth, colliderQueryHeight));


//                    Rectangle cbBase = base.getRect();
                    Boolean notCollided = true;
                    for (Collider dest : queryCol) {
                        if (base.getName() != dest.getName()) {
                            if (base.getInteractionLayer().contains(dest.getTag())) {

//                                Rectangle cbDest = dest.getRect();
                                if (base.instersects(dest)) {
                                    base.setIntersection((Path) Shape.intersect(base.getShape(), dest.getShape()));
                                    base.setIntersectionCollider(dest);
                                    notCollided = false;
                                    if (base.listener != null)
                                        base.listener.onCollision(dest);
                                }
                            }
                        }
                    }
                    if (notCollided) {
                        base.setIntersection(null);
                        base.setIntersectionCollider(null);
                    }
                }
            }else {
                for (Collider base: colliders) {
//                    Rectangle cbBase = base.getRect();
                    Boolean notCollided = true;
                    for (Collider dest : colliders) {
                        if (base.getName() != dest.getName()) {
                            if (base.getInteractionLayer().contains(dest.getTag())) {
//                                Rectangle cbDest = dest.getRect();
                                if (base.instersects(dest)) {
                                    base.setIntersection((Path) Shape.intersect(base.getShape(), dest.getShape()));
                                    base.setIntersectionCollider(dest);
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
            qtTree.insert(collider);
        }
    }

    protected void removeCollider(Collider collider){
        synchronized (this) {
            colliderOutBuffer.add(collider);
        }
    }



}
