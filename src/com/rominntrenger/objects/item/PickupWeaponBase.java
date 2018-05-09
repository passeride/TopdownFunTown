package com.rominntrenger.objects.item;

import com.bluebook.engine.GameApplication;
import com.bluebook.physics.CircleCollider;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.messageHandling.Describable;
import com.rominntrenger.messageHandling.MessageHandler;
import com.rominntrenger.objects.player.Player;
import com.rominntrenger.objects.weapon.WeaponBase;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PickupWeaponBase extends Item implements Describable {

    int weaponID;

    WeaponBase cli;

    double x, y, width, height;

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public PickupWeaponBase(Vec2 position, WeaponBase weaponBarrel) {
        super(position, Vec2.ZERO, null);
        setSize(new Vec2(0.7, 0.7));
        setRenderLayer(RenderLayer.RenderLayerName.HIGH_BLOCKS);

        this.cli = weaponBarrel;

        collider = new CircleCollider(this, 30);
        collider.setTag("Item");
        collider.addInteractionLayer("Walk");

    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);

        x = transform.getGlobalPosition().getX();
        y = transform.getGlobalPosition().getY();

        Vec2 squareSize = Vec2.multiply(GameSettings.getSquareScale(), 0.5);
        gc.strokeRect(x - squareSize.getX() / 2, y - squareSize.getY(), squareSize.getX(), squareSize.getY());
        gc.setFill(new Color(0.5, 0, 0, 0.5));
        gc.fillRect(x - squareSize.getX() / 2, y - squareSize.getY(), squareSize.getX(), squareSize.getY());
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(squareSize.getY()));
        String text = cli.character + "";
        gc.fillText(text, x - squareSize.getX() / 2, y);
    }

    public int getWeaponID() {
        return weaponID;
    }


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
