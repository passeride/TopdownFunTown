package com.rominntrenger.main.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.Vector2;
import javafx.scene.canvas.GraphicsContext;

public class Corpse extends Item {

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public Corpse(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
        setSize(new Vector2(2, 2));
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
