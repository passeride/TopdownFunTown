package com.rominntrenger.main.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.physics.CircleCollider;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.Vector2;
import javafx.scene.canvas.GraphicsContext;

public class StasisBox extends Item {

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
    public StasisBox(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
        setSize(new Vector2(1.7,1.7));
        setRenderLayer(RenderLayer.RenderLayerName.HIGH_BLOCKS);
        collider = new CircleCollider(this,90);
        collider.setTag("Block");
    }

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
    }
}
