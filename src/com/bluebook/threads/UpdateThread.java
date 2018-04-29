package com.bluebook.threads;

import com.bluebook.engine.GameEngine;

/**
 * A thread to handle logic for each tick
 */
public class UpdateThread implements Runnable {

    private GameEngine engine;
    private float frameRate = 60f;
    private long sleepTime = (long) ((1f / frameRate) * 1000f);
    private volatile boolean running = false;

    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;

    /**
     * Used to create the UpdateThread Will each tick call {@link GameEngine#update(double delta)}
     */
    public UpdateThread(GameEngine engine) {
        this.engine = engine;
    }

    @Override
    public void run() {
        running = true;
        long prevTick = System.currentTimeMillis();
        while (running) {

            long timeElapsed = System.currentTimeMillis() - prevTick;
            engine.update((double) timeElapsed / 1000);
            prevTick = System.currentTimeMillis();

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
                GameEngine.getInstance().update_FPS = 1_000_000_000.0 / elapsedNanosPerFrame;
            }

            try {
                Thread.sleep(sleepTime);

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
