package com.rominntrenger.objects.blocks;

import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.physics.CircleCollider;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.messageHandling.Describable;
import com.rominntrenger.messageHandling.MessageHandler;
import com.rominntrenger.objects.player.Player;
import com.rominntrenger.objects.player.RedRifle;
import com.rominntrenger.objects.player.Weapon;
import javafx.scene.canvas.GraphicsContext;

public class PickupWeapon extends Item implements Describable {

    int weaponID;

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public PickupWeapon(Vec2 position, Vec2 direction, Sprite sprite, int id) {
        super(position, direction, sprite);
        setSize(new Vec2(0.7, 0.7));
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

    public int getWeaponID() {
        return weaponID;
    }

    @Override
    public void showMessage() {
        MessageHandler.getInstance()
            .writeMessage("U got a new pew pew! \n This one is red!", sprite);
        Player p = ((RomInntrenger) GameApplication.getInstance()).player;
        Weapon w = new RedRifle(Vec2.ZERO,
            new AnimationSprite("/friendlies/weaponR", 2), Vec2.ZERO);
        p.setCurrentWeapon(w);
        ((RomInntrenger) GameApplication.getInstance()).currentWeapon = w;
        destroy();
        collider.destroy();
    }
}
