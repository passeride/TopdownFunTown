package com.rominntrenger.gui;

import com.bluebook.engine.GameApplication;
import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameMenu extends Parent {
        private VBox menu0, menu1, menu2;
        private Slider soundSlider;
        private Stage primaryStage;
        private final int offset = 400;
        private Rectangle bg;
        public MenuButton resumeButton, optionsButton, exitButton, backButtonMainMenu, soundButton, videoButton, goreButton, backButtonSoundMenu;


    public GameMenu(Stage primaryStage) {
        this.primaryStage = primaryStage;
            menu0 = new VBox(10);
            menu1 = new VBox(10);
            menu2 = new VBox(10);

            menu0.setTranslateX(100);
            menu0.setTranslateY(300);

            menu1.setTranslateX(100);
            menu1.setTranslateY(300);

            menu2.setTranslateX(100);
            menu2.setTranslateY(300);


            menu1.setTranslateX(offset);
            menu2.setTranslateX(offset);

            addButton();

            bg = new Rectangle(1920, 1080);
            bg.setFill(Color.GREY);
            bg.setOpacity(0.3);

            getChildren().addAll(bg, menu0);
        }

        public void addButton(){
            resumeButton = new MenuButton("START/RESUME");
            resumeButton.setOnMouseClicked(event -> {
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), this);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);
                fadeTransition.setOnFinished(evt -> setVisible(false));
//                GameApplication.getInstance().menu.setSTART_MENU(false);
//                GameApplication.getInstance().menu.setBackgroundImageVisible(false);
//                primaryStage.setScene(scene);
//                primaryStage.show();

                fadeTransition.play();

//                GameApplication.getInstance().getStage().getScene().setRoot(GameApplication.getInstance().getStage().getScene().getRoot());
                try {
                    GameApplication.getInstance().callGame(primaryStage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


            optionsButton = new MenuButton("OPTIONS");
            optionsButton.setOnMouseClicked(event -> {
                getChildren().add(menu1);

                TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.25), menu0);
                translateTransition.setToX(menu0.getTranslateX() - offset);

                TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), menu1);
                translateTransition1.setToX(menu0.getTranslateX());

                translateTransition.play();
                translateTransition1.play();

                translateTransition.setOnFinished(evt -> {
                    getChildren().remove(menu0);
                });
            });

            exitButton = new MenuButton("EXIT");
            exitButton.setOnMouseClicked(event -> {
                System.exit(0);
            });

            backButtonMainMenu = new MenuButton("BACK");
            backButtonMainMenu.setOnMouseClicked(event -> {
                getChildren().add(menu0);

                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu1);
                tt.setToX(menu1.getTranslateX() + offset);

                TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu0);
                tt1.setToX(menu1.getTranslateX());

                tt.play();
                tt1.play();

                tt.setOnFinished(evt -> {
                    getChildren().remove(menu1);
                });
            });

            soundButton = new MenuButton("SOUND");
            soundButton.setOnMouseClicked(event -> {
                getChildren().add(menu2);

                TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.25), menu1);
                translateTransition.setToX(menu1.getTranslateX() - offset);

                TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), menu2);
                translateTransition1.setToX(menu1.getTranslateX());

                translateTransition.play();
                translateTransition1.play();

                translateTransition.setOnFinished(evt -> {
                    getChildren().remove(menu1);
                });
            });
            backButtonSoundMenu = new MenuButton("BACK");
            backButtonSoundMenu.setOnMouseClicked(event -> {
                getChildren().add(menu1);

                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu2);
                tt.setToX(menu2.getTranslateX() + offset);

                TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu1);
                tt1.setToX(menu2.getTranslateX());

                tt.play();
                tt1.play();

                tt.setOnFinished(evt -> {
                    getChildren().remove(menu2);
                });
            });
            soundSlider = new Slider();
            soundSlider.setMin(0);
            soundSlider.setMax(100);
            soundSlider.setValue(100);
            soundSlider.setShowTickLabels(true);
            soundSlider.setShowTickMarks(true);


            videoButton = new MenuButton("VIDEO");
            goreButton = new MenuButton("GORE");

            menu0.getChildren().addAll(resumeButton, optionsButton, exitButton);
            menu1.getChildren().addAll(backButtonMainMenu, soundButton, videoButton, goreButton);
            menu2.getChildren().addAll(backButtonSoundMenu, soundSlider);
        }


}

