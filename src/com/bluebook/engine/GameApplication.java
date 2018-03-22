package com.bluebook.engine;

import com.bluebook.input.Input;
import com.bluebook.javafx.Controller;
import com.bluebook.util.GameSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public abstract class GameApplication extends Application {

    private static GameApplication singelton;
    protected Input input;
    protected GameEngine engine;
    private Stage stage;
    public static DoubleProperty X_scale = new SimpleDoubleProperty();
    public static DoubleProperty Y_scale = new SimpleDoubleProperty();
    public Map<String, String> loadedSettings;

    public GameApplication(){
        singelton = this;
    }

    /**
     * Singelton getter
     * @return
     */
    public static GameApplication getInstance() {
        return singelton;
    }

    private GameMenu gameMenu;
    private GameEngine gameEngine;

    @Override
    public void start(Stage primaryStage) throws Exception{

        loadSettings();

        this.stage = primaryStage;

        Pane rootMenu = new Pane();

        InputStream is = Files.newInputStream(Paths.get("/home/pm/Git/topdownfuntown/assets/pictures/mountain.jpg"));
        Image image = new Image(is);
        is.close();

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(1920);
        imageView.setFitHeight(1080);

        gameMenu = new GameMenu();

        rootMenu.getChildren().addAll(imageView, gameMenu);

        Scene menuScene = new Scene(rootMenu, 1920, 1080);
        menuScene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                if(!gameMenu.isVisible()){
                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), gameMenu);
                    fadeTransition.setFromValue(0);
                    fadeTransition.setToValue(1);

                    gameMenu.setVisible(true);
                    fadeTransition.play();

                    //calling the game
                    callGame(this.stage);

                }else{
                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), gameMenu);
                    fadeTransition.setFromValue(1);
                    fadeTransition.setToValue(0);
                    fadeTransition.setOnFinished(evt -> gameMenu.setVisible(false));
                    fadeTransition.play();



                }
            }
        });

        primaryStage.setTitle("TOP DOWN FUN TOWN");
        primaryStage.setScene(menuScene);
        primaryStage.show();


    }

    private class GameMenu extends Parent{
        public GameMenu(){
            GameApplication gameApplication = GameApplication.getInstance();
            VBox menu0 = new VBox(15);
            VBox menu1 = new VBox(15);

            menu0.setTranslateX(100);
            menu0.setTranslateY(200);

            menu1.setTranslateX(100);
            menu1.setTranslateY(200);

            final int offset = 400;

            menu1.setTranslateX(offset);

            MenuButton buttonResume = new MenuButton("RESUME");
            buttonResume.setOnMouseClicked(event -> {
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), this);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);
                fadeTransition.setOnFinished(evt -> this.setVisible(false));
                fadeTransition.play();

                Stage gameStage = gameApplication.getStage();
                callGame(gameStage);
            });

            MenuButton buttonOptions = new MenuButton("OPTIONS");
            buttonOptions.setOnMouseClicked(event -> {
                getChildren().add(menu1);
                TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.25), menu0);
                translateTransition.setToX(menu0.getTranslateX() - offset);

                TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), menu1);
                translateTransition1.setToX(menu0.getTranslateX());

                translateTransition.play();
                translateTransition1.play();

                translateTransition.setOnFinished(evt ->{
                    getChildren().remove(menu0);
                });
            });

            MenuButton buttonExit = new MenuButton("EXIT");
            buttonExit.setOnMouseClicked(event -> {
                System.exit(0);
            });

            MenuButton buttonBack = new MenuButton("BACK");
            buttonBack.setOnMouseClicked(event -> {
               getChildren().add(menu0);

               TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.25), menu1);
               translateTransition.setToX(menu1.getTranslateX() + offset);

               TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5), menu0);
               translateTransition1.setToX(menu1.getTranslateX());

               translateTransition.play();
               translateTransition1.play();

               translateTransition.setOnFinished(evt ->{
                   getChildren().remove(menu1);
               });
            });

            MenuButton buttonSound = new MenuButton("SOUND");
            MenuButton buttonVideo = new MenuButton("VIDEO");

            menu0.getChildren().addAll(buttonResume, buttonOptions, buttonExit);
            menu1.getChildren().addAll(buttonBack, buttonSound, buttonVideo);

            Rectangle bg = new Rectangle(1920, 1080);
            bg.setFill(Color.SLATEGRAY);
            bg.setOpacity(0.4);

            getChildren().addAll(bg, menu0);
        }
    }

    private static class MenuButton extends StackPane {
        private Text text;

        public MenuButton(String name){
            text = new Text(name);
            text.setFont(text.getFont().font(20));
            text.setFill(Color.WHITE);

            Rectangle bg = new Rectangle(250, 30);
            bg.setOpacity(0.6);
            bg.setFill(Color.BLACK);
            bg.setEffect(new GaussianBlur(3.5));

            setAlignment(Pos.CENTER_LEFT);
            setRotate(-0.5);
            getChildren().addAll(bg,text); // HUSK AT PARENT ER SATT TIL FXML DOC

            setOnMouseEntered(event ->{
                bg.setTranslateX(10);
                text.setTranslateX(10);
                bg.setFill(Color.WHITE);
                text.setFill(Color.BLACK);
            });

            setOnMouseExited(event ->{
                bg.setTranslateX(0);
                text.setTranslateX(0);
                bg.setFill(Color.BLACK);
                text.setFill(Color.WHITE);
            });

            DropShadow drop = new DropShadow(50, Color.WHITE);
            drop.setInput(new Glow());

            setOnMousePressed(event -> setEffect(drop));
            setOnMouseReleased(event -> setEffect(null));

        }
    }



    /**
     * This will be called after FXML is set up, should be a good starting point for loading resources
     */
    protected void onLoad(){

    }

    protected void loadSettings(){
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Gson gson = new Gson();
        GameSettings.setLoadedSettings(gson.fromJson(readSettingsFile(), type));
    }

    private String readSettingsFile(){
        File f = new File("./assets/settings/Default.json");
        String s = "";
        if(f.exists()) {
            try {
                s = new String(Files.readAllBytes(f.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    private void setStageKeyListener(Stage primaryStage){
        new Input(primaryStage);
    }

    private void setHeightListener(Stage primaryStage, Controller controller){
        primaryStage.heightProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Y_scale.set((double) newValue / GameSettings.getInt("game_resolution_Y"));
                //controller.setCanvasHeight((double)newValue);
            }

        });
    }

    private void setWidthListener(Stage primaryStage, Controller controller){
        primaryStage.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                X_scale.set((double) newValue/ GameSettings.getInt("game_resolution_X"));
                //controller.setCanvasWidth((double) newValue);
            }

        });
    }

    public double getSceneX(){
        return stage.getX();
    }

    public double getSceneY(){
        return stage.getY();
    }


    public double getScreenWidth(){
        return stage.getWidth();
    }

    public double getScreenHeight(){
        return stage.getHeight();
    }

    /**
     * This function will be called every tick, should be used for logic
     * @param delta seconds since last frame update
     */
    public abstract void update(double delta);


    public void setWindowTitle(String s){

    }

    public Stage getStage() {
        return stage;
    }

    public void callGame(Stage primaryStage){
        FXMLLoader fxmlGame = new FXMLLoader();


        Parent root = null;
        try {
            root = fxmlGame.load(getClass().getResource("../../bluebook/javafx/sample.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Controller controller = (Controller) fxmlGame.getController();

        setWidthListener(primaryStage, controller);
        setHeightListener(primaryStage, controller);
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setTitle("Top Down Fun Town");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setWidth(900);

        primaryStage.setHeight(900);


        if(GameSettings.getBoolean("fullscreen")){
            primaryStage.setFullScreen(true);
        }else{
            // Sets aspect ratio
            //primaryStage.minWidthProperty().bind(scene.heightProperty().multiply(2));
            //primaryStage.minHeightProperty().bind(scene.widthProperty().divide(2));
        }

        setStageKeyListener(primaryStage);

        engine = GameEngine.getInstance();
        input = Input.getInstance();


        X_scale.set(GameSettings.getInt("game_resolution_X") / getScreenWidth());
        //Y_scale = getScreenHeight() / Integer.parseInt(loadedSettings.get("game_resolution_Y"));
        Y_scale.set(GameSettings.getInt("game_resolution_Y") / getScreenHeight());

        controller.canvas.scaleXProperty().bindBidirectional(X_scale);
        controller.canvas.scaleYProperty().bindBidirectional(Y_scale);

        onLoad();

        engine.startUpdateThread();
        engine.startCollisionThread();
    }

}
