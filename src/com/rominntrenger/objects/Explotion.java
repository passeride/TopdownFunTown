package com.rominntrenger.objects;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameApplication;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.renderer.RenderLayer.RenderLayerName;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.objects.blocks.Soot;
import com.rominntrenger.objects.player.Player;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Explotion is an effect that will spawn a visual Push player back, and also do damadge
 */
public class Explotion extends GameObject {

    /**
     * Creates an ArrayList of Explotion.
     */
    private static CopyOnWriteArrayList<Explotion> allSplotions = new CopyOnWriteArrayList<>();

    double distance = 500;
    double force = 6000;
    int dmg = 4;
    private double splotionTime = 1.5;
    private double timePassed = 0.0;
    private double maxSize = 3;
    private double dropRate;

    CopyOnWriteArrayList<Player> players;
    boolean isFinnished = false;
    Vec2 startSize;

    /**
     * Explotion will spawn a Explotion on the position given with a random rotation This will push
     * back and hurt player if close
     */
    public Explotion(Vec2 position, Vec2 rotation, Vec2 startSize, double dropRate) {
        super(position, rotation,
            new AnimationSprite("effects/explosion", 5));
        ((AnimationSprite) sprite).setLength(0.1);
        allSplotions.add(this);
        this.startSize = startSize;
        this.dropRate = dropRate;
//        setSize(new Vec2(5, 5));
        setRenderLayer(RenderLayerName.HIGH_BLOCKS);
        players = ((RomInntrenger) GameApplication.getInstance()).getPlayers();

        if (players.size() == 0)
            destroy();

        for (Player p : players) {
            addForce(p);
            doDamadge(p);
        }
        playAudio();

        setAnimationDestroyListener();
    }

    /**
     * Clears all explosions. For resetting the game.
     */
    public static void clearAllExplotions() {
        Iterator<Explotion> iterator = allSplotions.iterator();
        while (iterator.hasNext()) {
            Explotion explotion = iterator.next();
            explotion.destroy();
        }
        allSplotions.clear();
    }

    /**
     * Checks if the animation is done playing.
     */
    void setAnimationDestroyListener() {
        ((AnimationSprite) sprite)
            .setOnAnimationFinnishedListener(() -> isFinnished = true);
    }

    /**
     * Destroy function, drops item if the randomizer is right, if not it drops a soot.
     */
    @Override
    public void destroy() {
        if (Math.random() > dropRate)
            ((RomInntrenger) GameApplication.getInstance()).addRandomItem.randomElement().spawn(getPosition());
        else
            new Soot(getPosition());
        allSplotions.remove(this);
        super.destroy();
    }

    /**
     * Update function, checks if the explosion is done playing.
     * @param delta
     */
    @Override
    public void update(double delta) {
        super.update(delta);

        timePassed += delta;
        if (timePassed >= splotionTime)
            destroy();
        double sizeModifier = timePassed / splotionTime;
        setSize(Vec2.add(startSize, new Vec2(sizeModifier * maxSize, sizeModifier * maxSize)));


        if (isFinnished) {
            destroy();
        }
    }

    /**
     * Plays audio from specific audio clip.
     */
    void playAudio() {
        AudioPlayer clip = new AudioPlayer("audio/PaalBoom.wav");
        clip.setSpatial(this);
        clip.playOnce();
    }

    /**
     * Checks if the Player is close enough to be damaged from the Explotion impact.
     * @param player
     */
    void doDamadge(Player player) {
        double distance2Player = player.getPosition().distance(getPosition());

        double dmgMultiplier = 1 - (distance2Player / distance);
        int dmg2Player = (int) (dmg * dmgMultiplier);
        if (distance2Player < distance) {
            player.hit(dmg2Player);
        }
    }

    /**
     * Adds physics that push the player away if it is close enough.
     * @param player
     */
    void addForce(Player player) {
        double distance2Player = player.getPosition().distance(getPosition());
        double forceMultiplier = 1 - (distance2Player / distance);

        player.rb2.addForce(Vec2.multiply(Vec2.Vector2FromAngleInDegrees(
            Vec2.getAngleBetweenInDegrees(player.getPosition(), getPosition())),
            force * forceMultiplier));

    }
}
