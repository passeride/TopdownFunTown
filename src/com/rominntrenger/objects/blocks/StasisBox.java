package com.rominntrenger.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.CircleCollider;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.Vec2;
import javafx.scene.canvas.GraphicsContext;

public class StasisBox extends Item {

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public StasisBox(Vec2 position, Vec2 direction, Sprite sprite) {
        super(position, direction, sprite);
        setSize(new Vec2(1.7, 1.7));
        setRenderLayer(RenderLayer.RenderLayerName.HIGH_BLOCKS);
        collider = new CircleCollider(this, 90);
        collider.setTag("Block");
    }

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
    }
}
