package com.bluebook.graphics;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * Will help Sprite to load images
 * Will also use caching of images to avoid to many reads
 */
public class SpriteLoader {

    private static HashMap<String, Image> images = new HashMap<>();
    private static HashMap<String, Image[]> animationImages = new HashMap<>();
    private static HashMap<String, Image> bgImages = new HashMap<>();
    private static int frameInputNum;

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

    /**
     * Will load png from asset/bg and return image
     * @param name
     * @return
     */
    public static  Image loadBackground(String name){
        if(!bgImages.containsKey(name)){
            bgImages.put(name, new Image("file:./assets/bg/" + name + ".png"));
        }
        return bgImages.get(name);
    }

    /**
     * Will load set of sprites with naming convention name_X.png where X is
     * a numbering sequence
     * @param name name of
     * @return
     */
    public static Image[] loadAnimationImage(String name, int frameNumber){
        if(!animationImages.containsKey(name)) {
            frameInputNum = frameNumber;
            Image[] imageArray = new Image[frameInputNum];
            for (int i = 0; i < frameInputNum; i++) {
                File f = new File("./assets/sprite/" + name + "_" + i + ".png");
                try {
                    imageArray[i] = new Image(new FileInputStream(f));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            animationImages.put(name, imageArray);
        }
        return animationImages.get(name);
    }
}