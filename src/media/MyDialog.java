package media;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static media.BufferedPictures.*;

public class MyDialog
{
    public static int width = 300, height = 200;
    public static int confirmDialog(String title, String content, String button1, String button2)
    {
        CustomDialog dia= new CustomDialog(title,content,button1,button2);
        return dia.getValue();
    }
    public static void showDialog(String title, String content)
    {
        CustomDialog dia= new CustomDialog(title,content);
    }
}
class CustomDialog extends JDialog implements ActionListener
{
    String title;
    String content;
    String ok = "确定";
    String cancel = "取消";
    public static int width = 300, height = 200;
    int returnValue=0;
    public int getValue()
    {
        return returnValue;
    }
    public CustomDialog(String title, String content,String button1,String button2)
    {
        this.title = title;
        this.content = content;
        this.ok=button1;
        this.cancel=button2;

        setSize(width,height);
        JLayeredPane layeredPane=new JLayeredPane();//新建一个分层器
        layeredPane.setLayout(null);    layeredPane.setLocation(0,0);   layeredPane.setSize(width,height);
        JPanel jp1=new JPanel(),jpb=new JPanel();//建立背景层
        jp1.setLayout(null);    jp1.setLocation(0,0);   jp1.setSize(width,height);
        jpb.setLayout(null);    jpb.setLocation(0,0);   jpb.setSize(width,height);

        // 1个图片标签,显示图片
        JLabel jlImg = new JLabel(DialogIcon);
        jlImg.setSize(width, height);
        jlImg.setLocation(0,0);
        jpb.add(jlImg);

        // 1个文字标签,显示文本
        JLabel jLabel = new JLabel(content);
        jLabel.setFont(new Font("楷体", Font.PLAIN, 20));
        // 设置文字的颜色为蓝色
        jLabel.setForeground(Color.black);
        jLabel.setLocation(width/10,height/5);
        jLabel.setSize(width/2,height/5);
        jp1.add(jLabel);
        JButton okBut = new JButton(ok);
        JButton cancelBut = new JButton(cancel);
        okBut.setBackground(Color.LIGHT_GRAY);
        okBut.setBorderPainted(false);
        okBut.setLocation(width/10,height*2/5);
        okBut.setSize(width/4,height/5);
        cancelBut.setLocation(width*6/10,height*2/5);
        cancelBut.setSize(width/4,height/5);
        cancelBut.setBackground(Color.LIGHT_GRAY);
        cancelBut.setBorderPainted(false);
        // 给按钮添加响应事件
        okBut.addActionListener(this);
        cancelBut.addActionListener(this);
        jp1.add(okBut);
        jp1.add(cancelBut);
        jp1.setOpaque(false);
        // 向对话框中加入各组件

        layeredPane.add(jpb,JLayeredPane.FRAME_CONTENT_LAYER);
        layeredPane.add(jp1,JLayeredPane.MODAL_LAYER);

        //setUndecorated(true);
        setLayout(null);
        setIconImage(DialogPic);// 窗口左上角的小图标
        setTitle(title);// 设置标题
        setModal(true);// 设置为模态窗口,此时不能操作父窗口
        setSize(width, height);// 设置对话框大小
        setLocationRelativeTo(null);// 对话框局域屏幕中央
        setResizable(false);// 对话框不可缩放
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);// 点击对话框关闭按钮时,销毁对话框
        this.add(layeredPane);
        this.setVisible(true);
    }
    public CustomDialog(String title, String content)
    {
        this.title = title;
        this.content = content;
        int width = 300, height = 200;
        // 创建1个图标实例,注意image目录要与src同级
        // 1个图片标签,显示图片
        JLabel jlImg = new JLabel(DialogIcon);
        jlImg.setSize(width, height);
        jlImg.setLocation(0,0);
        // 1个文字标签,显示文本
        JLabel jLabel = new JLabel(content);
        jLabel.setFont(new Font("楷体", Font.PLAIN, 20));
        // 设置文字的颜色为蓝色
        jLabel.setForeground(Color.black);
        jLabel.setLocation(width/10,height/5);
        jLabel.setSize(width/2,height/5);

        add(jLabel);
        add(jlImg);

        //setUndecorated(true);
        setLayout(null);
        setIconImage(DialogPic);// 窗口左上角的小图标
        setTitle(title);// 设置标题
        setModal(true);// 设置为模态窗口,此时不能操作父窗口
        setSize(width, height);// 设置对话框大小
        setLocationRelativeTo(null);// 对话框局域屏幕中央
        setResizable(false);// 对话框不可缩放
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);// 点击对话框关闭按钮时,销毁对话框
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // 判断是不是确定按钮被点击
        if (ok.equals(e.getActionCommand())) {
            this.setVisible(false);
            this.dispose();
            returnValue=1;
        }
        if (cancel.equals(e.getActionCommand())) {
            this.setVisible(false);
            this.dispose();
            returnValue=2;
        }
    }
}