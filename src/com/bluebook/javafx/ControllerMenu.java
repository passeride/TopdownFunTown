package com.bluebook.javafx;


import com.bluebook.engine.GameApplication;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMenu implements Initializable{

    GameApplication gameApplication;

    @FXML
    private ImageView imageViewPug;
    @FXML
    private Label label;


        @Override
    public void initialize(URL location, ResourceBundle resources){
        gameApplication = GameApplication.getInstance();
        Path path = new Path();
        path.getElements().add(new MoveTo(1800,970));
        path.getElements().add(new CubicCurveTo(760, 0, 760, 240, 1500, 800));
        path.getElements().add(new CubicCurveTo(0, 240, 0, 480, 760, 480));
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(3000));
        pathTransition.setPath(path);
        pathTransition.setNode(imageViewPug);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();
        }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        Stage stage = gameApplication.getStage();
        gameApplication.callGame(stage);
    }


}
