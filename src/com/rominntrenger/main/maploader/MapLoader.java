package com.rominntrenger.main.maploader;

import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MapLoader {

    private static HashMap<String, Image> images = new HashMap<>();
    private static HashMap<String, Image> bgImages = new HashMap<>();

    private BufferedImage image; // = new BufferedImage(188, 188, BufferedImage.TYPE_INT_RGB);
    public BufferedImage loadImage(String name, int w, int h) {
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        File f = new File("./assets/maps/" + name + ".png");
        // System.out.println(f.getAbsolutePath());
        // System.out.println("FILE EXISTS: " + f.exists());
        try {
            //URL url = new URL(getCodeBase(), "/assets/maps/" + name + ".png");
            image = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}