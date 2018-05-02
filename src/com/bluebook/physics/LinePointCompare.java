package com.bluebook.physics;

import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import java.util.Comparator;

public class LinePointCompare implements Comparator<LinePoint> {

    GameObject source;

    public LinePointCompare(GameObject source){
        this.source = source;
    }

    /**
     * Dummy constructor
     */
    public LinePointCompare(){

    }

    @Override
    public int compare(LinePoint pointA, LinePoint pointB) {
        if (pointA.angle > pointB.angle) return 1;
        if (pointA.angle < pointB.angle) return -1;
        if (!pointA.beginsSegment && pointB.beginsSegment) return 1;
        if (pointA.beginsSegment && !pointB.beginsSegment) return -1;
        return 0;
    }
}
