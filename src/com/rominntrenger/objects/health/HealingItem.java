package com.rominntrenger.objects.health;

import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.renderer.RenderLayer.RenderLayerName;
import com.bluebook.util.Vec2;
import com.rominntrenger.gui.Inventory;
import com.rominntrenger.gui.InventoryItem;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.messageHandling.Describable;
import com.rominntrenger.messageHandling.MessageHandler;
import com.rominntrenger.objects.blocks.Item;
import com.rominntrenger.objects.player.Player;
import javafx.scene.canvas.GraphicsContext;

public class HealingItem extends Item implements Describable {
    private boolean smallSize;
    private int healthValue = 10; // TODO: STANDARD SMALL BOI? BIG BOI? BOIZ

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public HealingItem(Vec2 position, Vec2 direction, Sprite sprite, boolean smallSize) {
        super(position, direction, sprite);
        this.smallSize = smallSize;

        //TODO: LUKAS FIKS TIL SETTING IF BIG BOY = 20 VALUE OR SOMTHNG

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
    public void draw(GraphicsContext gc) {
        super.draw(gc);
    }

    public void showMessage() {

    }

}
