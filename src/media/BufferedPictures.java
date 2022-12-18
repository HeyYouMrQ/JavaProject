package media;
import javax.swing.*;
import java.awt.*;

import static view.Handler.*;
import static media.MyDialog.*;
public class BufferedPictures {
    private static final int WIDTH= mainFrame.WIDTH,HEIGHT= mainFrame.HEIGHT;
    public static Image MENU = Toolkit.getDefaultToolkit().getImage("./resource/menu.jpg").getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
    public static Image GameBG = Toolkit.getDefaultToolkit().getImage("./resource/gamebg.jpg").getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
    public static Image DialogPic = Toolkit.getDefaultToolkit().getImage("./resource/msgbox.jpg").getScaledInstance(width, height, Image.SCALE_SMOOTH);
    public static ImageIcon DialogIcon=new ImageIcon(DialogPic);
}