package com.bluebook.physics;

import com.bluebook.engine.GameEngine;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.sun.javafx.geom.Line2D;

public class LineSegment {

    public LinePoint p1 = new LinePoint(), p2 = new LinePoint();
    Vec2 source;
    public double d;

    public LineSegment(Line2D line, Vec2 source){
            p1.position = new Vec2(line.x1, line.y1);
            p2.position = new Vec2(line.x2, line.y2);
            p1.segment = this;
            p2.segment = this;
            this.source = source;
            calculateDistance();
            calculateEndPointAngles();
            setSegmentBeginning();
    }

    void calculateDistance(){
        p1.dist = Math.abs(source.distance(p1.position));
        p2.dist = Math.abs(source.distance(p2.position));
    }

    void calculateEndPointAngles(){
        double x = source.getX();
        double y = source.getY();
        double dx = 0.5 * (p1.position.getX() + p2.position.getX()) - x;
        double dy = 0.5 * (p1.position.getY() + p2.position.getY()) - y;

        d = (dx * dx) + (dy * dy);
        p1.angle = Math.atan2(p1.position.getY() - y, p1.position.getX() - x);
        p2.angle = Math.atan2(p2.position.getY() - y, p2.position.getX() - x);

    }

    void setSegmentBeginning(){
        double dAngle = p2.angle - p1.angle;

        if (dAngle <= -Math.PI)
            dAngle += 2 * Math.PI;
        if (dAngle > Math.PI)
            dAngle -= 2 * Math.PI;

        p1.beginsSegment = dAngle > 0;
        p2.beginsSegment = !p1.beginsSegment;
    }
}
