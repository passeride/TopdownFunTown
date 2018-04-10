package com.bluebook.physics;

import com.bluebook.physics.quadtree.QuadTree;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * This class is called from {@link CollisionThread} and handles every instance of {@link Collider}
 */
public class HitDetectionHandler {

    private boolean useQuadTree = true;

    private static HitDetectionHandler singelton;

    public double colliderQueryWidth = 400, colliderQueryHeight = 400;

    ArrayList<Collider> colliders = new ArrayList<>();
    ArrayList<RayCast> raycasts = new ArrayList<>();

    // And out buffers needed for thread safe operation
    ArrayList<Collider> colliderInBuffer = new ArrayList<>();
    ArrayList<Collider> colliderOutBuffer = new ArrayList<>();
    public QuadTree qtTree = new QuadTree(new Rectangle(0, 0, 1920, 1080), 5);

    private HitDetectionHandler(){

    }

    protected void updatePositions(){
        for(Collider collider : colliders){
            collider.getGameObject().setSize(collider.getGameObject().getScale());
            collider.updatePosition();
        }
    }

    protected void buildQuadTree(){
        synchronized (this) {
            qtTree = new QuadTree(new Rectangle(0, 0, 1920, 1080), 2);
            for (Collider c : colliders) {
                qtTree.insert(c.getGameObject());
            }
        }
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
                    ArrayList<GameObject> close = qtTree.query(
                            new Rectangle(goLocPos.getX() - colliderQueryWidth / 2, goLocPos.getY() - colliderQueryHeight / 2, colliderQueryWidth, colliderQueryHeight));
                    ArrayList<Collider> queryCol = new ArrayList<>();
                    for (GameObject go : close) {
                        if (go.getCollider() != null)
                            queryCol.add(go.getCollider());
                    }

                    Rectangle cbBase = base.getRect();
                    Boolean notCollided = true;
                    for (Collider dest : queryCol) {
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
            }else {
                for (Collider base: colliders) {
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
            qtTree.insert(collider.getGameObject());
        }
    }

    protected void removeCollider(Collider collider){
        synchronized (this) {
            colliderOutBuffer.add(collider);
        }
    }



}
