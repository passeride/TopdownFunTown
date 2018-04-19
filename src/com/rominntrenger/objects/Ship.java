package com.rominntrenger.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.blocks.Item;
import javafx.scene.canvas.GraphicsContext;

public class Ship extends Item {

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public Ship(Vec2 position, Vec2 direction, Sprite sprite) {
        super(position, direction, sprite);
        setSize(new Vec2(1, 1));
        setRenderLayer(RenderLayer.RenderLayerName.HIGH_BLOCKS);
        // collider = new BoxCollider(this);
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
