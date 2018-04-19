package com.rominntrenger.main.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;

/**
 * Simple decal effect
 */
public class Blood extends GameObject {

    /**
     * Blood will create a blood decal on position given, with random rotation. Used when enemies
     * dies
     */
    public Blood(Vec2 position) {
        super(position, Vec2.Vector2FromAngleInDegrees(Math.random() * 360),
            new Sprite("effects/blood"));
        setRenderLayer(RenderLayer.RenderLayerName.ENEMIES);
    }
}
