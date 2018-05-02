package com.rominntrenger.objects.player;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.Projectile;
import com.rominntrenger.objects.enemy.Enemy;
import com.rominntrenger.objects.weapon.WeaponBarrel;
import com.rominntrenger.objects.weapon.WeaponBase;
import com.rominntrenger.objects.weapon.WeaponClip;

/**
 * Weapon class is used for weapons that the Player can equip and shoot with
 */
public abstract class Weapon extends GameObject {

    public Vec2 offset;
    double speed = 800;
    int dmg = 10;
    int alteredDmg = dmg;
    int ammoCap = 2000;
    int ammoRemaining = ammoCap;

    protected AudioPlayer audioPlayer = new AudioPlayer(testFil1);
    private static String testFil1 = "./assets/audio/scifi002.wav";
    protected String projectilePath = "/projectiles/projectile_gold_00";

    private long previousShotTime = 0;
    protected double shootInterval = 0.5;

    WeaponClip weaponClip;
    WeaponBarrel weaponBarrel;
    WeaponBase weaponBase;

    private Player holder;

    /**
     * Constructor for Weapon
     *
     * @param offset is the offset compared to the player.
     */
    public Weapon(Vec2 direction, Sprite sprite, Vec2 offset) {
        super(new Vec2(0, 23), direction, sprite);
        this.offset = offset;
    }

    public Vec2 getOffset() {
        return offset;
    }

    public void setOffset(Vec2 offset) {
        this.offset = offset;
    }

    @Override
    public void setDirection(Vec2 direction) {
        super.setDirection(Vec2.Vector2FromAngleInDegrees(direction.getAngleInDegrees() + 90));
    }

    /**
     * Shoot will shoot a projectile constructed in this class at the direction the player is faced
     */
    public boolean shoot() {
        if(System.currentTimeMillis() - previousShotTime >  shootInterval * 1000 && ammoRemaining > 0) {

            previousShotTime = System.currentTimeMillis();

            ammoRemaining --;
            audioPlayer.setSpatial(this);
            audioPlayer.playOnce();
            // score -= 50;
            Vec2 angle = Vec2
                .Vector2FromAngleInDegrees(transform.getGlobalRotation().getAngleInDegrees() - 90);

            Vec2 spawnPosition = transform.getWorldPosition();

            Projectile p = new Projectile(spawnPosition,
                Vec2
                    .Vector2FromAngleInDegrees(
                        transform.getGlobalRotation().getAngleInDegrees() - 90),
                new Sprite(projectilePath));
            p.setSpeed(speed);
            p.setSource(holder);
            p.getCollider().addInteractionLayer("Block");
            p.getCollider().addInteractionLayer("Hittable");
            p.getCollider().addInteractionLayer("Walk");
            p.setOnCollisionListener(other -> {
                if (other.getGameObject() instanceof Player && other.getGameObject() != p
                    .getSource()) {
                    Player player = (Player) other.getGameObject();
                    player.hit(alteredDmg);
                    if (GameEngine.DEBUG) {
                        System.out.println("Bullet Hit " + other.getName());
                    }
//                player.destroy();
                } else if (other.getGameObject() instanceof Enemy) {
                    Enemy e = ((Enemy)other.getGameObject());
                    e.hit(alteredDmg);
                    if(!e.isAlive()) { // CHeck if enemy survived
                        ((Player) p.getSource()).killedEnemy();
                    }
                }
                p.destroy();

            });
            return true;
        }else
            return false;
    }

    public WeaponClip getWeaponClip() {
        return weaponClip;
    }

    public void setWeaponClip(WeaponClip weaponClip) {
        this.weaponClip = weaponClip;
        alteredDmg = dmg + weaponClip.dmg;
    }

    public WeaponBarrel getWeaponBarrel() {
        return weaponBarrel;
    }

    public void setWeaponBarrel(WeaponBarrel weaponBarrel) {
        this.weaponBarrel = weaponBarrel;
    }

    public Player getHolder() {
        return holder;
    }

    public void setHolder(Player holder) {
        this.holder = holder;
    }

    public int getAmmoCap() {
        return ammoCap;
    }

    public void setAmmoCap(int ammoCap) {
        this.ammoCap = ammoCap;
    }

    public int getAmmoRemaining() {
        return ammoRemaining;
    }

    public void setAmmoRemaining(int ammoRemaining) {
        this.ammoRemaining = ammoRemaining;
    }

    public void reloadWeapon() {
        this.ammoRemaining = ammoCap;
    }

    public WeaponBase getWeaponBase() {
        return weaponBase;
    }

    public void setWeaponBase(WeaponBase weaponBase) {
        this.weaponBase = weaponBase;
    }
}