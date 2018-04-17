package com.rominntrenger.main.objects.player;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.Vector2;

/**
 * This is the default weapon the player starts with
 */
public class StarterWeapon extends Weapon {
    /**
     * Constructor for StarterWeapon given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     * @param offset    is the offset compared to the player.
     */
    public StarterWeapon(Vector2 position, Vector2 direction, Sprite sprite, Vector2 offset) {
        super(position, direction, sprite, offset);
    }
}
