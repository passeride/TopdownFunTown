package com.rominntrenger.main.gui;

import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;

public class InventoryItem extends GameObject {
    Sprite itemSprite;
    int keyValue;

   /* /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     */
/*    public InventoryItem(Vector2 position, Vector2 direction, Sprite sprite) {
        super(position, direction, sprite);
    }
 */

    public InventoryItem(Sprite sprite, int keyValue) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.itemSprite = sprite;
        this.keyValue = keyValue;
    }

    @Override
    public Sprite getSprite() {
        return itemSprite;
    }
}
