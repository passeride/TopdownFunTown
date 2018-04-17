package com.bluebook.physics;

import com.bluebook.camera.OrtographicCamera;
import com.bluebook.engine.GameApplication;
import com.bluebook.physics.quadtree.QuadTree;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.RomInntrenger;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * This class is called from {@link CollisionThread} and handles every instance of {@link BoxCollider}
 */
public class HitDetectionHandler {

    public boolean useQuadTree = true;

    private static HitDetectionHandler singelton;
    private boolean working = false;

    public double colliderQueryWidth = 500, colliderQueryHeight = 500;

    public ArrayList<Collider> colliders = new ArrayList<>();
    public ArrayList<Collider> raycast_colliders = new ArrayList<>();
    public ArrayList<RayCast> raycasts = new ArrayList<>();

    // And out buffers needed for thread safe operation
    ArrayList<Collider> colliderInBuffer = new ArrayList<>();
    ArrayList<Collider> colliderOutBuffer = new ArrayList<>();
    public QuadTree qtTree = new QuadTree(new Rectangle(0, 0, 1920 * 10, 1080 * 10), 1);

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
            qtTree = new QuadTree(new Rectangle(-1920 * 20 , -1080 * 20, 1920 * 40, 1080 * 40), 2);
            for (Collider c : colliders) {
                qtTree.insert(c);
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
                    ArrayList<Collider> queryCol = qtTree.query(base);
                    Boolean notCollided = true;
                    for (Collider dest : queryCol) {
                        if (base.getName() != dest.getName()) {
                            if (base.getInteractionLayer().contains(dest.getTag())) {
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
                    if (notCollided) {
                        base.setIntersection(null);
                        base.setIntersectionCollider(null);
                    }
                }
            }

            moveBuffer();
        }
    }


    public void doRaycasts(){
        // Raycasting
//        System.out.println("RAYCASTING1");
//         System.out.println("RAYCASTING");
//            for (RayCast r : raycasts) {
//                r.Cast();
//            }

    }

    public void moveBuffer(){
        synchronized (colliderInBuffer) {
            colliders.addAll(colliderInBuffer);
            colliderInBuffer.clear();
        }
        synchronized (colliderOutBuffer) {
            // Remove
            for (Collider c : colliderOutBuffer) {
                if (colliders.contains(c))
                    colliders.remove(c);
            }
            colliderOutBuffer.clear();

        }
        raycast_colliders.addAll(colliders);
    }

    public static HitDetectionHandler getInstance(){
        if(singelton == null)
            singelton = new HitDetectionHandler();
        return singelton;
    }

    protected void addCollider(Collider collider){
        synchronized (colliderInBuffer) {
            colliderInBuffer.add(collider);
        }
    }

    protected void removeCollider(Collider collider){
        synchronized (colliderOutBuffer) {
            colliderOutBuffer.add(collider);
        }
    }



}
