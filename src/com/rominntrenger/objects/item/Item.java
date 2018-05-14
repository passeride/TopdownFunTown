package com.rominntrenger.objects.item;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;

import java.util.ArrayList;
import java.util.List;

public class Item extends GameObject {

    /**
     * Creates an ArrayList of Item.
     */
    public static List<Item> allItems = new ArrayList<>();

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public Item(Vec2 position, Vec2 direction, Sprite sprite) {
        super(position, direction, sprite);
        allItems.add(this);
    }

    /**
     * Creates a new Item from existing Item.
     * @param pos
     * @return
     */
    public Item createNew(Vec2 pos) {
        return new Item(pos, (Vec2) direction.clone(), new Sprite(getSprite().getPath()));
    }

    /**
     * Removes all items from ArrayList, for resetting the game.
     */
    public static void removeAllItems(){
        for(Item i : allItems){
            i.destroy();
        }

        allItems.clear();
    }

}

