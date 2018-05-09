package com.bluebook.engine;

import com.bluebook.input.Input;
import com.bluebook.renderer.FPSLineGraph;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vec2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * The GameApplication class is used to create the foundation of any BlueBook gamePane
 * This will call the JavaFX code to start a frame, also start engine with all it's corresponding parts
 */
public abstract class GameApplication extends Application {

    private static final String SETTINGS_PATH = "settings/Default.json";

    private static GameApplication singleton;
    protected Input input;
    protected Stage primaryStage;
    public static DoubleProperty X_scale = new SimpleDoubleProperty();
    public static DoubleProperty Y_scale = new SimpleDoubleProperty();
    private Pane gamePane;
    private boolean gameStarted = false;

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

        Pane root = new Pane();

        Vec2 screen = GameSettings.getScreen();
        root.setPrefSize(screen.getX(), screen.getY());
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        callGame(primaryStage);
        primaryStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.F));


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
    protected void loadSettings() {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Gson gson = new Gson();
        GameSettings.setLoadedSettings(gson.fromJson(readSettingsFile(), type));
    }

    private String readSettingsFile() {
        ClassLoader cl = getClass().getClassLoader();
        InputStream stream;
        if (cl == null) {
            stream = ClassLoader.getSystemResourceAsStream(SETTINGS_PATH);
        } else {
            stream = cl.getResourceAsStream(SETTINGS_PATH);
        }

        StringBuilder sb = new StringBuilder();

        int c;

        try {
            while ((c = stream.read()) != -1) {
                sb.append((char) c);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private void setStageKeyListener(Stage primaryStage) {
        new Input(primaryStage);
    }

    private void setHeightListener(Stage primaryStage) {
        Y_scale.set(primaryStage.getHeight() / GameSettings.getInt("game_resolution_Y"));
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            Y_scale.set((double) newValue / GameSettings.getInt("game_resolution_Y"));
        });
    }

    private void setWidthListener(Stage primaryStage) {
        X_scale.set(primaryStage.getWidth() / GameSettings.getInt("game_resolution_X"));

        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            X_scale.set((double) newValue / GameSettings.getInt("game_resolution_X"));
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
     *
     * @param primaryStage primary stage of the application
     */
    public void callGame(Stage primaryStage) throws IOException {

        FXMLLoader fxmlGame = new FXMLLoader();

        if (gamePane == null) {
            gamePane = fxmlGame
                .load(getClass().getClassLoader().getResourceAsStream("com/bluebook/javafx/sample.fxml"));
        }
        primaryStage.getScene().setRoot(gamePane);

        setWidthListener(primaryStage);
        setHeightListener(primaryStage);

        if (GameSettings.getBoolean("fullscreen")) {
            primaryStage.setFullScreen(true);
        }

        setStageKeyListener(primaryStage);

        GameEngine engine = GameEngine.getInstance();
        input = Input.getInstance();


        if (!gameStarted) {
            onLoad();
            gameStarted = true;
        }

        engine.resumeGame();
    }

}
