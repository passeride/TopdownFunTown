package com.rominntrenger.objects.player;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.Vec2;

public class RedRifle extends Weapon {

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param offset is the offset compared to the player.
     */
    public RedRifle(Vec2 direction, Sprite sprite, Vec2 offset) {
        super(direction, sprite, offset);
        audioPlayer = new AudioPlayer("./assets/audio/lukasPew.wav");
        projectilePath = "/projectiles/projectile_g_02";
        speed = 1800;
    }


    @Override
    public void shoot() {
        super.shoot();
    }
}
