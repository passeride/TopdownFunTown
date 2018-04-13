package com.rominntrenger.main.gui;

import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Inventory extends GameObject {
    Sprite[] inventoryContents;
    // ArrayList<Sprite> inventoryContents = new ArrayList<Sprite>();
    Boolean hasItem = false;
    private double spacing = 20;
    private Sprite background;
    private int inventorySpace;
    private int itemNum = 0;

    public Inventory(int inventorySpots) {
        super(new Vector2(20,30), Vector2.ZERO, null);
        setRenderLayer(RenderLayer.RenderLayerName.GUI);
        background = new Sprite("items/crate");
        background.setOrigin(transform);

        inventoryContents = new Sprite[inventorySpots];
        addItem(sprite);
        inventorySpace = inventorySpots;
        hasItem = true;
    }

    @Override
    public void draw(GraphicsContext gc) {
        // background.drawGUI(gc, new Vector2(0.1 + ( 0.06 * itemNum / 2), 0.1), new Vector2(itemNum * 2.8, 1.5));
        background.drawGUI(gc, new Vector2(10,20), new Vector2(inventorySpace * 2.8, 1.5));
        if(hasItem) {
            for(int i = 0; i < inventoryContents.length; i++) {
                if(inventoryContents[i] != null) {
                    gc.setFill(Color.WHITE);
                    gc.setStroke(Color.BLACK);
                    gc.setLineWidth(1.0);
                    //gc.strokeRect(20, 40 * i, 200, 100);
                    //gc.fillRect(20, 40 * i, 200, 100);
                    //setSprite(inventoryContents[i]);
                    inventoryContents[i].drawGUI(gc, new Vector2(60 + (60 * i), 10 ));
                    //super.draw(gc);
                }
            }
        }
    }

    public void addItem(Sprite sprite) {
        if(itemNum < inventorySpace) {
            sprite.setOrigin(transform);
            inventoryContents[itemNum++] = sprite;

            System.out.println("Inventory Number is:"+ itemNum);
        }
    }

    public void removeItem(int ID) {
        inventoryContents[ID] = null;
    }


}
