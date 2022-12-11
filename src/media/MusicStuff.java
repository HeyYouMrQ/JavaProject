package media;
import javax.sound.sampled.*;
import java.io.File;
public class MusicStuff
{
    public void playMusic(String musicLocation)
    {
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