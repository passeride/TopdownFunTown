package com.rominntrenger.objects;

import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.physics.Collider;
import com.bluebook.physics.listeners.OnCollisionListener;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.messageHandling.Describable;
import com.rominntrenger.messageHandling.MessageHandler;
import com.rominntrenger.objects.player.Player;

public class Door extends GameObject implements Describable {

    private int doorID;
    private int playerID;

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public Door(Vec2 position, Vec2 direction, Sprite sprite, int ID) {
        super(position, direction, sprite);
        this.doorID = ID;
        this.setRenderLayer(RenderLayer.RenderLayerName.HIGH_BLOCKS);
        collider = new BoxCollider(this);
        collider.setTag("Block");
        this.collider.addInteractionLayer("Walk");

        collider.setOnCollisionListener(new OnCollisionListener() {
            @Override
            public void onCollision(Collider other) {
                if (other.getGameObject() instanceof Player) {
                    int playerKeyID = ((RomInntrenger) GameApplication.getInstance()).player
                        .getPlayerKey();
                    playerID = playerKeyID;
                    if (ID == playerID) {
                        destroy();
                        collider.destroy();
                    }
                }
            }
        });
    }


    @Override
    public void setSprite(Sprite sprite) {
        super.setSprite(sprite);
    }

    @Override
    public Sprite getSprite() {
        return super.getSprite();
    }

    public void setDoorID(int doorID) {
        this.doorID = doorID;
    }

    public int getDoorID() {
        return doorID;
    }

    public void showMessage() {
        if (doorID == playerID) {
            MessageHandler.getInstance().writeMessage("Old Timey Key in New Timey Space Station?");
        }
    }
}
