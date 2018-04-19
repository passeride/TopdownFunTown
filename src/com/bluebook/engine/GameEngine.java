package com.bluebook.engine;

import com.bluebook.physics.CollisionThread;
import com.bluebook.renderer.CanvasRenderer;
import com.bluebook.threads.UpdateThread;
import com.bluebook.util.GameObject;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;

/**
 * This class is the main engine of the game
 */
public class GameEngine {

    private UpdateThread updateThread;
    private CollisionThread collisionThread;
    private BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(1);

    private ArrayList<GameObject> updateObjects = new ArrayList<>();

    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;

    public static boolean DEBUG = false;

    private boolean isPaused = false;

    private static GameEngine singleton;

    /**
     * Used by drawThread to update draw_FPS, this will be drawn on screen in DEBUG_MODE triggered by {@link GameEngine#DEBUG} and drawn in {@link com.bluebook.renderer.FPSLineGraph}
     */
    public double draw_FPS = 0.0;

    /**
     * Used by {@link CollisionThread} to update collision_FPS, this will be drawn on screen in DEBUG_MODE triggered by {@link GameEngine#DEBUG} and drawn in {@link com.bluebook.renderer.FPSLineGraph}
     */
    public double collision_FPS = 0.0;

    /**
     * Used by {@link UpdateThread} to update draw_FPS, this will be drawn on screen in DEBUG_MODE triggered by {@link GameEngine#DEBUG} and drawn in {@link com.bluebook.renderer.FPSLineGraph}
     */
    public double update_FPS = 0.0;

    public Canvas canvas;

    /**
     * Constructor for GameEngine
     *
     * @param canvas Canvas for game to be drawn to
     */
    public GameEngine(Canvas canvas) {
        singleton = this;
        CanvasRenderer.getInstance().setCanvas(canvas);

        this.canvas = canvas;

        updateThread = new UpdateThread(this, messageQueue);
        collisionThread = new CollisionThread();
        startAnimationTimer();

    }

    private void startAnimationTimer() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                CanvasRenderer.getInstance().drawAll();

                // Finding FSP for debugging
                long oldFrameTime = frameTimes[frameTimeIndex];
                frameTimes[frameTimeIndex] = now;
                frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
                if (frameTimeIndex == 0) {
                    arrayFilled = true;
                }
                if (arrayFilled) {
                    long elapsedNanos = now - oldFrameTime;
                    long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
                    draw_FPS = 1_000_000_000.0 / elapsedNanosPerFrame;
                }
            }
        };
        timer.start();
    }

    public void pauseGame() {
        isPaused = true;
        stopCollisionThread();
        stopUpdateThread();
    }


    public void resumeGame() {
        isPaused = false;
        startUpdateThread();
        startCollisionThread();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public static GameEngine getInstance() {
        return singleton;
    }

    /**
     * This is used by the constructor of gameobject so it's update function is called
     */
    public void addGameObject(GameObject go) {
        synchronized (this) {
            updateObjects.add(go);
        }
    }

    /**
     * Removes the object from updateobjects, this is called during an gameobjects destruction
     */
    public void removeGameObject(GameObject go) {
        synchronized (this) {
            if (updateObjects.contains(go)) {
                updateObjects.remove(go);
            }
        }
    }

    /**
     * This should be called every tick from the {@link UpdateThread}
     */
    public void update(double delta) {
        synchronized (this) {
            GameApplication.getInstance().update(delta);
            GameObject[] updateGameObjects = new GameObject[updateObjects.size()];
            updateObjects.toArray(updateGameObjects);
            for (int i = 0; i < updateGameObjects.length; i++) {
                if (updateGameObjects[i] != null && updateGameObjects[i].isAlive()) {
                    updateGameObjects[i].update(delta);
                }
            }
        }
    }

    /**
     * Will start the update thread that runs concurrently to process logic
     */
    public void startUpdateThread() {
        Thread t = new Thread(updateThread);
        t.setDaemon(true);
        t.start();
    }

    /**
     * Will terminate logic thread
     */
    public void stopUpdateThread() {
        if (updateThread.isRunning()) {
            updateThread.terminate();
        }
    }

    public void startCollisionThread() {
        Thread t = new Thread(collisionThread);
        t.setDaemon(true);
        t.start();
    }

    public void stopCollisionThread() {
        if (collisionThread.isRunning()) {
            collisionThread.terminate();
        }
    }
}
