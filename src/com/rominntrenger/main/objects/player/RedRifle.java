package com.rominntrenger.main.objects.player;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.Vector2;

public class RedRifle extends Weapon{
    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     * @param offset    is the offset compared to the player.
     */
    public RedRifle(Vector2 position, Vector2 direction, Sprite sprite, Vector2 offset) {
        super(position, direction, sprite, offset);
        audioPlayer = new AudioPlayer("./assets/audio/lukasPew.wav");
        projectilePath = "/projectiles/projectile_g_02";
    }


    @Override
    public void shoot() {
        super.shoot();
    }
}
