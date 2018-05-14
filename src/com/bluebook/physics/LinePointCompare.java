package com.bluebook.physics;

import com.bluebook.util.GameObject;

import java.util.Comparator;

/**
 * Used to sort a list of linePoints, this is not static due to the needed persistence of the gameobject whitch represent the relative point they should be sorted from
 * They are sorted in a counter-clockwise fashion
 */
public class LinePointCompare implements Comparator<LinePoint> {

    GameObject source;

    public LinePointCompare(GameObject source) {
        this.source = source;
    }

    /**
     * Dummy constructor
     */
    public LinePointCompare() {

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
