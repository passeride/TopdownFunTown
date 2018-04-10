package com.bluebook.physics.quadtree;

import com.bluebook.camera.OrtographicCamera;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.sun.corba.se.impl.orbutil.graph.Graph;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuadTree {

    public ArrayList<GameObject> gameObjecs = new ArrayList<>();
    public QuadTree[] children = new QuadTree[4];
    public boolean isSubdevided = false;
    public Rectangle boundry;
    public int capacity = 0;

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

    public void insert(GameObject go){
        synchronized (this) {
            Vector2 goLocPos = go.getTransform().getGlobalPosition();

            if (boundry.intersects(goLocPos.getX(), goLocPos.getY(), 5, 5)) {
                if (isSubdevided) {
                    children[QuadChildren.NORTHWEST.value].insert(go);
                    children[QuadChildren.NORTHEAST.value].insert(go);
                    children[QuadChildren.SOUTHWEST.value].insert(go);
                    children[QuadChildren.SOUTHEAST.value].insert(go);
                } else {
                    if (gameObjecs.size() + 1 <= capacity) {
                        gameObjecs.add(go);
                    } else {
                        subdevide();
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

//        for(GameObject go : gameObjecs){
//            for(int i = 0; i < children.length; i ++){
//                children[i].insert(go);
//            }
//        }
//        gameObjecs.clear();
    }

    public ArrayList<GameObject> query(Rectangle rect){
        ArrayList<GameObject> ret = new ArrayList<>();
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

        for(GameObject go : gameObjecs){
            if(go.getCollider() == null)
                continue;
            Rectangle r = go.getCollider().getRect();
            if(rect.intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight())){
                ret.add(go);
            }
        }

        return ret;
    }

    public void draw(GraphicsContext gc){
        synchronized (this) {
            if (isSubdevided) {
                if(children[QuadChildren.NORTHWEST.value] != null)
                    children[QuadChildren.NORTHWEST.value].draw(gc);
                if(children[QuadChildren.NORTHEAST.value] != null)
                    children[QuadChildren.NORTHEAST.value].draw(gc);
                if(children[QuadChildren.SOUTHWEST.value] != null)
                    children[QuadChildren.SOUTHWEST.value].draw(gc);
                if(children[QuadChildren.SOUTHEAST.value] != null)
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
                for(GameObject go : gameObjecs){

                    Vector2 goPoss = go.getTransform().getGlobalPosition();
                    gc.fillRect(goPoss.getX(), goPoss.getY(), 5, 5);
                }


            }
        }
    }

}
