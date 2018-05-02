package com.bluebook.audio;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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

                File f = new File(path);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
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
