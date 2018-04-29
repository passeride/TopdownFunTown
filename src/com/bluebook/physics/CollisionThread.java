package com.bluebook.physics;

import com.bluebook.engine.GameEngine;

/**
 * This will check for collisions and notify
 *
 * And will go use FrameRate variable to
 */
public class CollisionThread implements Runnable {

    private HitDetectionHandler hitDet;
    private float frameRate = 60f;
    private long sleepTime = (long) ((1f / frameRate) * 1000f);
    private volatile boolean running = false;
    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;

    public CollisionThread() {
        hitDet = HitDetectionHandler.getInstance();
    }

    @Override
    public void run() {
        running = true;
        while (running) {

            long startTime = System.currentTimeMillis();
            hitDet.updatePositions();
            hitDet.lookForCollision();
            long prevTick = System.currentTimeMillis();
            long processTime = prevTick - startTime;
            long newSleepTime = sleepTime - processTime;

            // Finding FSP for debugging
            long now = System.nanoTime();
            long oldFrameTime = frameTimes[frameTimeIndex];
            frameTimes[frameTimeIndex] = now;
            frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
            if (frameTimeIndex == 0) {
                arrayFilled = true;
            }
            if (arrayFilled) {
                long elapsedNanos = now - oldFrameTime;
                long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
                GameEngine.getInstance().collision_FPS = 1_000_000_000.0 / elapsedNanosPerFrame;
            }

            try {
                Thread.sleep(newSleepTime > 0 ? newSleepTime : 0);

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void terminate() {
        running = false;
    }
}
