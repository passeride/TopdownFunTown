package com.rominntrenger.objects;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.listeners.OnAnimationFinishedListener;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.objects.player.Player;
import java.util.ArrayList;

/**
 * Explotion is an effect that will spawn a visual Push player back, and also do damadge
 */
public class Explotion extends GameObject {

    double distance = 500;
    double force = 60000;
    int dmg = 4;

    ArrayList<Player> players;

    boolean isFinnished = false;

    /**
     * Explotion will spawn a Explotion on the position given with a random rotation This will push
     * back and hurt player if close
     */
    public Explotion(Vec2 position) {
        super(position, Vec2.Vector2FromAngleInDegrees(Math.random() * 360),
            new AnimationSprite("effects/explosion", 6));
        setSize(new Vec2(5, 5));
        setRenderLayer(RenderLayer.RenderLayerName.PROJECTILE);
        players = ((RomInntrenger) GameApplication.getInstance()).getPlayers();

        playAudio();
        for(Player p : players) {
            addForce(p);
            doDamadge(p);
        }
        setAnimationDestroyListener();
    }

    void setAnimationDestroyListener() {
        ((AnimationSprite) sprite)
            .setOnAnimationFinnishedListener(new OnAnimationFinishedListener() {
                @Override
                public void AnimationFinnished() {
                    isFinnished = true;
                }
            });
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        if(isFinnished){
            destroy();
        }
    }

    void playAudio() {

        AudioPlayer clip = new AudioPlayer("./assets/audio/PaalBoom.wav");
        clip.setSpatial(this);
        clip.playOnce();
    }

    void doDamadge(Player player) {
        double distance2Player = player.getPosition().distance(getPosition());

        double dmgMultiplier = 1 - (distance2Player / distance);
        int dmg2Player = (int) (dmg * dmgMultiplier);
        if (distance2Player < distance) {
            player.hit(dmg2Player);
        }
    }

    void addForce(Player player) {
        double distance2Player = player.getPosition().distance(getPosition());
        double forceMultiplier = 1 - (distance2Player / distance);

        player.rb2.addForce(Vec2.multiply(Vec2.Vector2FromAngleInDegrees(
            Vec2.getAngleBetweenInDegrees(getPosition(), player.getPosition())),
            force * forceMultiplier));

    }
}
