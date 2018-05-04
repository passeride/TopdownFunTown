package com.rominntrenger.objects.enemy;


import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.FSM.Attack;
import com.rominntrenger.objects.Projectile;
import com.rominntrenger.objects.player.Player;
import java.util.Random;

public class AlienMother extends Enemy {

    private double shootInterval = 0.001;
    private long prevShot = 0;
    private int hit_dmg;

    public AlienMother(Vec2 position) {
        super(position, Vec2.ZERO, new Sprite("enemies/alienMother"));
        setSize(new Vec2(4,4));
        Random r = new Random();
        prevShot = System.currentTimeMillis() + r.nextInt((int) (shootInterval * 1000));
        speed = 200;

        max_health = GameSettings.getInt("Alien_mother_max_health");
        health = max_health;

        hit_dmg = GameSettings.getInt("Alien_mother_hit_dmg");
    }


    @Override
    public Enemy createNew(Vec2 pos) {
        return null;
    }

    public void update(double delta) {
        super.update(delta);
        // AlienMother.super.nextBehaviour();

        //if (super.behaviour instanceof Attack && (System.currentTimeMillis() - prevShot) / 1000 >= shootInterval) {
        if((System.currentTimeMillis() - prevShot) / 1000 >= shootInterval) {
            prevShot = System.currentTimeMillis();
            //shoot();
            spawn();
}
        //}
    }

    public void spawn() {
        new AlienGreen(getPosition());
    }


}