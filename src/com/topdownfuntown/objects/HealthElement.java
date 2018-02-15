package com.topdownfuntown.objects;

import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;
import com.topdownfuntown.main.Topdownfuntown;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HealthElement extends GameObject {

    Topdownfuntown topdownfuntown;
    private int hp;

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     */
    public HealthElement(Vector2 position) {
        super(position, Vector2.ZERO, null);
        hp = GameSettings.getInt("player_health");
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFont(new Font(80));
        gc.setFill(Color.RED);
        gc.fillText("Life: " + hp, position.getX(), position.getY());
    }
}
