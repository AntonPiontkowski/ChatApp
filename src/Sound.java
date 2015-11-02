import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public enum Sound {
    INCOMING ("sounds/incoming.wav"),
    OUTGOING ("sounds/outgoing.wav"),
    CLICK ("sounds/click.wav");

    private Clip clip;

    Sound(String fileName){
        try{
            File soundFile = new File(fileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (Exception e){}
    }
    public void play() {
        if (clip.isRunning())
            clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
    static void init(){
        values();
    }
}
