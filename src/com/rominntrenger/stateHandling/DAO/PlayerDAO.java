package com.rominntrenger.stateHandling.DAO;

import com.bluebook.graphics.AnimationSprite;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.player.Player;

import java.io.Serializable;

public class PlayerDAO implements Serializable {

    public int playerID;
    public double[] playerColor = new double[4];
    public int maxPlayerHealth;
    public int playerHealth;

    public double posX, posY, dir;
    public String spritePath;
    public boolean isAnimation = false;
    public int spriteFrames = 1;

    public WeaponDAO weapon;


    public PlayerDAO(Player player) {
        playerID = player.getPlayerID();
        // Color
        playerColor[0] = player.getPlayerColor().getRed();
        playerColor[1] = player.getPlayerColor().getGreen();
        playerColor[2] = player.getPlayerColor().getBlue();
        playerColor[3] = player.getPlayerColor().getOpacity();


        maxPlayerHealth = player.getMaxPlayerHealth();
        playerHealth = player.getPlayerHealth();
        Vec2 poss = player.getTransform().getWorldPosition();
        posX = poss.getX();
        posY = poss.getY();
        dir = player.getTransform().getGlobalRotation().getAngleInDegrees();
        spritePath = player.getSprite().getPath();
        if (player.getSprite() instanceof AnimationSprite) {
            isAnimation = true;
            spriteFrames = ((AnimationSprite) player.getSprite()).getFrameCount();
        }

        if (player.hasWeapon())
            weapon = new WeaponDAO(player.getCurrentWeapon());
    }

}
