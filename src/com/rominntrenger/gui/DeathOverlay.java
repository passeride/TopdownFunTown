package com.rominntrenger.gui;

import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer.RenderLayerName;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class DeathOverlay extends GameObject {

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public DeathOverlay() {
        super(Vec2.ZERO, Vec2.ZERO, new Sprite("pictures/getGood"));
        allwaysOnScreen = true;
        setRenderLayer(RenderLayerName.GUI);
    }

    @Override
    public void draw(GraphicsContext gc) {
        //super.draw(gc);
        sprite.drawGUI(gc, Vec2.ZERO, 1920, 1080);
        gc.setStroke(Color.WHITESMOKE);
        String text = new String("Press R to restart or P to reload from checkpoint");
        gc.strokeText(text, GameApplication.getInstance().getScreenWidth()/2, GameApplication.getInstance().getScreenHeight()/1.3);
        gc.setFont(Font.font("Arial", 50));
        //TODO: add text like 'press R to restart', and code the functionality

    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
