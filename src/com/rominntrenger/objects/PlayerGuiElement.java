package com.rominntrenger.objects;

import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer.RenderLayerName;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.player.Player;
import com.rominntrenger.objects.player.Weapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PlayerGuiElement extends GameObject{

    Player player;

    int width = 300;
    int height = 150;
    int x = 20;
    int y = 20;
    double margin = 10;


    public PlayerGuiElement(Player p) {
        super(Vec2.ZERO, Vec2.ZERO, null);
        setRenderLayer(RenderLayerName.GUI);
        allwaysOnScreen = true;
        this.player = p;

        if(p.getPlayerID() == 2 || p.getPlayerID() == 4)
            x = 1920 - width - 20;
        if(p.getPlayerID() == 3 || p.getPlayerID() == 4)
            y = 1080 - height - 20;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        gc.setLineDashes();
        gc.setLineWidth(1);
        Color c = player.getPlayerColor();
        Color bg = new Color(c.getRed(), c.getGreen(), c.getBlue(), 0.3);
        gc.setFill(bg);

        gc.strokeRect(x, y, width, height);
        gc.fillRect(x, y, width, height);

        double row = height / 4;

        // Health
        gc.strokeRect(x + margin /  2, y + margin / 2, width - margin, row - margin);
        gc.setFill(Color.GREEN);
        gc.fillRect(x + margin /  2, y + margin / 2, ((double)player.getPlayerHealth() / (double)player.getMaxPlayerHealth()) * (width - margin), row - margin);

        // Kills
        gc.strokeRect(x + margin /  2,  (row * 1 ) + y + margin / 2, width - margin, row - margin);
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(row - margin * 2));
        gc.fillText("K: " + player.getEnemiesKilled(), x + margin / 2,(row * 2) + y - margin / 2);

        // Ammo
        Weapon w = player.getCurrentWeapon();
        if(w != null) {
            gc.strokeRect(x + margin / 2, (row * 2) + y + margin / 2, width - margin, row - margin);

            gc.setFill(Color.GREEN);
            gc.fillRect(x + margin /  2, (row * 2 ) + y + margin / 2, ((double)w.getAmmoRemaining() / (double)w.getAmmoCap()) * (width - margin), row - margin);

            gc.setFill(Color.BLACK);
            gc.setFont(new Font(row - margin));
            String ammoString = "A: " + w.getAmmoRemaining() + " / " + w.getAmmoCap();
//            gc.fillText(ammoString, x + margin / 2, (row * 3) + y - margin);

        }

    }
}
