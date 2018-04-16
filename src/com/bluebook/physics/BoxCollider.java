package com.bluebook.physics;

import com.bluebook.camera.OrtographicCamera;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;
import com.sun.javafx.geom.Line2D;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

import java.util.List;

/**
 * BoxCollider is the phycisc class that handle collision
 *
 */
public class BoxCollider extends Collider {


    private Rectangle rect;


    /**
     * Constructor for {@link BoxCollider} require a {@link GameObject} to be attached to gameobject
     * Colliders need to use {@link BoxCollider#addInteractionLayer(String)} to get a whitelist of interactions to raport
     * @param go
     */
    public BoxCollider(GameObject go){
        super(go);
        synchronized (this) {
            updateRect();
        }
    }

    /**
     * Will update the rectangle used for collision based on the size of the attached gameobject
     */
    public void updateRect(){
        synchronized (this) {
            double xSize = Math.min(gameObject.getScaledSize().getX() + padding.getX(), 0.4);
            double ySize = Math.min(gameObject.getScaledSize().getY() + padding.getY(), 0.4);
            rect = new Rectangle(gameObject.getPosition().getX() - xSize, gameObject.getPosition().getY() - ySize * 2.0, xSize, ySize);
        }
    }

    protected void updatePosition(){
        if(gameObject != null){
            Vector2 scaleVec = GameSettings.getSquareScale();
            double xSize = scaleVec.getX() * gameObject.getScale().getX() + padding.getX();
            double ySize = scaleVec.getY() * gameObject.getScale().getY() + padding.getY();
            rect.setX(gameObject.getPosition().getX() - xSize / 2.0);
            rect.setY(gameObject.getPosition().getY() - ySize / 2.0);
            rect.setWidth(xSize);
            rect.setHeight(ySize);
            rect.setRotate(gameObject.getDirection().getAngleInDegrees());
        }
    }

    protected void updateCenterPoint(){
        Bounds b = intersection.getBoundsInParent();
        double centerX = b.getMinX() + (b.getMaxX() - b.getMinX()) / 2;
        double centerY = b.getMinY() + (b.getMaxY() - b.getMinY()) / 2;

        intersectionCenter = new Vector2(centerX, centerY);
    }

    /**
     * This is used to draw the hitboxes when debug is on
     * @param gc
     */
    @Override
    public void debugDraw(GraphicsContext gc){
        synchronized (this) {
            gc.setStroke(Color.GREEN);
            gc.save();
            //rotateGraphicsContext(gc, rect);
            Rectangle tmpRect = rect;
//            gc.strokeRect(tmpRect.getX(), tmpRect.getY(),  tmpRect.getWidth(), tmpRect.getHeight());
            if(rect != null) {
                Line2D[] lines = getLines();
                for (Line2D l : lines) {
                    gc.strokeLine(l.x1, l.y1, l.x2, l.y2);
                }
            }


            if (intersection != null) {
                gc.setFill(Color.RED);
                intersection.setFill(Color.RED);
                Bounds b = intersection.getBoundsInParent();
                gc.beginPath();

                double offX = OrtographicCamera.main != null ? OrtographicCamera.main.getX() : 0.0;
                double offY = OrtographicCamera.main != null ? OrtographicCamera.main.getY() : 0.0;



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

            gc.restore();
        }
    }

    /**
     * Will return the rectangle as 4 {@link Line2D} elements, to be used with raycasting
     * @return array of 4 {@link Line2D} elements making a rectangle
     */
    @Override
    public Line2D[] getLines(){
        if(OrtographicCamera.main != null){
            double xOff = OrtographicCamera.main.getX();
            double yOff = OrtographicCamera.main.getY();
            Line2D[] ret = new Line2D[4];
            // rect
            Vector2 rotationPoint = new Vector2(rect.getX() + xOff + rect.getWidth() / 2, rect.getY() + yOff + rect.getHeight() / 2);
            double angle = rect.getRotate();
            Vector2 topLeft = Vector2.rotateVectorAroundPoint(new Vector2(rect.getX() + xOff, rect.getY() + yOff), rotationPoint, angle);
            Vector2 bottomLeft = Vector2.rotateVectorAroundPoint(new Vector2(rect.getX() + xOff, rect.getY() + yOff + rect.getHeight()), rotationPoint, angle);
            Vector2 topRight = Vector2.rotateVectorAroundPoint(new Vector2(rect.getX() + xOff + rect.getWidth(), rect.getY() + yOff), rotationPoint, angle);
            Vector2 bottomRight = Vector2.rotateVectorAroundPoint(new Vector2(rect.getX() + xOff + rect.getWidth(), rect.getY() + yOff + rect.getHeight()), rotationPoint, angle);

            ret[0] = new Line2D((float) topLeft.getX(), (float) topLeft.getY(), (float) bottomLeft.getX(), (float) bottomLeft.getY());
            ret[1] = new Line2D((float) topLeft.getX(), (float) topLeft.getY(), (float) topRight.getX(), (float) topRight.getY());
            ret[2] = new Line2D((float) bottomRight.getX(), (float) bottomRight.getY(), (float) topRight.getX(), (float) topRight.getY());
            ret[3] = new Line2D((float) bottomRight.getX(), (float) bottomRight.getY(), (float) bottomLeft.getX(), (float) bottomLeft.getY());

            return ret;
        }else {
            Line2D[] ret = new Line2D[4];
            // rect
            Vector2 rotationPoint = new Vector2(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
            double angle = rect.getRotate();
            Vector2 topLeft = Vector2.rotateVectorAroundPoint(new Vector2(rect.getX(), rect.getY()), rotationPoint, angle);
            Vector2 bottomLeft = Vector2.rotateVectorAroundPoint(new Vector2(rect.getX(), rect.getY() + rect.getHeight()), rotationPoint, angle);
            Vector2 topRight = Vector2.rotateVectorAroundPoint(new Vector2(rect.getX() + rect.getWidth(), rect.getY()), rotationPoint, angle);
            Vector2 bottomRight = Vector2.rotateVectorAroundPoint(new Vector2(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight()), rotationPoint, angle);

            ret[0] = new Line2D((float) topLeft.getX(), (float) topLeft.getY(), (float) bottomLeft.getX(), (float) bottomLeft.getY());
            ret[1] = new Line2D((float) topLeft.getX(), (float) topLeft.getY(), (float) topRight.getX(), (float) topRight.getY());
            ret[2] = new Line2D((float) bottomRight.getX(), (float) bottomRight.getY(), (float) topRight.getX(), (float) topRight.getY());
            ret[3] = new Line2D((float) bottomRight.getX(), (float) bottomRight.getY(), (float) bottomLeft.getX(), (float) bottomLeft.getY());

            return ret;
        }
    }

    private GraphicsContext rotateGraphicsContext(GraphicsContext gc, Rectangle rect){

        this.rect.setRotate(getGameObject().getDirection().getAngleInDegrees());
        Rotate r = new Rotate(rect.getRotate(), rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        return gc;
    }


    @Override
    public Shape getShape() {
        return rect;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    @Override
    public void setIntersection(Path intersection) {
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
        return rect;
    }

    @Override
    public boolean instersects(Collider other) {
        if(other instanceof BoxCollider){
            return (getRect().getBoundsInParent().intersects(((BoxCollider) other).rect.getBoundsInParent()));
        }else if(other instanceof CircleCollider){
            CircleCollider circle =  (CircleCollider) other;
            Vector2 circleCenter = circle.getPosition();
            Vector2 boxCenter = new Vector2(getRect().getX() + getRect().getWidth() / 2, getRect().getY() + getRect().getHeight() / 2);
            Vector2 hitVector = Vector2.subtract(boxCenter, circleCenter).getNormalizedVector();
            hitVector = Vector2.multiply(hitVector, circle.getRadius());
            hitVector = Vector2.add(circleCenter, hitVector);
            return (getRect().getBoundsInParent().intersects(new Rectangle(hitVector.getX(),  hitVector.getY(), 2, 2).getBoundsInParent()));
        }
        else return false;
    }


}
