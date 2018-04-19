package com.rominntrenger.main.gui;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;

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
/*    public InventoryItem(Vec2 position, Vec2 direction, Sprite sprite) {
        super(position, direction, sprite);
    }
 */

    public InventoryItem(Sprite sprite, int keyValue) {
        super(Vec2.ZERO, Vec2.ZERO, null);
        this.itemSprite = sprite;
        this.keyValue = keyValue;
        allwaysOnScreen = true;

    }

    @Override
    public Sprite getSprite() {
        return itemSprite;
    }
}
