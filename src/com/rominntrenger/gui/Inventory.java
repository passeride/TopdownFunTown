package com.rominntrenger.gui;

import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import javafx.scene.canvas.GraphicsContext;

public class Inventory extends GameObject {

    // Sprite[] inventoryContents;
    InventoryItem[] inventoryContents;
    Boolean hasItem = false;
    private double spacing = 20;
    private Sprite background;
    private int inventorySpace;
    private int itemNum = 0;
    private int x = 25;
    private int y = 135;
    private int bgWidth;
    private int bgHeight = 100;
    private int itemWidth = 64;
    private int itemHeight = 64;

    public Inventory(int inventorySpots) {
        super(new Vec2(20, 30), Vec2.ZERO, null);
        setRenderLayer(RenderLayer.RenderLayerName.GUI);
        background = new Sprite("../bg/inventoryBG");
        background.setOrigin(transform);
        allwaysOnScreen = true;

        /*
        //fills the box
        gc.setFill(Color.rgb(13,13,13,0.8));
        gc.fillRect(margin / 2, resolutionY - height / 2 - margin / 2, (resolutionX - margin), height - margin);


        //border for box
        gc.setStroke(Color.rgb(217, 217, 217));
        gc.setLineWidth(5);
        gc.strokeRect(margin / 2, resolutionY - height / 2 - margin / 2, (resolutionX - margin), height - margin);
        */

        inventoryContents = new InventoryItem[inventorySpots];
        //addItem(item);
        inventorySpace = inventorySpots;
        hasItem = true;
    }

    @Override
    public void draw(GraphicsContext gc) {
        bgWidth = 70 + inventorySpace * 100;
        background.drawGUI(gc, new Vec2(x, y), bgWidth, bgHeight);

        if (hasItem) {
            for (int i = 0; i < inventoryContents.length; i++) {
                if (inventoryContents[i] != null) {
                    inventoryContents[i].getSprite().drawGUI(gc,
                        new Vec2(x + (100 * i) + 50, y + (bgHeight / 2 - itemHeight / 2)),
                        itemWidth, itemHeight);
                }
            }
        }
    }

    public void addItem(InventoryItem item) {
        System.out.println("Start InventorNyum:" + itemNum);
        if (itemNum < inventorySpace) {
            item.getSprite().setOrigin(transform);
            inventoryContents[itemNum++] = item;

            System.out.println("Inventory Number is:" + itemNum);
        }
    }

    public void removeItem(int ID) {
        inventoryContents[ID] = null;
    }


}
