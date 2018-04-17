package com.rominntrenger.main.objects;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.listeners.OnAnimationFinishedListener;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.main.objects.player.Player;

public class Explotion extends GameObject {

    public double distance = 500;
    public int dmg = 4;

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     */
    public Explotion(Vector2 position) {
        super(position, Vector2.UP, new AnimationSprite("effects/explotion", 11));
        setSize(new Vector2(5, 5));
        setRenderLayer(RenderLayer.RenderLayerName.PROJECTILE);
        Player p = ((RomInntrenger) GameApplication.getInstance()).player;
        p.rb2.addForce(Vector2.multiply(Vector2.Vector2FromAngleInDegrees(Vector2.getAngleBetweenInDegrees(getPosition(), p.getPosition())), 400000.0));

        double distance2Player = p.getPosition().distance(getPosition());

        double dmgMultiplier = 1 - (distance2Player / distance);
        int dmg2Player = (int)(dmg * dmgMultiplier);
        System.out.println(distance2Player  + " : " + dmgMultiplier);

        if(distance2Player < distance){
            p.hit(dmg2Player);
        }

        AudioPlayer clip = new AudioPlayer("./assets/audio/PaalBoom.wav");
        clip.playOnce();


        ((AnimationSprite)sprite).setOnAnimationFinnishedListener(new OnAnimationFinishedListener() {
            @Override
            public void AnimationFinnished() {
                destroy();
            }
        });
    }

    @Override
    public void update(double detla) {
        super.update(detla);
    }
}
