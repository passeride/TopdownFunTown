package com.bluebook.physics.listeners;

import com.bluebook.physics.Collider;

/**
 * This will handle collision enter and exit
 */
public interface OnCollisionListener {

    void onCollision(Collider other);

    //void onCollisionExit(Collider other);

}
