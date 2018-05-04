package com.rominntrenger.objects.health;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.RenderLayer.RenderLayerName;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.messageHandling.Describable;
import com.rominntrenger.objects.item.Item;
import com.rominntrenger.objects.player.Player;
import javafx.scene.canvas.GraphicsContext;

public class HealingItem extends Item implements Describable {
    private boolean smallSize;
    private int healthValue = GameSettings.getInt("smallHeal_value");

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public HealingItem(Vec2 position, Vec2 direction, Sprite sprite, boolean smallSize) {
        super(position, direction, sprite);
        this.smallSize = smallSize;

        if(!smallSize)
            healthValue = GameSettings.getInt("bigHeal_value");

        setSize(new Vec2(1, 1));
        setRenderLayer(RenderLayerName.HIGH_BLOCKS);
        collider = new BoxCollider(this);
        collider.setTag("Item");
        collider.addInteractionLayer("Walk");
        collider.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if (other.getGameObject() instanceof Player) {
                    Player pl = (Player) other.getGameObject();
                    pl.heal(healthValue);
                }
                destroy();
                collider.destroy();
            }
        });

    }

    @Override
    public HealingItem createNew(Vec2 pos) {
        return new HealingItem(pos, (Vec2)direction.clone(), new Sprite(getSprite().getPath()), smallSize);
    }

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
    }

    public void showMessage() {

    }

}
