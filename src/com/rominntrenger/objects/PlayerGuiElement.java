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
    private int space = 5;
    Sprite health;
    int healthW;
    int healthH;
    Sprite skull;
    int skullW;
    int skullH;
    Sprite ammo;
    int ammoW;
    int ammoH;

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

        gc.strokeRect(x, y, width+32+32, height); // TODO: Unhardcode this somehow, can't use healthW+skullW+ammoW because they do not yet exist
        gc.fillRect(x, y, width+32+32, height);

        double row = height / 4;

        // Health
        health = new Sprite("./gui/health",transform);
        health.setSquareHeight(35);
        health.setSquareWidth(35);
        healthH = (int)health.getSquareHeight();
        healthW = (int)health.getSquareWidth();
        health.drawGUI(gc, new Vec2(x, y), healthW, healthH);

        gc.strokeRect(x + (double)healthW + margin + space /  2, y + margin / 2, width - margin, row - margin);
        gc.setFill(Color.GREEN);
        gc.fillRect(x + (double)healthW + margin + space /  2, y + margin / 2, ((double)player.getPlayerHealth() / (double)player.getMaxPlayerHealth()) * (width - margin), row - margin);

        // Kills
        skull = new Sprite("./gui/killNum",transform);
        skull.setSquareHeight(35);
        skull.setSquareWidth(35);
        skullH = (int)skull.getSquareHeight();
        skullW = (int)skull.getSquareWidth();
        skull.drawGUI(gc, new Vec2(x, y + healthH), skullW, skullH);

        gc.strokeRect(x + skullW + margin + space /  2,  (row * 1 ) + y + margin / 2, width - margin, row - margin);
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(row - margin * 2+space));
        gc.fillText(" "+player.getEnemiesKilled(), x + skullW + (space*2) + margin / 2 ,(row * 2) + y - margin / 2);

        // Ammo
        ammo = new Sprite("./gui/ammo",transform);
        ammo.setSquareHeight(35);
        ammo.setSquareWidth(35);
        ammoH = (int)ammo.getSquareHeight();
        ammoW = (int)ammo.getSquareWidth();
        ammo.drawGUI(gc, new Vec2(x, y + healthH + skullH), ammoW, ammoH);

        Weapon w = player.getCurrentWeapon();
        if(w != null) {
            gc.strokeRect(x + margin + ammoW + space / 2, (row * 2) + y + skullH + healthH + margin / 2, width - margin, row - margin);

            gc.setFill(Color.GREEN);
            gc.fillRect(x + margin + ammoW + space /  2, (row * 2 ) + y + skullH + healthH + margin / 2, ((double)w.getAmmoRemaining() / (double)w.getAmmoCap()) * (width - margin), row - margin);

            gc.setFill(Color.BLACK);
            gc.setFont(new Font(row - margin));
            String ammoString = "A: " + w.getAmmoRemaining() + " / " + w.getAmmoCap();
//            gc.fillText(ammoString, x + margin / 2, (row * 3) + y - margin);

        }

    }
}