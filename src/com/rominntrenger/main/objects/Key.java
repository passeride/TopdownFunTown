package com.rominntrenger.main.objects;

import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.main.gui.Inventory;
import com.rominntrenger.main.gui.InventoryItem;
import com.rominntrenger.main.messageHandling.Describable;
import com.rominntrenger.main.messageHandling.MessageHandler;
import com.rominntrenger.main.objects.blocks.Item;
import com.rominntrenger.main.objects.player.Player;
import javafx.scene.canvas.GraphicsContext;

public class Key extends Item implements Describable {

    private int keyID;

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public Key(Vec2 position, Vec2 direction, Sprite sprite, int ID) {
        super(position, direction, sprite);
        this.keyID = ID;

        setSize(new Vec2(0.5, 0.5));
        setRenderLayer(RenderLayer.RenderLayerName.TILES);
        collider = new BoxCollider(this);
        collider.setTag("Item");
        collider.addInteractionLayer("Walk");
    }


    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
    }

    public void showMessage() {
        MessageHandler.getInstance().writeMessage("Wow! A Key!!", sprite);
        Player p = ((RomInntrenger) GameApplication.getInstance()).player;
        p.setPlayerKey(this.keyID);
        Inventory i = ((RomInntrenger) GameApplication.getInstance()).inventory;
        i.addItem(new InventoryItem(sprite, this.keyID));
        destroy();
        collider.destroy();
        System.out.println("The Key is: " + p.getPlayerKey());
    }
}
