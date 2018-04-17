package com.rominntrenger.main.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;

public class Blood extends GameObject {
    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     */
    public Blood(Vector2 position) {
        super(position, Vector2.Vector2FromAngleInDegrees(Math.random() * 360), new Sprite("effects/blood"));
        setRenderLayer(RenderLayer.RenderLayerName.ENEMIES);
    }
}
