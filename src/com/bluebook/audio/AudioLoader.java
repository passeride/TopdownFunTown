package com.bluebook.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * class 'AudioLoader' is generated to handle loading of audio-files from the assets folder.
 */
public class AudioLoader {

    private static Clip clip; //clip lagres i minnet for hurtigere aksessering

    private static HashMap<String, Clip> audioClips = new HashMap<>();


    /**
     * loads audio-file through the path specified to the function by the use of a clip.
     * @param path
     * @return clip
     */
    public static Clip loadAudioClip(String path) {
        boolean success = false;
        if(!audioClips.containsKey(path)) {
            try {

                //Debug
                File f = new File(path);
                // normal
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURL());
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);

                audioClips.put(path, clip);
                success = true;

            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
        return audioClips.get(path);
    }


}
