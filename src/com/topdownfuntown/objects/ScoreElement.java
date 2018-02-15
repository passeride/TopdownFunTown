package com.topdownfuntown.objects;

import com.bluebook.engine.GameApplication;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.topdownfuntown.main.Topdownfuntown;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class ScoreElement extends GameObject {

    Topdownfuntown topdownfuntownObject;
    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     */
    public ScoreElement(Vector2 position) { super(position, Vector2.ZERO, null); }

    @Override
    public void draw(GraphicsContext gc){
        topdownfuntownObject = (Topdownfuntown) GameApplication.getInstance();
        gc.setFont(new Font(80));
        gc.fillText("Score: " + topdownfuntownObject.getScore(), position.getX(),  position.getY());
    }
}
