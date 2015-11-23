import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/*

AUDIO SUPPORT CLASS

 */
public enum Sounds {
    DISABLED("gui/sounds/disabled.wav"), // Added
    INCOMING("gui/sounds/incoming.wav"), // Added
    SEND("gui/sounds/send.wav"),         // Added
    RECEIVE("gui/sounds/receive.wav"),   // Added
    ERROR("gui/sounds/error.wav");       // Added

    private Clip clip;

    Sounds(String fileName) {
        try {
            File soundFile = new File(fileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (Exception e) {
        }
    }

    public void play() {
        if (clip.isRunning())
            clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    static void init() {
        values();
    }
}
