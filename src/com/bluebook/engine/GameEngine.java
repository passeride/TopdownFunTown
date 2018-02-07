package com.bluebook.engine;

import javafx.scene.canvas.Canvas;
import com.bluebook.objects.Player;
import com.bluebook.renderer.CanvasRenderer;
import com.bluebook.util.Sprite;
import com.bluebook.util.SpriteLoader;
import com.bluebook.util.UpdateThread;
import com.bluebook.util.Vector2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * This class is the main engine of the game
 */
public class GameEngine {

    private UpdateThread updateThread;
    private final BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(1);

    public static final boolean DEBUG = true;

    Canvas canvas;
    Player p;

    /**
     * Constructor for GameEngine
     * @param canvas Canvas for game to be drawn to
     */
    public GameEngine(Canvas canvas){
        CanvasRenderer.getInstance().setCanvas(canvas);

        this.canvas = canvas;


        updateThread = new UpdateThread(this, messageQueue);
        startUpdateThread();

        if(DEBUG){
            debugSetup();
        }
    }

    public void debugSetup(){
        p = new Player(new Vector2(0, 0), new Vector2(0, 0), new Sprite(SpriteLoader.loadImage("senik")));

    }

    public Player getPlayer(){
        return p;
    }

    public void debugSetPlayerPosition(Vector2 pos){
        p.setPosition(pos);
    }

    /**
     * This should be called every tick from the {@link UpdateThread}
     * @param delta
     */
    public void update(double delta){
        CanvasRenderer.getInstance().DrawAll();
    }

    private void startUpdateThread(){
        Thread t = new Thread(updateThread);
        t.setDaemon(true);
        t.start();
    }

    private void stopUpdateThread(){
        if(updateThread.isRunning())
            updateThread.terminate();
    }
}
