package com.bluebook.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * class 'AudioLoader' is generated to handle loading of audio-files from the assets folder.
 */
public class AudioLoader {

    private static Clip clip; //clip lagres i minnet for hurtigere aksessering
    private static FloatControl gainControl;

    /**
     * loads audio-file through the path specified to the function by the use of a clip.
     * @param path
     * @return clip
     */
    public static Clip loadAudioClip(String path) {
        try {

            //Debug
            File f = new File(path);
            System.out.println("EXISTS: " + f.exists() + " PATH: " + f.getAbsolutePath());

            // normal
            AudioInputStream audioIn = AudioSystem.getAudioInputStream( f.toURL() );
            clip = AudioSystem.getClip();
            clip.open( audioIn );

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e){
            e.printStackTrace();
        }
        return clip;
    }

    public static FloatControl getGainControl(){
        return gainControl;
    }
}
