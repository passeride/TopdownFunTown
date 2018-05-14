package com.bluebook.physics;

import com.bluebook.util.Vec2;

/**
 * Simple dataobject used by {@link Light2D}
 */
public class LinePoint {

    public Vec2 position;
    public boolean beginsSegment = false;
    public LineSegment segment;
    double angle;
    double dist;


}
