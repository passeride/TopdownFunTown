package com.rominntrenger.main.objects.blocks;

import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.physics.CircleCollider;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.main.messageHandling.Describable;
import com.rominntrenger.main.messageHandling.MessageHandler;
import com.rominntrenger.main.objects.blocks.Item;
import com.rominntrenger.main.objects.player.Player;
import com.rominntrenger.main.objects.player.RedRifle;
import com.rominntrenger.main.objects.player.Weapon;
import javafx.scene.canvas.GraphicsContext;

public class PickupWeapon extends Item implements Describable {
    int weaponID;

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
    public PickupWeapon(Vector2 position, Vector2 direction, Sprite sprite, int id) {
        super(position, direction, sprite);
        setSize(new Vector2(1,1));
        setRenderLayer(RenderLayer.RenderLayerName.HIGH_BLOCKS);
        collider = new BoxCollider(this);
        this.weaponID = id;

        collider = new CircleCollider(this, 30);
        collider.setTag("Item");
        collider.addInteractionLayer("Walk");

    }

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
    }

    @Override
    public void setSprite(Sprite sprite) {
        super.setSprite(sprite);
    }

    public int getWeaponID() { return weaponID; }

    @Override
    public void showMessage() {
        MessageHandler.getInstance().writeMessage("U got a new pew pew! \n This one is red!", sprite);
        Player p = ((RomInntrenger) GameApplication.getInstance()).player;
        Weapon w = new RedRifle(new Vector2(0,23), Vector2.ZERO, new AnimationSprite("/friendlies/arms",2), Vector2.ZERO);
        p.setCurrentWeapon(w);
        ((RomInntrenger)GameApplication.getInstance()).currentWeapon = w;
    }
}
