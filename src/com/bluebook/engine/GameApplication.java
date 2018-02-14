package com.bluebook.engine;

import com.bluebook.input.Input;
import com.bluebook.javafx.Controller;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class GameApplication extends Application {

    private static GameApplication singelton;
    protected Input input;
    protected GameEngine engine;
    private Stage stage;

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

        this.stage = primaryStage;

        FXMLLoader fxml = new FXMLLoader();

        Parent root = fxml.load(getClass().getResource("../../bluebook/javafx/sample.fxml").openStream());
        Controller controller = (Controller) fxml.getController();

        setWidthListener(primaryStage, controller);
        setHeightListener(primaryStage, controller);

        primaryStage.setTitle("Top Down Fun Town");
        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.show();
        primaryStage.setFullScreen(true);

        setStageKeyListener(primaryStage);

        engine = GameEngine.getInstance();
        input = Input.getInstance();

        onLoad();

        engine.startUpdateThread();
        engine.startCollisionThread();
    }

    /**
     * This will be called after FXML is set up, should be a good starting point for loading resources
     */
    protected void onLoad(){

    }

    private void setStageKeyListener(Stage primaryStage){
        new Input(primaryStage);
    }

    private void setHeightListener(Stage primaryStage, Controller controller){
        primaryStage.heightProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                controller.setCanvasHeight((double)newValue);
            }

        });
    }

    private void setWidthListener(Stage primaryStage, Controller controller){
        primaryStage.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                controller.setCanvasWidth((double) newValue);
            }

        });
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

}
