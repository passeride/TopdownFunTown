package com.rominntrenger.main.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.Vector2;
import javafx.scene.canvas.GraphicsContext;

public class Tile extends Item {

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public Tile(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
        setSize(new Vector2(1, 1));
        this.setRenderLayer(RenderLayer.RenderLayerName.BACKGROUND);
    }

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
    }

    @Override
    public void setSprite(Sprite sprite) {
        super.setSprite(sprite);
    }
}
