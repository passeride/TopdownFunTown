package com.rominntrenger.main.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.Vector2;

public class Wall extends Item {

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public Wall(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
        setSize(new Vector2(1, 1));
        setRenderLayer(RenderLayer.RenderLayerName.HIGH_BLOCKS);
        collider = new BoxCollider(this);
        collider.setTag("Block");
    }
}
