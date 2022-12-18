package media;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
public class RoundButton extends JButton {
    public static void main(String[] args) {
        JFrame j=new JFrame();
        j.setSize(1000,800);
        j.setVisible(true);
        RoundButton r=new RoundButton();
        r.setForeground(Color.cyan);
        r.setBackground(Color.RED);
        r.setSize(500,300);
        j.add(r);
    }
    int width,height;
    public RoundButton(String label) {
        super(label);
        this.setLayout(null);
        this.setFocusable(true);//Sets the focusable state of this Component to the specified value. This value overrides the Component's default focusability.
//这个调用使JButton不画背景，而允许我们画一个圆的背景。
        setContentAreaFilled(false);
        repaint();
    }
    public RoundButton() {
        // 这些声明把按钮扩展为一个圆而不是一个椭圆。
        this.setLayout(null);
        this.setFocusable(true);//Sets the focusable state of this Component to the specified value. This value overrides the Component's default focusability.
        //这个调用使JButton不画背景，而允许我们画一个圆的背景。
        setContentAreaFilled(false);
        repaint();
    }
    @Override
    public void setSize(int width,int height)
    {
        super.setSize(width,height);
        this.width=width;
        this.height=height;
    }
    @Override
    // 画圆的背景和标签
    protected void paintComponent(Graphics g) {

        if (getModel().isArmed())
            // 你可以选一个高亮的颜色作为圆形按钮类的属性
            g.setColor(Color.YELLOW);
        else if(getModel().isRollover())
            g.setColor(Color.green);
        else if(!getModel().isArmed())
            g.setColor(Color.MAGENTA);

        //g.fillOval(0, 0, width-1, height-1);
        g.fillRoundRect(0,0,width-1,height-1,width*2/3,height*2/3);
        //这个调用会画一个标签和焦点矩形。
        super.paintComponent(g);
    }
    @Override
    // 用简单的弧画按钮的边界。
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());//边界为前景色
        //g.drawOval(0, 0, width-1, height-1);
        g.drawRoundRect(0,0,width-1,height-1,width*2/3,height*2/3);
    }
    // 侦测点击事件
    Shape shape;
    @Override
    public boolean contains(int x, int y)
    {
        // 如果按钮改变大小，产生一个新的形状对象。
        if (shape == null || !shape.getBounds().equals(getBounds()))
            shape = new Ellipse2D.Float(0, 0, width-1, height-1);
        return shape.contains(x, y);
    }
}