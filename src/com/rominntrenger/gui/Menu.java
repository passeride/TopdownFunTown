package com.rominntrenger.gui;

import com.bluebook.util.GameSettings;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Menu extends Parent{


     private boolean START_MENU = false;
     private GameMenu gameMenu;
     private Scene scene;
     private Stage primaryStage;
     private ImageView backgroundImage;
     private Image image;
     private boolean isBackgroundImageVisible = true;
     Pane root;
    //private static Menu singleton;

//    public static Menu getInstance() {
//        return singleton;
//    }


    public Menu(Stage primaryStage){
        //singleton = this;
        this.primaryStage = primaryStage;
     }

     public void callMenu(){
         START_MENU = GameSettings.getBoolean("start_menu");

        root = new Pane();
        root.setPrefSize(1920, 1080);

         InputStream is = null;
         try {
             is = Files.newInputStream(Paths.get("./assets/pictures/bg.gif"));
         } catch (IOException e) {
             e.printStackTrace();
         }
         System.out.println(is);
         image = new Image(is);
         try {
             is.close();
         } catch (IOException e) {
             e.printStackTrace();
         }

         backgroundImage = new ImageView(image);
         backgroundImage.setFitWidth(1920);
         backgroundImage.setFitHeight(1080);
         if(isBackgroundImageVisible)
            backgroundImage.setVisible(true);
         else{
             backgroundImage.setVisible(false);
             System.out.println("i if test");
         }

         gameMenu = new GameMenu(primaryStage);
         gameMenu.setVisible(true);

         root.getChildren().addAll(backgroundImage, gameMenu);

         scene = new Scene(root);
         keyBoardInput();

         primaryStage.setScene(scene);
         primaryStage.show();
     }

     public void keyBoardInput(){
         scene.setOnKeyPressed(event -> {
             if (event.getCode() == KeyCode.ESCAPE) {
                 if (!gameMenu.isVisible()) {
                     FadeTransition ft = new FadeTransition(Duration.seconds(0.5), gameMenu);
                     ft.setFromValue(0);
                     ft.setToValue(1);

                     gameMenu.setVisible(true);
                     ft.play();
                 }
                 else {
                     FadeTransition ft = new FadeTransition(Duration.seconds(0.5), gameMenu);
                     ft.setFromValue(1);
                     ft.setToValue(0);
                     ft.setOnFinished(evt -> gameMenu.setVisible(false));
                     ft.play();
                 }
             }
         });
     }

    public boolean isSTART_MENU() {
        return START_MENU;
    }

    public void setSTART_MENU(boolean START_MENU) {
        this.START_MENU = START_MENU;
    }

    public boolean isBackgroundImageVisible() {
        return isBackgroundImageVisible;
    }

    public void setBackgroundImageVisible(boolean backgroundImageVisible) {
        backgroundImage.setVisible(false);

        System.out.println("i funksjon"+isBackgroundImageVisible);
    }

    public void setRoot(Pane root) {
        this.root = root;
    }

    public Pane getRoot() {
        return root;
    }

    //        this.stage = primaryStage;

        //loadFXML(primaryStage);

}
