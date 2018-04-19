package com.bluebook.physics.listeners;

import com.bluebook.physics.Collider;

/**
 * This will trigger on collision
 */
public interface OnCollisionListener {

    void onCollision(Collider other);
}
