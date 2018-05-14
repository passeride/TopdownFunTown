package com.rominntrenger.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer.RenderLayerName;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;

import java.util.ArrayList;

/**
 * Simple decal effect
 */
public class Soot extends GameObject {

    /**
     * Creates an ArrayList of Soot.
     */
    public static ArrayList<Soot> soots = new ArrayList<>();

    /**
     * Soot will create a soot decal on position given, with random rotation. Used when the
     * exploding enemies dies.
     */
    public Soot(Vec2 position) {
        super(position, Vec2.Vector2FromAngleInDegrees(Math.random() * 360),
            new Sprite("effects/soot"));
        setRenderLayer(RenderLayerName.LOW_BLOCKS);
        soots.add(this);
    }

    /**
     * Empties the ArrayList for resetting the game.
     */
    public static void clearAll() {
        for (Soot b : soots) {
            b.destroy();
        }
    }
}
