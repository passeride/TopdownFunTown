package com.rominntrenger.gui;

import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer.RenderLayerName;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import javafx.scene.canvas.GraphicsContext;


public class DeathOverlay extends GameObject {

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public DeathOverlay() {
        super(Vec2.ZERO, Vec2.ZERO, new Sprite("../pictures/getGood"));
        allwaysOnScreen = true;
        setRenderLayer(RenderLayerName.GUI);
    }

    @Override
    public void draw(GraphicsContext gc) {
        //super.draw(gc);
        sprite.drawGUI(gc, Vec2.ZERO, 1920, 1080);
        //TODO: add text like 'press R to restart', and code the functionality

    }


}
