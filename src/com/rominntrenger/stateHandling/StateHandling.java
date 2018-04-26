package com.rominntrenger.stateHandling;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class StateHandling {

    int waveNumber = 0;
    int playerHealth = 100;
    double playerPositionX;
    double playerPositionY;
    double playerDirectionInDegrees;

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
    public int loadPlayerHealth(){
        try {
            FileInputStream saveFile = new FileInputStream("./saveFiles/SavePlayerHealth.sav");
            ObjectInputStream save = new ObjectInputStream(saveFile);
            playerHealth = (int) save.readObject();
            save.close();
            System.out.println("playerHealth : " + playerHealth);
            return playerHealth;

        }catch (Exception exc){
            exc.printStackTrace();
        }
        if(playerHealth == 0)
            playerHealth = 10;
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
    public double loadPlayerDirectioninDegrees(){
        try{
            FileInputStream saveFile = new FileInputStream("./saveFiles/SavePlayerDirectionInDegrees.sav");
            ObjectInputStream save = new ObjectInputStream(saveFile);
            playerDirectionInDegrees = (double) save.readObject();
            save.close();
            System.out.println("player in degrees : " + playerDirectionInDegrees);
            return playerDirectionInDegrees;
        }catch (Exception exc){
            exc.printStackTrace();
        }
        return playerDirectionInDegrees;
    }

}
