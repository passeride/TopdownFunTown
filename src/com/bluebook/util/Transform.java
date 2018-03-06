package com.bluebook.util;

import java.util.ArrayList;

/**
 * Transform keeps the position, rotation,  scale and hierarcical position of the object into consideration
 */
public class Transform extends Component{

    protected Vector2 position;
    protected Vector2 rotation;
    protected Vector2 scale;
    protected Vector2 position_offsett;
    protected Vector2 rotation_offsett;
    protected Vector2 scale_offsett;
    protected Transform parent;
    protected ArrayList<Transform> children = new ArrayList<>();

    /**
     * Creating base constructor for root level transforms
     */
    public Transform(){
        position = Vector2.ZERO;
        rotation = Vector2.ZERO;
        scale = new Vector2(1, 1);
    }

    public void translate(Vector2 moveVector){
        position = Vector2.add(position, moveVector);
    }

    private Vector2 getPositionOffsett(){
        if(parent != null)
            return Vector2.add(Vector2.rotateVectorAroundPoint(position, Vector2.ZERO, parent.getLocalRotation().getAngleInDegrees()), parent.getPositionOffsett());
        else
            return position;
    }

    private Vector2 getRotationOffsett(){
        if(parent != null)
            return Vector2.add(rotation, parent.getRotationOffsett());
        else
            return rotation;
    }

    private Vector2 getScaleoffsett(){
        if(parent != null)
            return Vector2.multiply(scale, parent.getScaleoffsett());
        else
            return scale;
    }

    public Vector2 getGlobalPosition(){
        return getPositionOffsett();
    }

    public Vector2 getGlobalScale(){
        return getScaleoffsett();
    }

    public Vector2 getGlobalRotation(){
        return getRotationOffsett();
    }

    public Vector2 getLocalPosition() {
        return position;
    }

    public void setLocalPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getLocalRotation() {
        return rotation;
    }

    public void setLocalRotation(Vector2 rotation) {
        this.rotation = rotation;
    }

    public Vector2 getLocalScale() {
        return scale;
    }

    public void setLocalScale(Vector2 scale) {
        this.scale = scale;
    }

    public Transform getParent() {
        return parent;
    }

    public Vector2 getPosition_offsett() {
        return position_offsett;
    }

    public void setPosition_offsett(Vector2 position_offsett) {
        this.position_offsett = position_offsett;
    }

    public Vector2 getRotation_offsett() {
        return rotation_offsett;
    }

    public void setRotation_offsett(Vector2 rotation_offsett) {
        this.rotation_offsett = rotation_offsett;
    }

    public Vector2 getScale_offsett() {
        return scale_offsett;
    }

    public void setScale_offsett(Vector2 scale_offsett) {
        this.scale_offsett = scale_offsett;
    }

    public void setParent(Transform parent) {
        // Un assign from previous parent
        if(this.parent != null)
                this.parent.removeChild(this);
        // Assign as child in new parent
        this.parent = parent;
        parent.addChild(this);
    }

    private void addChild(Transform child){
        children.add(child);
    }

    private void removeChild(Transform child){
        if(children.contains(child))
            children.remove(child);
    }

}
