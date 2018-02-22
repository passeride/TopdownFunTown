package com.bluebook.graphics;

import com.bluebook.util.Vector2;

import java.util.*;

public class ConvexHull {

    /**
     * An enum denoting a directional-turn between 3 Vector2s (vectors).
     */
    protected static enum Turn { CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR }

    /**
     * Returns true iff all Vector2s in <code>Vector2s</code> are collinear.
     *
     * @param Vector2s the list of Vector2s.
     * @return       true iff all Vector2s in <code>Vector2s</code> are collinear.
     */
    protected static boolean areAllCollinear(List<Vector2> Vector2s) {

        if(Vector2s.size() < 2) {
            return true;
        }

        final Vector2 a = Vector2s.get(0);
        final Vector2 b = Vector2s.get(1);

        for(int i = 2; i < Vector2s.size(); i++) {

            Vector2 c = Vector2s.get(i);

            if(getTurn(a, b, c) != Turn.COLLINEAR) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the convex hull of the Vector2s created from <code>xs</code>
     * and <code>ys</code>. Note that the first and last Vector2 in the returned
     * <code>List&lt;java.awt.Vector2&gt;</code> are the same Vector2.
     *
     * @param xs the x coordinates.
     * @param ys the y coordinates.
     * @return   the convex hull of the Vector2s created from <code>xs</code>
     *           and <code>ys</code>.
     * @throws IllegalArgumentException if <code>xs</code> and <code>ys</code>
     *                                  don't have the same size, if all Vector2s
     *                                  are collinear or if there are less than
     *                                  3 unique Vector2s present.
     */
    public static List<Vector2> getConvexHull(int[] xs, int[] ys) throws IllegalArgumentException {

        if(xs.length != ys.length) {
            throw new IllegalArgumentException("xs and ys don't have the same size");
        }

        List<Vector2> Vector2s = new ArrayList<Vector2>();

        for(int i = 0; i < xs.length; i++) {
            Vector2s.add(new Vector2(xs[i], ys[i]));
        }

        return getConvexHull(Vector2s);
    }

    /**
     * Returns the convex hull of the Vector2s created from the list
     * <code>Vector2s</code>. Note that the first and last Vector2 in the
     * returned <code>List&lt;java.awt.Vector2&gt;</code> are the same
     * Vector2.
     *
     * @param Vector2s the list of Vector2s.
     * @return       the convex hull of the Vector2s created from the list
     *               <code>Vector2s</code>.
     * @throws IllegalArgumentException if all Vector2s are collinear or if there
     *                                  are less than 3 unique Vector2s present.
     */
    public static List<Vector2> getConvexHull(List<Vector2> Vector2s) throws IllegalArgumentException {

        List<Vector2> sorted = new ArrayList<Vector2>(getSortedVector2Set(Vector2s));

        if(sorted.size() < 3) {
            throw new IllegalArgumentException("can only create a convex hull of 3 or more unique Vector2s");
        }

        if(areAllCollinear(sorted)) {
            throw new IllegalArgumentException("cannot create a convex hull from collinear Vector2s");
        }

        Stack<Vector2> stack = new Stack<Vector2>();
        stack.push(sorted.get(0));
        stack.push(sorted.get(1));

        for (int i = 2; i < sorted.size(); i++) {

            Vector2 head = sorted.get(i);
            Vector2 middle = stack.pop();
            Vector2 tail = stack.peek();

            Turn turn = getTurn(tail, middle, head);

            switch(turn) {
                case COUNTER_CLOCKWISE:
                    stack.push(middle);
                    stack.push(head);
                    break;
                case CLOCKWISE:
                    i--;
                    break;
                case COLLINEAR:
                    stack.push(head);
                    break;
            }
        }

        // close the hull
        stack.push(sorted.get(0));

        return new ArrayList<Vector2>(stack);
    }

    /**
     * Returns the Vector2s with the lowest y coordinate. In case more than 1 such
     * Vector2 exists, the one with the lowest x coordinate is returned.
     *
     * @param Vector2s the list of Vector2s to return the lowest Vector2 from.
     * @return       the Vector2s with the lowest y coordinate. In case more than
     *               1 such Vector2 exists, the one with the lowest x coordinate
     *               is returned.
     */
    protected static Vector2 getLowestVector2(List<Vector2> Vector2s) {

        Vector2 lowest = Vector2s.get(0);

        for(int i = 1; i < Vector2s.size(); i++) {

            Vector2 temp = Vector2s.get(i);

            if(temp.getY() < lowest.getY() || (temp.getY() == lowest.getY() && temp.getX() < lowest.getX())) {
                lowest = temp;
            }
        }

        return lowest;
    }

    /**
     * Returns a sorted set of Vector2s from the list <code>Vector2s</code>. The
     * set of Vector2s are sorted in increasing order of the angle they and the
     * lowest Vector2 <tt>P</tt> make with the x-axis. If tow (or more) Vector2s
     * form the same angle towards <tt>P</tt>, the one closest to <tt>P</tt>
     * comes first.
     *
     * @param Vector2s the list of Vector2s to sort.
     * @return       a sorted set of Vector2s from the list <code>Vector2s</code>.
     */
    protected static Set<Vector2> getSortedVector2Set(List<Vector2> Vector2s) {

        final Vector2 lowest = getLowestVector2(Vector2s);

        TreeSet<Vector2> set = new TreeSet<Vector2>(new Comparator<Vector2>() {
            @Override
            public int compare(Vector2 a, Vector2 b) {

                if(a == b || a.equals(b)) {
                    return 0;
                }

                // use longs to guard against int-underflow
                double thetaA = Math.atan2((long)a.getY()- lowest.getY(), (long)a.getX() - lowest.getX());
                double thetaB = Math.atan2((long)b.getY() - lowest.getY(), (long)b.getX() - lowest.getX());

                if(thetaA < thetaB) {
                    return -1;
                }
                else if(thetaA > thetaB) {
                    return 1;
                }
                else {
                    // collinear with the 'lowest' Vector2, let the Vector2 closest to it come first

                    // use longs to guard against int-over/underflow
                    double distanceA = Math.sqrt((((long)lowest.getX() - a.getX()) * ((long)lowest.getX() - a.getX())) +
                            (((long)lowest.getY() - a.getY()) * ((long)lowest.getY() - a.getY())));
                    double distanceB = Math.sqrt((((long)lowest.getX() - b.getX()) * ((long)lowest.getX() - b.getX())) +
                            (((long)lowest.getY() - b.getY()) * ((long)lowest.getY() - b.getY())));

                    if(distanceA < distanceB) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
            }
        });

        set.addAll(Vector2s);

        return set;
    }

    /**
     * Returns the GrahamScan#Turn formed by traversing through the
     * ordered Vector2s <code>a</code>, <code>b</code> and <code>c</code>.
     * More specifically, the cross product <tt>C</tt> between the
     * 3 Vector2s (vectors) is calculated:
     *
     * <tt>(b.x-a.x * c.y-a.y) - (b.y-a.y * c.x-a.x)</tt>
     *
     * and if <tt>C</tt> is less than 0, the turn is CLOCKWISE, if
     * <tt>C</tt> is more than 0, the turn is COUNTER_CLOCKWISE, else
     * the three Vector2s are COLLINEAR.
     *
     * @param a the starting Vector2.
     * @param b the second Vector2.
     * @param c the end Vector2.
     * @return the GrahamScan#Turn formed by traversing through the
     *         ordered Vector2s <code>a</code>, <code>b</code> and
     *         <code>c</code>.
     */
    protected static Turn getTurn(Vector2 a, Vector2 b, Vector2 c) {

        // use longs to guard against int-over/underflow
        long crossProduct = (((long)b.getX() - (long)a.getX()) * ((long)c.getY() - (long)a.getY())) -
                (((long)b.getY() - (long)a.getY()) * ((long)c.getX() - (long)a.getX()));

        if(crossProduct > 0) {
            return Turn.COUNTER_CLOCKWISE;
        }
        else if(crossProduct < 0) {
            return Turn.CLOCKWISE;
        }
        else {
            return Turn.COLLINEAR;
        }
    }
}