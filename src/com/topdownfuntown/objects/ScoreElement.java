package com.topdownfuntown.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class ScoreElement extends GameObject {

    public int score = 0;

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     */
    public ScoreElement(Vector2 position) {
        super(position, Vector2.ZERO, null);
    }

    @Override
    public void draw(GraphicsContext gc){
        gc.setFont(new Font(80));
        gc.fillText("Score: " + score, position.getX(),  position.getY());
    }
}
