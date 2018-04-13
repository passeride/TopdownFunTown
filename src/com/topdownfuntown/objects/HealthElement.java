package com.topdownfuntown.objects;

import com.bluebook.camera.OrtographicCamera;
import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;
import com.topdownfuntown.main.Topdownfuntown;
import javafx.scene.canvas.GraphicsContext;

public class HealthElement extends GameObject {

    Topdownfuntown topdownfuntown;
    private int hp, maxHp;

    int numberOfSpriteElements = 16;
    Sprite[] sprites = new Sprite[numberOfSpriteElements];

    /**
     * Constructor for GameObject given position rotation and sprite
     *
     * @param position
     */
    public HealthElement(Vector2 position) {
        super(new Vector2(0, 0), Vector2.ZERO, null);
        hp = GameSettings.getInt("player_health");
        maxHp = hp;
        setRenderLayer(RenderLayer.RenderLayerName.GUI);
        setSize(new Vector2(3, 3));

        for(int i = 1; i < numberOfSpriteElements; i++){
            sprites[i] = new Sprite("health/health" + (numberOfSpriteElements - i));
        }
    }

    public int getSpriteNumber(){
        double ret = (double)hp / (double)maxHp * ((double)numberOfSpriteElements - 1.0);
        return (int)Math.min(Math.max(ret, 1),numberOfSpriteElements - 1);
    }

    @Override
    public void draw(GraphicsContext gc) {
        setPosition(new Vector2(-OrtographicCamera.main.getX() + 200, -OrtographicCamera.main.getY() + 200));
        setSprite(sprites[getSpriteNumber()]);
        Sprite sp = sprites[getSpriteNumber()];
        sp.drawGUIProsentage(gc, new Vector2(0.2, 0.3), new Vector2(2, 4));

    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
