package com.topdownfuntown.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HealthElement extends GameObject {

    public static int health = 5;
    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     */
    public HealthElement(Vector2 position) {
        super(position, Vector2.ZERO, null);
    }

    @Override
    public void draw(GraphicsContext gc){
        gc.setFont(new Font(80));
        gc.setFill(Color.RED);
        gc.fillText("Life: " + health, position.getX(),  position.getY());
    }
}
