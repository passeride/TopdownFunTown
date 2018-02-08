package com.bluebook.util;

import java.awt.geom.Point2D;

/**
 * This class is used for positions and angles
 */
public class Vector2 extends Point2D{

    private double X;
    private double Y;

    public static final Vector2 UP = new Vector2(0, -1);
    public static final Vector2 DOWN = new Vector2(0, 1);
    public static final Vector2 LEFT = new Vector2(-1, 0);
    public static final Vector2 RIGHT = new Vector2(1, 0);
    public static final Vector2 ZERO = new Vector2(0, 0);

    /**
     * 2 dimensional vector
     * @param X Coordinates
     * @param Y Coordinates
     */
    public Vector2(double X, double Y){
        this.X = X;
        this.Y = Y;
    }

    /**
     * Will create a normalized vector based on radians input
     * @param radians
     * @return Vector2 Object
     */
    public static Vector2 Vector2FromAgnleInRadians(double radians){
        return new Vector2(Math.cos(radians), Math.sin(radians));
    }

    /**
     * Will create a normalized vector based on radians input
     * @param angle
     * @return Vector2 Object
     */
    public static Vector2 Vector2FromAngleInDegrees(double angle){
        return new Vector2(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)));
    }

    /**
     * Will add these two vectors together
     * @param a
     * @param b
     * @return a new vector resutling form the addition of the two input vectors
     */
    public static Vector2 add(Vector2 a, Vector2 b){
        return new Vector2(a.X + b.X, a.Y + b.Y);
    }

    /**
     * Returns a new vector resulting from a - b
     * @param a
     * @param b
     * @return
     */
    public static Vector2 subtract(Vector2 a, Vector2 b){
        return new Vector2(a.X - b.X, a.Y - b.Y);
    }

    /**
     * Will return the dotproduct of a and b
     * @param a
     * @param b
     * @return
     */
    public static double dotProduct(Vector2 a, Vector2 b){
        return a.X * b.X + a.Y * b.Y;
    }

    /**
     * Will return the angle between two points in degrees
     * @param a
     * @param b
     * @return
     */
    public static double getAngleBetweenInDegrees(Vector2 a, Vector2 b){
        double angle = Math.toDegrees(Math.atan2(b.Y - a.Y, b.X - a.X));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }

    /**
     * Will return a vector that is a multiplied by b
     * @param a
     * @param b
     * @return
     */
    public static Vector2 multiply(Vector2 a, double b){
        return new Vector2(a.X * b, a.Y * b);
    }

    /**
     * Returns the length of the vector
     * @return length of vector
     */
    public double getLength(){
        return Math.sqrt(X * X + Y * Y);
    }

    /**
     * Gets the distance between two vectors
     * @param dest Second vector to get distance from
     * @return Distance in units
     */
    public double distance(Vector2 dest){
        return Math.sqrt(Math.pow( X - dest.X, 2) + Math.pow(Y - dest.Y, 2));
    }

    /**
     * Gives the angle of a given vector
     * @return vector in degrees
     */
    public double getAngleInDegrees(){
        return Math.atan2(Y, X)*180/Math.PI;
    }

    /**
     * Gives the angle of a given vector
     * @return vector in radians
     */
    public double getAngleInRadians(){
        return Math.atan2(Y, X);
    }

    /**
     * Returns a new vector2 normalized object
     * @return The normalized vector
     */
    public Vector2 getNormalizedVector(){
        Vector2 ret = new Vector2(X, Y);
        double length = ret.getLength();
        ret.X /= length;
        ret.Y /= length;
        return ret;
    }

    /**
     * Normalizes vector
     * The vector will now have the same direction but with a norm length (Length of 1)
     */
    public void normalize(){
        double length = getLength();
        X /= length;
        Y /= length;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.X = x;
        this.Y = y;
    }

    public void setY(double y) {
        Y = y;
    }

}
