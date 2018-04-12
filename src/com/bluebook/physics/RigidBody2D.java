package com.bluebook.physics;

import com.bluebook.util.Component;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;

public class RigidBody2D extends Component {

    public static final boolean VelocityVerlet = true;

    public double mass = 1.0;
    public Vector2 acceleration = new Vector2(0,0);
    public Vector2 avgAcceleration = new Vector2(0, 0);
    public Vector2 velocity = new Vector2(0,0);
    public Vector2 position = new Vector2(0,0);
    public double friction = .7;
    public GameObject gameObject;

//    private Vector2 linearVelocity;
//    private Vector2 linearDrag;
//    private Vector2 angularVelocity;
//    private Vector2 angularDrag;
//    //TODO: Replace with FALLOFF CURVE
//    private double velocityThreshold = 0;
//    GameObject go;

    public RigidBody2D(GameObject go){
        this.gameObject = go;
        position = this.gameObject.getTransform().getGlobalPosition();
//        linearVelocity = Vector2.ZERO;
//        linearDrag = new Vector2(2, 2);
//        angularVelocity = Vector2.ZERO;
//        angularDrag = new Vector2(0.002, 0.002);
    }

    /**
     * Update updateds the object's position with new information from the physics.
     * This must be called every tick
     * @param delta deltaTime
     */
    public void update(double delta){
//        if(Math.abs(linearVelocity.getX()) > velocityThreshold || Math.abs(linearVelocity.getY()) > velocityThreshold) {
//            linearVelocity = Vector2.multiply(linearVelocity, new Vector2(1 - linearDrag.getX() * delta, 1 - linearDrag.getY() * delta));
//        }else{
//            linearVelocity = Vector2.ZERO;
//        }
//        angularVelocity = Vector2.multiply(angularVelocity, new Vector2( 1 - angularDrag.getX() * delta, 1 - angularDrag.getY() * delta));

        if(VelocityVerlet){
            // Velocity Verlet
            velocity = Vector2.add(velocity, Vector2.multiply(avgAcceleration, delta));
            velocity = Vector2.multiply(velocity, 1 - friction * delta);

            Vector2 last_acceleration = acceleration;
            double px = position.getX();
            double py = position.getY();
            px += velocity.getX() * delta + ( 0.5 * last_acceleration.getX() * Math.pow(delta, 2));
            py += velocity.getY() * delta + ( 0.5 * last_acceleration.getY() * Math.pow(delta, 2));
            position = new Vector2(px, py);

            avgAcceleration = Vector2.ZERO;
            acceleration = Vector2.ZERO;

        }else {

            //Euler's method
            velocity = Vector2.add(velocity, Vector2.multiply(acceleration, delta));
            position = Vector2.add(position, Vector2.multiply(velocity, delta));

        }
    }

    public void addForce(Vector2 force){
//        linearVelocity = Vector2.add(linearVelocity, force);
        if(VelocityVerlet) {
            //  Velocity Verlet
            Vector2 lastAcceleration = acceleration;
            Vector2 newAcceleration = Vector2.add(acceleration, Vector2.devide(force, mass));
            avgAcceleration = Vector2.devide(Vector2.add(lastAcceleration, newAcceleration), 2);
            acceleration = newAcceleration;
        }else {

            // Euler's Method
            acceleration = Vector2.add(acceleration, Vector2.devide(force, mass));
        }
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

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }

    public Vector2 getAvgAcceleration() {
        return avgAcceleration;
    }

    public void setAvgAcceleration(Vector2 avgAcceleration) {
        this.avgAcceleration = avgAcceleration;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }
}
