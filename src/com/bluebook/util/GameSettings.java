package com.bluebook.util;

import com.bluebook.util.Exceptions.SettingNotFoundException;
import java.util.Map;

/**
 * This class is used to load a settings json file and make them accessible
 * Each of the getXXXX will trowh an unchecked exception if the requested config key is not found.
 * To let the user know.
 */
public class GameSettings {

    public static int SCREEN_WIDTH, SCREEN_HEIGHT;

    private static Map<String, String> loadedSettings;

    public static double scale = 1.0;

    public static void setLoadedSettings(Map<String, String> inMap) {
        loadedSettings = inMap;
    }

    /**
     * Will return a double from the settings file if String matches key
     * @param queryString is key that should match config in settings file
     * @return if string is not found will return -1 and throw exception
     */
    public static double getDouble(String queryString) {
        try {
            if(loadedSettings.containsKey(queryString))
                return Double.parseDouble(loadedSettings.get(queryString));
            else
                throw new SettingNotFoundException("Settings with key '" + queryString + "' does not exist");
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Will return a String from the settings file if String matches key
     * @param queryString is key that should match config in settings file
     * @return if String is not found will return "" and throw exception
     */
    public static String getString(String queryString){
            try {
                if(loadedSettings.containsKey(queryString))
                    return loadedSettings.get(queryString);
                else
                    throw new SettingNotFoundException("Settings with key '" + queryString + "' does not exist");
            } catch (SettingNotFoundException e) {
                e.printStackTrace();
                return "";
            }
    }

    /**
     * Will return a integer from the settings file if String matches key
     * @param queryString is key that should match config in settings file
     * @return if String is not found will return -1 and throw exception
     */
    public static int getInt(String queryString) {
        try {
            if(loadedSettings.containsKey(queryString))
                return Integer.parseInt(loadedSettings.get(queryString));
            else
                throw new SettingNotFoundException("Settings with key '" + queryString + "' does not exist");
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Will return a boolean from the settings file if String matches key
     * @param queryString is key that should match config in settings file
     * @return if String is not found will return FALSE and throw exception
     */
    public static boolean getBoolean(String queryString) {
        try {
            if(loadedSettings.containsKey(queryString))
                return Boolean.parseBoolean(loadedSettings.get(queryString));
            else
                throw new SettingNotFoundException("Settings with key '" + queryString + "' does not exist");
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Will return a {@link Vec2} containing the Width (X) and Height (Y) of 1 square on the canvas
     * The number of squares is specified in the settings file under "grid_X"/"grid_y"
     * @return a {@link Vec2} with the squares width and height in pixels
     */
    public static Vec2 getSquareScale() {
        int resolutionX = GameSettings.getInt("game_resolution_X");
        int gridX = GameSettings.getInt("grid_X");
        double x = resolutionX / gridX;
        x *= scale;

        int resolutionY = GameSettings.getInt("game_resolution_Y");
        int gridY = GameSettings.getInt("grid_Y");
        double y = resolutionY / gridY;
        y *= scale;

        return new Vec2(x, y);
    }

    /**
     * Will return a {@link Vec2} where X is width of canvas and Y is height of canvas
     * @return A {@link Vec2} with the screens width and height
     */
    public static Vec2 getScreen() {
        int resolutionX = GameSettings.getInt("game_resolution_X");
        int resolutionY = GameSettings.getInt("game_resolution_Y");
        return new Vec2(resolutionX, resolutionY);
    }
}
