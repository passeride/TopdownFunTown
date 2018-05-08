package com.rominntrenger.objects.enemy;


import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer.RenderLayerName;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.FSM.Attack;
import com.rominntrenger.objects.Key;
import com.rominntrenger.objects.Projectile;
import com.rominntrenger.objects.player.Player;
import java.util.Random;

public class AlienGreenKey extends Enemy {
    private double shootInterval = 0.001;
    private long prevShot = 0;
    public Key keyB;

    public AlienGreenKey(Vec2 position) {
        super(position, Vec2.ZERO, new AnimationSprite("/enemies/enemyGreen", 3));
        Random r = new Random();
        prevShot = System.currentTimeMillis() + r.nextInt((int) (shootInterval * 1000));
        // setTarget(((Topdownfuntown)GameApplication.getInstance()).getPlayer());
        speed = 200;
    }

    public void update(double delta) {
        super.update(delta);
        AlienGreenKey.super.nextBehaviour();

        if (super.behaviour instanceof Attack && (System.currentTimeMillis() - prevShot) / 1000 >= shootInterval) {
            prevShot = System.currentTimeMillis();
            shoot();
        }
    }

    public void shoot() {
        Projectile p = new Projectile(transform.getLocalPosition(), transform.getGlobalRotation(),
            new Sprite("/projectiles/projectile_enemy_00"));
        p.getCollider().addInteractionLayer("UnHittable");
        p.getCollider().addInteractionLayer("Block");
        p.getSprite().setSquareHeight(32);
        p.getSprite().setSquareWidth(32);
        p.setPeriod(1.2f);
        p.setAmplitude(3f);
        p.setPhase(200f);
        p.setSpeed(1600);
        p.setSine(true);

        p.setOnCollisionListener(other -> {
            if (other.getGameObject() instanceof Player) {
                Player pl = (Player) other.getGameObject();
                pl.hit(bullet_dmg);
                pl.rb2.addForce(Vec2.multiply(Vec2.Vector2FromAngleInDegrees(
                    Vec2.getAngleBetweenInDegrees(getPosition(), pl.getPosition())),
                    3000.0));
            }
            p.destroy();
        });
    }

    public void wander(Vec2 position) {
        double x = position.getX();
        double y = position.getY();
    }

    @Override
    public void destroy() {
        keyB = new Key(this.getPosition(), Vec2.ZERO, new Sprite("./items/keyB"),
            3);
        keyB.setRenderLayer(RenderLayerName.HIGH_BLOCKS);
        super.destroy();
    }

    @Override
    public Enemy createNew(Vec2 pos) {
        return null;
    }

}
