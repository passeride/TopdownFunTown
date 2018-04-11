package com.rominntrenger.main.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;

public class Door extends GameObject {


    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     */
    public Door(Vector2 position, Vector2 direction) {
        super(position, direction, new Sprite("../bg/door_locked"));
        this.setRenderLayer(RenderLayer.RenderLayerName.LOW_BLOCKS);
    }
}
