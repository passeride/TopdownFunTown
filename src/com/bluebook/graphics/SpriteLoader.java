package com.bluebook.graphics;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class SpriteLoader {

    private static HashMap<String, Image> images = new HashMap<>();
    /**
     * Will load a png from assets/sprite/ and return the image, to be used with spriteclass
     * @param name
     * @return
     */
    public static Image loadImage(String name){
        if(!images.containsKey(name)){
            images.put(name, new Image("file:./assets/sprite/" + name + ".png"));
        }
        return images.get(name);
    }

    public static Image[] loadAnimationImage(String name){
        Image[] imageArray = new Image[3];
        for (int i = 0; i < 3; i++) {
            File f = new File("./assets/sprite/" + name + "_" + i + ".png");
            System.out.println("File exists: " + f.exists() + " File path: " + f.getAbsolutePath());
            try {
                imageArray[i] = new Image(new FileInputStream(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return imageArray;
    }
}
