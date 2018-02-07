package com.bluebook.threads;

import com.bluebook.engine.GameEngine;

import java.util.concurrent.BlockingQueue;

/**
 * A thread to handle logic for each tick
 */
public class UpdateThread implements Runnable {

    private BlockingQueue<String> messageQueue;
    private GameEngine engine;
    private float frameRate = 60f;
    private long sleepTime =  (long)((1f / frameRate) * 1000f) ;
    private volatile boolean running = true;
    private long prevTick = 0;

    /**
     * Used to create the UpdateThread
     * Will each tick call {@link GameEngine#update(double delta)}
     * @param engine
     * @param messageQueue
     */
    public UpdateThread(GameEngine engine, BlockingQueue<String> messageQueue){
        this.engine = engine;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        running = true;
        while(running){

            long timeElapsed = System.currentTimeMillis() - prevTick;
            engine.update((double)timeElapsed / 1000);
            prevTick = System.currentTimeMillis();

            try {
                Thread.sleep(sleepTime);

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
