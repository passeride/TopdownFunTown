package com.rominntrenger.gui;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;

public class InventoryItem extends GameObject {

    Sprite itemSprite;
    int keyValue;

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

    public int getKeyValue() {
        return keyValue;
    }
}