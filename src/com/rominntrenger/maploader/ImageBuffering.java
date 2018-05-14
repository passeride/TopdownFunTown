package com.rominntrenger.maploader;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class ImageBuffering {
    private BufferedImage image;;

    /**
     * Returns a BufferedImage from a given image file.
     * @param name
     * @return
     */
    public BufferedImage loadImage(String name) {
        image = null;
        try {
            image = ImageIO.read(ImageBuffering.class.getClassLoader().getResourceAsStream("sprite/maps/" + name + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}