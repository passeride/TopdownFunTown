package com.bluebook.physics;

import com.bluebook.camera.OrtographicCamera;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.sun.javafx.geom.Line2D;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.List;

public class CircleCollider extends Collider{

    private double radius;
    private Vector2 position;
    private int lineResolution = 12;

    public CircleCollider(GameObject go, double radius){
        super(go);
        this.radius = radius;
        position = go.getPosition();
    }

    @Override
    protected void updatePosition() {
        position = gameObject.getPosition();
    }

    @Override
    protected void updateCenterPoint() {
        synchronized (this){

        }
    }

    @Override
    public void debugDraw(GraphicsContext gc) {
        synchronized (this){

            double offX = OrtographicCamera.main != null ? OrtographicCamera.main.getX() : 0.0;
            double offY = OrtographicCamera.main != null ? OrtographicCamera.main.getY() : 0.0;

            gc.setStroke(Color.GREEN);
            gc.strokeArc(position.getX() + offX - radius,  position.getY() + offY - radius, radius * 2, radius * 2, 0, 360, ArcType.CHORD);
            gc.setStroke(Color.TEAL);
            Line2D[] lines = getLines();
            for(int i = 0; i < lines.length; i++){
                gc.strokeLine(lines[i].x1, lines[i].y1, lines[i].x2, lines[i].y2);
            }

            if (intersection != null) {
                gc.setFill(Color.RED);
                intersection.setFill(Color.RED);
                Bounds b = intersection.getBoundsInParent();
                gc.beginPath();


                double centerX = b.getMinX() + (b.getMaxX() - b.getMinX()) / 2;
                double centerY = b.getMinY() + (b.getMaxY() - b.getMinY()) / 2;
                List<PathElement> elements = intersection.getElements();
                for (PathElement pe : elements) {
                    if (pe.getClass() == MoveTo.class) {
                        gc.moveTo(((MoveTo) pe).getX() + offX, ((MoveTo) pe).getY() + offY);
                    } else if (pe.getClass() == LineTo.class) {
                        gc.lineTo(((LineTo) pe).getX() + offX, ((LineTo) pe).getY() + offY);
                    }
                }


                gc.closePath();
                gc.fill();
                gc.stroke();

                gc.setFill(Color.BLUE);
                gc.fillRect(centerX + 2 + offX, centerY + 2 + offY, 4, 4);
//            gc.setFill(Color.GREEN);
//            gc.fillRect(centerX - 25, centerY - 25,  50, 50);
            }
        }
    }

    @Override
    void setIntersection(Path intersection) {
        synchronized (this) {
            this.intersection = intersection;
            if (intersection != null) {
                updateCenterPoint();
            } else {
                intersectionCenter = null;
            }
        }
    }

    @Override
    public boolean instersects(Collider other) {
        if(other instanceof CircleCollider){
            CircleCollider otherCircle = (CircleCollider)  other;
            return position.distance(otherCircle.position) < radius + otherCircle.radius;
        }else if(other instanceof BoxCollider){
            BoxCollider box = (BoxCollider) other;
            Vector2 circleCenter = getPosition();
            Vector2 boxCenter = new Vector2(box.getRect().getX() + box.getRect().getWidth() / 2,
                    box.getRect().getY() + box.getRect().getHeight() / 2);
            Vector2 hitVector = Vector2.subtract(boxCenter,circleCenter).getNormalizedVector();
            hitVector = Vector2.multiply(hitVector, getRadius());
            hitVector = Vector2.add(circleCenter, hitVector);
            return (box.getRect().getBoundsInParent().intersects(new Rectangle(hitVector.getX(),  hitVector.getY(), 2, 2).getBoundsInParent()));
        }
        else return false;
    }

    @Override
    Line2D[] getLines() {

        Vector2 pos = gameObject.getTransform().getGlobalPosition();

        Vector2[] circlePoints = new Vector2[lineResolution];
        for(int i = 0; i < circlePoints.length; i ++){
            circlePoints[i] = Vector2.rotateVectorAroundPoint(Vector2.add(pos, new Vector2(0, radius)), pos,(360 / lineResolution)* i);
        }


        Line2D[] ret = new Line2D[lineResolution - 1];

        for(int i = 0; i < ret.length; i++){
            if(i != ret.length){
                Vector2 start = circlePoints[i];
                Vector2 dest = circlePoints[i + 1];
                ret[i] = new Line2D((float)start.getX(), (float)start.getY(), (float)dest.getX(), (float)dest.getY());
            }else{
                Vector2 start = circlePoints[i];
                Vector2 dest = circlePoints[0];
                ret[i] = new Line2D((float)start.getX(), (float)start.getY(), (float)dest.getX(), (float)dest.getY());
            }

        }

        return ret;
    }


    @Override
    public Shape getShape() {
        return new Circle(position.getX(), position.getY(), radius);
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
