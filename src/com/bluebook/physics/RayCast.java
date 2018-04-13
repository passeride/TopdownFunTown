package com.bluebook.physics;

import com.bluebook.util.GameObject;
import com.sun.javafx.geom.Line2D;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will create a raycast to interact with {@link Collider}
 */
public class RayCast {

    private Line2D ray;
    private float max_distance = 200;
    private RayCastHit hit = null;
    private double angle;
    private GameObject source;
    private List<String> interactionLayer = new ArrayList<>();

    /**
     * This will create a raycast for the PhysicsThread to compile from a game object in an angle
     * @param angle
     * @param source
     */
    public RayCast(double angle, GameObject source){
        HitDetectionHandler.getInstance().raycasts.add(this);
        this.source = source;
        this.angle = angle;
        updatePosition();
    }


    /**
     * This will create a raycast for the PhysicsThread to compile from a game object in an angle
     * @param angle
     * @param source
     */
    public RayCast(double angle, GameObject source, float max_distance){
        HitDetectionHandler.getInstance().raycasts.add(this);
        this.source = source;
        this.angle = angle;
        this.max_distance = max_distance;
        updatePosition();
    }

    /**
     * Used to update the position of the ray as on the gameobject
     */
    public void updatePosition(){
        ray = new Line2D((float)source.getTransform().getGlobalPosition().getX(),
                (float)source.getTransform().getGlobalPosition().getY(),
                (float) source.getTransform().getGlobalPosition().getX() + (float)Math.cos(angle) * max_distance,
                (float)source.getTransform().getGlobalPosition().getY() + (float)Math.sin(angle) * max_distance);
    }

    public void Cast(){
        updatePosition();
        float collisionDistance = max_distance;
        Collider colliderHit = null;
        for(Collider c : HitDetectionHandler.getInstance().colliders){
            if(interactionLayer.contains(c.getTag())) {
                Line2D[] box = c.getLines();
                for (Line2D l : box) {
                    //float distanceHit = getRayCast(ray, l);
                    float distanceHit = getRayCast(ray.x1, ray.y1,
                            ray.x1 + (float)Math.cos(angle) * collisionDistance,
                            ray.y1 + (float)Math.sin(angle) * collisionDistance,
                            l.x1, l.y1, l.x2, l.y2);
                    //System.out.println("Testing collision on angle :  " + angle + " got distance " + distanceHit);
                    if (distanceHit > 0 && distanceHit < collisionDistance) {
                        //System.out.println("COllision found " + c.getName() + " DISTANCE " + distanceHit + " ANGLE: " + angle);
                        collisionDistance = distanceHit;
                        colliderHit = c;
                    }
                }
            }
        }
        RayCastHit ret = new RayCastHit();
        ret.colliderHit = colliderHit;
        ret.isHit = colliderHit != null;
        ret.ray = new Line2D(ray.x1, ray.y1, ray.x1 + (float) (Math.cos(angle) * collisionDistance), ray.y1 + (float) (Math.sin(angle) * collisionDistance));
        ret.originalRay = this.ray;
        this.hit = ret;
    }

    private float getRayCast(Line2D ray, Line2D collision){
        return getRayCast(ray.x1, ray.y1, ray.x2,  ray.y2, collision.x1, collision.y1,  collision.x2, collision.y2);
    }

    float dist(float LineStartX, float LineStartY, float LineEndX, float LineEndY)
    {
        return (float) Math.sqrt((LineEndX - LineStartX) * (LineEndX - LineStartX) + (LineEndY - LineStartY) * (LineEndY - LineStartY));
    }

    float getRayCast(float RayStartX, float RayStartY, float RayCosineDist, float RaySineDist, float LineStartX, float LineStartY, float LineEndX, float LineEndY) {
        float RayEndX, RayEndY, LineXDiff, LineYDiff;
        RayEndX = RayCosineDist - RayStartX;
        RayEndY = RaySineDist - RayStartY;
        LineXDiff = LineEndX - LineStartX;
        LineYDiff = LineEndY - LineStartY;
        float s, t;
        s = (-RayEndY * (RayStartX - LineStartX) + RayEndX * (RayStartY - LineStartY)) / (-LineXDiff * RayEndY + RayEndX * LineYDiff);
        t = (LineXDiff * (RayStartY - LineStartY) - LineYDiff * (RayStartX - LineStartX)) / (-LineXDiff * RayEndY + RayEndX * LineYDiff);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            // Collision detected
            float x = RayStartX + (t * RayEndX);
            float y = RayStartY + (t * RayEndY);

            return dist(RayStartX, RayStartY, x, y);
        }

        return -1; // No collision
    }

    public float getMax_distance() {
        return max_distance;
    }

    public void setMax_distance(float max_distance) {
        this.max_distance = max_distance;
    }

    public RayCastHit getHit() {
        return hit;
    }

    public void setHit(RayCastHit hit) {
        this.hit = hit;
    }

    public void addInteractionLayer(String interactionTag){
        interactionLayer.add(interactionTag);
    }

    public void removeInteractionLayer(String interactionTag){
        if (interactionLayer.contains(interactionTag))
            interactionLayer.remove(interactionTag);
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
        updatePosition();
    }

    public Line2D getRay() {
        return ray;
    }

}
