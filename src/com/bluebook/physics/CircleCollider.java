package com.bluebook.physics;

import com.bluebook.camera.OrthographicCamera;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.sun.javafx.geom.Line2D;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.List;

public class CircleCollider extends Collider {

    private double radius;
    private static int lineResolution = 12;

    public CircleCollider(GameObject go, double radius) {
        super(go);
        this.radius = radius;
        this.position = go.getPosition();
        lineResolution = GameSettings.getInt("Physics_circle_line_resolution");
    }


    @Override
    protected void updatePosition() {
        position = gameObject.getPosition();
    }

    @Override
    protected void updateCenterPoint() {
        synchronized (this) {

        }
    }

    @Override
    public void debugDraw(GraphicsContext gc) {
        synchronized (this) {

            double offX = OrthographicCamera.main != null ? OrthographicCamera.main.getX() : 0.0;
            double offY = OrthographicCamera.main != null ? OrthographicCamera.main.getY() : 0.0;

            gc.setStroke(Color.GREEN);
            gc.strokeArc(position.getX() + offX - radius, position.getY() + offY - radius,
                radius * 2, radius * 2, 0, 360, ArcType.CHORD);
            gc.setStroke(Color.TEAL);
            Line2D[] lines = getLines();
            for (Line2D line : lines) {
                gc.strokeLine(line.x1, line.y1, line.x2, line.y2);
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
    public Rectangle getBoudningBox() {
        return new Rectangle(position.getX() - radius, position.getY() - radius, radius, radius);
    }

    @Override
    public boolean instersects(Collider other) {
        if (other instanceof CircleCollider) {
            CircleCollider otherCircle = (CircleCollider) other;
            return position.distance(otherCircle.position) < radius + otherCircle.radius;
        } else if (other instanceof BoxCollider) {
            BoxCollider box = (BoxCollider) other;
            Vec2 circleCenter = getPosition();
            Vec2 boxCenter = new Vec2(box.getRect().getX() + box.getRect().getWidth() / 2,
                box.getRect().getY() + box.getRect().getHeight() / 2);
            Vec2 hitVector = Vec2.subtract(boxCenter, circleCenter).getNormalizedVector();
            hitVector = Vec2.multiply(hitVector, getRadius());
            hitVector = Vec2.add(circleCenter, hitVector);
            return (box.getRect().getBoundsInParent().intersects(
                new Rectangle(hitVector.getX(), hitVector.getY(), 2, 2).getBoundsInParent()));
        } else {
            return false;
        }
    }

    @Override
    Line2D[] getLines() {

        Vec2 pos = gameObject.getTransform().getGlobalPosition();

        Vec2[] circlePoints = new Vec2[lineResolution];
        for (int i = 0; i < circlePoints.length; i++) {
            circlePoints[i] = Vec2
                .rotateVectorAroundPoint(Vec2.add(pos, new Vec2(0, radius)), pos,
                    (360 / lineResolution) * i);
        }

        Line2D[] ret = new Line2D[lineResolution - 1];

        for (int i = 0; i < ret.length; i++) {
            if (i != ret.length - 1) {
                Vec2 start = circlePoints[i];
                Vec2 dest = circlePoints[i + 1];
                ret[i] = new Line2D((float) start.getX(), (float) start.getY(), (float) dest.getX(),
                    (float) dest.getY());
            } else {
                Vec2 start = circlePoints[i];
                Vec2 dest = circlePoints[0];
                ret[i] = new Line2D((float) start.getX(), (float) start.getY(), (float) dest.getX(),
                    (float) dest.getY());
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

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }
}
