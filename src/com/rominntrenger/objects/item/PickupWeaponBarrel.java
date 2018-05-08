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

        Vec2 squareSize = Vec2.multiply(GameSettings.getSquareScale(), 0.5);
        x = transform.getGlobalPosition().getX();
        y = transform.getGlobalPosition().getY();

            gc.strokeRect(x - squareSize.getX() / 2, y - squareSize.getY(), squareSize.getX(), squareSize.getY());
            gc.setFill(new Color(0, 0.5, 0, 0.5));
            gc.fillRect(x - squareSize.getX() / 2, y - squareSize.getY(), squareSize.getX(), squareSize.getY());
            gc.setFill(Color.BLACK);
            gc.setFont(new Font(squareSize.getY()));
            String text = cli.character + "";
            gc.fillText(text, x - squareSize.getX() / 2, y);
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
