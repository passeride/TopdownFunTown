package com.rominntrenger.objects.weapon;

import com.bluebook.physics.BoxCollider;
import com.bluebook.physics.Collider;
import com.bluebook.renderer.RenderLayer.RenderLayerName;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.player.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;

public class WeaponClipUpgrade extends GameObject {




    WeaponClip wc;
    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public WeaponClipUpgrade(Vec2 position, WeaponClip wc) {
        super(position, Vec2.ZERO, null);
        setRenderLayer(RenderLayerName.HIGH_BLOCKS);

        collider = new BoxCollider(this);
        collider.setTag("Upgrade");
        collider.addInteractionLayer("UnHittable");
        collider.setOnCollisionListener((Collider other) -> {
            if(other.getGameObject() instanceof Player){
                Player player = ((Player)other.getGameObject());
                Weapon w = player.getCurrentWeapon();
                if(w != null)
                    w.setWeaponClip(wc);
                destroy();
            }
        });
        this.wc = wc;
    }

    @Override
    public void draw(GraphicsContext gc) {
        Vec2 pos = transform.getGlobalPosition();
        Vec2 square = GameSettings.getSquareScale();


        gc.setFill(new RadialGradient(0, 0, pos.getX(), pos.getY(), square.getX(), false,
            CycleMethod.NO_CYCLE,
            new Stop(0.0, new Color(1, 0, 0, 1)),
            new Stop(1.0, new Color(0, 0, 1, 1))));

        gc.setStroke(Color.BLACK);
        gc.strokeRect(pos.getX() - square.getX() / 2, pos.getY() - square.getY() / 2, square.getX(), square.getY());
        gc.fillRect(pos.getX() - square.getX() / 2, pos.getY() - square.getY() / 2, square.getX(), square.getY());
        gc.setFont(new Font(square.getX()));
        gc.setFill(Color.BLACK);
        gc.fillText(wc.character+"", pos.getX(), pos.getY());
    }
}
