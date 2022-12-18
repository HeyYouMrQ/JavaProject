package controller;

import chessComponent.*;
import media.FileChooser;
import media.MyDialog;
import model.ChessColor;
import view.ChessGameFrame;
import view.Chessboard;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static view.ChessGameFrame.*;
import static view.Handler.mainFrame;

/**
 * 这个类主要完成由窗体上组件触发的动作。
 * 例如点击button等
 * ChessGameFrame中组件调用本类的对象，在本类中的方法里完成逻辑运算，将运算的结果传递至chessboard中绘制
 */
public class GameController {
    public static boolean [][] isRever=new boolean[8][4];
    public Chessboard fromChessboard,toChessboard;
    public static int[] aimCapturingboardMeNum =new int[7], aimCapturingboardHeNum =new int[7];
    private static Stack<Integer> reverseStack(Stack<Integer> in)
    {
        Stack<Integer>ret=new Stack<>();
        int nail=in.size()-1;
        while (nail>=0)
            ret.push(in.get(nail--));
        return ret;
    }
    private void loadGame()
    {
        chessboard.capturingBoardHe.setVisible(false);
        chessboard.capturingBoardMe.setVisible(false);
        chessboard.setVisible(false);
        ChessGameFrame.gamePanel.remove(chessboard.capturingBoardHe);
        ChessGameFrame.gamePanel.remove(chessboard.capturingBoardMe);
        ChessGameFrame.gamePanel.remove(chessboard);
        chessboard=toChessboard;
        chessboard.setLocation(mainFrame.WIDTH / 5, mainFrame.HEIGHT / 5);
        chessboard.capturingBoardHe.setLocation(mainFrame.WIDTH/5 - mainFrame.CHESSBOARD_SIZE/8, mainFrame.HEIGHT / 4);
        chessboard.capturingBoardMe.setLocation(mainFrame.WIDTH/5 + mainFrame.CHESSBOARD_SIZE/2, mainFrame.HEIGHT / 4);//自己放在右边
        ChessGameFrame.gamePanel.add(chessboard);
        gamePanel.add(chessboard.capturingBoardHe);
        gamePanel.add(chessboard.capturingBoardMe);
        repaintAll();
    }
    private boolean checkValid()//演化尝试
    {
        Stack<Integer> ope=reverseStack(toChessboard.ope),firCom=reverseStack(toChessboard.firCom),firCol=reverseStack(toChessboard.firCol)//倒带
        ,firX=reverseStack(toChessboard.firX),firY=reverseStack(toChessboard.firY),secCom=reverseStack(toChessboard.secCom),secCol=reverseStack(toChessboard.secCol)
        ,secX=reverseStack(toChessboard.secX),secY=reverseStack(toChessboard.secY),firCannonSecRev=reverseStack(toChessboard.firCannonSecRev)
        ,capturingIsMe=reverseStack(toChessboard.capturingIsMe),capturingLabel=reverseStack(toChessboard.capturingLabel);

        while (!ope.empty())//检查演化过程
        {
            if(ope.peek()==1)
            {
                int a=fromChessboard.clickController.onClick(fromChessboard.getChessComponents()[firX.peek()][firY.peek()]);
                if(a!=2)
                    return false;
            }
            else if(ope.peek()==2)
            {
                if(fromChessboard.clickController.onClick(fromChessboard.getChessComponents()[firX.peek()][firY.peek()])!=1)
                    return false;
                if(fromChessboard.clickController.onClick(fromChessboard.getChessComponents()[secX.peek()][secY.peek()])!=3)
                    return false;
            }
            else return false;
            ope.pop();
            firCom.pop();   firCol.pop();   firX.pop(); firY.pop();
            secCom.pop();   secCol.pop();   secX.pop(); secY.pop(); firCannonSecRev.pop();
            capturingIsMe.pop();    capturingLabel.pop();
        }
        for(int i=0;i<=7;i++)//检查演化结果
        {
            for(int j=0;j<=3;j++)
                if(fromChessboard.getChessComponents()[i][j].label!=toChessboard.getChessComponents()[i][j].label ||
                        fromChessboard.getChessComponents()[i][j].getChessColor()!=toChessboard.getChessComponents()[i][j].getChessColor())
                    return false;
        }
        if(fromChessboard.mePlayer.getColor().equals(Color.RED))
        {
            for(int i=0;i<=6;i++)
            {
                if(fromChessboard.capturingBoardMe.getCapturingChesses()[i].num!= aimCapturingboardMeNum[i])
                    return false;
                if(fromChessboard.capturingBoardHe.getCapturingChesses()[i].num!= aimCapturingboardHeNum[i])
                    return false;
            }
        }
        else if(fromChessboard.mePlayer.getColor().equals(Color.BLACK))
        {
            for(int i=0;i<=6;i++)
            {
                if(fromChessboard.capturingBoardMe.getCapturingChesses()[i].num!= aimCapturingboardHeNum[i])
                    return false;
                if(fromChessboard.capturingBoardHe.getCapturingChesses()[i].num!= aimCapturingboardMeNum[i])
                    return false;
            }
        }
        return true;
    }
    private int checkError(List<String> chessData) {
        String[] elements = null;
        for(int i=0;i<=7;i++)
            for(int j=0;j<=3;j++)
                isRever[i][j]=false;
        for(int i=0;i<=6;i++)
        {
            aimCapturingboardMeNum[i]=0;
            aimCapturingboardHeNum[i]=0;
        }
        for(int i=0;i<=7;i++)//初始棋盘：红大写黑小写，gamvhsce将士相车马兵炮空,v for vehicle
        {
            elements=chessData.get(i).split(" ");
            if(elements.length!=4)
                return 2;
            for(int j=0;j<=3;j++)
            {
                boolean isRED=Character.isUpperCase(elements[j].charAt(0));
                char tmp=Character.toLowerCase(elements[j].charAt(0));
                switch (tmp)
                {
                    case 'g':
                        fromChessboard.initComponents[i][j].chessType=0;
                        break;
                    case 'a':
                        fromChessboard.initComponents[i][j].chessType=1;
                        break;
                    case 'm':
                        fromChessboard.initComponents[i][j].chessType=2;
                        break;
                    case 'v':
                        fromChessboard.initComponents[i][j].chessType=3;
                        break;
                    case 'h':
                        fromChessboard.initComponents[i][j].chessType=4;
                        break;
                    case 's':
                        fromChessboard.initComponents[i][j].chessType=5;
                        break;
                    case 'c':
                        fromChessboard.initComponents[i][j].chessType=6;
                        break;
                    default:
                        return 3;
                }
                fromChessboard.initComponents[i][j].player=(isRED?0:1);
            }
        }
        fromChessboard.mePlayer.setColor(chessData.get(17).equals("RED")?Color.RED:Color.BLACK);
        fromChessboard.initAllChessOnBoard(1);
        for(int i=8;i<=15;i++)//棋盘：红大写黑小写，gamvhsce将士相车马兵炮空,v for vehicle
        {
            elements=chessData.get(i).split(" ");
            if(elements.length!=4)
                return 2;
            for(int j=0;j<=3;j++)
            {
                boolean isRED=Character.isUpperCase(elements[j].charAt(0));
                isRever[i-8][j]=(elements[j].charAt(1)=='1');
                char tmp=Character.toLowerCase(elements[j].charAt(0));
                switch (tmp)
                {
                    case 'g':
                        toChessboard.initComponents[i-8][j].chessType=0;
                        break;
                    case 'a':
                        toChessboard.initComponents[i-8][j].chessType=1;
                        break;
                    case 'm':
                        toChessboard.initComponents[i-8][j].chessType=2;
                        break;
                    case 'v':
                        toChessboard.initComponents[i-8][j].chessType=3;
                        break;
                    case 'h':
                        toChessboard.initComponents[i-8][j].chessType=4;
                        break;
                    case 's':
                        toChessboard.initComponents[i-8][j].chessType=5;
                        break;
                    case 'c':
                        toChessboard.initComponents[i-8][j].chessType=6;
                        break;
                    case 'e':
                        toChessboard.initComponents[i-8][j].chessType=7;
                        break;
                    default:
                        return 3;
                }
                toChessboard.initComponents[i-8][j].player=(isRED?0:1);
            }
        }
        if(chessData.get(16).equals("RED"))
            toChessboard.setCurrentColor(ChessColor.RED);
        else if(chessData.get(16).equals("BLACK"))
            toChessboard.setCurrentColor(ChessColor.BLACK);
        else if(Character.toLowerCase(chessData.get(16).charAt(0))=='g'||Character.toLowerCase(chessData.get(16).charAt(0))=='a'||
        Character.toLowerCase(chessData.get(16).charAt(0))=='m'||Character.toLowerCase(chessData.get(16).charAt(0))=='v'||
                Character.toLowerCase(chessData.get(16).charAt(0))=='h'||Character.toLowerCase(chessData.get(16).charAt(0))=='s'||
                Character.toLowerCase(chessData.get(16).charAt(0))=='c'||Character.toLowerCase(chessData.get(16).charAt(0))=='e')//gamvhsce
            return 2;
        else return 4;
        toChessboard.mePlayer.setColor(chessData.get(17).equals("RED")?Color.RED:Color.BLACK);
        toChessboard.redPlayer.setCurrentScore(Integer.parseInt(chessData.get(18)));
        toChessboard.blackPlayer.setCurrentScore(Integer.parseInt(chessData.get(19)));

        elements = chessData.get(20).split(" ");//0-6:将士相车马兵炮
        for(int i=0;i<=6;i++)
            aimCapturingboardMeNum[i]=Integer.parseInt(elements[i]);
        elements = chessData.get(21).split(" ");
        for(int i=0;i<=6;i++)
            aimCapturingboardHeNum[i]=Integer.parseInt(elements[i]);

        for(int i=22;i<chessData.size();i++)
        {
            elements = chessData.get(i).split(" ");
            toChessboard.ope.push(Integer.parseInt(elements[0]));toChessboard.firCom.push(Integer.parseInt(elements[1]));
            toChessboard.firCol.push(Integer.parseInt(elements[2]));toChessboard.firX.push(Integer.parseInt(elements[3]));
            toChessboard.firY.push(Integer.parseInt(elements[4]));toChessboard.secCom.push(Integer.parseInt(elements[5]));
            toChessboard.secCol.push(Integer.parseInt(elements[6]));
            toChessboard.secX.push(Integer.parseInt(elements[7]));toChessboard.secY.push(Integer.parseInt(elements[8]));
            toChessboard.firCannonSecRev.push(Integer.parseInt(elements[9]));
            toChessboard.capturingIsMe.push(Integer.parseInt(elements[10]));toChessboard.capturingLabel.push(Integer.parseInt(elements[11]));
        }

        toChessboard.initAllChessOnBoard(1);
        if(!checkValid())
            return 5;
        return 0;
    }
    private boolean extensionOK(String in)
    {
        if(in.length()<4)
            return false;
        String tmp=in.substring(in.length()-4,in.length());
        if(!tmp.toLowerCase().equals(".txt"))
            return false;
        return true;
    }
    public void loadGameFromFile() {
        List<String> chessData=null;
        try 
        {
            Path path=Path.of(FileChooser.chooseFile(0));
            if(!extensionOK(path.toString()))
            {
                //JOptionPane.showMessageDialog(chessboard, "error 101");
                MyDialog.showDialog("文件小精灵","error 101");
                return;
            }
            chessData=Files.readAllLines(path);
        }
        catch (IOException e) {e.printStackTrace();}
        catch (Exception e) {throw new RuntimeException(e);}
        fromChessboard=new Chessboard(mainFrame.CHESSBOARD_SIZE/2, mainFrame.CHESSBOARD_SIZE);
        toChessboard=new Chessboard(mainFrame.CHESSBOARD_SIZE/2, mainFrame.CHESSBOARD_SIZE);
        int errorState = checkError(chessData);
        switch (errorState) {
            case 2:
                MyDialog.showDialog("文件小精灵","error 102");
                return;
            case 3:
                MyDialog.showDialog("文件小精灵","error 103");
                return;
            case 4:
                MyDialog.showDialog("文件小精灵","error 104");
                return;
            case 5:
                MyDialog.showDialog("文件小精灵","error 105");
                return;
            //default:
        }
        loadGame();
    }
    private List<String> convertToList() {
        List<String> lines = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<=7;i++)//初始棋盘：红大写黑小写，gamvhsc将士相车马兵炮,v for vehicle
        {
            for(int j=0;j<=3;j++)
            {
                boolean isRED=chessboard.initComponents[i][j].player==0;
                if(chessboard.initComponents[i][j].chessType==0)
                    sb.append(isRED?'G':'g');
                else if(chessboard.initComponents[i][j].chessType==1)
                    sb.append(isRED?'A':'a');
                else if(chessboard.initComponents[i][j].chessType==2)
                    sb.append(isRED?'M':'m');
                else if(chessboard.initComponents[i][j].chessType==3)
                    sb.append(isRED?'V':'v');
                else if(chessboard.initComponents[i][j].chessType==4)
                    sb.append(isRED?'H':'h');
                else if(chessboard.initComponents[i][j].chessType==5)
                    sb.append(isRED?'S':'s');
                else if(chessboard.initComponents[i][j].chessType==6)
                    sb.append(isRED?'C':'c');
                sb.append(' ');
            }
            sb.setLength(sb.length()-1);
            lines.add(sb.toString());
            sb.setLength(0);
        }
        for(int i=0;i<=7;i++)//棋盘：红大写黑小写，gamvhsc将士相车马兵炮,v for vehicle
        {
            for(int j=0;j<=3;j++)
            {
                boolean isRED=chessboard.getChessComponents()[i][j].getChessColor().equals(ChessColor.RED);
                boolean isRever=chessboard.getChessComponents()[i][j].isReversal();
                if(chessboard.getChessComponents()[i][j] instanceof GeneralChessComponent)
                    sb.append(isRED?'G':'g');
                else if(chessboard.getChessComponents()[i][j] instanceof AdvisorChessComponent)
                    sb.append(isRED?'A':'a');
                else if(chessboard.getChessComponents()[i][j] instanceof MinisterChessComponent)
                    sb.append(isRED?'M':'m');
                else if(chessboard.getChessComponents()[i][j] instanceof ChariotChessComponent)
                    sb.append(isRED?'V':'v');
                else if(chessboard.getChessComponents()[i][j] instanceof HorseChessComponent)
                    sb.append(isRED?'H':'h');
                else if(chessboard.getChessComponents()[i][j] instanceof SoldierChessComponent)
                    sb.append(isRED?'S':'s');
                else if(chessboard.getChessComponents()[i][j] instanceof CannonChessComponent)
                    sb.append(isRED?'C':'c');
                else if(chessboard.getChessComponents()[i][j] instanceof EmptySlotComponent)
                    sb.append(isRED?'E':'e');
                sb.append(isRever?"1 ":"0 ");
            }
            sb.setLength(sb.length()-1);
            lines.add(sb.toString());
            sb.setLength(0);
        }
        lines.add(chessboard.getCurrentColor().toString());
        lines.add(chessboard.mePlayer.getColor().equals(Color.RED)?"RED":"BLACK");
        lines.add(String.format("%d",chessboard.redPlayer.getCurrentScore()));
        lines.add(String.format("%d",chessboard.blackPlayer.getCurrentScore()));

        for(int i=0;i<=6;i++)//捕获,0-6:将士相车马兵炮  先己后他
            sb.append(String.format("%d ",chessboard.capturingBoardMe.getCapturingChesses()[i].num));
        sb.setLength(sb.length()-1);
        lines.add(sb.toString());
        sb.setLength(0);
        for(int i=0;i<=6;i++)
            sb.append(String.format("%d ",chessboard.capturingBoardHe.getCapturingChesses()[i].num));
        sb.setLength(sb.length()-1);
        lines.add(sb.toString());
        sb.setLength(0);

        Stack<Integer> ope1=reverseStack(chessboard.ope),firCom1=reverseStack(chessboard.firCom),firCol1=reverseStack(chessboard.firCol)
                ,firX1=reverseStack(chessboard.firX),firY1=reverseStack(chessboard.firY),secCom1=reverseStack(chessboard.secCom),secCol1=reverseStack(chessboard.secCol)
                ,secX1=reverseStack(chessboard.secX),secY1=reverseStack(chessboard.secY),firCannonSecRev1=reverseStack(chessboard.firCannonSecRev)
                , capturingIsMe1=reverseStack(chessboard.capturingIsMe),capturingLabel1=reverseStack(chessboard.capturingLabel);
        while (!ope1.empty())
        {
            sb.append(ope1.pop().toString()+' ').append(firCom1.pop().toString()+' ').append(firCol1.pop().toString()+' ')
                    .append(firX1.pop().toString()+' ').append(firY1.pop().toString()+' ').append(secCom1.pop().toString()+' ')
                    .append(secCol1.pop().toString()+' ').append(secX1.pop().toString()+' ').append(secY1.pop().toString()+' ')
                    .append(firCannonSecRev1.pop().toString()+' ').append(capturingIsMe1.pop().toString()+' ').append(capturingLabel1.pop().toString());
            lines.add(sb.toString());
            sb.setLength(0);
        }
        return lines;
    }
    public void saveGameToFile() {
        //String path = JOptionPane.showInputDialog(this, "存档路径：");
        try {
            Files.write(Path.of(FileChooser.chooseFile(1)+"/archive.txt"), this.convertToList(), Charset.defaultCharset());}
        catch (IOException e) {e.printStackTrace();}
        catch (Exception e) {throw new RuntimeException(e);}
    }
}