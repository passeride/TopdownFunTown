package com.bluebook.javafx;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import com.bluebook.javafx.Controller;
import com.bluebook.input.InputHandler;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxml = new FXMLLoader();
        Parent root = fxml.load(getClass().getResource("sample.fxml").openStream());
        Controller controller = (Controller) fxml.getController();

        setWidthListener(primaryStage, controller);
        setHeightListener(primaryStage, controller);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        setStageKeyListener(primaryStage,  controller);

    }

    private void setStageKeyListener(Stage primaryStage, Controller controller){
        primaryStage.addEventHandler(KeyEvent.ANY, new InputHandler(controller.getEngine()));
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

    public static void main(String[] args) {
        launch(args);
    }
}
