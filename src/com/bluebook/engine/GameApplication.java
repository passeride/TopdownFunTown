package com.bluebook.engine;

import com.bluebook.input.Input;
import com.bluebook.javafx.Controller;
import com.bluebook.javafx.Main;
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

    public GameApplication(){
        singelton = this;
    }

    public static GameApplication getInstance() {
        return singelton;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxml = new FXMLLoader();

        Parent root = fxml.load(getClass().getResource("../../bluebook/javafx/sample.fxml").openStream());
        Controller controller = (Controller) fxml.getController();

        setWidthListener(primaryStage, controller);
        setHeightListener(primaryStage, controller);

        primaryStage.setTitle("Top Down Fun Town");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        setStageKeyListener(primaryStage);

        engine = GameEngine.getInstance();
        input = Input.getInstance();

        onLoad();

        engine.startUpdateThread();
    }

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


    public abstract void update(double delta);


    public void setWindowTitle(String s){

    }

}
