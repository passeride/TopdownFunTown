package com.bluebook.physics;

import com.bluebook.renderer.CanvasRenderer;
import com.bluebook.util.Vector2;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;


/**
 * Used for colliding, based on javafx bounds
 */
public class CollisionBox extends Bounds{


    private Vector2 position;
    private Vector2 size;

    public Rectangle rect;

    public CollisionBox(Vector2 position, Vector2 size){
        super(position.getX(), position.getY(), 0.0 ,size.getX(), size.getY(), 0.0);
        this.position = position;
        this.size = size;
        rect = new Rectangle(position.getX(), position.getY(), size.getX(), size.getY());


    }

    public void debugDraw(GraphicsContext gc){
        gc.setStroke(Color.GREEN);
        gc.save();
        rotateGraphicsContext(gc, rect);
        gc.strokeRect(rect.getX(), rect.getY(),  rect.getWidth(), rect.getHeight());
        gc.restore();
    }

    private GraphicsContext rotateGraphicsContext(GraphicsContext gc, Rectangle rect){
        Rotate r = new Rotate(rect.getRotate(), rect.getX(), rect.getY());
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        return gc;
    }



    @Override
    public boolean isEmpty() {
        return size.getX() < 0 || size.getY() < 0;
    }

    @Override
    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    @Override
    public boolean contains(Point3D p) {
        return contains(p.getX(), p.getY());
    }

    @Override
    public boolean contains(double X, double Y) {
        if(X > position.getX() && X < position.getX() + size.getX() && Y > position.getY() && Y < position.getY() + size
                .getY()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean contains(double x, double y, double z) {
        return  contains(x, y);
    }

    @Override
    public boolean contains(Bounds b) {
        return doesListContains(rectangleBoundsToPoints(b));
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return doesListContains(valueBoundsToPoints(x, y, w, h));
    }

    @Override
    public boolean contains(double x, double y, double z, double w, double h, double d) {
        return doesListContains(valueBoundsToPoints(x, y, w, h));
    }

    @Override
    public boolean intersects(Bounds b) {
        //return doesListIntersect(rectangleBoundsToPoints(b));
        return (Math.abs(getMinX() - b.getMinX()) * 2 < (getWidth() + b.getWidth())) &&
                (Math.abs(getMinY() - b.getMinY()) * 2 < (getHeight() + b.getHeight()));
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return doesListIntersect(valueBoundsToPoints(x, y, w, h));
    }

    @Override
    public boolean intersects(double x, double y, double z, double w, double h, double d) {
        return doesListIntersect(valueBoundsToPoints(x, y, w, h));
    }

    private Point2D[] valueBoundsToPoints(double x, double y, double w, double h){
        Point2D[] ret = new Point2D[4];
        ret[0] = new Point2D(x, y);
        ret[1] = new Point2D(x + w, y);
        ret[2] = new Point2D(x, y + h);
        ret[3] = new Point2D(x + w, y + h);
        return ret;
    }

    private Point2D[] rectangleBoundsToPoints(Bounds b){
        Point2D[] ret = new Point2D[4];
        ret[0] = new Point2D(b.getMinX(), b.getMinY());
        ret[1] = new Point2D(b.getMaxX(), b.getMinY());
        ret[2] = new Point2D(b.getMinX(), b.getMaxY());
        ret[3] = new Point2D(b.getMaxX(), b.getMaxY());
        return ret;
    }

    private boolean doesListContains(Point2D[] list){
        for(Point2D p : list){
            if(!contains(p))
                return false;
        }
        return true;
    }

    private boolean doesListIntersect(Point2D[] list){
        for(Point2D p : list){
            if(contains(p))
                return true;
        }
        return false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        rect.setX(position.getX());
        rect.setY(position.getY());
        this.position = position;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }

}
