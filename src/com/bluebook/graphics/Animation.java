package com.bluebook.graphics;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import com.bluebook.graphics.AnimationSprite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Animation extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage theStage)
    {
/*
        Group root = new Group();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );

        Canvas canvas = new Canvas( 512, 512 );
        root.getChildren().add( canvas );

        GraphicsContext gc = canvas.getGraphicsContext2D();

        AnimationSprite enemy = new AnimationSprite();
        Image[] imageArray = new Image[3];
        for (int i = 0; i < 3; i++) {
            File f = new File("./assets/sprite/enemyGreen_" + i + ".png");
            System.out.println("File exists: " + f.exists() + " File path: " + f.getAbsolutePath());
            try {
                imageArray[i] = new Image(new FileInputStream(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        enemy.animation = imageArray;
        enemy.length = 0.100;

        final long startNanoTime = System.nanoTime();

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                double t = (currentNanoTime - startNanoTime) / 1_000_000_000.0;
                gc.drawImage(enemy.getNextFrame(t), 150, 25);
            }
        }.start();

        theStage.show();*/
    }
}