package com.bluebook.util;

import java.awt.geom.Point2D;

/**
 * This class is used for positions and angles
 */
public class Vec2 extends Point2D {

    private double X;
    private double Y;

    public static final Vec2 UP = new Vec2(0, -1);
    public static final Vec2 DOWN = new Vec2(0, 1);
    public static final Vec2 LEFT = new Vec2(-1, 0);
    public static final Vec2 RIGHT = new Vec2(1, 0);
    public static final Vec2 ZERO = new Vec2(0, 0);

    /**
     * 2 dimensional vector
     *
     * @param X Coordinates
     * @param Y Coordinates
     */
    public Vec2(double X, double Y) {
        this.X = X;
        this.Y = Y;
    }

    /**
     * Will create a normalized vector based on radians input
     *
     * @return Vec2 Object
     */
    public static Vec2 Vector2FromAgnleInRadians(double radians) {
        return new Vec2(Math.cos(radians), Math.sin(radians));
    }

    /**
     * Will create a normalized vector based on radians input
     *
     * @return Vec2 Object
     */
    public static Vec2 Vector2FromAngleInDegrees(double angle) {
        return new Vec2(Math.cos(Math.toRadians(angle)), Math.sin(Math.toRadians(angle)));
    }

    /**
     * Will add these two vectors together
     *
     * @return a new vector resutling form the addition of the two input vectors
     */
    public static Vec2 add(Vec2 a, Vec2 b) {
        return new Vec2(a.X + b.X, a.Y + b.Y);
    }

    /**
     * Returns a new vector resulting from a - b
     */
    public static Vec2 subtract(Vec2 a, Vec2 b) {
        return new Vec2(a.X - b.X, a.Y - b.Y);
    }

    /**
     * Will return the dotproduct of a and b
     */
    public static double dotProduct(Vec2 a, Vec2 b) {
        return a.X * b.X + a.Y * b.Y;
    }

    /**
     * Will return the angle between two points in degrees
     */
    public static double getAngleBetweenInDegrees(Vec2 a, Vec2 b) {
        double angle = Math.toDegrees(Math.atan2(b.Y - a.Y, b.X - a.X));

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    /**
     * Will return a vector that is a multiplied by b
     */
    public static Vec2 multiply(Vec2 a, double b) {
        return new Vec2(a.X * b, a.Y * b);
    }

    public static Vec2 multiply(Vec2 a, Vec2 b) {
        return new Vec2(a.getX() * b.getX(), a.getY() * b.getY());
    }


    public static Vec2 devide(Vec2 a, Vec2 b) {
        return new Vec2(a.getX() / b.getX(), a.getY() / b.getY());
    }

    public static Vec2 devide(Vec2 a, double b) {
        return new Vec2(a.getX() / b, a.getY() / b);

    }

    /**
     * Returns the length of the vector
     *
     * @return length of vector
     */
    public double getLength() {
        return Math.sqrt(X * X + Y * Y);
    }

    /**
     * Gets the distance between two vectors
     *
     * @param dest Second vector to get distance from
     * @return Distance in units
     */
    public double distance(Vec2 dest) {
        return Math.sqrt(Math.pow(X - dest.X, 2) + Math.pow(Y - dest.Y, 2));
    }

    /**
     * Gives the angle of a given vector
     *
     * @return vector in degrees
     */
    public double getAngleInDegrees() {
        return Math.atan2(Y, X) * 180 / Math.PI;
    }

    /**
     * Gives the angle of a given vector
     *
     * @return vector in radians
     */
    public double getAngleInRadians() {
        return Math.atan2(Y, X);
    }

    /**
     * Returns a new vector2 normalized object
     *
     * @return The normalized vector
     */
    public Vec2 getNormalizedVector() {
        Vec2 ret = new Vec2(X, Y);
        double length = ret.getLength();
        ret.X /= length;
        ret.Y /= length;
        return ret;
    }


    /**
     * Will rotate vec around rotationPoint the amout of degrees specified in angle,  and return new
     * vector
     *
     * @param vec Vector to be rotated
     * @param rotationPoint Point of rotation
     * @param angle Angle in degrees to be rotated
     * @return A new Vector
     */
    public static Vec2 rotateVectorAroundPoint(Vec2 vec, Vec2 rotationPoint,
        double angle) {
        /*
        x_rotated = ((x - x_origin) * cos(angle)) - ((y_origin - y) * sin(angle)) + x_origin
        y_rotated = ((y_origin - y) * cos(angle)) - ((x - x_origin) * sin(angle)) + y_origins
         */
        angle = Math.toRadians(angle);
        double x = (vec.getX() - rotationPoint.getX()) * Math.cos(angle)
            - (rotationPoint.getY() - vec.getY()) * Math.sin(angle) + rotationPoint.getX();
        double y = (rotationPoint.getY() - vec.getY()) * Math.cos(angle) + (
            (vec.getX() - rotationPoint.getX()) * Math.sin(angle)) + rotationPoint.getY();

        return new Vec2(x, y);
    }

    /**
     * Normalizes vector The vector will now have the same direction but with a norm length (Length of
     * 1)
     */
    public void normalize() {
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
