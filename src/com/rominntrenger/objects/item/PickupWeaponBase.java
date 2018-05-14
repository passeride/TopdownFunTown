package com.rominntrenger.objects.item;

import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.CircleCollider;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.messageHandling.Describable;
import com.rominntrenger.messageHandling.MessageHandler;
import com.rominntrenger.objects.player.Player;
import com.rominntrenger.objects.weapon.WeaponBase;

public class PickupWeaponBase extends Item implements Describable {
    WeaponBase cli;
    double x, y, width, height;

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public PickupWeaponBase(Vec2 position, WeaponBase weaponBarrel) {
        super(position, Vec2.ZERO, new Sprite("items/weaponBody"));
        setSize(new Vec2(0.7, 0.7));
        setRenderLayer(RenderLayer.RenderLayerName.HIGH_BLOCKS);

        this.cli = weaponBarrel;

        collider = new CircleCollider(this, 30);
        collider.setTag("Item");
        collider.addInteractionLayer("Walk");

    }

    /**
     * Must be implemented with Describable.
     * Prints a message to the screen when PickUpWeaponBase is picked up by player.
     */
    @Override
    public void showMessage() {
        MessageHandler.getInstance()
            .writeMessage("This Base is different! \n Behold!");
        Player p = ((RomInntrenger) GameApplication.getInstance()).getClosestPlayer(transform.getGlobalPosition());
        p.getCurrentWeapon().setWeaponBase(cli);
        collider.destroy();
        destroy();

    }
}
