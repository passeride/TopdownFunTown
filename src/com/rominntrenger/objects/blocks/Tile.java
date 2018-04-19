package com.rominntrenger.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.Vec2;
import javafx.scene.canvas.GraphicsContext;

public class Tile extends Item {

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public Tile(Vec2 position, Vec2 direction, Sprite sprite) {
        super(position, direction, sprite);
        setSize(new Vec2(1, 1));
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
