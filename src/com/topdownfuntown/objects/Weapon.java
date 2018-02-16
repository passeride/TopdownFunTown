package com.topdownfuntown.objects;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.topdownfuntown.main.Topdownfuntown;

abstract class Weapon extends GameObject {
    public Vector2 offset;
    private AudioPlayer audioPlayer = new AudioPlayer(testFil1);
    private static String testFil1 = "./assets/audio/scifi002.wav";

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     * @param direction
     * @param sprite
     * @param offset is the offset compared to the player.
     */
    public Weapon(Vector2 position, Vector2 direction, Sprite sprite, Vector2 offset) {
        super(position, direction, sprite);
        this.offset = offset;
    }

    public Vector2 getOffset() {
        return offset;
    }

    public void setOffset(Vector2 offset) {
        this.offset = offset;
    }

    public void shoot(){
        audioPlayer.setSpital(this);
        audioPlayer.playOnce();
        // score -= 50;
        Projectile p = new Projectile(getPosition(), getDirection(), new Sprite("/projectiles/bullet"));
        p.setOnCollisionListener(other -> {
            //System.out.println("HIT:  "  + other.getName() + "  :  " + other.getTag());
            if(other.getTag() == "Hittable") {
                if(other.getGameObject() instanceof Player) {
                    Player player = (Player) other.getGameObject();
                    player.hit();
                    if (GameEngine.DEBUG)
                        System.out.println("Bullet Hit " + other.getName());
                    player.destroy();
                }else{
                    // score += (int)(0.2 * p.getLengthTraveled());
                    other.getGameObject().destroy();
                    p.destroy();
                }
            }
        });

    }
}