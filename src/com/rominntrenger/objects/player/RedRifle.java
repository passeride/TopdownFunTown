package com.rominntrenger.objects.player;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.weapon.WeaponBarrel;
import com.rominntrenger.objects.weapon.WeaponBase;
import com.rominntrenger.objects.weapon.WeaponClip;

public class RedRifle extends Weapon {

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param offset is the offset compared to the player.
     */
    public RedRifle(Vec2 direction, Sprite sprite, Vec2 offset) {
        super(direction, sprite, offset);
        audioPlayer = new AudioPlayer("audio/lukasPew.wav");
        projectilePath = "projectiles/projectile_g_02";
        shootInterval = 0.01;
        speed = 1800;

        setWeaponBase(new WeaponBase());
        setWeaponClip(new WeaponClip());
        setWeaponBarrel(new WeaponBarrel());
    }

    @Override
    public boolean shoot() {
        return super.shoot();
    }
}
