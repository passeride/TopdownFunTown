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
import com.rominntrenger.objects.weapon.WeaponClip;

public class PickupWeaponClip extends Item implements Describable {

    int weaponID;

    WeaponClip cli;

    double x, y, width, height;

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public PickupWeaponClip(Vec2 position, WeaponClip weaponClip) {
        super(position, Vec2.ZERO, new Sprite("items/weaponClip"));
        setSize(new Vec2(0.7, 0.7));
        setRenderLayer(RenderLayer.RenderLayerName.HIGH_BLOCKS);

        this.cli = weaponClip;

        collider = new CircleCollider(this, 30);
        collider.setTag("Item");
        collider.addInteractionLayer("Walk");
    }

    @Override
    public void setSprite(Sprite sprite) {
        super.setSprite(sprite);
    }

    public int getWeaponID() {
        return weaponID;
    }


    @Override
    public void showMessage() {
        MessageHandler.getInstance()
            .writeMessage("This Clip is different! \n Behold!");
        Player p = ((RomInntrenger) GameApplication.getInstance()).getClosestPlayer(transform.getGlobalPosition());
        p.getCurrentWeapon().setWeaponClip(cli);
        collider.destroy();
        destroy();

    }
}
