package com.bluebook.graphics;

import com.bluebook.engine.GameEngine;
import com.bluebook.util.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Creates Animated sprite.
 * {@link AnimationSprite#length} sets the speed of the animation.
 */
public class AnimationSprite extends Sprite{
    public Image[] animation;
    public double length;
    public int frameNum;
    long startNanoTime;

    boolean playing = true;

    /**
     * Sprite object with required image
     * @param name is the name of the asset to be loaded
     */
    public AnimationSprite(String name, int frames) {
        super();
        this.frameNum = frames;
        this.animation = SpriteLoader.loadAnimationImage(name,frameNum);
        length = 0.10;
        startNanoTime = System.nanoTime();
    }

    /**
     * Returns the current image of the animation.
     * @param time in nanoseconds elapsed since start.
     * @return animation[index] is the current frame.
     */
    private Image getNextFrame(double time) {
        int index = (int)((time % (animation.length * length)) / length);
        return animation[index];
    }

    public void Play(){
        startNanoTime = System.nanoTime();
    }

    @Override
    public void draw(GraphicsContext gc, Vector2 position){
        double t = 0;
        if(!GameEngine.getInstance().isPaused())
            t = (System.nanoTime() - startNanoTime) / 1_000_000_000.0;
        Image currentFrame = getNextFrame(t);
        this.img = currentFrame;
        super.draw(gc, position);
    }

    @Override
    public void draw(GraphicsContext gc, Vector2 position, Vector2 angle){
        rotate(Vector2.Vector2FromAngleInDegrees(angle.getAngleInDegrees() + 90));
        draw(gc, position);
    }

}