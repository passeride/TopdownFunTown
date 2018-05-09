package com.bluebook.physics;

import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;

/**
 * RigidBody2D is used to add som simple rudementary physics to objects usding {@link
 * RigidBody2D#VelocityVerlet} will switch between two different physics models, but True has been
 * most kind to uss
 */
public class RigidBody2D {

    private static boolean VelocityVerlet = true;

    private double mass = 1.0;
    private Vec2 acceleration = new Vec2(0, 0);
    private Vec2 avgAcceleration = new Vec2(0, 0);
    private Vec2 velocity = new Vec2(0, 0);
    private Vec2 position;
    private double friction;
    private GameObject gameObject;

    private double velocityThreshold;

    public RigidBody2D(GameObject go) {
        this.gameObject = go;
        position = this.gameObject.getTransform().getGlobalPosition();
        VelocityVerlet = GameSettings.getBoolean("Physics_rigidbody2d_VelocityVerlet");
        friction = GameSettings.getDouble("Physics_rigidbody2d_friction");
        velocityThreshold = GameSettings.getDouble("Physics_rigidbody2d_velocity_threshhold");
    }

    /**
     * Update updateds the object's position with new information from the physics. This must be
     * called every tick
     *
     * @param delta deltaTime
     */
    public void update(double delta) {

        if (VelocityVerlet) {
            // Velocity Verlet
            velocity = Vec2.add(velocity, Vec2.multiply(avgAcceleration, delta));
            velocity = Vec2.multiply(velocity, 1 - friction);

            Vec2 last_acceleration = acceleration;
            double px = position.getX();
            double py = position.getY();
            px += velocity.getX() * delta + (0.5 * last_acceleration.getX() * Math.pow(delta, 2));
            py += velocity.getY() * delta + (0.5 * last_acceleration.getY() * Math.pow(delta, 2));
            position = new Vec2(px, py);

            avgAcceleration = Vec2.ZERO;
            acceleration = Vec2.ZERO;

            if ((Math.abs(velocity.getX()) < velocityThreshold
                || Math.abs(velocity.getY()) < velocityThreshold)) {
                velocity = Vec2.ZERO;
            }

        } else {

            //Euler's method
            velocity = Vec2.add(velocity, Vec2.multiply(acceleration, delta));
            position = Vec2.add(position, Vec2.multiply(velocity, delta));

        }
    }

    /**
     * Will push objects in direction of force
     *
     * @param force Vector of direction to be pushed
     */
    public void addForce(Vec2 force) {
//        linearVelocity = Vec2.add(linearVelocity, force);
        if (VelocityVerlet) {
            //  Velocity Verlet
            Vec2 lastAcceleration = acceleration;
            Vec2 newAcceleration = Vec2.add(acceleration, Vec2.devide(force, mass));
            avgAcceleration = Vec2.devide(Vec2.add(lastAcceleration, newAcceleration), 2);
            acceleration = newAcceleration;
        } else {

            // Euler's Method
            acceleration = Vec2.add(acceleration, Vec2.devide(force, mass));
        }

        // Set max
        acceleration.setX(Math.min(100, Math.min(0, acceleration.getX())));
        acceleration.setY(Math.min(100, Math.min(0, acceleration.getY())));

    }


    public static boolean isVelocityVerlet() {
        return VelocityVerlet;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public Vec2 getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vec2 acceleration) {
        this.acceleration = acceleration;
    }

    public Vec2 getAvgAcceleration() {
        return avgAcceleration;
    }

    public void setAvgAcceleration(Vec2 avgAcceleration) {
        this.avgAcceleration = avgAcceleration;
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec2 velocity) {
        this.velocity = velocity;
    }

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }
}
