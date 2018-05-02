package com.bluebook.engine;

import com.bluebook.input.Input;
import com.bluebook.javafx.Controller;
import com.bluebook.renderer.FPSLineGraph;
import com.bluebook.util.GameSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rominntrenger.gui.Menu;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sun.misc.IOUtils;

/**
 * The GameApplication class is used to create the foundation of any BlueBook gamePane
 * This will call the JavaFX code to start a frame, also start engine with all it's corresponding parts
 */
public abstract class GameApplication extends Application{

    private static final String SETTINGS_PATH = "settings/Default.json";

    private static GameApplication singleton;
//    private boolean START_MENU = false;
    protected Input input;
    private GameEngine engine;
    private Stage primaryStage;
    public static DoubleProperty X_scale = new SimpleDoubleProperty();
    public static DoubleProperty Y_scale = new SimpleDoubleProperty();
//    private GameMenu gameMenu;
    public Menu menu;
    public Pane gamePane;
    boolean gameStarted = false;

    public GameApplication() {
        singleton = this;
    }

    /**
     * Singelton getter
     */
    public static GameApplication getInstance() {
        return singleton;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
            loadSettings();
            this.primaryStage = primaryStage;
            menu = new Menu(primaryStage);
            menu.callMenu();
            primaryStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.F));


    }

    protected void callMenu(){
        GameEngine.getInstance().pauseGame();
        GameApplication.getInstance().getStage().getScene().setRoot(menu.getRoot());

    }

    /**
     * This will be called after FXML is set up, should be a good starting point for loading
     * resources
     */
    protected void onLoad() {

    }

    /**
     * Used to read ./assets/settings/Default.json that contains required settings
     */
    private void loadSettings() {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Gson gson = new Gson();
        GameSettings.setLoadedSettings(gson.fromJson(readSettingsFile(), type));
    }

    private String readSettingsFile() {
        ClassLoader cl = getClass().getClassLoader();
        InputStream stream;
        if (cl==null) {
            stream = ClassLoader.getSystemResourceAsStream(SETTINGS_PATH);
        }else{
            stream = cl.getResourceAsStream(SETTINGS_PATH);
        }

        StringBuilder sb = new StringBuilder();

        int c;

        try {
            while ((c = stream.read()) != -1){
                sb.append((char)c);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private String resolveName(String name) {
        if (name == null) {
            return name;
        }
        if (!name.startsWith("/")) {
            Class c = this.getClass();
            while (c.isArray()) {
                c = c.getComponentType();
            }
            String baseName = c.getName();
            int index = baseName.lastIndexOf('.');
            if (index != -1) {
                name = baseName.substring(0, index).replace('.', '/') + "/" + name;
            }
        } else {
            name = name.substring(1);
        }
        return name;
    }


    private void setStageKeyListener(Stage primaryStage) {
        new Input(primaryStage);
    }

    private void setHeightListener(Stage primaryStage, Controller controller) {
        Y_scale.set( primaryStage.getHeight() / GameSettings.getInt("game_resolution_Y"));
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            Y_scale.set((double) newValue / GameSettings.getInt("game_resolution_Y"));
            //controller.setCanvasHeight((double)newValue);
        });
    }

    private void setWidthListener(Stage primaryStage, Controller controller) {
        X_scale.set(primaryStage.getWidth() / GameSettings.getInt("game_resolution_X"));

        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            X_scale.set((double) newValue / GameSettings.getInt("game_resolution_X"));
            //controller.setCanvasWidth((double) newValue);
        });
    }

    public double getScreenWidth() {
        return primaryStage.getWidth();
    }

    public double getScreenHeight() {
        return primaryStage.getHeight();
    }

    /**
     * This function will be called every tick, should be used for logic
     *
     * @param delta seconds since last frame update
     */
    public abstract void update(double delta);

    public Stage getStage() {
        return primaryStage;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (FPSLineGraph.main != null) {
            System.out.println("Average draw_FPS: " + FPSLineGraph.main.getAverage());
        }
    }

    /**
     * Will start the gamePane, used from Menu to start Game FXML
     * @param primaryStage primary stage of the application
     */
    public void callGame(Stage primaryStage) throws IOException{
        menu.setVisible(false);

        FXMLLoader fxmlGame = new FXMLLoader();

        if(gamePane == null) {
            gamePane = fxmlGame
                    .load(getClass().getClassLoader().getResourceAsStream("com/bluebook/javafx/sample.fxml"));
        }

        primaryStage.getScene().setRoot(gamePane);
        Controller controller = fxmlGame.getController();
//        primaryStage.getScene().setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.ESCAPE) {
//                if (!menu.isVisible()) {
//                    primaryStage.getScene().setRoot(root);
////                    FadeTransition ft = new FadeTransition(Duration.seconds(0.5), menu);
////                    ft.setFromValue(0);
////                    ft.setToValue(1);
////
////                    menu.setVisible(true);
////                    ft.play();
//                }
//                else {
//                    primaryStage.getScene().setRoot(menu.getRoot());
////                    FadeTransition ft = new FadeTransition(Duration.seconds(0.5), menu);
////                    ft.setFromValue(1);
////                    ft.setToValue(0);
////                    ft.setOnFinished(evt -> menu.setVisible(false));
////                    ft.play();
//                }
//            }
//        });
//
        setWidthListener(primaryStage, controller);
       setHeightListener(primaryStage, controller);
//        Scene scene = new Scene(root, 800, 800);
//        primaryStage.setTitle("Top Down Fun Town");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//        primaryStage.setWidth(900);
//
//        primaryStage.setHeight(900);

        if (GameSettings.getBoolean("fullscreen")) {
            primaryStage.setFullScreen(true);
        }

        setStageKeyListener(primaryStage);

        engine = GameEngine.getInstance();
        input = Input.getInstance();


        if(!gameStarted) {
            onLoad();
            gameStarted = true;
        }

        engine.resumeGame();
    }

    public Pane getGamePane() {
        return gamePane;
    }

}
