package com.topdownfuntown.objects;

import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.graphics.listeners.OnAnimationFinishedListener;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.topdownfuntown.main.Topdownfuntown;
import javafx.scene.canvas.GraphicsContext;

public class Explotion extends GameObject {
    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     */
    public Explotion(Vector2 position) {
        super(position, Vector2.UP, new AnimationSprite("effects/explotion", 11));
        setSize(new Vector2(5, 5));
        setRenderLayer(RenderLayer.RenderLayerName.HIGH_BLOCKS);
        Player p = ((Topdownfuntown) GameApplication.getInstance()).getPlayer();
        p.rb2.addForce(Vector2.multiply(Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(getPosition(), p.getPosition())), 1000.0));
        ((AnimationSprite)sprite).setOnAnimationFinnishedListener(new OnAnimationFinishedListener() {
            @Override
            public void AnimationFinnished() {
                destroy();
            }
        });
    }

    @Override
    public void update(double detla) {
        super.update(detla);
    }
}
