package com.bluebook.physics;

import com.bluebook.engine.GameEngine;
import com.bluebook.physics.Collider;
import com.bluebook.physics.HitDetectionHandler;

import java.util.concurrent.BlockingQueue;

/**
 * This will check for collisions and notify
 */
public class CollisionThread implements Runnable{

    private HitDetectionHandler hitDet;
    private float frameRate = 60f;
    private long sleepTime =  (long)((1f / frameRate) * 1000f) ;
    private volatile boolean running = true;
    private long prevTick = 0;

    public CollisionThread(){
        hitDet = HitDetectionHandler.getInstance();
    }

    @Override
    public void run() {
        running = true;
        while(running){

            long timeElapsed = System.currentTimeMillis() - prevTick;
            long startTime = System.currentTimeMillis();
            hitDet.updatePositions();
            hitDet.lookForCollision();
            prevTick = System.currentTimeMillis();
            long processTime = prevTick - startTime;
            long newSleepTime = sleepTime - processTime;

            try {
                Thread.sleep(newSleepTime > 0 ?  newSleepTime : 0);

            }catch(InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean isRunning(){
        return running;
    }

    public void terminate(){
        running = false;
    }
}
