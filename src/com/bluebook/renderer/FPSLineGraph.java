package com.bluebook.renderer;

import com.bluebook.engine.GameEngine;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

/**
 * used for debug drawing a graph of fps
 */
public class FPSLineGraph {

    public static FPSLineGraph main;
    private ArrayList<Double> fpsArray = new ArrayList<>();
    private int arrayCounter = 0;

    double width = 600, height = 150, X = 0, Y = 0;

    double max_value = 100, min_value = 10;

    int pointsOnGraph = 100;

    int horziontal_lines = 5;
    int fontSize = 20;

    public FPSLineGraph(){
        main = this;
    }

    // TODO: make stuff right bro, also optimize
    public void draw(GraphicsContext gc){

        // Drawing horizontal  lines
        for(int i = 0;  i < horziontal_lines; i++){
            double tmpY = Y + (height / horziontal_lines) * i;
            //double tmpY = lerp(Y, Y + height, i / horziontal_lines);
            gc.setFont(new Font(fontSize));
            gc.fillText((int)(max_value - ( (max_value - min_value) / horziontal_lines) * i) + ":", X, tmpY);
            gc.setLineWidth( 2);
            gc.strokeLine(X + fontSize, tmpY, X + width, tmpY);
        }

        Double[] tmpArr = new Double[100];
        fpsArray.toArray(tmpArr);

        Double prev = 0.0;
        int points =  fpsArray.size() > pointsOnGraph ? pointsOnGraph : fpsArray.size();
        gc.setFill(Color.BLUE);
        for(int i = 0; i < points; i++){

            double FPS = fpsArray.get(fpsArray.size() - i -1);
            double tmpY = Y + height + mapFPSValue(FPS);
            gc.strokeLine(X + (width / pointsOnGraph) * (points - i),  tmpY, X + (width / pointsOnGraph) * (points - i + 1),tmpY);
        }

        gc.setFont(new Font(fontSize));
        double FPS = GameEngine.getInstance().FPS;
        gc.setFill(FPS > 60 ? Color.BLACK : Color.RED);
        gc.fillText("FPS: "  + GameEngine.getInstance().FPS, X, Y + height);
    }

    public double getAverage(){
        double fpsSum = 0.0;
        for(Double d: fpsArray){
            fpsSum += (double)d;
        }

        return fpsSum / fpsArray.size();
    }

    public double mapFPSValue(double  fps){
        return fps / max_value - min_value * height;
    }

    float lerp(float point1, float point2, float alpha)
    {
        return point1 + alpha * (point2 - point1);
    }

    public void addFPS(double FPS){
        fpsArray.add(FPS);
        if(fpsArray.size() > pointsOnGraph){
            ArrayList<Double> tmp = (ArrayList<Double>)fpsArray.clone();
            fpsArray.clear();
            fpsArray.addAll(tmp.subList(tmp.size() - pointsOnGraph, tmp.size()));
        }
    }



}
