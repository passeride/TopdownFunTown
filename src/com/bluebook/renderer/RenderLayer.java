package com.bluebook.renderer;

import com.bluebook.util.GameObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;

/**
 * This is used by {@link CanvasRenderer} to create layers for sprites to be drawn
 */
public class RenderLayer {

    private ArrayList<GameObject> drawables = new ArrayList<>();

    /**
     * Used by {@link RenderLayer} to signify what the layers name is
     */
    public enum RenderLayerName {
        BACKGROUND(0),
        TILES(1),
        ENEMIES(2),
        LOW_BLOCKS(3),
        PLAYER(4),
        HIGH_BLOCKS(5),
        PROJECTILE(6),
        GUI(7);

        private static Map<Integer, RenderLayerName> map = new HashMap<Integer, RenderLayerName>();

        static {
            for (RenderLayerName rLN : RenderLayerName.values()) {
                map.put(rLN.value, rLN);
            }
        }

        public static RenderLayerName valueOf(int renderLayerNameValue) {
            return map.get(renderLayerNameValue);
        }

        public static String[] names = {"BACKGROUND", "ENEMIES", "LOW_BLOCKS", "PLAYER",
            "HIGH_BLOCKS", "PROJECTILE", "GUI"};

        public static RenderLayerName get(int i) {
            return valueOf(i);
        }

        public static int getTotal() {
            return names.length;
        }

        private int value;

        RenderLayerName(int value) {
            this.value = value;
        }

        public String getName() {
            return names[value];
        }

        public int getValue() {
            return value;
        }

    }

    protected void drawAll(GraphicsContext gc) {
        synchronized (this) {
            int drawablesSize = drawables.size();
            GameObject[] gameObjects = new GameObject[drawablesSize];
            gameObjects = drawables.toArray(gameObjects);
            for (int i = 0; i < drawablesSize; i++) {
                GameObject go = gameObjects[i];
                if (go != null && go.isOnScreen()) {
                    go.draw(gc);
                }
            }
        }

    }

    protected void drawAll(GraphicsRenderer gr) {
        int drawablesSize = drawables.size();
        for (int i = 0; i < drawablesSize; i++) {
            GameObject go = drawables.get(i);
            if (go != null) {
                go.draw(gr);
            }
        }

    }

    public boolean hasGameObject(GameObject go) {
        synchronized (this) {
            return drawables.contains(go);
        }
    }


    public void addGameObject(GameObject go) {
        synchronized (this) {
            drawables.add(go);
        }
    }

    public void removeGameObject(GameObject go) {
        synchronized (this) {
            if (drawables.contains(go)) {
                drawables.remove(go);
            }
        }
    }

}
