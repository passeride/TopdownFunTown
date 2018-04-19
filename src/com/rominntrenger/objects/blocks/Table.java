package com.rominntrenger.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.Vec2;
import javafx.scene.canvas.GraphicsContext;

public class Table extends Item {

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public Table(Vec2 position, Vec2 direction, Sprite sprite) {
        super(position, direction, sprite);
        setSize(new Vec2(1.5, 2));
        setRenderLayer(RenderLayer.RenderLayerName.HIGH_BLOCKS);
        collider = new BoxCollider(this);
    }

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
    }
}
