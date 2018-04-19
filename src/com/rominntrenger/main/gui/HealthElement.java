package com.rominntrenger.main.gui;

import com.bluebook.camera.OrthographicCamera;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HealthElement extends GameObject {

    private int hp, maxHp;

    int numberOfSpriteElements = 16;
    Sprite[] sprites = new Sprite[numberOfSpriteElements];

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public HealthElement(Vec2 position) {
        super(new Vec2(0, 0), Vec2.ZERO, null);
        allwaysOnScreen = true;
        hp = GameSettings.getInt("player_health");
        maxHp = hp;
        setRenderLayer(RenderLayer.RenderLayerName.GUI);
        setSize(new Vec2(3, 3));

        for (int i = 1; i < numberOfSpriteElements; i++) {
            sprites[i] = new Sprite("health/health" + (numberOfSpriteElements - i));
        }
    }

    public int getSpriteNumber() {
        double ret = (double) hp / (double) maxHp * ((double) numberOfSpriteElements - 1.0);
        return (int) Math.min(Math.max(ret, 1), numberOfSpriteElements - 1);
    }

    @Override
    public void draw(GraphicsContext gc) {
        setPosition(new Vec2(-OrthographicCamera.main.getX() + 200,
            -OrthographicCamera.main.getY() + 200));
        setSprite(sprites[getSpriteNumber()]);
        Sprite sp = sprites[getSpriteNumber()];
        sp.drawGUI(gc, new Vec2(25, 45), 600, 250);

        if (GameEngine.DEBUG) {
            gc.setFill(Color.BLACK);
            gc.setFont(new Font("Arial", 50));
            gc.fillText("HP: " + hp, 50, 75);
        }

    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
