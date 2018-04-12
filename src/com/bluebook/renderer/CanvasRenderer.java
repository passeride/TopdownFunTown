package com.bluebook.renderer;

import com.bluebook.camera.OrtographicCamera;
import com.bluebook.engine.GameEngine;
import com.bluebook.physics.Collider;
import com.bluebook.physics.HitDetectionHandler;
import com.bluebook.util.GameObject;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/**
 * This class is used to draw all {@link GameObject}
 */
public class CanvasRenderer {

    private ArrayList<GameObject> drawables = new ArrayList<>();
    private RenderLayer[] layers = new RenderLayer[RenderLayer.RenderLayerName.values().length];
    private ArrayList<Collider> colliderDebugDrawables = new ArrayList<>();

    private static CanvasRenderer singelton;

    private FPSLineGraph flg;

    private Canvas canvas;

    private CanvasRenderer(){
        for(int i = 0; i < layers.length; i++){
            layers[i] = new RenderLayer(RenderLayer.RenderLayerName.get(i));
        }
        flg = new FPSLineGraph();

    }

    public void addCollider(Collider col){
        synchronized (this) {
            colliderDebugDrawables.add(col);
        }
    }

    public void removeCollider(Collider col){
        synchronized (this) {
            if(colliderDebugDrawables.contains(col))
                colliderDebugDrawables.remove(col);
        }
    }

    /**
     * This function will add the GameObject to the list of drawables to be drawn onto the canvas
     * IF nothing is specified the {@link RenderLayer} beeing used is LOW_BLOCKS
     * @param in Object to be drawn on canvas
     */
    public void addGameObject(GameObject in){
        synchronized (this) {
            layers[RenderLayer.RenderLayerName.LOW_BLOCKS.getValue()].addGameObject(in);
        }
    }

    /**
     * Will add drawable to layer
     * @param in Object to be drawn
     * @param layer RenderLayer to be used
     */
    public void  addGameObject(GameObject in, RenderLayer.RenderLayerName layer){
        synchronized (this) {
            layers[layer.getValue()].addGameObject(in);
        }
    }

    /**
     * Will move gameobject to designated renderlayer
     * @param go
     * @param layer
     */
    public void moveGameObjectToLayer(GameObject go, RenderLayer.RenderLayerName layer){
        synchronized (this){
            removeGameObject(go);
            addGameObject(go, layer);
        }
    }

    /**
     * Will return the layer this objects is in
     * @param go {@link GameObject} to be searched for
     * @return {@link com.bluebook.renderer.RenderLayer.RenderLayerName} that contains GO
     */
    public RenderLayer.RenderLayerName getLayer(GameObject go){
        synchronized (this){
            for(int i = 0; i < layers.length; i++){
                if(layers[i].hasGameObject(go))
                    return RenderLayer.RenderLayerName.get(i);
            }
        }
        return null;
    }

    /**
     * Will remove the {@link GameObject} from all {@link RenderLayer}
     * @param go {@link GameObject} to be removed
     */
    public void removeGameObject(GameObject go){
        synchronized (this) {
            for(int i =  0; i < layers.length; i++){
                layers[i].removeGameObject(go);
            }
        }
    }

    /**
     * Returns the instance
     */
    public static CanvasRenderer getInstance(){
        if(singelton == null)
            singelton = new CanvasRenderer();

        return singelton;
    }

    /**
     * Draw all objects onto canvas
     */
    public void drawAll(){
        synchronized (this) {
            GraphicsContext gc = canvas.getGraphicsContext2D();

            gc.save();


            clearCanvas(gc);
            for(int i = 0; i < layers.length;  i++){
                layers[i].drawAll(gc);
            }

            if (GameEngine.DEBUG) {
                for (Collider c : colliderDebugDrawables) {
                    c.debugDraw(gc);
                }
                flg.addFPS(GameEngine.getInstance().FPS);
                flg.draw(gc);
                HitDetectionHandler.getInstance().qtTree.draw(gc);
            }
            if(OrtographicCamera.main != null)
            gc.restore();
        }
    }

    /**
     * Draw all objects onto canvas
     * @param canvas canvas for objects to be drawn to
     */
    public void drawAll(Canvas canvas){
        setCanvas(canvas);
        drawAll();
    }

    private void clearCanvas(GraphicsContext gc){ ;
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }

    public void setCanvas(Canvas canvas){
        this.canvas = canvas;
    }

}
