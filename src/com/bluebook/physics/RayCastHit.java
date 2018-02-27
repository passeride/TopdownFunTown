package com.bluebook.physics;

import com.sun.javafx.geom.Line2D;

import javax.sound.sampled.Line;

public class RayCastHit {
    public Line2D ray;
    public Line2D originalRay;
    public Collider colliderHit;
    public boolean isHit;
    public double distance;
}
