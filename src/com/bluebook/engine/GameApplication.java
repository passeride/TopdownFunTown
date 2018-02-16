package com.bluebook.engine;

import com.bluebook.input.Input;
import com.bluebook.javafx.Controller;
import com.bluebook.javafx.ControllerMenu;
import com.bluebook.util.GameSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
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

    @Override
    public void start(Stage primaryStage) throws Exception{

        loadSettings();

        this.stage = primaryStage;

        FXMLLoader fxml = new FXMLLoader();

        Parent rootMenu = fxml.load(getClass().getResource("../../bluebook/javafx/menu.fxml").openStream());
        ControllerMenu controllerMenu = (ControllerMenu) fxml.getController();

        primaryStage.setTitle("TOP DOWN FUN TOWN");
        primaryStage.setScene(new Scene(rootMenu, 1920 , 1080));
        primaryStage.show();


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



        if(GameSettings.getBoolean("fullscreen")){
            primaryStage.setFullScreen(true);
        }else{
            // Sets aspect ratio
            primaryStage.minWidthProperty().bind(scene.heightProperty().multiply(2));
            primaryStage.minHeightProperty().bind(scene.widthProperty().divide(2));
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
