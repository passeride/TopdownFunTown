package com.rominntrenger.gui;

import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Class for creating menu buttons and their actions
 */
class MenuButton extends StackPane {
    private Text text;

    /**
     * Constructor for creating menu buttons and their actions
     */
    MenuButton(String name) {
        text = new Text(name);
        text.getFont();
        text.setFont(Font.font(20));
        text.setFill(Color.WHITE);

        Rectangle rectangle = new Rectangle(250, 30);
        rectangle.setOpacity(0.6);
        rectangle.setFill(Color.BLACK);
        rectangle.setEffect(new GaussianBlur(3.5));

        setAlignment(Pos.CENTER_LEFT);
        setRotate(-0.5);
        getChildren().addAll(rectangle, text);

        setOnMouseEntered(event -> {
            rectangle.setTranslateX(10);
            text.setTranslateX(10);
            rectangle.setFill(Color.WHITE);
            text.setFill(Color.BLACK);
        });

        setOnMouseExited(event -> {
            rectangle.setTranslateX(0);
            text.setTranslateX(0);
            rectangle.setFill(Color.BLACK);
            text.setFill(Color.WHITE);
        });

        DropShadow drop = new DropShadow(50, Color.WHITE);
        drop.setInput(new Glow());

        setOnMousePressed(event -> setEffect(drop));
        setOnMouseReleased(event -> setEffect(null));
    }


    public void setText(String format) {
        this.text.setText(format);
    }
}
