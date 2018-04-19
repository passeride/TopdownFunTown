package com.bluebook.physics;

import com.bluebook.physics.quadtree.QuadTree;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;
import java.util.ArrayList;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * This class is called from {@link CollisionThread} and handles every instance of {@link
 * BoxCollider}
 */
public class HitDetectionHandler {

    /**
     * Will trigger weather to use QuadTree or round robin to do hit detection, This is set in the setting file
     */
    public static boolean USE_QUADTREE = true;
    /**
     * Will trigger weather to use Raycast or not, this is set in the settings file
     */
    public static boolean DO_RAYCAST = false;

    private static HitDetectionHandler singleton;

    public ArrayList<Collider> colliders = new ArrayList<>();
    public ArrayList<Collider> raycast_colliders = new ArrayList<>();
    public ArrayList<RayCast> raycasts = new ArrayList<>();

    // And out buffers needed for thread safe operation
    private ArrayList<Collider> colliderInBuffer = new ArrayList<>();
    private ArrayList<Collider> colliderOutBuffer = new ArrayList<>();
    public QuadTree qtTree;

    public static HitDetectionHandler getInstance() {
        if (singleton == null) {
            singleton = new HitDetectionHandler();
        }
        return singleton;
    }

    private HitDetectionHandler() {
        USE_QUADTREE = GameSettings.getBoolean("Physics_use_quadtree");
        DO_RAYCAST = GameSettings.getBoolean("Physics_use_raycast");
    }

    /**
     * Will update position to all {@link Collider} relative to their attached {@link com.bluebook.util.GameObject}
     */
    public void updatePositions() {
        for (Collider collider : colliders) {
            collider.getGameObject().setSize(collider.getGameObject().getScale());
            collider.updatePosition();
        }
    }

    private void buildQuadTree() {
        synchronized (this) {
            Vector2 screen = GameSettings.getScreen();
            qtTree = new QuadTree(new Rectangle(-screen.getX() * 20, -screen.getY() * 20, screen.getX() * 40, screen.getY() * 40), 2); // Creating oversized quadtree
            for (Collider c : colliders) {
                qtTree.insert(c);
            }
        }
    }

    private void checkCollision(Collider base){
        ArrayList<Collider> otherColliders;
        if(USE_QUADTREE)
            otherColliders = qtTree.query(base);
        else
            otherColliders = colliders;

        Boolean notCollided = true;

        for (Collider dest : otherColliders) {
            if (base.getName() != dest.getName() &&
                base.getInteractionLayer().contains(dest.getTag()) &&
                base.instersects(dest)) { // Check not duplicate, interactionlayer and intersection

                base.setIntersection(
                    (Path) Shape.intersect(base.getShape(), dest.getShape()));
                base.setIntersectionCollider(dest);

                notCollided = false;

                if (base.listener != null) {
                    base.listener.onCollision(dest);
                }
            }
        }


        if (notCollided) {
            base.setIntersection(null);
            base.setIntersectionCollider(null);
        }

    }

    /**
     * This will go over  all collision and raytracing looking for intersections
     */
    protected void lookForCollision() {
        synchronized (this) {

            if(USE_QUADTREE)
                buildQuadTree();

            for(Collider base : colliders){
                checkCollision(base);
            }

            if(DO_RAYCAST)
                doRaycasts();

            moveBuffer();
        }
    }


    private void doRaycasts() {
        for (RayCast r : raycasts) {
            r.Cast();
        }
    }

    private void moveBuffer() {
        synchronized (colliderInBuffer) {
            colliders.addAll(colliderInBuffer);
            colliderInBuffer.clear();
        }
        synchronized (colliderOutBuffer) {
            // Remove
            for (Collider c : colliderOutBuffer) {
                if (colliders.contains(c)) {
                    colliders.remove(c);
                }
            }
            colliderOutBuffer.clear();

        }
        raycast_colliders.addAll(colliders);
    }

    /**
     * Will add collider into temporary buffer to be added during next cycle
     * @param collider
     */
    protected void addCollider(Collider collider) {
        synchronized (colliderInBuffer) {
            colliderInBuffer.add(collider);
        }
    }

    /**
     * Will remove collider from hitdetection during next cycle
     * @param collider
     */
    protected void removeCollider(Collider collider) {
        synchronized (colliderOutBuffer) {
            colliderOutBuffer.add(collider);
        }
    }


}
