package com.bluebook.renderer;

import com.bluebook.camera.OrthographicCamera;
import com.bluebook.engine.GameEngine;
import com.bluebook.physics.Collider;
import com.bluebook.physics.HitDetectionHandler;
import com.bluebook.util.GameObject;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class is used to draw all {@link GameObject}
 */
public class CanvasRenderer {

    private static final boolean useGraphicsRenderer = false;
    private ArrayList<GameObject> drawables = new ArrayList<>();
    private RenderLayer[] layers = new RenderLayer[RenderLayer.RenderLayerName.values().length];
    private CopyOnWriteArrayList<Collider> colliderDebugDrawables = new CopyOnWriteArrayList<>();

    private Color bgColor = Color.BLACK;

    private static CanvasRenderer singelton;

    private FPSLineGraph flg;

    private Canvas canvas;

    private CanvasRenderer() {
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new RenderLayer();
        }
        flg = new FPSLineGraph();

    }

    public void addCollider(Collider col) {
        synchronized (colliderDebugDrawables) {
            colliderDebugDrawables.add(col);
        }
    }

    public void removeCollider(Collider col) {
        synchronized (colliderDebugDrawables) {
            colliderDebugDrawables.remove(col);
        }
    }

    /**
     * This function will add the GameObject to the list of drawables to be drawn onto the canvas IF
     * nothing is specified the {@link RenderLayer} beeing used is LOW_BLOCKS
     *
     * @param in Object to be drawn on canvas
     */
    public void addGameObject(GameObject in) {
//        synchronized (this) {
        layers[RenderLayer.RenderLayerName.LOW_BLOCKS.getValue()].addGameObject(in);
//        }
    }

    /**
     * Will add drawable to layer
     *
     * @param in    Object to be drawn
     * @param layer RenderLayer to be used
     */
    private void addGameObject(GameObject in, RenderLayer.RenderLayerName layer) {
//        synchronized (this) {
        layers[layer.getValue()].addGameObject(in);
//        }
    }

    /**
     * Will move gameobject to designated renderlayer
     */
    public void moveGameObjectToLayer(GameObject go, RenderLayer.RenderLayerName layer) {
//        synchronized (this) {
        removeGameObject(go);
        addGameObject(go, layer);
//        }
    }

    /**
     * Will return the layer this objects is in
     *
     * @param go {@link GameObject} to be searched for
     * @return {@link com.bluebook.renderer.RenderLayer.RenderLayerName} that contains GO
     */
    public RenderLayer.RenderLayerName getLayer(GameObject go) {
//        synchronized (this) {
        for (int i = 0; i < layers.length; i++) {
            if (layers[i].hasGameObject(go)) {
                return RenderLayer.RenderLayerName.get(i);
            }
        }
//        }
        return null;
    }

    /**
     * Will remove the {@link GameObject} from all {@link RenderLayer}
     *
     * @param go {@link GameObject} to be removed
     */
    public void removeGameObject(GameObject go) {
        for (RenderLayer layer : layers) {
            layer.removeGameObject(go);
        }
    }

    /**
     * Returns the instance
     */
    public static CanvasRenderer getInstance() {
        if (singelton == null) {
            singelton = new CanvasRenderer();
        }

        return singelton;
    }

    /**
     * Draw all objects onto canvas
     */
    public void drawAll() {
//        synchronized (this) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.save();

        clearCanvas(gc);
        gc.setFill(bgColor);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        GraphicsRenderer gr = new GraphicsRenderer(gc);

//            gc.setEffect(new ColorAdjust(0, 0, -0.8, 0));

        for (RenderLayer layer : layers) {
            if (useGraphicsRenderer) {
                layer.drawAll(gr);
            } else {
                layer.drawAll(gc);
            }
        }
        gc.restore();


        if (GameEngine.DEBUG) {
            synchronized (colliderDebugDrawables) {
                for (Collider c : colliderDebugDrawables) {
                    c.debugDraw(gc);
                }
            }
            flg.addFPS(GameEngine.getInstance().draw_FPS);
            flg.draw(gc);
            if (HitDetectionHandler.getInstance().qtTree != null)
                HitDetectionHandler.getInstance().qtTree.draw(gc);

        }
        if (OrthographicCamera.main != null) {
            gc.restore();
        }

//        }

    }

    private void setInverseClip(final Node node, final Shape clip) {
        final Rectangle inverse = new Rectangle();
        inverse.setWidth(node.getLayoutBounds().getWidth());
        inverse.setHeight(node.getLayoutBounds().getHeight());
        node.setClip(Shape.subtract(inverse, clip));
    }

    /**
     * Draw all objects onto canvas
     *
     * @param canvas canvas for objects to be drawn to
     */
    public void drawAll(Canvas canvas) {
        setCanvas(canvas);
        drawAll();
    }

    private void clearCanvas(GraphicsContext gc) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

}
