package com.bluebook.util;

import java.util.Map;

public class GameSettings {

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
}
