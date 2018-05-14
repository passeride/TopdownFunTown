package com.bluebook.physics;

import com.bluebook.camera.OrthographicCamera;
import com.bluebook.util.Vec2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A QuadTree is used to query a 2D plane for elements within a given area This is used to improve
 * hitdetection from a n^2 problem to a Log(n) problem.
 * <p>
 * This was implemented in hopes to put the HitDetection on the main thread, but this prooved
 * dificult and was not needed. So the
 */
public class QuadTree {

    private ArrayList<Collider> colliders = new ArrayList<>();
    private QuadTree[] children = new QuadTree[4];
    private boolean isSubdivided = false;
    private Rectangle boundary;
    private int capacity;
    private double colliderQueryWidth = 200, colliderQueryHeight = 200;

    /**
     * Simple enum used to specify the 4 subquads of a quad NORTHWEST NORTHEAST SOUTHWEST SOUTHEAST
     */
    public enum QuadChildren {
        NORTHWEST(0), NORTHEAST(1), SOUTHWEST(2), SOUTHEAST(3);
        private static Map<Integer, QuadChildren> map = new HashMap<>();

        static {
            for (QuadChildren rLN : QuadChildren.values()) {
                map.put(rLN.value, rLN);
            }
        }

        public static QuadChildren valueOf(int renderLayerNameValue) {
            return map.get(renderLayerNameValue);
        }

        public static String[] names = {"NORTHWEST", "NORTHEAST", "SOUTHWEST", "SOUTHEAST"};

        public static QuadChildren get(int i) {
            return valueOf(i);
        }

        private int value;

        QuadChildren(int value) {
            this.value = value;
        }

        public String getName() {
            return names[value];
        }
    }


    public void reset() {
        colliders.clear();
        for (QuadTree qt : children) {
            qt = null;
        }
        isSubdivided = false;
    }

    /**
     * Will create a node in QuadTree with given boundary and capacity
     *
     * @param boundary the {@link Rectangle} that it holds elements from
     */
    public QuadTree(Rectangle boundary, int capacity) {
        this.boundary = boundary;
        this.capacity = capacity;
    }

    /**
     * Insert an {@link Collider} into the quadtree to be recieve it when querying
     *
     * @param col {@link Collider} to be inserted into the {@link QuadTree}
     */
    public void insert(Collider col) {
        synchronized (this) {
            Vec2 goLocPos = col.getPosition();

            if (boundary.intersects(goLocPos.getX(), goLocPos.getY(), 6, 6)) {
                if (isSubdivided) {
                    children[QuadChildren.NORTHWEST.value].insert(col);
                    children[QuadChildren.NORTHEAST.value].insert(col);
                    children[QuadChildren.SOUTHWEST.value].insert(col);
                    children[QuadChildren.SOUTHEAST.value].insert(col);
                } else {
                    if (colliders.size() + 1 <= capacity) {
                        colliders.add(col);
                    } else {
                        subdivided();
                        insert(col);
                    }
                }
            }
        }
    }

    /**
     * Subdevides the quadtree into 4 rectangles
     */
    void subdivided() {
        isSubdivided = true;

        double halfWidth = boundary.getWidth() / 2;
        double halfHeight = boundary.getHeight() / 2;

        // NORTHWEST
        Rectangle nwR = new Rectangle(boundary.getX(), boundary.getY(), halfWidth, halfHeight);
        children[QuadChildren.NORTHWEST.value] = new QuadTree(nwR, capacity);
        // NORTEAST
        Rectangle neR = new Rectangle(boundary.getX() + halfWidth, boundary.getY(), halfWidth,
            halfHeight);
        children[QuadChildren.NORTHEAST.value] = new QuadTree(neR, capacity);
        // SOUTHWEST
        Rectangle swR = new Rectangle(boundary.getX(), boundary.getY() + halfHeight, halfWidth,
            halfHeight);
        children[QuadChildren.SOUTHWEST.value] = new QuadTree(swR, capacity);
        // SOUTHEAST
        Rectangle seR = new Rectangle(boundary.getX() + halfWidth, boundary.getY() + halfHeight,
            halfWidth, halfHeight);
        children[QuadChildren.SOUTHEAST.value] = new QuadTree(seR, capacity);
    }

    /**
     * Query all objects with a predefined size from {@link Collider}'s center
     *
     * @param col center of query
     * @return List of {@link Collider}'s within a predefined distance from {@param col}
     */
    public ArrayList<Collider> query(Collider col) {
        Vec2 pos = col.getPosition();
        return query(
            new Rectangle(pos.getX() - colliderQueryWidth / 4, pos.getY() - colliderQueryHeight / 4,
                colliderQueryWidth, colliderQueryHeight));
    }

    /**
     * Query a specific area of the {@link QuadTree}
     *
     * @param rect The area to query
     * @return List of {@link Collider}'s within queried area
     */
    public ArrayList<Collider> query(Rectangle rect) {
        ArrayList<Collider> ret = new ArrayList<>();
        if (isSubdivided) {
            if (children[QuadChildren.NORTHWEST.value].boundary.getBoundsInParent()
                .intersects(rect.getBoundsInParent())) {
                ret.addAll(children[QuadChildren.NORTHWEST.value].query(rect));
            }
            if (children[QuadChildren.NORTHEAST.value].boundary.getBoundsInParent()
                .intersects(rect.getBoundsInParent())) {
                ret.addAll(children[QuadChildren.NORTHEAST.value].query(rect));
            }
            if (children[QuadChildren.SOUTHWEST.value].boundary.getBoundsInParent()
                .intersects(rect.getBoundsInParent())) {
                ret.addAll(children[QuadChildren.SOUTHWEST.value].query(rect));
            }
            if (children[QuadChildren.SOUTHEAST.value].boundary.getBoundsInParent()
                .intersects(rect.getBoundsInParent())) {
                ret.addAll(children[QuadChildren.SOUTHEAST.value].query(rect));
            }
        }

        for (Collider col : colliders) {
            if (col == null) {
                continue;
            }

            Rectangle r = col.getBoudningBox();
            if (rect.intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight())) {
                ret.add(col);
            }

        }

        return ret;
    }

    /**
     * If debug is on, this will draw the quadmap. Used for debugging physics and collision
     */
    public void draw(GraphicsContext gc) {
        synchronized (this) {
            if (HitDetectionHandler.USE_QUADTREE) {
                if (isSubdivided) {
                    if (children[QuadChildren.NORTHWEST.value] != null) {
                        children[QuadChildren.NORTHWEST.value].draw(gc);
                    }
                    if (children[QuadChildren.NORTHEAST.value] != null) {
                        children[QuadChildren.NORTHEAST.value].draw(gc);
                    }
                    if (children[QuadChildren.SOUTHWEST.value] != null) {
                        children[QuadChildren.SOUTHWEST.value].draw(gc);
                    }
                    if (children[QuadChildren.SOUTHEAST.value] != null) {
                        children[QuadChildren.SOUTHEAST.value].draw(gc);
                    }
                }

                if (OrthographicCamera.main != null) {
                    gc.setLineWidth(3);
                    gc.setStroke(Color.BLACK);
                    //gc.setLineDashes(2, 7, 2, 8);
                    Vec2 camOff = OrthographicCamera.getOffset();
                    double x = boundary.getX() + camOff.getX();
                    double y = boundary.getY() + camOff.getY();
                    double w = boundary.getWidth();
                    double h = boundary.getHeight();
                    gc.strokeLine(x, y, x + w, y);
                    gc.strokeLine(x, y, x, y + h);
                    gc.strokeLine(x, y + h, x + w, y + h);
                    gc.strokeLine(x + w, y, x + w, y + h);
                    gc.setFill(Color.WHITE);

                }
            }
        }
    }

}
