package com.rominntrenger.objects;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.graphics.Sprite;
import com.bluebook.renderer.RenderLayer.RenderLayerName;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.messageHandling.MessageHandler;
import com.rominntrenger.objects.enemy.Enemy;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * {@link WaveManager} will controll the waves, and also keep track of what enemies to spawn where
 */
public class WaveManager extends GameObject {

    public static ArrayList<AlienHive> hives = new ArrayList<>();

    private static WaveManager singelton;

    private int width = 375;
    private int height = 100;
    private int boxRadius = 90;

    private int waveNumber = 10;

    private long waveStart = 0;

    private long pauseStart = 0;
    private double pauseTime = 1.5;

    private boolean hivesDownMessage = false;


    private WaveSate state = WaveSate.PAUSE;

    public enum WaveSate{
        WAVE, BOSS, PAUSE;
    }

    LinearGradient pauseGradient;
    LinearGradient waveGradient;
    LinearGradient bossGradient;

    public static WaveManager getInstance(){
        if(singelton == null)
            singelton = new WaveManager();
        return singelton;
    }

    private WaveManager() {
        super(Vec2.ZERO, Vec2.ZERO, null);
        allwaysOnScreen = true;
        setRenderLayer(RenderLayerName.GUI);

        // Setup
        pauseGradient =  new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.GREEN),
            new Stop(1.0, Color.TRANSPARENT));
        waveGradient =  new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.RED),
            new Stop(1.0, Color.TRANSPARENT));
        bossGradient =  new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.PURPLE),
            new Stop(1.0, Color.TRANSPARENT));

        pauseStart = System.currentTimeMillis();
    }

    LinearGradient getGradient(){
        switch(state){
            case PAUSE:
                return pauseGradient;
            case WAVE:
                return waveGradient;
            case BOSS:
                return bossGradient;
            default:
                return null;
        }
    }

    @Override
    public void update(double delta) {
        if(state == WaveSate.PAUSE) {
            double timeLeft = pauseTime - (System.currentTimeMillis() - pauseStart) / 1000.0;
            if (timeLeft <= 0.0) {
                startWave();
            }
        }else if(state == WaveSate.WAVE){

            if(getAliveHives() == 0){
                if(!hivesDownMessage){
                    if(Enemy.allEnemies.size() > hives.size()){
                        MessageHandler.getInstance()
                            .writeMessage("All the hives are down!\n Now just kill the smoll bois!",
                                new Sprite("portraits/mc_grumpy"));
                    }
                    hivesDownMessage = true;
                }

                if(Enemy.allEnemies.size() == hives.size())
                    endWave(true);
            }
            for(AlienHive ah : hives){
                ah.spawn(waveNumber);
            }
        }
    }

    void endWave(boolean showMessage){
        int alive = hives.size();
        for(AlienHive ah : hives){
            if(!ah.isActive())
                alive--;
        }

        pauseStart = System.currentTimeMillis();
        state = WaveSate.PAUSE;

        if(showMessage) {
            MessageHandler.getInstance().writeMessage("Dem boiz be back real soon \n\n Get ready!",
                new Sprite("portraits/mc_happy"));
            AudioPlayer ap = new AudioPlayer("audio/BoisBeBac.wav");
            ap.playOnce();
        }
    }

    void startWave(){
        state = WaveSate.WAVE;
        waveStart = System.currentTimeMillis();
        waveNumber++;

        hivesDownMessage = false;

        for(AlienHive ah : hives){
            ah.reset();
        }
    }

    int getAliveHives(){
        int alive = hives.size();
        for(AlienHive ah : hives){
            if(!ah.isActive())
                alive--;
        }
        return  alive;
    }

    public void clearGamestate(){
        hivesDownMessage = true;
        endWave(false);
        for(Enemy e : Enemy.allEnemies){
            if(!(e instanceof AlienHive)){
                e.destroy();
            }
        }
        Enemy.allEnemies.clear();


    }

    @Override
    public void draw(GraphicsContext gc) {

        gc.setFill(getGradient());
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);
        gc.setLineDashes(0);

        int x = 1920 / 2 - width / 2;
        int xEnd = 1920 / 2 + width / 2;

        gc.beginPath();
        gc.bezierCurveTo(0, 0, x - 100, - height / 2, x, height - boxRadius);
        gc.bezierCurveTo(x, height - boxRadius, x, height, x + boxRadius, height);
        gc.lineTo(xEnd - boxRadius, height);
        gc.bezierCurveTo(xEnd - boxRadius, height, xEnd, height, xEnd, height - boxRadius);
        gc.bezierCurveTo(xEnd, height - boxRadius, xEnd + 100, - height / 2, 1920, 0);
        gc.closePath();
        gc.stroke();
        gc.fill();

        // Text
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(height / 2));
        gc.setTextAlign(TextAlignment.CENTER);

        if(state == WaveSate.WAVE)
        gc.fillText("Wave " + waveNumber, 1920 / 2, height / 2);
        else if(state == WaveSate.PAUSE)
            gc.fillText("Next wave in:", 1920 / 2, height / 2);


        double timeLeft = pauseTime - (System.currentTimeMillis() - pauseStart) / 1000.0;
        if(state == WaveSate.WAVE){
            timeLeft = (System.currentTimeMillis() - waveStart) / 1000.0;
        }
        gc.setFont(new Font(height / 3));
        DecimalFormat df = new DecimalFormat("0.0");
        gc.fillText(df.format(timeLeft), 1920 / 2, height - height /  5);

    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public void setWaveNumber(int waveNumber) {
        this.waveNumber = waveNumber;
    }

    public WaveSate getState() {
        return state;
    }

    public void setState(WaveSate state) {
        this.state = state;
    }
}
