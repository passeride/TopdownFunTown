package com.bluebook.javafx;

import com.bluebook.input.Input;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("TEST");
        FXMLLoader fxml = new FXMLLoader();
        System.out.println(getClass().getResource(".").getPath());
        Parent root = fxml.load(getClass().getResource("./sample.fxml").openStream());

        Controller controller = (Controller) fxml.getController();

        setWidthListener(primaryStage, controller);
        setHeightListener(primaryStage, controller);

        primaryStage.setTitle("Game GAME! GAAAAME!");
        primaryStage.setScene(new Scene(root, 800, 400));
        primaryStage.show();

        setStageKeyListener(primaryStage);

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

    public static void main(String[] args) {
        launch(args);
    }
}
