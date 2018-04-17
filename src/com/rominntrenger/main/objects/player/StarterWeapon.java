package com.rominntrenger.main.objects.player;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.Vector2;

public class StarterWeapon extends Weapon {
    /**
     * Constructor for GameObject given position rotation and sprite
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
