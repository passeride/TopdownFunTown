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

/**
 * Explotion is an effect that will spawn a visual Push player back, and also do damadge
 */
public class Explotion extends GameObject {

    double distance = 500;
    double force = 60000;
    int dmg = 4;

    Player player;

    /**
     * Explotion will spawn a Explotion on the position given with a random rotation This will push
     * back and hurt player if close
     */
    public Explotion(Vector2 position) {
        super(position, Vector2.Vector2FromAngleInDegrees(Math.random() * 360),
            new AnimationSprite("effects/explotion", 11));
        setSize(new Vector2(5, 5));
        setRenderLayer(RenderLayer.RenderLayerName.PROJECTILE);
        player = ((RomInntrenger) GameApplication.getInstance()).player;

        playAudio();
        addForce();
        doDamadge();
        setAnimationDestroyListener();
    }

    void setAnimationDestroyListener() {
        ((AnimationSprite) sprite)
            .setOnAnimationFinnishedListener(new OnAnimationFinishedListener() {
                @Override
                public void AnimationFinnished() {
                    destroy();
                }
            });
    }

    @Override
    public void update(double delta) {
        super.update(delta);
    }

    void playAudio() {

        AudioPlayer clip = new AudioPlayer("./assets/audio/PaalBoom.wav");
        clip.setSpatial(this);
        clip.playOnce();
    }

    void doDamadge() {
        double distance2Player = player.getPosition().distance(getPosition());

        double dmgMultiplier = 1 - (distance2Player / distance);
        int dmg2Player = (int) (dmg * dmgMultiplier);
        if (distance2Player < distance) {
            player.hit(dmg2Player);
        }
    }

    void addForce() {
        double distance2Player = player.getPosition().distance(getPosition());
        double forceMultiplier = 1 - (distance2Player / distance);

        player.rb2.addForce(Vector2.multiply(Vector2.Vector2FromAngleInDegrees(
            Vector2.getAngleBetweenInDegrees(getPosition(), player.getPosition())),
            force * forceMultiplier));

    }
}
