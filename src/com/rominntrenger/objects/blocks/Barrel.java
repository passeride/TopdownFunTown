package com.rominntrenger.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.Projectile;
import com.rominntrenger.objects.player.Player;
import javafx.scene.canvas.GraphicsContext;

public class Barrel extends Item {

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public Barrel(Vec2 position, Vec2 direction, Sprite sprite) {
        super(position, direction, sprite);
        setSize(new Vec2(1, 1));
        setRenderLayer(RenderLayer.RenderLayerName.HIGH_BLOCKS);
        collider = new BoxCollider(this);
        collider.setTag("Block");
        collider.addInteractionLayer("DMG");
        collider.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if(other.getGameObject() instanceof Projectile) {
                    // destroy();
                    //TODO: Maybe create an explosion here?
                }
            }
        });
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
