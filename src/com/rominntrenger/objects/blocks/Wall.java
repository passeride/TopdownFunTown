package com.rominntrenger.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;

public class Wall extends GameObject {

    /**
     * Constructor for GameObject given position rotation and sprite.
     * Sets the wall to high_blocks render layer so the character will be
     * below it if there is a slight overlap when colliding.
     */
    public Wall(Vec2 position, Vec2 direction, Sprite sprite) {
        super(position, direction, sprite);
        setSize(new Vec2(1, 1));
        setRenderLayer(RenderLayer.RenderLayerName.HIGH_BLOCKS);
        collider = new BoxCollider(this);
        collider.setTag("Block");
        collider.addInteractionLayer("Obscure");
    }
}
