package com.rominntrenger.main.objects.player;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.Vec2;

/**
 * This is the default weapon the player starts with
 */
public class StarterWeapon extends Weapon {

    /**
     * Constructor for StarterWeapon given position rotation and sprite
     *
     * @param offset is the offset compared to the player.
     */
    public StarterWeapon(Vec2 position, Vec2 direction, Sprite sprite, Vec2 offset) {
        super(position, direction, sprite, offset);
    }
}
