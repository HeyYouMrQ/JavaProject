package tempInDevelopment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Temp {
    public static void toUse()
    {
    /*    JFrame j1=new JFrame(string);
        j1.setCursor(new Cursor(Cursor.HAND_CURSOR));//其实像JFrame这样的AbstractButton的子类都可以
        mainFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR)); 可以用来人机、联机时使用
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
    private class MyWindowListener implements WindowListener, WindowStateListener, WindowFocusListener {
        //addWindowListener(new MyWindowListener()); 在需要使用的控件中加这个
        // Called back upon clicking close-window button
        @Override
        public void windowClosing(WindowEvent evt) {System.exit(0);}
        // Not Used, BUT need to provide an empty body to compile.
        @Override public void windowOpened(WindowEvent evt) { }
        @Override public void windowClosed(WindowEvent evt) { }
        // For Debugging
        @Override public void windowIconified(WindowEvent evt) {/*setExtendedState(JFrame.ICONIFIED);*/ }
        @Override public void windowDeiconified(WindowEvent evt) {/*setExtendedState(JFrame.MAXIMIZED_BOTH);*/}
        @Override public void windowActivated(WindowEvent evt) {}
        @Override public void windowDeactivated(WindowEvent evt) {}
        @Override
        public void windowGainedFocus(WindowEvent e) {}
        @Override
        public void windowLostFocus(WindowEvent e) {}
        @Override
        public void windowStateChanged(WindowEvent e) {}
    }
    public class CirclePanel extends JPanel {
        private int radius = 50; // Default circle radius
        private Color color = Color.BLACK;
        private final Random random = new Random();
        public CirclePanel(int width, int height) {
            enableEvents(AWTEvent.MOUSE_EVENT_MASK);//代表当前组件可以接受鼠标监听事件  非常好用！！！
            //重大发现：其实JFrame,JPanel,JComponent,JLabel等都有paintComponent()和processMouseEvent()重写方法的！不过需要继承再重写。
            setLayout(null);
            this.setBackground(Color.WHITE);
            this.setSize(width, (int) (height * 0.66));
            this.setLocation(0, 0);
        }
        public void enlarge() {radius = (int) (radius * 1.1);this.repaint();}
        public void shrink() {radius = (int) (radius * 0.9);this.repaint();}
        /**Repaint the circle
         * 如果父组件调用了repaint(),那么它的子组件也都会调用自己的repaint()
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(this.color);
            g.drawString(String.format("Radius: %d",this.radius),10,15);
            g.fillOval(this.getWidth() / 2 - radius, this.getHeight() / 2 - radius, 2 * radius, 2 * radius);
        }
        //当鼠标与当前Component触发事件时，会自动调用这个方法
        @Override
        protected void processMouseEvent(MouseEvent e) {
            super.processMouseEvent(e);
            if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                System.out.println(color);
                repaint();//调用当前类的paintComponent
            }
        }
    }
}
