package com.rominntrenger.main.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.physics.BoxCollider;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.objects.blocks.Item;
import javafx.scene.canvas.GraphicsContext;

public class Key extends Item {
    private int keyID;

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
    public Key(Vector2 position, Vector2 direction, Sprite sprite, int ID) {
        super(position, direction, sprite);
        this.keyID = ID;

        setSize(new Vector2(1,1));
        setRenderLayer(RenderLayer.RenderLayerName.TILES);
        collider = new BoxCollider(this);
    }

/*    public Key(Vector2 position, Vector2 direction, Sprite sprite, Door door) {
        super(position, direction, sprite);
        setSize(new Vector2(1,1));

        this.door = door;
        collider = new BoxCollider(this);
        setRenderLayer(RenderLayer.RenderLayerName.TILES);
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    public Door getDoor() {
        return door;
    } */

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
    }
}
