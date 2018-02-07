package com.bluebook.graphics;

import javafx.scene.image.Image;

public class SpriteLoader {

    /**
     * Will load a png from assets/sprite/ and return the image, to be used with spriteclass
     * @param name
     * @return
     */
    public static Image loadImage(String name){
        return new Image("file:./assets/sprite/" + name + ".png");
    }
}
