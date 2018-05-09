package com.bluebook.engine;

import com.bluebook.physics.CollisionThread;
import com.bluebook.renderer.CanvasRenderer;
import com.bluebook.threads.UpdateThread;
import com.bluebook.util.GameObject;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class is the main engine of the gamePane
 */
public class GameEngine {

    private UpdateThread updateThread;
    private CollisionThread collisionThread;

    private CopyOnWriteArrayList<GameObject> updateObjects = new CopyOnWriteArrayList<>();

    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;

    public static boolean DEBUG = false;

    private boolean isPaused = true;

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

    private Canvas canvas;

    private AnimationTimer drawTimer;

    /**
     * Constructor for GameEngine
     *
     * @param canvas Canvas for gamePane to be drawn to
     */
    public GameEngine(Canvas canvas) {
        singleton = this;
        CanvasRenderer.getInstance().setCanvas(canvas);

        this.canvas = canvas;

        updateThread = new UpdateThread(this);
        collisionThread = new CollisionThread();
        startAnimationTimer();

    }

    protected void startAnimationTimer() {
        drawTimer = new AnimationTimer() {
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
        drawTimer.start();
    }

    /**
     * Will terminate the {@link UpdateThread} and {@link CollisionThread}
     * but the drawTimer will still be called
     */
    public void pauseGame() {
        if (!isPaused) {
            new Thread(() -> {
                isPaused = true;
                stopUpdateThread();
                stopCollisionThread();
                //        drawTimer.stop();


            }).start();

        }
    }

    /**
     * Will resume the {@link UpdateThread} and {@link CollisionThread}
     * But will do so via a third thread created in this function, this is due to the possebility that
     * UpdateThread will pause this to do some changes and then resume.
     * This will not let UpdateThread go a full cycle, so it will not terminate.
     * Resulting in parlell duplicates of {@link UpdateThread}
     */
    public void resumeGame() {
        if (isPaused) {
            /*
             * If UpdateThread calls pauseGame() and then resumeGame(), it will be duplicated and run in parralell
             * Therefore we must create a seprate thread to reinstate it. With a dealy, to let it die in a safe manner.
             *
             */
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isPaused = false;
                startUpdateThread();
                startCollisionThread();
                //        drawTimer.start();
            });
            t.setName("Thread restart thread");
            t.setDaemon(true);
            t.start();
        }
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
    public synchronized void addGameObject(GameObject go) {
        updateObjects.add(go);
    }

    /**
     * Removes the object from updateobjects, this is called during an gameobjects destruction
     */
    public synchronized void removeGameObject(GameObject go) {
        updateObjects.remove(go);
    }

    /**
     * This should be called every tick from the {@link UpdateThread}
     */
    public void update(double delta) {
        synchronized (this) {
            GameApplication.getInstance().update(delta);
            GameObject[] updateGameObjects = new GameObject[updateObjects.size()];
            updateObjects.toArray(updateGameObjects);
            for (GameObject updateGameObject : updateGameObjects) {
                if (updateGameObject != null && updateGameObject.isAlive()) {
                    updateGameObject.update(delta);
                }
            }
        }
    }

    /**
     * Will start the update thread that runs concurrently to process logic
     */
    public void startUpdateThread() {
        if (!updateThread.isRunning()) {
            Thread t = new Thread(updateThread);
            t.setName("Update Thread");
            t.setDaemon(true);
            t.start();
        }
    }

    /**
     * Will terminate logic thread
     */
    private void stopUpdateThread() {
        if (updateThread.isRunning()) {
            updateThread.terminate();
        }
    }

    public void startCollisionThread() {
        if (!collisionThread.isRunning()) {
            Thread t = new Thread(collisionThread);
            t.setName("Collision Thread");
            t.setDaemon(true);
            t.start();
        }
    }

    private void stopCollisionThread() {
        if (collisionThread.isRunning()) {
            collisionThread.terminate();
        }
    }
}
