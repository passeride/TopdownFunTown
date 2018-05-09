package com.rominntrenger.objects.item;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.health.HealingItem;
import com.rominntrenger.objects.weapon.WeaponBarrel;
import com.rominntrenger.objects.weapon.WeaponBase;
import com.rominntrenger.objects.weapon.WeaponClip;

public class ItemRandomizerToken {

    private ItemType entity;

    private WeaponClip weaponClip;
    private WeaponBarrel weaponBarrel;
    private WeaponBase weaponBase;

    public enum ItemType {
        HEAL_BIG, HEAL_SMALL, WEAPON_CLIP, WEAPON_BARREL, WEAPON_BASE
    }


    public ItemRandomizerToken(ItemType entity) {
        this.entity = entity;
    }

    public ItemRandomizerToken(WeaponClip weaponClip) {
        this.entity = ItemType.WEAPON_CLIP;
        this.weaponClip = weaponClip;
    }

    public ItemRandomizerToken(WeaponBarrel weaponBarrel) {
        this.entity = ItemType.WEAPON_BARREL;
        this.weaponBarrel = weaponBarrel;
    }

    public ItemRandomizerToken(WeaponBase weaponBase) {
        this.entity = ItemType.WEAPON_BASE;
        this.weaponBase = weaponBase;
    }

    public void spawn(Vec2 pos) {
        switch (entity) {
            case HEAL_BIG:
                new HealingItem(pos, Vec2.ZERO, new Sprite("items/healSmall"), true);
                break;
            case HEAL_SMALL:
                new HealingItem(Vec2.ZERO, Vec2.ZERO, new Sprite("items/healBig"), false);
                break;
            case WEAPON_CLIP:
                new PickupWeaponClip(pos, weaponClip);
                break;
            case WEAPON_BARREL:
                new PickupWeaponBarrel(pos, weaponBarrel);
                break;
            case WEAPON_BASE:
                new PickupWeaponBase(pos, weaponBase);
                break;
            default:
                break;


        }
    }

}
