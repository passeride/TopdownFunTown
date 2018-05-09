package com.bluebook.util;

import com.bluebook.camera.OrthographicCamera;

import java.util.ArrayList;

/**
 * Transform keeps the position, rotation,  scale and hierarcical position of the object into
 * consideration
 */
public class Transform {

    private Vec2 position;
    private Vec2 rotation;
    private Vec2 scale;
    private Vec2 position_offset;
    private Vec2 rotation_offset;
    private Vec2 scale_offset;
    private Transform parent;
    private ArrayList<Transform> children = new ArrayList<>();

    /**
     * Creating base constructor for root level transforms
     */
    public Transform() {
        position = Vec2.ZERO;
        rotation = Vec2.ZERO;
        scale = new Vec2(1, 1);
    }

    public void translate(Vec2 moveVector) {
        position = Vec2.add(position, moveVector);
    }

    private Vec2 getPositionOffsett() {
        if (parent != null) {
            return Vec2.add(
                Vec2.rotateVectorAroundPoint(Vec2.multiply(position, parent.scale),
                    Vec2.ZERO,
                    parent.getLocalRotation().getAngleInDegrees()),
                parent.getPositionOffsett());
        } else if (OrthographicCamera.main != null) {
            return Vec2.add(position,
                new Vec2(OrthographicCamera.main.getX(), OrthographicCamera.main.getY()));
        } else {
            return position;
        }
    }

    private Vec2 getPositionOffsettWithoutCamera() {
        if (parent != null) {
            return Vec2.add(
                Vec2.rotateVectorAroundPoint(Vec2.multiply(position, parent.scale),
                    Vec2.ZERO,
                    parent.getLocalRotation().getAngleInDegrees()),
                parent.getPositionOffsettWithoutCamera());
        } else {
            return position;
        }
    }

    private Vec2 getRotationOffsett() {
        if (parent != null) {
            return Vec2.add(rotation, parent.getRotationOffsett());
        } else {
            return rotation;
        }
    }

    private Vec2 getScaleoffsett() {
        if (parent != null) {
            return Vec2.multiply(scale, parent.getScaleoffsett());
        } else {
            return scale;
        }
    }

    public Vec2 getWorldPosition() {
        return getPositionOffsettWithoutCamera();
    }

    public Vec2 getGlobalPosition() {
        return getPositionOffsett();
    }

    public Vec2 getGlobalScale() {
        return getScaleoffsett();
    }

    public Vec2 getGlobalRotation() {
        return getRotationOffsett();
    }

    public Vec2 getLocalPosition() {
        return position;
    }

    public void setLocalPosition(Vec2 position) {
        this.position = position;
    }

    public Vec2 getLocalRotation() {
        return rotation;
    }

    public void setLocalRotation(Vec2 rotation) {
        this.rotation = rotation;
    }

    public Vec2 getLocalScale() {
        return scale;
    }

    public void setLocalScale(Vec2 scale) {
        this.scale = scale;
    }

    public Transform getParent() {
        return parent;
    }

    public Vec2 getPosition_offset() {
        return position_offset;
    }

    public void setPosition_offset(Vec2 position_offset) {
        this.position_offset = position_offset;
    }

    public Vec2 getRotation_offset() {
        return rotation_offset;
    }

    public void setRotation_offset(Vec2 rotation_offset) {
        this.rotation_offset = rotation_offset;
    }

    public Vec2 getScale_offset() {
        return scale_offset;
    }

    public void setScale_offset(Vec2 scale_offset) {
        this.scale_offset = scale_offset;
    }

    public void setParent(Transform parent) {
        // Un assign from previous parent
        if (this.parent != null) {
            this.parent.removeChild(this);
        }
        // Assign as child in new parent
        this.parent = parent;
        parent.addChild(this);
    }

    private void addChild(Transform child) {
        children.add(child);
    }

    private void removeChild(Transform child) {
        children.remove(child);
    }

}
