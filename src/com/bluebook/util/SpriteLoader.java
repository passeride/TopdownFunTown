package com.bluebook.util;

import javafx.scene.image.Image;

public class SpriteLoader {

    public static Image loadImage(String name){
        return new Image("file:./assets/sprite/" + name + ".png");
    }
}
