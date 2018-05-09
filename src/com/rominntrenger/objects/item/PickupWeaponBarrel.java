package com.rominntrenger.objects.item;

import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.CircleCollider;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.messageHandling.Describable;
import com.rominntrenger.messageHandling.MessageHandler;
import com.rominntrenger.objects.player.Player;
import com.rominntrenger.objects.weapon.WeaponBarrel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PickupWeaponBarrel extends Item implements Describable {

    int weaponID;

    WeaponBarrel cli;

    double x, y, width, height;

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public PickupWeaponBarrel(Vec2 position, WeaponBarrel weaponBarrel) {
        super(position, Vec2.ZERO, new Sprite("items/weaponBarrel"));
        setSize(new Vec2(0.7, 0.7));
        setRenderLayer(RenderLayer.RenderLayerName.HIGH_BLOCKS);

        this.cli = weaponBarrel;

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
            .writeMessage("This BARREL is different! \n Behold!");
        Player p = ((RomInntrenger) GameApplication.getInstance()).getClosestPlayer(transform.getGlobalPosition());
        p.getCurrentWeapon().setWeaponBarrel(cli);
        collider.destroy();
        destroy();

    }
}
