package com.rominntrenger.gui;

import com.bluebook.engine.GameApplication;
import com.bluebook.engine.GameEngine;
import java.io.IOException;
import java.io.InputStream;
import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Class that creates the menu and handles moving back and forth between game and menu
 */
public class Menu extends Parent {
    private GameMenu gameMenu;
    private Scene scene;
    private Stage primaryStage;
    private Pane root;

    /**
     * Constructor for Menu, sets the Stage.
     * @param primaryStage is the primary stage in your javaFX application
     */
    public Menu(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Calls the menu by creating it
     */
    public void callMenu() {
        root = new Pane();
        root.setPrefSize(1920, 1080);

        InputStream is;
        is = getClass().getClassLoader().getResourceAsStream("sprite/pictures/bg.gif");
        System.out.println(is);
        Image image = new Image(is);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageView backgroundImage = new ImageView(image);
        backgroundImage.setFitWidth(1920);
        backgroundImage.setFitHeight(1080);
        boolean isBackgroundImageVisible = true;
        backgroundImage.setVisible(true);

        gameMenu = new GameMenu(primaryStage);
        gameMenu.setVisible(true);

        root.getChildren().addAll(backgroundImage, gameMenu);

        scene = new Scene(root);
        keyBoardInput();

        primaryStage.setScene(scene);
    }

    /**
     * Checks if escape is clicked and starts game and hides menu or shows menu and pauses game
     */
    public void keyBoardInput() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                if (!gameMenu.isVisible()) {
                    FadeTransition ft = new FadeTransition(Duration.seconds(0.5), gameMenu);
                    ft.setFromValue(0);
                    ft.setToValue(1);

                    gameMenu.setVisible(true);
                    ft.setOnFinished(evt -> GameEngine.getInstance().pauseGame());
                    ft.play();
                } else {
                    FadeTransition ft = new FadeTransition(Duration.seconds(0.5), gameMenu);
                    ft.setFromValue(1);
                    ft.setToValue(0);
                    ft.setOnFinished(evt -> gameMenu.setVisible(false));
                    ft.play();
                    try {
                        GameApplication.getInstance().callGame(primaryStage);
                        GameEngine.getInstance().pauseGame();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public Pane getRoot() {
        return root;
    }
}
