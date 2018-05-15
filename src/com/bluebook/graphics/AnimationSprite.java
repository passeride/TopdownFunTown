package com.bluebook.graphics;

import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.listeners.OnAnimationFinishedListener;
import com.bluebook.util.Vec2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Creates Animated sprite. {@link AnimationSprite#length} sets the speed of the animation.
 */
public class AnimationSprite extends Sprite {

    private Image[] animation;
    private double length;
    private long startNanoTime;

    private OnAnimationFinishedListener doneListener;

    private boolean playing = true;


    /**
     * Sprite object with required image
     *
     * @param name is the name of the asset to be loaded
     */
    public AnimationSprite(String name, int frames) {
        super();
        setPath(name);
        this.animation = SpriteLoader.loadAnimationImage(name, frames);
        img = animation[0];
        length = 0.10;
        startNanoTime = System.nanoTime();
    }

    /**
     * Returns the current image of the animation.
     *
     * @param time in nanoseconds elapsed since start.
     * @return animation[index] is the current frame.
     */
    private Image getNextFrame(double time) {
        int index = (int) ((time % (animation.length * length)) / length);
        return animation[index];
    }

    public void Play() {
        startNanoTime = System.nanoTime();
    }

    /**
     * Takes in a GraphicsContext and a Position, draws
     * the current frame to the screen.
     * @param gc
     * @param position
     */
    @Override
    public void draw(GraphicsContext gc, Vec2 position) {
        if (playing) {
            double t = 0;
            if (!GameEngine.getInstance().isPaused()) {
                t = (System.nanoTime() - startNanoTime) / 1_000_000_000.0;
            }
            Image currentFrame = getNextFrame(t);
            this.img = currentFrame;
            super.draw(gc, position);
            if (currentFrame == animation[animation.length - 1]) {
                if (doneListener != null) {
                    doneListener.AnimationFinnished();
                }
            }
        } else {
            super.draw(gc, position);

        }
    }

    @Override
    public void draw(GraphicsContext gc, Vec2 position, Vec2 angle) {
        rotate(Vec2.Vector2FromAngleInDegrees(angle.getAngleInDegrees() + 90));
        draw(gc, position);
    }

    /**
     * Will return TRUE if animation is playing and FALSE if animation is not playing
     * @return
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Will set the Animation playing if TRUE is passed in, and stop animation if FALSE is passed in.
     * @param playing
     */
    public void setPlaying(boolean playing) {
        this.playing = playing;
        if (!playing) {
            this.img = animation[0];
        }
    }

    /**
     * Will set an endListener, will be called when animation is finnished.
     * PS: This will execute on the RenderThread so keep in mind.
     * @param listener
     */
    public void setOnAnimationFinnishedListener(OnAnimationFinishedListener listener) {
        doneListener = listener;
    }

    public int getFrameCount() {
        return animation.length;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}