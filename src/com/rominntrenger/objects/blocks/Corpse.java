package com.rominntrenger.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.item.Item;
import javafx.scene.canvas.GraphicsContext;

public class Corpse extends Item {

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public Corpse(Vec2 position, Vec2 direction, Sprite sprite) {
        super(position, direction, sprite);
        setSize(new Vec2(2, 2));
        setRenderLayer(RenderLayer.RenderLayerName.LOW_BLOCKS);
        collider = new BoxCollider(this);
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
