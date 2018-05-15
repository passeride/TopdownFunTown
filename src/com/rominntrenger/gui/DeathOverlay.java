package com.rominntrenger.gui;

import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer.RenderLayerName;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Class that will contain gameObjects which will appair when a player dies.
 */
public class DeathOverlay extends GameObject {

    /**
     * Constructor for GameObject given position rotation and sprite.
     * Set to be on the topmost renderlayer and on screen until it gets removed.
     */
    public DeathOverlay() {
        super(Vec2.ZERO, Vec2.ZERO, new Sprite("pictures/getGood"));
        allwaysOnScreen = true;
        setRenderLayer(RenderLayerName.GUI);
    }

    /**
     * Draw method that draws to the screen
     * @param gc - graphicsContext used to draw onto the canvas
     */
    @Override
    public void draw(GraphicsContext gc) {
        sprite.drawGUI(gc, Vec2.ZERO, GameSettings.getInt("game_resolution_X"), GameSettings.getInt("game_resolution_Y"));
        gc.setStroke(Color.WHITESMOKE);
        String text = new String("Press ESC to exit the game or P to reload from checkpoint");
        gc.strokeText(text, GameApplication.getInstance().getScreenWidth() / 2, GameApplication.getInstance().getScreenHeight() / 1.3);
        gc.setFont(Font.font("Arial", 50));
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
