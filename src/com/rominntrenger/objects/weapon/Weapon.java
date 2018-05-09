package com.rominntrenger.objects.weapon;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.Projectile;
import com.rominntrenger.objects.enemy.Enemy;
import com.rominntrenger.objects.player.Player;

/**
 * Weapon class is used for weapons that the Player can equip and shoot with
 */
public class Weapon extends GameObject {

    public Vec2 offset;
    double speed = 800;
    int dmg = 10;
    int alteredDmg = dmg;
    int ammoCap = 2000;
    int ammoRemaining = ammoCap;
    double spread = 0;
    double reloadDuration = 1.5;
    double reloadTimer = 0;
    boolean isReloading = false;

    protected AudioPlayer audioPlayer = new AudioPlayer(testFil1);
    private static String testFil1 = "audio/scifi002.wav";
    protected String projectilePath = "projectiles/projectile_gold_00";

    private long previousShotTime = 0;
    protected double shootInterval = 0.1;

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
        audioPlayer = new AudioPlayer("audio/lukasPew.wav");
        projectilePath = "projectiles/projectile_g_02";

        // setting base modifiers
        setWeaponBase(new WeaponBase());
        setWeaponClip(new WeaponClip());
        setWeaponBarrel(new WeaponBarrel());
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
        if (!isReloading && System.currentTimeMillis() - previousShotTime > shootInterval * 1000 && ammoRemaining > 0) {

            previousShotTime = System.currentTimeMillis();

            ammoRemaining--;
            audioPlayer.setSpatial(holder);
            audioPlayer.playOnce();
            // score -= 50;
            Vec2 angle = Vec2
                .Vector2FromAngleInDegrees(transform.getGlobalRotation().getAngleInDegrees() - 90 + (Math.random() * spread * 2 - spread));

            Vec2 spawnPosition = transform.getWorldPosition();

            Projectile p = new Projectile(spawnPosition,
                Vec2
                    .Vector2FromAngleInDegrees(
                        transform.getGlobalRotation().getAngleInDegrees() - 90 + (Math.random() * spread * 2 - spread)),
                new Sprite(projectilePath));
            p.setSpeed(speed);
            p.setSource(holder);
            p.getCollider().addInteractionLayer("Block");
            p.getCollider().addInteractionLayer("Hittable");
            p.getCollider().addInteractionLayer("Walk");
            p.setOnCollisionListener(other -> {
                System.out.println(other.getTag() + " CLASS: " + other.getGameObject().getClass().toString());
                System.out.println("MyHolderClass: " + holder.getClass().toString());
                System.out.println("Equals? : " + (other.getGameObject() == holder));
                if (other.getGameObject() instanceof Player && other.getGameObject() != holder) {
                    Player player = (Player) other.getGameObject();
                    player.hit(alteredDmg);
                    if (GameEngine.DEBUG) {
                        System.out.println("Bullet Hit " + other.getName());
                    }
                    p.destroy();

//                player.destroy();
                } else if (other.getGameObject() instanceof Enemy) {
                    Enemy e = ((Enemy) other.getGameObject());
                    e.hit(alteredDmg);
                    if (!e.isAlive()) { // CHeck if enemy survived
                        ((Player) p.getSource()).killedEnemy();
                    }
                    p.destroy();
                } else if (other.getTag() == "Block") {
                    p.destroy();
                }

            });
            return true;
        } else if(ammoRemaining <= 0){
            reloadWeapon();
            return false;
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

    @Override
    public void update(double delta) {
        super.update(delta);
        if (isReloading) {
            reloadTimer += delta;
            if (reloadTimer >= reloadDuration) {
                isReloading = false;
                ammoRemaining = ammoCap;
                AudioPlayer ap = new AudioPlayer("audio/Crunch2.wav");
                ap.playOnce();
            }
        }

        if (weaponClip != null && weaponBase != null && weaponBarrel != null) {
            ammoCap = weaponClip.ammoCap;
            if(ammoRemaining > ammoCap)
                reloadWeapon();
            dmg = (int) (weaponClip.dmg * weaponBase.dmgMultiplier);
            spread = weaponBarrel.spread;
            reloadDuration = weaponClip.reloadTime;
            shootInterval = weaponBase.shootInterval;
        }
    }

    public void reloadWeapon() {
        if (!isReloading) {
            reloadTimer = 0;
            isReloading = true;
            AudioPlayer ap = new AudioPlayer("audio/Crunch1.wav");
            ap.playOnce();
        }
//        this.ammoRemaining = ammoCap;
    }

    public WeaponBase getWeaponBase() {
        return weaponBase;
    }

    public void setWeaponBase(WeaponBase weaponBase) {
        this.weaponBase = weaponBase;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public int getAlteredDmg() {
        return alteredDmg;
    }

    public void setAlteredDmg(int alteredDmg) {
        this.alteredDmg = alteredDmg;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public void setAudioPlayer(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public String getProjectilePath() {
        return projectilePath;
    }

    public void setProjectilePath(String projectilePath) {
        this.projectilePath = projectilePath;
    }

    public long getPreviousShotTime() {
        return previousShotTime;
    }

    public void setPreviousShotTime(long previousShotTime) {
        this.previousShotTime = previousShotTime;
    }

    public double getShootInterval() {
        return shootInterval;
    }

    public void setShootInterval(double shootInterval) {
        this.shootInterval = shootInterval;
    }

    public double getReloadDuration() {
        return reloadDuration;
    }

    public void setReloadDuration(double reloadDuration) {
        this.reloadDuration = reloadDuration;
    }

    public double getReloadTimer() {
        return reloadTimer;
    }

    public void setReloadTimer(long reloadTimer) {
        this.reloadTimer = reloadTimer;
    }

    public boolean isReloading() {
        return isReloading;
    }

    public void setReloading(boolean reloading) {
        isReloading = reloading;
    }
}