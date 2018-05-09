package com.bluebook.audio;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * class 'AudioLoader' is generated to handle loading of audio-files from the assets folder.
 */
public class AudioLoader {

    /**
     * audioClips hashmap is used to query when the same clips is requested multiple times
     * To save some read from disk
     */
    private static HashMap<String, Clip> audioClips = new HashMap<>();

    /**
     * loads audio-file through the spritePath specified to the function by the use of a clip.
     *
     * @return clip
     */
    public static Clip loadAudioClip(String path) {
        if (!audioClips.containsKey(path)) {
            try {

//                File f = new File(path);
                //read audio data from whatever source (file/classloader/etc.)
                InputStream audioSrc = AudioLoader.class.getClassLoader().getResourceAsStream(path);
//add buffer for mark/reset support
                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);

                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);

                audioClips.put(path, clip);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
        return audioClips.get(path);
    }

}
