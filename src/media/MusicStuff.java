package media;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.URL;

public class MusicStuff
{
    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }
    public static Volume volume = Volume.LOW;
    public void playMusic(String musicLocation)
    {
        try
        {
            File musicPath = new File(musicLocation);
            if (musicPath.exists())
            {
                URL url = this.getClass().getClassLoader().getResource(musicLocation);
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(url);
                //AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                //if (clip.isRunning()) clip.stop();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
/*
    String filepath ="海德薇变奏曲.wav";
    MusicStuff musicObject = new MusicStuff();
    musicObject.playMusic(filepath);
//播放方式
*/