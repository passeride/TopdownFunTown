package com.rominntrenger.main;

import com.rominntrenger.main.maploader.ImageBuffering;
import com.rominntrenger.main.maploader.MapCreator;

import java.awt.image.BufferedImage;

public class RomInntrenger {

    public static void main(String[] args) {
       /*  ImageBuffering loader = new ImageBuffering();
        BufferedImage level;
        level = loader.loadImage("mapStart",32,32);
        ID[][] newLevel = LevelImageLoader.loadLevel(level);
        System.out.println(cake[3][6]);
        */

        ImageBuffering loader = new ImageBuffering();
        BufferedImage thisMap;
        thisMap = loader.loadImage("mapStart",32,32);
        MapCreator level = new MapCreator("../bg/backgroundGradient_01", thisMap);
        level.createLevel();
    }
}
