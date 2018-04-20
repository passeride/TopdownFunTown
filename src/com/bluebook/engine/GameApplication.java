package com.bluebook.engine;

import com.bluebook.input.Input;
import com.bluebook.javafx.Controller;
import com.bluebook.renderer.FPSLineGraph;
import com.bluebook.util.GameSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private GameMenu gameMenu;
    @FXML
    private AnchorPane anchorPane;

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

        Pane root = new Pane();
        root.setPrefSize(1920, 1080);

        InputStream is = Files.newInputStream(Paths.get("./assets/pictures/bg.gif"));
        System.out.println(is);
        Image img = new Image(is);
        is.close();

        ImageView imgView = new ImageView(img);
        imgView.setFitWidth(1920);
        imgView.setFitHeight(1080);

        gameMenu = new GameMenu();
        gameMenu.setVisible(true);

        root.getChildren().addAll(imgView, gameMenu);

        Scene scene = new Scene(root);
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

        primaryStage.setScene(scene);
        primaryStage.show();

        this.stage = primaryStage;

        //loadFXML(primaryStage);

    }

//    private void loadFXML(Stage primaryStage) throws IOException {
//        FXMLLoader fxml = new FXMLLoader();
//        fxml.setLocation(new File("assets").toURI().toURL());
//
//        Parent root;
//        if (START_MENU) {
//            root = fxml
//                .load(getClass().getResource("../../bluebook/javafx/MenuC.fxml").openStream());
//        } else {
//            root = fxml
//                .load(getClass().getResource("../../bluebook/javafx/sample.fxml").openStream());
//        }
//
//        primaryStage.setTitle(GameSettings.getString("window_title"));
//        primaryStage.setScene(new Scene(root, GameSettings.getDouble("game_resolution_X"), GameSettings.getDouble("game_resolution_Y")));
//        primaryStage.show();
//
//        if (!START_MENU) {
//            callGame(primaryStage);
//        }
//    }

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

    private class GameMenu extends Parent {
        public GameMenu() {
            VBox menu0 = new VBox(10);
            VBox menu1 = new VBox(10);

            menu0.setTranslateX(100);
            menu0.setTranslateY(300);

            menu1.setTranslateX(100);
            menu1.setTranslateY(300);

            final int offset = 400;

            menu1.setTranslateX(offset);

            MenuButton resumeButtion = new MenuButton("START/RESUME");
            resumeButtion.setOnMouseClicked(event -> {
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), this);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);
                fadeTransition.setOnFinished(evt -> setVisible(false));
                fadeTransition.play();
                Stage stage = getStage();
                try {
                    callGame(stage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


            MenuButton optionsButton = new MenuButton("OPTIONS");
            optionsButton.setOnMouseClicked(event -> {
                getChildren().add(menu1);

                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu0);
                tt.setToX(menu0.getTranslateX() - offset);

                TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu1);
                tt1.setToX(menu0.getTranslateX());

                tt.play();
                tt1.play();

                tt.setOnFinished(evt -> {
                    getChildren().remove(menu0);
                });
            });

            MenuButton exitButton = new MenuButton("EXIT");
            exitButton.setOnMouseClicked(event -> {
                System.exit(0);
            });

            MenuButton backButton = new MenuButton("BACK");
            backButton.setOnMouseClicked(event -> {
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

            MenuButton soundButton = new MenuButton("SOUND");
            MenuButton videoButton = new MenuButton("VIDEO");
            MenuButton goreButton = new MenuButton("GORE");

            menu0.getChildren().addAll(resumeButtion, optionsButton, exitButton);
            menu1.getChildren().addAll(backButton, soundButton, videoButton, goreButton);

            Rectangle bg = new Rectangle(1920, 1080);
            bg.setFill(Color.GREY);
            bg.setOpacity(0.3);

            getChildren().addAll(bg, menu0);
        }
    }

    private static class MenuButton extends StackPane {
        private Text text;

        public MenuButton(String name) {
            text = new Text(name);
            text.setFont(text.getFont().font(20));
            text.setFill(Color.WHITE);

            Rectangle bg = new Rectangle(250, 30);
            bg.setOpacity(0.6);
            bg.setFill(Color.BLACK);
            bg.setEffect(new GaussianBlur(3.5));

            setAlignment(Pos.CENTER_LEFT);
            setRotate(-0.5);
            getChildren().addAll(bg, text);

            setOnMouseEntered(event -> {
                bg.setTranslateX(10);
                text.setTranslateX(10);
                bg.setFill(Color.WHITE);
                text.setFill(Color.BLACK);
            });

            setOnMouseExited(event -> {
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


}
