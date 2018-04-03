package com.bluebook.physics;

import com.bluebook.camera.OrtographicCamera;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.CanvasRenderer;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;
import com.sun.javafx.geom.Line2D;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

/**
 * Collider is the phycisc class that handle collision
 *
 */
public class Collider {

    private String name;
    private String tag;

    private List<String> interactionLayer = new ArrayList<>();

    private Path intersection;
    private Vector2 intersectionCenter;
    private Vector2 padding = Vector2.ZERO;

    private GameObject gameObject;
    protected OnCollisionListener listener;
    private Rectangle rect;


    /**
     * Constructor for {@link Collider} require a {@link GameObject} to be attached to gameobject
     * Colliders need to use {@link Collider#addInteractionLayer(String)} to get a whitelist of interactions to raport
     * @param go
     */
    public Collider(GameObject go){
        this.gameObject = go;
        attachToGameObject(go);
        updateRect();
        HitDetectionHandler.getInstance().addCollider(this);
        CanvasRenderer.getInstance().addCollider(this);
    }

    /**
     * Will update the rectangle used for collision based on the size of the attached gameobject
     */
    public void updateRect(){
        double xSize = gameObject.getScaledSize().getX() + padding.getX();
        double ySize = gameObject.getScaledSize().getY() + padding.getY();
        rect = new Rectangle(gameObject.getPosition().getX() - xSize, gameObject.getPosition().getY() - ySize * 2.0, xSize, ySize);
    }

    protected void updatePosition(){
        if(gameObject != null){
            Vector2 scaleVec = GameSettings.getScreenScale();
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
    public void debugDraw(GraphicsContext gc){
        synchronized (this) {
            gc.setStroke(Color.GREEN);
            gc.save();
            //rotateGraphicsContext(gc, rect);
            //gc.strokeRect(rect.getX(), rect.getY(),  rect.getWidth(), rect.getHeight());
            for (Line2D l : getLines()) {
                gc.strokeLine(l.x1, l.y1, l.x2, l.y2);
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
                        gc.moveTo(((MoveTo) pe).getX(), ((MoveTo) pe).getY());
                    } else if (pe.getClass() == LineTo.class) {
                        gc.lineTo(((LineTo) pe).getX(), ((LineTo) pe).getY());
                    }
                }

                gc.closePath();
                gc.fill();
                gc.stroke();
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

    /**
     * Used to destroy gameobject, and remove it's references from other objects
     */
    public void destroy(){
        CanvasRenderer.getInstance().removeCollider(this);
        HitDetectionHandler.getInstance().removeCollider(this);
    }

    public boolean isAttached(){
        return gameObject != null;
    }

    public void attachToGameObject(GameObject go){
        this.gameObject = go;
    }

    public void dettactchGameObject(){
        this.gameObject = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setOnCollisionListener(OnCollisionListener listener){
        this.listener = listener;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public Shape getIntersection() {
        return intersection;
    }

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

    public Vector2 getIntersectionCenter() {
        return intersectionCenter;
    }

    public void setIntersectionCenter(Vector2 intersectionCenter) {
        this.intersectionCenter = intersectionCenter;
    }

    public Vector2 getPadding() {
        return padding;
    }

    public void setPadding(Vector2 padding) {
        this.padding = padding;
    }

    public List<String> getInteractionLayer(){
        return this.interactionLayer;
    }

    /**
     * This will remove an interactcionLayer
     * @param tagName
     */
    public void removeInteractcionLayer(String tagName){
        if(interactionLayer.contains(tagName)){
            interactionLayer.remove(tagName);
        }
    }

    /**
     * Will add a string corresponding to a TAG, and interactions between this collider and colliders with this tag will be marked
     * @param tagName
     */
    public void addInteractionLayer(String tagName) {
        interactionLayer.add(tagName);
    }
}
