package com.rominntrenger.main;

import java.awt.image.BufferedImage;

public class LevelEditor {

    //BufferedImage != JavaFX
    public void loadLevel(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        int blockSize;

        for (int xx = 0; xx < w; xx++) {
            for (int yy = 0; yy < h; yy++) {
                int pixel = image.getRGB(xx, yy);
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

            }

        }

    }

}