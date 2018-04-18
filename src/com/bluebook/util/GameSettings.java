package com.bluebook.util;

import com.bluebook.engine.GameEngine;

import java.util.Map;

/**
 * This class is used to load a settings file and make them accessible
 *
 */
public class GameSettings {

    public static int SCREEN_WIDTH, SCREEN_HEIGHT;

    private static Map<String, String> loadedSettings;

    public static void setLoadedSettings(Map<String, String> inMap){
        loadedSettings = inMap;
    }

    public static double getDouble(String s){
        return Double.parseDouble(loadedSettings.get(s));
    }

    public static String getString(String s){
        return loadedSettings.get(s);
    }

    public static int getInt(String s){
        return Integer.parseInt(loadedSettings.get(s));
    }

    public static boolean getBoolean(String s){
        return Boolean.parseBoolean(loadedSettings.get(s));
    }

    public static Vector2 getScreenScale(){
        int resolutionX = GameSettings.getInt("game_resolution_X");
        int resolutionY = GameSettings.getInt("game_resolution_Y");

        System.out.println("GAME ENGINE WIDTH :" + GameEngine.getInstance().canvas.getWidth());

        double x =GameEngine.getInstance().canvas.getHeight() / resolutionX;
        double y = GameEngine.getInstance().canvas.getWidth() / resolutionY;
        return new Vector2(1, 1);
    }

    public static Vector2 getSquareScale(){
        int resolutionX = GameSettings.getInt("game_resolution_X");
        double deadZoneX = GameSettings.getDouble("map_movement_padding_X") * 2;
        int gridX = GameSettings.getInt("grid_X");
        double x = ((resolutionX * (1-deadZoneX)) / gridX);

        int resolutionY = GameSettings.getInt("game_resolution_Y");
        double deadZoneY = GameSettings.getDouble("map_movement_padding_Y") * 2;
        int gridY = GameSettings.getInt("grid_Y");
        double y = ((resolutionY * (1-deadZoneY)) / gridY);

        return new Vector2(x, y);
    }

    public static Vector2 getScreen(){
        int resolutionX = GameSettings.getInt("game_resolution_X");
        int resolutionY = GameSettings.getInt("game_resolution_Y");
        return new Vector2(resolutionX, resolutionY);
    }
}
