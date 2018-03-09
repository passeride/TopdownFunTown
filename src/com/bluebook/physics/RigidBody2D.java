package com.bluebook.physics;

import com.bluebook.util.Component;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;

public class RigidBody2D extends Component {

    private Vector2 linearVelocity;
    private Vector2 linearDrag;
    private Vector2 angularVelocity;
    private Vector2 angularDrag;
    private double velocityThreshold = 200;
    GameObject go;

    public RigidBody2D(GameObject go){
        this.go = go;
        linearVelocity = Vector2.ZERO;
        linearDrag = new Vector2(2, 2);
        angularVelocity = Vector2.ZERO;
        angularDrag = new Vector2(0.002, 0.002);
    }

    public void update(double delta){
        if(Math.abs(linearVelocity.getX()) > velocityThreshold || Math.abs(linearVelocity.getY()) > velocityThreshold) {
            linearVelocity = Vector2.multiply(linearVelocity, new Vector2(1 - linearDrag.getX() * delta, 1 - linearDrag.getY() * delta));
        }else{
            linearVelocity = Vector2.ZERO;
        }
        angularVelocity = Vector2.multiply(angularVelocity, new Vector2( 1 - angularDrag.getX() * delta, 1 - angularDrag.getY() * delta));
    }

    public void addForce(Vector2 force){
        linearVelocity = Vector2.add(linearVelocity, force);
    }

    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    public void setLinearVelocity(Vector2 linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    public Vector2 getLinearDrag() {
        return linearDrag;
    }

    public void setLinearDrag(Vector2 linearDrag) {
        this.linearDrag = linearDrag;
    }

    public void setAngularVelocity(Vector2 angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public Vector2 getAngularDrag() {
        return angularDrag;
    }

    public void setAngularDrag(Vector2 angularDrag) {
        this.angularDrag = angularDrag;
    }

    public GameObject getGo() {
        return go;
    }

    public void setGo(GameObject go) {
        this.go = go;
    }

    public Vector2 getAngularVelocity(){
        return angularVelocity;
    }



}
