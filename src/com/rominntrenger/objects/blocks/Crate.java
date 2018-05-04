package com.rominntrenger.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.Projectile;
import com.rominntrenger.objects.item.Item;
import javafx.scene.canvas.GraphicsContext;

public class Crate extends Item {

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public Crate(Vec2 position, Vec2 direction, Sprite sprite) {
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
                    destroy();
                }
            }
        });
    }

    @Override
    public Crate createNew(Vec2 pos) {
        return new Crate(pos, (Vec2)direction.clone(), new Sprite(getSprite().getPath()));
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
