package media;

import view.ChessGameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import static media.BufferedPictures.DialogIcon;
import static media.BufferedPictures.DialogPic;

public class MyDialog
{
    public static int width = 300, height = 200;
    public static int confirmDialog(String title, String content, String button1, String button2)
    {
        CustomDialog dia= new CustomDialog(title,content,button1,button2,0);
        return dia.getValue();
    }
    public static void showDialog(String title, String content)
    {
        CustomDialog dia= new CustomDialog(title,content,1);
    }
    public static String textDialog(String title, String content)
    {
        CustomDialog dia= new CustomDialog(title,content,2);
        return dia.getString();
    }
    public static String userDialog()
    {
        UserCustomDialog dia= new UserCustomDialog();
        return dia.getString();
    }
}
class CustomDialog extends JDialog implements ActionListener
{
    String title;
    String content;
    String ok = "确定";
    String cancel = "取消";
    int mode;
    public static int width = 300, height = 200;
    int returnValue=0;
    String returnString="";
    public int getValue()
    {
        return returnValue;
    }
    public String getString()
    {
        return returnString;
    }
    public CustomDialog(String title, String content,String button1,String button2,int mode)//mode=0
    {
        this.title = title;
        this.content = content;
        this.ok=button1;
        this.cancel=button2;
        this.mode=mode;

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
    private JTextField text;
    public CustomDialog(String title, String content,int mode)//1只有一个确认按钮，2再加一个文本框
    {
        this.title = title;
        this.content = content;
        this.mode=mode;
        setSize(width,height);
        JLayeredPane layeredPane=new JLayeredPane();//新建一个分层器
        layeredPane.setLayout(null);    layeredPane.setLocation(0,0);   layeredPane.setSize(width,height);
        JPanel jp1=new JPanel(),jpb=new JPanel();//建立背景层
        jp1.setLayout(null);    jp1.setLocation(0,0);   jp1.setSize(width,height);
        jpb.setLayout(null);    jpb.setLocation(0,0);   jpb.setSize(width,height);

        JLabel jlImg = new JLabel(DialogIcon);
        jlImg.setSize(width, height);
        jlImg.setLocation(0,0);
        jpb.add(jlImg);

        JLabel jLabel = new JLabel(content);
        jLabel.setFont(new Font("楷体", Font.PLAIN, 20));
        // 设置文字的颜色为蓝色
        jLabel.setForeground(Color.black);
        jLabel.setLocation(width/10,height/5);
        jLabel.setSize(width/2,height/5);
        jp1.add(jLabel);

        if(mode==2)
        {
            jLabel.setLocation(width/10,height/15);
            text=new JTextField(50);
            text.setFont(new Font("宋体", Font.PLAIN, 16));
            text.setForeground(Color.black);
            text.setLocation(width/10,height/4);
            text.setSize(width*3/4,height/10);
            text.setOpaque(false);
            jp1.add(text);
        }

        JButton okBut = new JButton(ok);
        okBut.setBackground(Color.LIGHT_GRAY);
        okBut.setBorderPainted(false);
        okBut.setLocation(width/3,height/2);
        okBut.setSize(width/4,height/5);
        okBut.addActionListener(this);

        jp1.add(okBut);
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
    @Override
    public void actionPerformed(ActionEvent e) {
        // 判断是不是确定按钮被点击
        if (ok.equals(e.getActionCommand())) {
            if(mode==0)
                returnValue=1;
            else if(mode==2)
                returnString=text.getText();
            this.setVisible(false);
            this.dispose();
        }
        if (cancel.equals(e.getActionCommand())) {
            this.setVisible(false);
            this.dispose();
            returnValue=2;
        }
    }
    @Override
    public void processWindowEvent(WindowEvent e)
    {
        if(e.getID() == WindowEvent.WINDOW_CLOSING)
        {
           //super.processWindowEvent(e);注释掉这个，就相当于禁止直接关闭了
        }
    }
}
class UserCustomDialog extends JDialog implements ActionListener
{
    String title="用户小精灵";
    String regi = "注册";
    String login = "登陆";
    String cancel = "取消";
    public static int width = 300, height = 200;
    String returnString=null;
    public String getString()
    {
        return returnString;
    }
    private JTextField user,psw;
    public UserCustomDialog()
    {
        setSize(width,height);
        JLayeredPane layeredPane=new JLayeredPane();//新建一个分层器
        layeredPane.setLayout(null);    layeredPane.setLocation(0,0);   layeredPane.setSize(width,height);
        JPanel jp1=new JPanel(),jpb=new JPanel();//建立背景层
        jp1.setLayout(null);    jp1.setLocation(0,0);   jp1.setSize(width,height);
        jpb.setLayout(null);    jpb.setLocation(0,0);   jpb.setSize(width,height);

        JLabel jlImg = new JLabel(DialogIcon);
        jlImg.setSize(width, height);
        jlImg.setLocation(0,0);
        jpb.add(jlImg);

        JLabel userLabel = new JLabel("用户");
        userLabel.setFont(new Font("楷体", Font.PLAIN, 16));
        // 设置文字的颜色为蓝色
        userLabel.setForeground(Color.black);
        userLabel.setLocation(width/40,height/10);
        userLabel.setSize(width/8,height/10);
        jp1.add(userLabel);

        JLabel pswLabel = new JLabel("密码");
        pswLabel.setFont(new Font("楷体", Font.PLAIN, 16));
        // 设置文字的颜色为蓝色
        pswLabel.setForeground(Color.black);
        pswLabel.setLocation(width/40,height/4);
        pswLabel.setSize(width/8,height/10);
        jp1.add(pswLabel);

        user=new JTextField(20);
        user.setFont(new Font("宋体", Font.PLAIN, 16));
        user.setForeground(Color.black);
        user.setLocation(width/7,height/10);
        user.setSize(width*3/4,height/10);
        user.setOpaque(false);
        jp1.add(user);

        psw=new JTextField(20);
        psw.setFont(new Font("宋体", Font.PLAIN, 16));
        psw.setForeground(Color.black);
        psw.setLocation(width/7,height/4);
        psw.setSize(width*3/4,height/10);
        psw.setOpaque(false);
        jp1.add(psw);

        JButton regiBut = new JButton(regi);
        regiBut.setBackground(Color.LIGHT_GRAY);
        regiBut.setBorderPainted(false);
        regiBut.setLocation(width/10,height/2);
        regiBut.setSize(width/5,height/5);
        regiBut.addActionListener(this);

        JButton loginBut = new JButton(login);
        loginBut.setBackground(Color.LIGHT_GRAY);
        loginBut.setBorderPainted(false);
        loginBut.setLocation(width*2/5,height/2);
        loginBut.setSize(width/5,height/5);
        loginBut.addActionListener(this);

        JButton cancelBut = new JButton(cancel);
        cancelBut.setBackground(Color.LIGHT_GRAY);
        cancelBut.setBorderPainted(false);
        cancelBut.setLocation(width*7/10,height/2);
        cancelBut.setSize(width/5,height/5);
        cancelBut.addActionListener(this);

        jp1.add(regiBut);
        jp1.add(loginBut);
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
    @Override
    public void actionPerformed(ActionEvent e) {//todo
        // 判断是不是确定按钮被点击
        if (regi.equals(e.getActionCommand())) {
        //    ChessGameFrame.user=user.getText();
        //    ChessGameFrame.client.psw=psw.getText();
        //    ChessGameFrame.client.send(3);
        //    while (ChessGameFrame.client.regOK==0){}
        //    if(ChessGameFrame.client.regOK==1)
            {
                MyDialog.showDialog("账号小精灵","注册成功！");
        //        ChessGameFrame.client.regOK=0;
            }
        /*    else
            {
                MyDialog.showDialog("账号小精灵","注册失败！");
                ChessGameFrame.client.regOK=0;
            }*/
        }
        if (login.equals(e.getActionCommand())) {

            ChessGameFrame.user=user.getText();
            ChessGameFrame.client.psw=psw.getText();
        //    ChessGameFrame.client.send(4);

        //    while (ChessGameFrame.client.logOK==0){}
        //    if(ChessGameFrame.client.logOK==1)
            {
                returnString=user.getText();
                MyDialog.showDialog("账号小精灵","登陆成功！");
                this.setVisible(false);
                this.dispose();
            //    ChessGameFrame.client.regOK=0;
            }
        /*    else
            {
                MyDialog.showDialog("账号小精灵","登陆失败！");
                ChessGameFrame.client.regOK=0;
            }*/
        }
        if (cancel.equals(e.getActionCommand())) {
            returnString=null;
            this.setVisible(false);
            this.dispose();
        }
    }
    @Override
    public void processWindowEvent(WindowEvent e)
    {
        if(e.getID() == WindowEvent.WINDOW_CLOSING)
        {
            //super.processWindowEvent(e);注释掉这个，就相当于禁止直接关闭了
        }
    }
}