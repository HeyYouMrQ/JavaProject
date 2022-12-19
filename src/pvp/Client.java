package pvp;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import static view.ChessGameFrame.*;
import static view.Handler.mainFrame;

public class Client extends JFrame
{
    public int regOK=0,logOK=0;//0无1成功2失败
    public String psw=null;
    public JList user_list=new JList();
    PrintWriter out;// 声明输出流对象
    public Client(String ip)
    {
        createClientSocket(ip);
    }
    public void link()
    {
        oppoUser = (String) user_list.getSelectedValue();
        send(6,user);
        pvpPlayerListPanel.setVisible(false);
        pvpPlayerListPanel.setEnabled(false);
        gamePanel.setEnabled(true);
        gamePanel.setVisible(true);
        mainFrame.setContentPane(gamePanel);
    }
    public boolean contendForFirst()
    {
        //未开发
        return false;
    }
    public void transIniCsb()
    {
        //未开发
    }
    public void work(int ope1,int firX1,int firY1,int secX1,int secY1)
    {
        send(2,String.format("%d%d%d%d%d",ope1,firX1,firY1,secX1,secY1));
    }
    private void handleIniCsb(String csb)
    {
        //未开发
    }
    private void handleStep(String step)
    {
        int ope1=Integer.parseInt(step.substring(0)),firX1=Integer.parseInt(step.substring(1)),
                firY1=Integer.parseInt(step.substring(2)),secX1=Integer.parseInt(step.substring(3)),
                secY1=Integer.parseInt(step.substring(4));
        if(ope1==1)
            chessboard.clickController.onClick(chessboard.getChessComponents()[firX1][firY1]);
        else if(ope1==2)
        {
            chessboard.clickController.onClick(chessboard.getChessComponents()[firX1][firY1]);
            chessboard.clickController.onClick(chessboard.getChessComponents()[secX1][secY1]);
        }
    }
    public void createClientSocket(String ip)
    {
        try
        {
            Socket socket = new Socket(ip, 2022);// 创建套接字对象
            out = new PrintWriter(socket.getOutputStream(), true);// 创建输出流对象
            new ClientThread(socket).start();// 创建并启动线程对象
        } catch (UnknownHostException e) {e.printStackTrace();
        } catch (IOException e) {e.printStackTrace();}
    }
    class ClientThread extends Thread
    {
        Socket socket;
        public ClientThread(Socket socket) {
            this.socket = socket;
        }
        public void run()
        {
            client.user_list = new JList();
            client.user_list.setModel(new DefaultComboBoxModel(new String[] { "" }));
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 创建输入流对象
                DefaultComboBoxModel model = (DefaultComboBoxModel) user_list.getModel();// 获得列表框的模型
                while (true)
                {
                    String info = in.readLine().trim();// 读取信息
                    if (info.equals("csb:"))//未开发
                        handleIniCsb(info.substring(4));
                    else if (info.startsWith("stp:"))
                        handleStep(info.substring(4));
                    else if(info.startsWith("lnk:"))
                    {
                        oppoUser=info.substring(4);
                        pvpPlayerListPanel.setVisible(false);
                        pvpPlayerListPanel.setEnabled(false);
                        gamePanel.setEnabled(true);
                        gamePanel.setVisible(true);
                        mainFrame.setContentPane(gamePanel);
                    }
                    else if (info.startsWith("usr:"))
                    {
                        boolean itemFlag = false;// 标记是否为列表框添加列表项，为true不添加，为false添加
                        for (int i = 0; i < model.getSize(); i++)
                            if (info.substring(4).equals((String) model.getElementAt(i)))
                                itemFlag = true;
                        if (!itemFlag)
                            model.addElement(info.substring(4));// 添加列表项
                    }
                   /* else if (info.startsWith("reg:"))
                    {
                        if(info.substring(4).equals("1"))
                            regOK=1;
                        else regOK=2;
                    }
                    else if (info.startsWith("log:"))
                    {
                        if(info.substring(4).equals("1"))
                            logOK=1;
                        else logOK=2;
                    }*/
                }
            }catch (IOException e) {e.printStackTrace();}
        }
    }
    public void send(int mode,String info)//1送棋盘2送步骤3送注册验证4送登陆验证5送用户信息
    {
        String receiveUserName = (String) user_list.getSelectedValue();// 获得接收信息的用户
        String msg=null;
        if(mode==1)
            msg="csb:From:"+user+"To:"+oppoUser+":"+info;
        else if(mode==2)
            msg="stp:From:"+user+"To:"+oppoUser+":"+info;
        else if(mode==5)
            msg="usr:"+user;
        else if(mode==6)
            msg="lnk:From:"+user+"To:"+receiveUserName+":";

        out.println(msg);// 发送信息
        out.flush();// 刷新输出缓冲区
    }
}