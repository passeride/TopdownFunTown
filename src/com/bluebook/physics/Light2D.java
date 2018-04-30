package com.bluebook.physics;

import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import java.util.ArrayList;

public class Light2D {

    public static Light2D light;

    GameObject source;

    public double[][] polygon;

    public Light2D(GameObject source) {
        this.source = source;
        light = this;
    }


    public double[][] calculateVisibility(ArrayList<LineSegment> list) throws  IllegalArgumentException{

        ArrayList<LinePoint> points = new ArrayList<>();

        list.forEach(p -> points.add(p.p1));
        list.forEach(p -> points.add(p.p2));

        LinePointCompare lpc = new LinePointCompare(source);

        points.sort(lpc);

        ArrayList<Vec2> outputPoints = new ArrayList<>();
        ArrayList<LineSegment> openSegments = new ArrayList<>();
        Vec2 pos = source.getTransform().getGlobalPosition();

        LinePoint prevPoint = points.get(0);

        double beginAngle = 0;
        LineSegment openSegment = null;

        boolean changedSegment = false;


        for(int pass = 0; pass < 2; pass++) {
            for (LinePoint lp : points) {
                openSegments.sort((o1, o2) -> segmentInFrontOf(o1, o2, pos) ? 1 : -1);

                LineSegment nearestStart = null;
                if (openSegments.size() > 0)
                    nearestStart = openSegments.get(0);

                if (lp.beginsSegment) {
                    openSegments.add(lp.segment);
                } else {
                    openSegments.remove(lp.segment);
                }

                openSegments.sort((o1, o2) -> segmentInFrontOf(o1, o2, pos) ? 1 : -1);
                LineSegment nearestEnd = null;
                if (openSegments.size() > 0)
                    nearestEnd = openSegments.get(0);

                if (nearestStart != nearestEnd) {
                    if(pass == 1) {
                        outputPoints.addAll(getOutPoints(pos, beginAngle, lp.angle, nearestStart));
                    }
                    beginAngle = lp.angle;

                }

            }
        }

        double[][] polygon = new double[2][outputPoints.size()];

        for (int i = 0; i < polygon[0].length; i++) {
            Vec2 p = outputPoints.get(i);
            polygon[0][i] = p.getX();
            polygon[1][i] = p.getY();
        }

        this.polygon = polygon;
        return polygon;

    }

    /*
bool pointInPolygon() {

  int   i, j=polyCorners-1 ;
  bool  oddNodes=NO      ;

  for (i=0; i<polyCorners; i++) {
    if (polyY[i]<y && polyY[j]>=y
    ||  polyY[j]<y && polyY[i]>=y) {
      if (polyX[i]+(y-polyY[i])/(polyY[j]-polyY[i])*(polyX[j]-polyX[i])<x) {
        oddNodes=!oddNodes;
        }
      }
    j=i;
    }

  return oddNodes; }
     */

    public boolean pointInPoly(Vec2 point){
        try {
            if (polygon == null)
                return false;

            double x = point.getX();
            double y = point.getY();

            double[] polyY = polygon[1].clone();
            double[] polyX = polygon[0].clone();

            int j = polyY.length - 1;

            boolean oddNodes = false;

            for (int i = 0; i < polyY.length; i++) {
                if ((polyY[i] < y && polyY[j] >= y
                    || polyY[j] < y && polyY[i] >= y)
                    && (polyX[i] <= x || polyX[j] <= x)) {
                    if (polyX[i] + (y - polyY[i]) / (polyY[j] - polyY[i]) * (polyX[j] - polyX[i])
                        < x) {
                        oddNodes = !oddNodes;
                    }
                }
                j = i;
            }
            return oddNodes;
        }catch(ArrayIndexOutOfBoundsException exception) {
            return false;
        }

    }


    ArrayList<Vec2> getOutPoints(Vec2 origin, double angle1, double angle2, LineSegment segment) {
        ArrayList<Vec2> ret = new ArrayList<>();
        Vec2 p1 = origin;
        Vec2 p2 = new Vec2(origin.getX() + Math.cos(angle1), origin.getY() + Math.sin(angle1));
        Vec2 p3 = new Vec2(0, 0);
        Vec2 p4 = new Vec2(0, 0);
        if (segment != null) {
            p3.setX(segment.p1.position.getX());
            p3.setY(segment.p1.position.getY());

            p4.setX(segment.p2.position.getX());
            p4.setY(segment.p2.position.getY());
        } else {
            p3.setX(origin.getX() + Math.cos(angle1) * 200);
            p3.setY(origin.getY() + Math.sin(angle1) * 200);

            p4.setX(origin.getX() + Math.cos(angle2) * 200);
            p4.setY(origin.getY() + Math.sin(angle2) * 200);
        }

        Vec2 begin = lineIntersection(p3, p4, p1, p2);

        p2.setX(origin.getX() + Math.cos(angle2));
        p2.setY(origin.getY() + Math.sin(angle2));

        Vec2 end = lineIntersection(p3, p4, p1, p2);

        ret.add(begin);
        ret.add(end);

        return ret;
    }

    Vec2 lineIntersection(Vec2 point1, Vec2 point2, Vec2 point3, Vec2 point4) {
        double s = (
            (point4.getX() - point3.getX()) * (point1.getY() - point3.getY()) -
                (point4.getY() - point3.getY()) * (point1.getX() - point3.getX())
        ) / (
            (point4.getY() - point3.getY()) * (point2.getX() - point1.getX()) -
                (point4.getX() - point3.getX()) * (point2.getY() - point1.getY())
        );

        double x = point1.getX() + s * (point2.getX() - point1.getX());
        double y = point1.getY() + s * (point2.getY() - point1.getY());
        return new Vec2(x, y);
    }

    boolean segmentInFrontOf(LineSegment a, LineSegment b, Vec2 relativePoint) {

        if(a == null || b == null || relativePoint == null)
            return false;

        boolean A1 = leftOf(a, interpolate(b.p1.position, b.p2.position, 0.01));
        boolean A2 = leftOf(a, interpolate(b.p2.position, b.p1.position, 0.01));
        boolean A3 = leftOf(a, relativePoint);
        boolean B1 = leftOf(b, interpolate(a.p1.position, a.p2.position, 0.01));
        boolean B2 = leftOf(b, interpolate(a.p2.position, a.p1.position, 0.01));
        boolean B3 = leftOf(b, relativePoint);

        if (B1 == B2 && B2 != B3) {
            return true;
        }
        if (A1 == A2 && A2 == A3) {
            return true;
        }
        if (A1 == A2 && A2 != A3) {
            return false;
        }
        if (B1 == B2 && B2 == B3) {
            return false;
        }

        return false;
    }

    Vec2 interpolate(Vec2 a, Vec2 b, double f) {
        double x = a.getX() * (1 - f) + b.getX() * f;
        double y = a.getY() * (1 - f) + b.getY() * f;
        return new Vec2(x, y);
    }

    boolean leftOf(LineSegment ls, Vec2 lp) {
        double cross =
            (ls.p2.position.getX() - ls.p1.position.getX()) * (lp.getY() - ls.p1.position.getY())
                - (ls.p2.position.getY() - ls.p1.position.getY()) * (lp.getX() - ls.p1.position
                .getX());
        return cross < 0;
    }

}