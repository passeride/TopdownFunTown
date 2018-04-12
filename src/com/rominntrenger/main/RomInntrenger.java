package com.rominntrenger.main;

import com.rominntrenger.main.maploader.NewLevel;
import com.rominntrenger.main.maploader.MapLoader;

import java.awt.image.BufferedImage;

public class RomInntrenger {

    public static void main(String[] args) {
       /*  MapLoader loader = new MapLoader();
        BufferedImage level;
        level = loader.loadImage("mapStart",32,32);
        ID[][] newLevel = LevelLoader.loadLevel(level);
        System.out.println(cake[3][6]);
        */

        MapLoader loader = new MapLoader();
        BufferedImage thisMap;
        thisMap = loader.loadImage("mapStart",32,32);
        // TODO: ERRORES HERE BEWARE
//        NewLevel level = new NewLevel(thisMap);
//        level.createLevel();
    }
}
