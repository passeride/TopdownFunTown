package com.rominntrenger.stateHandling;

import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.AnimationSprite;
import com.bluebook.util.Vec2;
import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.objects.PlayerSpawn;
import com.rominntrenger.objects.WaveManager;
import com.rominntrenger.objects.player.Player;
import com.rominntrenger.objects.player.RedRifle;
import com.rominntrenger.objects.weapon.Weapon;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class StateHandling {

    int waveNumber = 0;
    int playerHealth = 100;
    double playerPositionX;
    double playerPositionY;
    double playerDirectionInDegrees;
    String pathToPlayerSprite = "./assets/sprite/friendlies/character_0.png";
    RomInntrenger romInntrenger;
    ArrayList<Player> playerArray;


    /**
     * Saves the current gamestate in different files as serialization wasent functioning correctly
     * @param waveNumber
     * @param playerHealth
     * @param playerPositionX
     * @param playerPositionY
     * @param playerDirectionInDegrees
     */
    public void saveGame(int waveNumber, int playerHealth, double playerPositionX, double playerPositionY, double playerDirectionInDegrees){
        try{
            FileOutputStream saveWaveNumberF = new FileOutputStream("./saveFiles/SaveWaveNumber.sav");
            ObjectOutputStream saveWaveNumber = new ObjectOutputStream(saveWaveNumberF);

            FileOutputStream savePlayerHealthF = new FileOutputStream("./saveFiles/SavePlayerHealth.sav");
            ObjectOutputStream savePlayerHealth = new ObjectOutputStream(savePlayerHealthF);

            FileOutputStream savePlayerPositionXF = new FileOutputStream("./saveFiles/SavePlayerPositionX.sav");
            ObjectOutputStream savePlayerPositionX = new ObjectOutputStream(savePlayerPositionXF);

            FileOutputStream savePlayerPositionYF = new FileOutputStream("./saveFiles/SavePlayerPositionY.sav");
            ObjectOutputStream savePLayerPositionY = new ObjectOutputStream(savePlayerPositionYF);

            FileOutputStream savePlayerDirectionInDegreesF = new FileOutputStream("./saveFiles/SavePlayerDirectionInDegrees.sav");
            ObjectOutputStream savePlayerDirectionInDegrees = new ObjectOutputStream(savePlayerDirectionInDegreesF);

            saveWaveNumber.writeObject(waveNumber);
            savePlayerHealth.writeObject(playerHealth);
            savePlayerPositionX.writeObject(playerPositionX);
            savePLayerPositionY.writeObject(playerPositionY);
            savePlayerDirectionInDegrees.writeObject(playerDirectionInDegrees);

            saveWaveNumber.close(); //closes both ObjectOutputStream and FIleOutputStream
            savePlayerHealth.close();
            savePlayerPositionX.close();
            savePLayerPositionY.close();
            savePlayerDirectionInDegrees.close();

        }catch (Exception exc){
            exc.printStackTrace();
        }
    }

    public int loadWaveNumber(){
        try{
            FileInputStream saveFile = new FileInputStream("./saveFiles/SaveWaveNumber.sav");
            ObjectInputStream save = new ObjectInputStream(saveFile);
            waveNumber = (int) save.readObject();
            save.close();
            System.out.println("waveNumber : " + waveNumber);



            return waveNumber;

        }catch (Exception exc){
            exc.printStackTrace();
        }
        if(waveNumber == 0)
            waveNumber = 1;
        return waveNumber;
    }
    public int loadPlayerHealth(Player player){
        try {
            FileInputStream saveFile = new FileInputStream("./saveFiles/SavePlayerHealth.sav");
            ObjectInputStream save = new ObjectInputStream(saveFile);
            playerHealth = (int) save.readObject();
            player.setPlayerHealth(playerHealth);
            save.close();
            System.out.println("playerHealth : " + playerHealth);

            return playerHealth;

        }catch (Exception exc){
            exc.printStackTrace();
        }
        if(playerHealth <= 0)
            playerHealth = 50;
        return  playerHealth;
    }
    public double loadPlayerPositionX(){
        try{
            FileInputStream saveFile = new FileInputStream("./saveFiles/SavePlayerPositionX.sav");
            ObjectInputStream save = new ObjectInputStream(saveFile);
            playerPositionX = (double) save.readObject();
            save.close();
            System.out.println("playerPosX : " + playerPositionX);
            return playerPositionX;

        }catch (Exception exc){
            exc.printStackTrace();
        }
        return playerPositionX;

    }
    public double loadPlayerPositionY(){
        try{
            FileInputStream saveFile = new FileInputStream("./saveFiles/SavePlayerPositionY.sav");
            ObjectInputStream save = new ObjectInputStream(saveFile);
            playerPositionY = (double) save.readObject();
            save.close();
            System.out.println("playerposY : " + playerPositionY);
            return playerPositionY;

        }catch (Exception exc){
            exc.printStackTrace();
        }
        return playerPositionY;

    }
    public double loadPlayerDirectionInDegrees(Player player){
        try{
            FileInputStream saveFile = new FileInputStream("./saveFiles/SavePlayerDirectionInDegrees.sav");
            ObjectInputStream save = new ObjectInputStream(saveFile);
            playerDirectionInDegrees = (double) save.readObject();
//            player = romInntrenger.getPlayers().get(0);
            player.setDirection(Vec2.Vector2FromAngleInDegrees(playerDirectionInDegrees));
            save.close();
            System.out.println("player in degrees : " + playerDirectionInDegrees);
            return playerDirectionInDegrees;
        }catch (Exception exc){
            exc.printStackTrace();
        }
        return playerDirectionInDegrees;
    }

    public void setPlayerPositionFromLoadFile(StateHandling stateHandling, Player player){
        double posX = stateHandling.loadPlayerPositionX();
        double posY = stateHandling.loadPlayerPositionY();
        player.setPosition(new Vec2(posX, posY));
    }

    public void setAllLoadData(StateHandling stateHandling){
        // Pause Game
        GameEngine.getInstance().pauseGame();
        RomInntrenger romInntrenger = (RomInntrenger) RomInntrenger.getInstance();
        romInntrenger.clearGamestate();

        // Resuming player
        Player player = new Player(PlayerSpawn.position, Vec2.ZERO,
            new AnimationSprite("friendlies/character", 4), 0);
        player.setCurrentWeapon(new Weapon(Vec2.ZERO,
            new AnimationSprite("friendlies/weaponR", 2), Vec2.ZERO));
        player.setPlayerID(0);
        player.setPlayerColor(romInntrenger.playerColor[0]);

        // Wave
        WaveManager waveManager = WaveManager.getInstance();
        int waveNumber = stateHandling.loadWaveNumber();
        waveManager.setWaveNumber(waveNumber);


        // Resuming game
        GameEngine.getInstance().resumeGame();
    }
}
