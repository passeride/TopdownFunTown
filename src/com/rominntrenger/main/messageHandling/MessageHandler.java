package com.rominntrenger.main.messageHandling;

import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer;
import com.bluebook.util.GameObject;
import com.bluebook.util.GameSettings;
import com.bluebook.util.Vector2;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MessageHandler extends GameObject{

    private boolean isActive = true;
    private Sprite sprite;
    private String message;
    private String printMessage;
    private long start;
    private long duration = 5;
    private Font font;
    private int resolutionX = GameSettings.getInt("game_resolution_X");
    private int resolutionY = GameSettings.getInt("game_resolution_Y");

    private MessageHandler(){
        super(Vector2.ZERO, Vector2.ZERO, new Sprite(""));
        this.setRenderLayer(RenderLayer.RenderLayerName.GUI);
        File file = new File("assets/fonts/Pixel-Miners.otf");
        try {
            font = Font.loadFont(new FileInputStream(file), 40);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static MessageHandler instance;

    public static MessageHandler getInstance(){
        if (instance == null){
            instance = new MessageHandler();
        }
        return instance;
    }

    @Override
    public void update(double delta) {
        super.update(delta);
        if(System.currentTimeMillis() > start + duration * 1000){
            isActive = false;
            sprite = null;
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
        if(isActive){

            //fills the box
            gc.setFill(Color.rgb(13,13,13,0.8));
            gc.fillRect(0, 800, resolutionX, resolutionY);

            //border for box
            gc.setStroke(Color.rgb(217, 217, 217));
            gc.setLineWidth(5);
            gc.strokeRect(10, 810, resolutionX, (resolutionY/4)-5);

            //sets font smoothing and font type
            gc.setFill(Color.WHITE);
            gc.setFontSmoothingType(FontSmoothingType.LCD);
            gc.setFont(font);


            if(sprite != null){
                //TODO: Draw sprite
                sprite.drawGUI(gc, new Vector2(15,815));
                //displays text
                gc.fillText(printMessage,45 + GameSettings.getScreenScale().getX(),870);

            }else{
                //displays text
                gc.fillText(printMessage,35,870);

            }


        }

    }

    public void writeMessage(String s, Sprite sprite){
        this.sprite = sprite;
        sprite.setOrigin(transform);
        writeMessage(s);
    }

    /**This function writes a message to the screen inside box from drawMessageBox function
     *@param s - string that the text will be.
     */
    public void writeMessage(String s){
        isActive = true;
        start  = System.currentTimeMillis();
        message = s;

        //attempt at typing effect for text
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(1500));
            }
            @Override
            protected void interpolate(double frac) {
                final int lenght = message.length();
                final int n = Math.round(lenght * (float) frac);
                printMessage = message.substring(0, n);
            }
        };
        animation.play();
    }

    /**sets the duration of the display box.
     * @param duration is the given duration for the display box
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

}
