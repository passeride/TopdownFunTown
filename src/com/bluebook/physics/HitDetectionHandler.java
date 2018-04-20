package com.bluebook.physics;

import com.bluebook.camera.OrthographicCamera;
import com.bluebook.engine.GameApplication;
import com.bluebook.physics.quadtree.QuadTree;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.objects.player.Player;
import com.sun.javafx.geom.Line2D;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
    public static boolean DO_RAYCAST = true;

    public static boolean DO_SHADOW_SWEEP = true;

    private static HitDetectionHandler singleton;

    public ArrayList<Collider> colliders = new ArrayList<>();
    public ArrayList<Collider> raycast_colliders = new ArrayList<>();
    public ArrayList<RayCast> raycasts = new ArrayList<>();

    public ArrayList<Line2D> lines = new ArrayList<>();

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
        DO_SHADOW_SWEEP = GameSettings.getBoolean("Physics_use_shadowSweep");

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
            Vec2 screen = GameSettings.getScreen();
            qtTree = new QuadTree(new Rectangle(-screen.getX() * 20, -screen.getY() * 20, screen.getX() * 40, screen.getY() * 40), 5); // Creating oversized quadtree
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
            if ( base.getGameObject() != dest.getGameObject() &&
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

            lines.clear();
            if(USE_QUADTREE)
                buildQuadTree();

            for(Collider base : colliders){
                checkCollision(base);

                if(DO_RAYCAST && !(base.getGameObject() instanceof Player) && base.interactionLayer.contains("Obscure")) {
                    Line2D[] baseLines = base.getLines();
                    for(Line2D l : baseLines)
                        lines.add(l);


                }
            }

            if(DO_RAYCAST)
                doRaycasts();

            moveBuffer();
        }
    }

    public static int counter = 0;

    private void doRaycasts() {
        if(DO_SHADOW_SWEEP){
            Vec2 cam = OrthographicCamera.getOffset();
            Vec2 screen = GameSettings.getScreen();

            float LeftX = (float) cam.getX();
            float TopY = (float) cam.getY();
            float RightX = (float)(cam.getX() - screen.getX());
            float BottomY = (float)(cam.getY() - screen.getY());

            lines.add(new Line2D(LeftX, TopY, RightX, TopY));
            lines.add(new Line2D(RightX, TopY, RightX, BottomY));
            lines.add(new Line2D(RightX, BottomY, LeftX, BottomY));
            lines.add(new Line2D(LeftX, BottomY, LeftX, TopY));


            Vec2 source = ((RomInntrenger) GameApplication.getInstance()).player.getTransform().getGlobalPosition();

            ArrayList<LineSegment> lineSegments = new ArrayList<>();
            lines.forEach(l -> lineSegments.add(new LineSegment(l, source)));

            ((RomInntrenger) GameApplication.getInstance()).player.light2D.calculateVisibility(lineSegments);


        }else {
            CopyOnWriteArrayList<Line2D> cowLines = new CopyOnWriteArrayList<>();
            cowLines.addAll(lines);

            ExecutorService executor = Executors.newFixedThreadPool(1000);

            List<Future<RayCastHit>> list = new ArrayList<>();

            counter = 0;
//        System.out.println(raycasts.size());

            for (int i = 0; i < raycasts.size(); i++) {
                Future<RayCastHit> future = executor.submit(() -> {

                    int id = counter++;
                    if (id < raycasts.size()) {
                        raycasts.get(id).Cast(cowLines);

                        return raycasts.get(id).getHit();
                    } else {
                        return null;
                    }
                });
                list.add(future);

            }

            for (Future<RayCastHit> fut : list) {
                try {
                    RayCastHit rch = fut.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            executor.shutdownNow();
//        for (RayCast r : raycasts) {
//            r.Cast(cowLines);
//        }
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
     * @param collider {@link Collider}  to be added
     */
    protected void addCollider(Collider collider) {
        synchronized (colliderInBuffer) {
            colliderInBuffer.add(collider);
        }
    }

    /**
     * Will remove collider from hitdetection during next cycle
     * @param collider {@link Collider} to be removed
     */
    protected void removeCollider(Collider collider) {
        synchronized (colliderOutBuffer) {
            colliderOutBuffer.add(collider);
        }
    }


}
