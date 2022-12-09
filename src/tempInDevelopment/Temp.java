package tempInDevelopment;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Temp {
    public static void toUse()
    {
    /*    JFrame j1=new JFrame(string);
        j1.setCursor(new Cursor(Cursor.HAND_CURSOR));//其实像JFrame这样的AbstractButton的子类都可以
        j1.setIconImage();
        JButton j2=new JButton(icon或string或都有都可以);
        j2.setIcon(……);
        j2.setPressedIcon(……);  …………等很多方法
        JLabel j3=new JLabel(icon或string或都有都可以);
        j3.setIcon();

        Image i=new BufferedImage(……);
        Image image = Toolkit.getDefaultToolkit().getImage("picture.jpg").getScaledInstance(100, 100, Image.SCALE_FAST);
        ImageIcon s=new ImageIcon(Image image);
        s.paintIcon(Component c,Graphics g,int x,int y);
        s.setImage(Image image);
        JLabel label = new JLabel(new ImageIcon("a.png"));
        */
    }
    public static class MouseHandler implements MouseListener, MouseMotionListener {
        // MouseListener event 响应
        // handle event when mouse released immediately after press
        public void mouseClicked(MouseEvent event) {}
        // handle event when mouse pressed
        public void mousePressed(MouseEvent event) {}
        // handle event when mouse released
        public void mouseReleased(MouseEvent event) {}
        // handle event when mouse enters area
        public void mouseEntered(MouseEvent event) {

        }
        // handle event when mouse exits area
        public void mouseExited(MouseEvent event) {

        }
        // MouseMotionListener event 响应
        // handle event when user drags mouse with button pressed
        public void mouseDragged(MouseEvent event) {}
        // handle event when user moves mouse
        public void mouseMoved(MouseEvent event) {}
    }
}
