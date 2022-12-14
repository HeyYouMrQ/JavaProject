package media;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
public class MusicStuff
{
    public static String EXPLODE="explode.wav", GONG="gong.wav", SHOOT="shoot.wav";//todo
    public static enum Volume {MUTE,ON}
    public static Volume volume = Volume.ON;
    public void playMusic(String musicLocation)
    {
        if(volume!=Volume.MUTE)
        try
        {
            File musicPath = new File(musicLocation);
            if (musicPath.exists())
            {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
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