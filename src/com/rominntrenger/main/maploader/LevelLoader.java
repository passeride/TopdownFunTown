package com.rominntrenger.main.maploader;

import java.awt.image.BufferedImage;

public class LevelLoader {

    public void loadLevel(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        int blockSize;
        String color = new String();

        for (int xx = 0; xx < w; xx++) {
            for (int yy = 0; yy < h; yy++) {
                int pixel = image.getRGB(xx, yy);
                color = toHex(pixel);

                if(color == "#FF0000")
                    System.out.println(color);
                else if(color =="#000000")
                    System.out.println(color);
                else
                    System.out.println(color);

                /*
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;


                if (red == 255 && green != 255 && blue != 255)
                    //RomInntrenger.addObject(player);, legg til i ArrayList<> av objekter

                if (blue == 255 && green != 255 && red != 255)
                    // Legg til ting
                if (green == 255 && blue != 255 && red != 255)
                    // Legg til ting
                if (green == 255 && blue == 255 && red != 255)
                    {
                        // Legg til ting, la til brackets fordi fikk error.
                    }
                */

            }

        }
    }

    public String toHex(int value) {
        String hexColor = String.format("#%06X", (0xFFFFFF & value));
        return hexColor;
    }

}