package com.bluebook.graphics;

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
    long startNanoTime;

    boolean playing = true;

    /**
     * Sprite object with required image
     * @param img is an Array of images to be drawn
     */
    public AnimationSprite(Image[] img) {
        super();
        this.animation = img;
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
        gc.save();

        if(isRotated)
            gc = rotateGraphicsContext(gc, position);

        double t = (System.nanoTime() - startNanoTime) / 1_000_000_000.0;
        Image img = getNextFrame(t);
        gc.drawImage(img, position.getX() - squareWidth / 2f, position.getY() - squareHeight / 2f, squareWidth, squareHeight);
        gc.restore();
    }

    @Override
    public void draw(GraphicsContext gc, Vector2 position, Vector2 angle){
        rotate(Vector2.Vector2FromAngleInDegrees(angle.getAngleInDegrees() + 90));
        draw(gc, position);
    }

}