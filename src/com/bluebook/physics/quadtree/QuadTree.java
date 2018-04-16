package com.bluebook.physics.quadtree;

import com.bluebook.camera.OrtographicCamera;
import com.bluebook.physics.BoxCollider;
import com.bluebook.physics.CircleCollider;
import com.bluebook.physics.Collider;
import com.bluebook.physics.HitDetectionHandler;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.objects.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.css.Rect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A QuadTree is used to query a 2D plane for elements within a given area
 * This is used to improve hitdetection from a n^2 problem to a Log(n) problem.
 *
 * This was implemented in hopes to put the HitDetection on the main thread, but this prooved dificult and was not needed.
 * So the
 */
public class QuadTree {

    public static final boolean useGlobalPosition = true;

    public ArrayList<Collider> colliders = new ArrayList<>();
    public QuadTree[] children = new QuadTree[4];
    public boolean isSubdevided = false;
    public Rectangle boundry;
    public int capacity = 0;
    public double colliderQueryWidth = 500, colliderQueryHeight = 500;

    public enum QuadChildren{
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

        public static QuadChildren get(int i){
            return valueOf(i);
        }

        public static int getTotal(){
            return names.length;
        }

        private int value;

        private QuadChildren(int value) {
            this.value = value;
        }

        public String getName(){
            return names[value];
        }

        public int getValue() {
            return value;
        }
    }


    public QuadTree(Rectangle boundry, int capacity){
        this.boundry = boundry;
        this.capacity = capacity;
    }

    public void insert(Collider col){
        synchronized (this) {
            Vector2 goLocPos;

            if(useGlobalPosition){
                goLocPos = col.getGameObject().getTransform().getGlobalPosition();
            } else{
                goLocPos = col.getGameObject().getTransform().getLocalPosition();
            }

            if (boundry.intersects(goLocPos.getX(), goLocPos.getY(), 5, 5)) {
                if (isSubdevided) {
                    children[QuadChildren.NORTHWEST.value].insert(col);
                    children[QuadChildren.NORTHEAST.value].insert(col);
                    children[QuadChildren.SOUTHWEST.value].insert(col);
                    children[QuadChildren.SOUTHEAST.value].insert(col);
                } else {
                    if (colliders.size() + 1 <= capacity) {
                        colliders.add(col);
                    } else {
                        subdevide();
                        insert(col);
                    }
                }
            }
        }
    }

    void subdevide(){
        isSubdevided = true;

        double halfWidth = boundry.getWidth() / 2;
        double halfHeight = boundry.getHeight() / 2;

        // NORTHWEST
        Rectangle nwR = new Rectangle(boundry.getX(), boundry.getY(), halfWidth, halfHeight);
        children[QuadChildren.NORTHWEST.value] = new QuadTree(nwR, capacity);
        // NORTEAST
        Rectangle neR = new Rectangle(boundry.getX() + halfWidth, boundry.getY(), halfWidth, halfHeight);
        children[QuadChildren.NORTHEAST.value] = new QuadTree(neR, capacity);
        // SOUTHWEST
        Rectangle swR = new Rectangle(boundry.getX(), boundry.getY() + halfHeight, halfWidth, halfHeight);
        children[QuadChildren.SOUTHWEST.value] = new QuadTree(swR, capacity);
        // SOUTHEAST
        Rectangle seR = new Rectangle(boundry.getX() + halfWidth, boundry.getY() + halfHeight, halfWidth, halfHeight);
        children[QuadChildren.SOUTHEAST.value] = new QuadTree(seR, capacity);

//        for(GameObject go : colliders){
//            for(int i = 0; i < children.length; i ++){
//                children[i].insert(go);
//            }
//        }
//        colliders.clear();
    }

    public ArrayList<Collider> query(Collider col){
        Vector2 pos;
        if(useGlobalPosition){
            pos = col.getGameObject().getTransform().getGlobalPosition();
        } else{
            pos = col.getGameObject().getTransform().getLocalPosition();
        }
        return query(new Rectangle(pos.getX() - colliderQueryWidth / 4, pos.getY() - colliderQueryHeight / 4, colliderQueryWidth, colliderQueryHeight));
    }

    public boolean intersects(Rectangle rect1, Rectangle rect2){
        double X1LEFT = rect1.getX();
        double X1RIGHT = rect1.getX() + rect1.getWidth();
        double Y1TOP = rect1.getY();
        double Y1BOTTOM = rect1.getY() + rect1.getHeight();

        double X2LEFT = rect2.getX();
        double X2RIGHT = rect2.getX() + rect2.getWidth();
        double Y2TOP = rect2.getY();
        double Y2BOTTOM = rect2.getY() + rect2.getHeight();

        return X1LEFT < X2RIGHT && X1RIGHT > X2LEFT && Y1TOP > Y2BOTTOM && Y1BOTTOM < Y2TOP;
    }

    public ArrayList<Collider> query(Rectangle rect){
        ArrayList<Collider> ret = new ArrayList<>();
        if(isSubdevided){
            if(children[QuadChildren.NORTHWEST.value].boundry.intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight())){
                ret.addAll(children[QuadChildren.NORTHWEST.value].query(rect));
            }
            if(children[QuadChildren.NORTHEAST.value].boundry.intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight())){
                ret.addAll(children[QuadChildren.NORTHEAST.value].query(rect));
            }
            if(children[QuadChildren.SOUTHWEST.value].boundry.intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight())){
                ret.addAll(children[QuadChildren.SOUTHWEST.value].query(rect));
            }
            if(children[QuadChildren.SOUTHWEST.value].boundry.intersects(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight())){
                ret.addAll(children[QuadChildren.SOUTHWEST.value].query(rect));
            }
        }

        for(Collider col : colliders){
            if(col == null)
                continue;

            Rectangle r = col.getBoudningBox();
            if (rect.intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight())) {
                ret.add(col);
            }

        }

        return ret;
    }

    public void draw(GraphicsContext gc){
        synchronized (this) {
            if (HitDetectionHandler.getInstance().useQuadTree) {
                if (isSubdevided) {
                    if (children[QuadChildren.NORTHWEST.value] != null)
                        children[QuadChildren.NORTHWEST.value].draw(gc);
                    if (children[QuadChildren.NORTHEAST.value] != null)
                        children[QuadChildren.NORTHEAST.value].draw(gc);
                    if (children[QuadChildren.SOUTHWEST.value] != null)
                        children[QuadChildren.SOUTHWEST.value].draw(gc);
                    if (children[QuadChildren.SOUTHEAST.value] != null)
                        children[QuadChildren.SOUTHEAST.value].draw(gc);
                }

                if (OrtographicCamera.main != null) {
                    gc.setLineWidth(3);
                    gc.setStroke(Color.BLACK);
                    //gc.setLineDashes(2, 7, 2, 8);
                    double x = boundry.getX();
                    double y = boundry.getY();
                    double w = boundry.getWidth();
                    double h = boundry.getHeight();
                    gc.strokeLine(x, y, x + w, y);
                    gc.strokeLine(x, y, x, y + h);
                    gc.strokeLine(x, y + h, x + w, y + h);
                    gc.strokeLine(x + w, y, x + w, y + h);

                    gc.setFill(Color.RED);
                    for (Collider go : colliders) {

                        Vector2 goPoss;
                        if(useGlobalPosition){
                           goPoss = go.getGameObject().getTransform().getGlobalPosition();
                        }else{
                            goPoss = go.getGameObject().getTransform().getLocalPosition();
                        }
                        gc.fillRect(goPoss.getX(), goPoss.getY(), 5, 5);

                        if(go.getGameObject() instanceof Player){
                            gc.setStroke(Color.PURPLE);
                            gc.strokeRect(goPoss.getX() - colliderQueryWidth / 2, goPoss.getY() - colliderQueryHeight / 2, colliderQueryWidth, colliderQueryWidth);
                            ArrayList<Collider> cols = query(go);
                            gc.setFill(Color.GREEN);
                            for(Collider col : cols){
                                Vector2 playerColliderPoss;
                                if(useGlobalPosition){
                                    playerColliderPoss = go.getGameObject().getTransform().getGlobalPosition();
                                }else{
                                    playerColliderPoss = go.getGameObject().getTransform().getLocalPosition();
                                }
                                gc.fillRect(playerColliderPoss.getX() - 5, playerColliderPoss.getY() - 5, 10, 10);
                            }
                        }
                    }


                }
            }
        }
    }

}
