package com.rominntrenger.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.renderer.RenderLayer.RenderLayerName;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;

import java.util.ArrayList;

/**
 * Simple decal effect
 */
public class Blood extends GameObject {

    public static ArrayList<Blood> bloods = new ArrayList<>();

    /**
     * Blood will create a blood decal on position given, with random rotation. Used when enemies
     * dies
     */
    public Blood(Vec2 position) {
        super(position, Vec2.Vector2FromAngleInDegrees(Math.random() * 360),
            new Sprite("effects/blood"));
        setRenderLayer(RenderLayerName.LOW_BLOCKS);
        this.bloods.add(this);
    }

    public static void clearAll(){
        for(Blood b : bloods){
            b.destroy();
        }
    }
}
