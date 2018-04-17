package com.rominntrenger.main.objects.player;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.objects.Projectile;
import com.rominntrenger.main.objects.enemy.Enemy;

public abstract class Weapon extends GameObject {
    public Vector2 offset;
    protected AudioPlayer audioPlayer = new AudioPlayer(testFil1);
    private static String testFil1 = "./assets/audio/scifi002.wav";
    protected String projectilePath = "/projectiles/projectile_gold_00";

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

    @Override
    public void setDirection(Vector2 direction) {
        super.setDirection(Vector2.Vector2FromAngleInDegrees(direction.getAngleInDegrees() + 90));
    }


    public void shoot(){
        audioPlayer.setSpital(this);
        audioPlayer.playOnce();
        // score -= 50;
        Vector2 angle = Vector2.Vector2FromAngleInDegrees(transform.getGlobalRotation().getAngleInDegrees() - 90);

        Vector2 spawnPosition = transform.getParent().getLocalPosition();

        Projectile p = new Projectile(spawnPosition,
                Vector2.Vector2FromAngleInDegrees(transform.getGlobalRotation().getAngleInDegrees() - 90),
                new Sprite(projectilePath));
        p.setSpeed(800);
        p.getCollider().addInteractionLayer("Block");
        p.getCollider().addInteractionLayer("Hittable");
        p.setOnCollisionListener(other -> {
            //System.out.println("HIT:  "  + other.getName() + "  :  " + other.getTag());
            if(other.getGameObject() instanceof Player) {
                Player player = (Player) other.getGameObject();
                player.hit(10);
                if (GameEngine.DEBUG)
                    System.out.println("Bullet Hit " + other.getName());
                player.destroy();
            }else if(other.getGameObject() instanceof Enemy){
                other.getGameObject().destroy();
            }
            p.destroy();

        });

    }
}