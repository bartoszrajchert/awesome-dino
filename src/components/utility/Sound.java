package components.utility;

import javax.sound.sampled.*;
import java.net.URL;

/**
 * Resources
 *  - https://stackoverflow.com/questions/2416935/how-to-play-wav-files-with-java
 */

public class Sound {
    Clip clip;
    AudioInputStream ais;
    URL path;

    public Sound(String path) {
        this.path = getClass().getResource(path);
    }

    public void play() {
        try {
            ais = AudioSystem.getAudioInputStream(path);
            clip = AudioSystem.getClip();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playInLoop() {
        try {
            ais = AudioSystem.getAudioInputStream(path);
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
