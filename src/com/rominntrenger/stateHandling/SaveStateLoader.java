package com.rominntrenger.stateHandling;

import com.bluebook.camera.OrthographicCamera;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.messageHandling.MessageHandler;
import com.rominntrenger.objects.WaveManager;
import com.rominntrenger.objects.player.Player;
import com.rominntrenger.objects.weapon.Weapon;
import com.rominntrenger.stateHandling.DAO.MetaDAO;
import com.rominntrenger.stateHandling.DAO.PlayerDAO;
import com.rominntrenger.stateHandling.DAO.WeaponDAO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javafx.scene.paint.Color;

public class SaveStateLoader {

    public static MetaDAO load() throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream("./myFile.data");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        return (MetaDAO) in.readObject();
    }

    public static MetaDAO loadFromFile(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        return (MetaDAO) in.readObject();
    }

    public static void loadPreviousSave(RomInntrenger rom) {

        GameEngine.getInstance().pauseGame();

        MetaDAO meta = null;
        try {
            meta = load();
        } catch (IOException e) {
            e.printStackTrace();
            GameEngine.getInstance().resumeGame();
            MessageHandler.getInstance().writeMessage("There was a problem locating the saveFile\n Will create a new save");
            SaveStateSaver.save(rom);
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            GameEngine.getInstance().resumeGame();
            MessageHandler.getInstance().writeMessage("There was a problem loading the saveFile\n Will create a new save");
            SaveStateSaver.save(rom);
            return;
        }

        rom.clearGamestate();
        loadPlayers(rom, meta);
        WaveManager.getInstance().setWaveNumber(meta.waveManager.waveNumber);

        OrthographicCamera.main.moveToFollow();

        GameEngine.getInstance().resumeGame();
        MessageHandler.getInstance().writeMessage("Load Complete");

    }

    private static void loadPlayers(RomInntrenger rom, MetaDAO meta) {
        for (PlayerDAO playerDAO : meta.players) {
            Sprite s;
            if (playerDAO.isAnimation)
                s = new AnimationSprite(playerDAO.spritePath, playerDAO.spriteFrames);
            else
                s = new Sprite(playerDAO.spritePath);
            Player p = new Player(new Vec2(playerDAO.posX, playerDAO.posY),
                Vec2.Vector2FromAngleInDegrees(playerDAO.dir),
                s,
                playerDAO.playerID);

            p.setPlayerColor(new Color(playerDAO.playerColor[0],
                playerDAO.playerColor[1],
                playerDAO.playerColor[2],
                playerDAO.playerColor[3]));

            p.setMaxPlayerHealth(playerDAO.maxPlayerHealth);
            p.setPlayerHealth(playerDAO.playerHealth);


            p.setCurrentWeapon(parseWeapon(playerDAO.weapon));

        }
    }

    private static Weapon parseWeapon(WeaponDAO w) {
        Weapon ret = new Weapon(Vec2.ZERO, new AnimationSprite(w.spritePath, w.spriteFrames), Vec2.ZERO);
        ret.setSpeed(w.speed);
        ret.setDmg(w.dmg);
        ret.setAlteredDmg(w.alteredDmg);
        ret.setAmmoCap(w.ammoCap);
        ret.setAmmoRemaining(w.ammoRemaining);
        ret.setProjectilePath(w.projectilePath);
        ret.setShootInterval(w.shootInterval);
        ret.setPreviousShotTime(w.previousShotTime);
        ret.setWeaponClip(w.weaponClip);
        ret.setWeaponBarrel(w.weaponBarrel);
        ret.setWeaponBase(w.weaponBase);
        return ret;
    }
}
