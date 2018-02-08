package com.bluebook.engine;

import com.bluebook.physics.CollisionThread;
import com.bluebook.util.GameObject;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import com.topdownfuntown.objects.Player;
import com.bluebook.renderer.CanvasRenderer;
import com.bluebook.threads.UpdateThread;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * This class is the main engine of the game
 */
public class GameEngine {

    private UpdateThread updateThread;
    private CollisionThread collisionThread;
    private final BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(1);

    public ArrayList<GameObject> updateObjects = new ArrayList<>();

    public static boolean DEBUG = false;

    private boolean isPaused = false;

    public static GameEngine singelton;

    Canvas canvas;
    Player p;

    /**
     * Constructor for GameEngine
     * @param canvas Canvas for game to be drawn to
     */
    public GameEngine(Canvas canvas){
        singelton = this;
        CanvasRenderer.getInstance().setCanvas(canvas);

        this.canvas = canvas;


        updateThread = new UpdateThread(this, messageQueue);
        collisionThread = new CollisionThread();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                CanvasRenderer.getInstance().drawAll();
            }
        };
        timer.start();
    }

    public void Pause(){
        isPaused = true;
        stopCollisionThread();
        stopUpdateThread();
    }

    public void unPause(){
        isPaused = false;
        startUpdateThread();
        startCollisionThread();
    }

    public boolean isPaused(){
        return isPaused;
    }

    /**
     * Singelton getter
     * @return
     */
    public static GameEngine getInstance(){
        return singelton;
    }

    /**
     * This is used by the constructor of gameobject so it's update function is called
     * @param go
     */
    public void addGameObject(GameObject go){
        updateObjects.add(go);
    }

    /**
     * Removes the object from updateobjects, this is called during an gameobjects destruction
     * @param go
     */
    public void removeGameObject(GameObject go){
        if(updateObjects.contains(go))
            updateObjects.remove(go);
    }

    /**
     * This should be called every tick from the {@link UpdateThread}
     * @param delta
     */
    public void update(double delta){
        for(GameObject go : updateObjects)
            go.update(delta);
        GameApplication.getInstance().update(delta);
        //CanvasRenderer.getInstance().drawAll();
    }

    /**
     * Will start the update thread that runs concurrently to process logic
     */
    public void startUpdateThread(){
        Thread t = new Thread(updateThread);
        t.setDaemon(true);
        t.start();
    }

    /**
     * Will terminate logic thread
     */
    public void stopUpdateThread(){
        if(updateThread.isRunning())
            updateThread.terminate();
    }

    public void startCollisionThread(){
        Thread t = new Thread(collisionThread);
        t.setDaemon(true);
        t.start();
    }

    public void stopCollisionThread(){
        if (collisionThread.isRunning()) {
            collisionThread.terminate();
        }
    }
}
