package com.rominntrenger.objects;

import com.bluebook.renderer.RenderLayer.RenderLayerName;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.player.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ScoreElement extends GameObject {

    private Player player;

    public ScoreElement(Player p) {
        super(Vec2.ZERO, Vec2.ZERO, null);
        setRenderLayer(RenderLayerName.GUI);
        allwaysOnScreen = true;
        this.player = p;
    }


    @Override
    public void draw(GraphicsContext gc) {
//        super.draw(gc);

        int enemiesKilled = player.getEnemiesKilled();
        gc.setFont(new Font(40));
//        gc.setFill(Color.WHITE);
//        gc.fillRect(20, 300, 200, 40);
        gc.setFill(Color.BLACK);
        gc.fillText("Enemies Killed: " + enemiesKilled, 20, 300);
    }
}
