package com.bluebook.engine;

import com.bluebook.input.Input;
import com.bluebook.javafx.Controller;
import com.bluebook.renderer.FPSLineGraph;
import com.bluebook.util.GameSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The GameApplication class is used to create the foundation of any BlueBook game
 * This will call the JavaFX code to start a frame, also start engine with all it's corresponding parts
 */
public abstract class GameApplication extends Application {

    private static final String SETTINGS_PATH = "./assets/settings/Default.json";

    private static GameApplication singleton;
    private boolean START_MENU = false;
    protected Input input;
    private GameEngine engine;
    private Stage stage;
    private static DoubleProperty X_scale = new SimpleDoubleProperty();
    private static DoubleProperty Y_scale = new SimpleDoubleProperty();

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

        START_MENU = GameSettings.getBoolean("start_menu");

        this.stage = primaryStage;

        loadFXML(primaryStage);

    }

    private void loadFXML(Stage primaryStage) throws IOException {
        FXMLLoader fxml = new FXMLLoader();
        fxml.setLocation(new File("assets").toURI().toURL());

        Parent root;
        if (START_MENU) {
            root = fxml
                .load(getClass().getResource("../../bluebook/javafx/menu.fxml").openStream());
        } else {
            root = fxml
                .load(getClass().getResource("../../bluebook/javafx/sample.fxml").openStream());
        }

        primaryStage.setTitle(GameSettings.getString("window_title"));
        primaryStage.setScene(new Scene(root, GameSettings.getDouble("game_resolution_X"), GameSettings.getDouble("game_resolution_Y")));
        primaryStage.show();

        if (!START_MENU) {
            callGame(primaryStage);
        }
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
        File f = new File(SETTINGS_PATH);
        String s = "";
        if (f.exists()) {
            try {
                s = new String(Files.readAllBytes(f.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    private void setStageKeyListener(Stage primaryStage) {
        new Input(primaryStage);
    }

    private void setHeightListener(Stage primaryStage, Controller controller) {
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            Y_scale.set((double) newValue / GameSettings.getInt("game_resolution_Y"));
            //controller.setCanvasHeight((double)newValue);
        });
    }

    private void setWidthListener(Stage primaryStage, Controller controller) {
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            X_scale.set((double) newValue / GameSettings.getInt("game_resolution_X"));
            //controller.setCanvasWidth((double) newValue);
        });
    }

    public double getScreenWidth() {
        return stage.getWidth();
    }

    public double getScreenHeight() {
        return stage.getHeight();
    }

    /**
     * This function will be called every tick, should be used for logic
     *
     * @param delta seconds since last frame update
     */
    public abstract void update(double delta);

    public Stage getStage() {
        return stage;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (FPSLineGraph.main != null) {
            System.out.println("Average draw_FPS: " + FPSLineGraph.main.getAverage());
        }
    }

    /**
     * Will start the game, used from Menu to start Game FXML
     * @param primaryStage primary stage of the application
     */
    public void callGame(Stage primaryStage) throws IOException{
        FXMLLoader fxmlGame = new FXMLLoader();

        Parent root = fxmlGame
                .load(getClass().getResource("../../bluebook/javafx/sample.fxml").openStream());

        Controller controller = fxmlGame.getController();

        setWidthListener(primaryStage, controller);
        setHeightListener(primaryStage, controller);
        Scene scene = new Scene(root, 800, 800);
        primaryStage.setTitle("Top Down Fun Town");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setWidth(900);

        primaryStage.setHeight(900);

        if (GameSettings.getBoolean("fullscreen")) {
            primaryStage.setFullScreen(true);
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
