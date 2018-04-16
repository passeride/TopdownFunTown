package com.bluebook.audio;

import com.bluebook.util.GameObject;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * class 'AudioPlayer' is generated to play audio-files loaded by the class 'AudioLoader'
 */
public class AudioPlayer {

    private boolean spital = false;
    private Clip clip;

    private GameObject source;

    private FloatControl gainControl;

    /**
     * Constructor for AudioPlayer which sets and loads audio-path and gains control of the audio through FloatControls gainControl
     * @param path path
     */
    public AudioPlayer(String path){
        clip = AudioLoader.loadAudioClip(path);
        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    }

    /**
     * plays the selected audio once
     */
    public void playOnce(){
        if(spital){
            if(clip.isControlSupported(FloatControl.Type.PAN)) {
                FloatControl balance = (FloatControl) clip.getControl(FloatControl.Type.PAN);
                float pan = (float)(Math.max(0, Math.min( 1, source.getProcentageXPosition() * 1.5 - .75)));
                balance.setValue(pan);
            }else if(clip.isControlSupported(FloatControl.Type.BALANCE)){
                FloatControl balance = (FloatControl) clip.getControl(FloatControl.Type.BALANCE);
                float pan = (float)(Math.max(0, Math.min( 1, source.getProcentageXPosition() * 1.5 - .75)));
                balance.setValue(pan);
            }else{
                System.out.println("NO PAN OR BALANCE SUPPORTED channels: " + clip.getFormat().getChannels());
            }
        }
        if(clip == null){
            return;
        }
        stop();
        clip.setFramePosition(0);
        while (!clip.isRunning()){
            clip.start();
        }
    }

    /**
     * stops currently playing audio if clip.isRunning() returns true
     */
    private void stop(){
        if(clip.isRunning()){
            clip.stop();
        }
    }

    /**
     * stops, drains then closes the selected audio-clip.
     */
    public void close(){
        stop();
        clip.drain();
        clip.close();
    }

    /**
     * sets LOOP_CONTINOUSLY to true and calls clip.PlayOce()
     */
    public void playLoop(){
        clip.loop(clip.LOOP_CONTINUOUSLY);
        playOnce();
    }

    /**
     * Sets the volum of selected audio-clip through decibel. e.g. -30 to lower the amount
     * @param decibel decibel
     */
    public void setVolume(float decibel){
        gainControl.setValue(decibel);
    }

    /**
     * Checks if the audio-clip is running
     * @return true OR false
     */
    public boolean isRunning(){
        return clip.isRunning();
    }

    /**
     * Checks if spital is true OR false
     * @return true OR false
     */
    public boolean isSpital() {
        return spital;
    }

    /**
     * Sets the boolean value of spital
     * @param source GameObject source
     */
    public void setSpital(GameObject source) {
        this.spital = true;
        this.source = source;
    }
}
