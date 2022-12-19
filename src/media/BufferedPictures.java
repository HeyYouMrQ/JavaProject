package media;
import javax.swing.*;
import java.awt.*;

import static view.Handler.*;
import static media.MyDialog.*;
public class BufferedPictures {
    private static final int WIDTH= mainFrame.WIDTH,HEIGHT= mainFrame.HEIGHT;
    public static Image MENU = Toolkit.getDefaultToolkit().getImage("./resource/menu.jpg").getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
    public static Image GameBG = Toolkit.getDefaultToolkit().getImage("./resource/gamebg1.jpg").getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
    public static Image DialogPic = Toolkit.getDefaultToolkit().getImage("./resource/msgbox.jpg").getScaledInstance(width, height, Image.SCALE_SMOOTH);
    public static ImageIcon DialogIcon=new ImageIcon(DialogPic);

    public static Image CheatOff =Toolkit.getDefaultToolkit().getImage("./resource/cheatoff.png").getScaledInstance(WIDTH/6,HEIGHT/12,Image.SCALE_SMOOTH);
    public static ImageIcon CheatOffIcon=new ImageIcon(CheatOff);
    public static Image CheatOn =Toolkit.getDefaultToolkit().getImage("./resource/cheaton.png").getScaledInstance(WIDTH/6,HEIGHT/12,Image.SCALE_SMOOTH);
    public static ImageIcon CheatOnIcon=new ImageIcon(CheatOn);

    public static Image WithdrawOff =Toolkit.getDefaultToolkit().getImage("./resource/withdrawoff.png").getScaledInstance(WIDTH/6,HEIGHT/12,Image.SCALE_SMOOTH);
    public static ImageIcon WithdrawOffIcon=new ImageIcon(WithdrawOff);
    public static Image WithdrawOn =Toolkit.getDefaultToolkit().getImage("./resource/withdrawon.png").getScaledInstance(WIDTH/6,HEIGHT/12,Image.SCALE_SMOOTH);
    public static ImageIcon WithdrawOnIcon=new ImageIcon(WithdrawOn);

    public static Image PcOff =Toolkit.getDefaultToolkit().getImage("./resource/pcoff.png").getScaledInstance(WIDTH/12,HEIGHT/12,Image.SCALE_SMOOTH);
    public static ImageIcon PcOffIcon=new ImageIcon(PcOff);
    public static Image PcOn =Toolkit.getDefaultToolkit().getImage("./resource/pcon.png").getScaledInstance(WIDTH/12,HEIGHT/12,Image.SCALE_SMOOTH);
    public static ImageIcon PcOnIcon=new ImageIcon(PcOn);

    public static Image Easy =Toolkit.getDefaultToolkit().getImage("./resource/easy.png").getScaledInstance(WIDTH/12,HEIGHT/12,Image.SCALE_SMOOTH);
    public static ImageIcon EasyIcon=new ImageIcon(Easy);
    public static Image Difficult =Toolkit.getDefaultToolkit().getImage("./resource/difficult.png").getScaledInstance(WIDTH/12,HEIGHT/12,Image.SCALE_SMOOTH);
    public static ImageIcon DifficultIcon=new ImageIcon(Difficult);
}