package computerPlayer;

import chessComponent.CannonChessComponent;
import chessComponent.SquareComponent;
import model.ChessColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static view.ChessGameFrame.*;
import static view.Chessboard.canListenToMe;
import static view.Handler.mainFrame;
public class ComputerPlayer extends Thread
{
    public static boolean stop=true;
    private static int difficultyMode=0;//0随机1贪心
    private final shift up=new shift(-1,0),down= new shift(1,0),
                left=new shift(0,-1),right=new shift(0,1);
    private static class shift
    {
        public int sx,sy;
        public shift(int sx,int sy)
        {
            this.sx=sx;
            this.sy=sy;
        }
    }
    private void playMode0()
    {
        while(chessboard.getCurrentColor()!=(chessboard.mePlayer.getColor().equals(Color.RED)? ChessColor.RED:ChessColor.BLACK))
        {
            Random r=new Random();
            int rx=r.nextInt(8),ry=r.nextInt(4);
            SquareComponent firChess=chessboard.getChessComponents()[rx][ry];
            if(chessboard.clickController.onClick(firChess)!=0)
            {
                if(chessboard.getCurrentColor().equals(chessboard.mePlayer.getColor().equals(Color.RED)? ChessColor.RED: ChessColor.BLACK)) return;//翻开
                try {sleep(500);} catch (InterruptedException ex) {}
                ArrayList<shift>ar=new ArrayList<>();
                if(!(firChess instanceof CannonChessComponent))
                {
                    if(rx+up.sx>=0) ar.add(up); if(rx+down.sx<=7) ar.add(down);
                    if(ry+left.sy>=0) ar.add(left);if(ry+right.sy<=3) ar.add(right);
                    Collections.shuffle(ar);
                    for (int i=0;i<ar.size();i++)
                    {
                        if(chessboard.clickController.onClick(chessboard.getChessComponents()[rx+ar.get(i).sx][ry+ar.get(i).sy])==3)
                            return;
                    }
                }
                else if (firChess instanceof CannonChessComponent)
                {
                    ar.add(up); ar.add(down);   ar.add(left); ar.add(right);
                    Collections.shuffle(ar);
                    for (int i=0;i<ar.size();i++)
                    {
                        int nx=rx+ar.get(i).sx,ny=ry+ar.get(i).sy;
                        while (nx>=0 && nx<=7 && ny>=0 && ny<=3)
                        {
                            if(chessboard.clickController.onClick(chessboard.getChessComponents()[nx][ny])==3)
                                return;
                            nx+= ar.get(i).sx;   ny+= ar.get(i).sy;
                        }
                    }
                }
                if(firChess.isSelected())
                    chessboard.clickController.onClick(firChess);//取消选取
            }
        }
    }
    private void playMode1()
    {
        int maxScore=0;//记录结果
        SquareComponent fromChess=null,toChess=null;//=null是不能去掉的！不然会报错
        SquareComponent firChess,secChess;//过程变量
        for(int i=0;i<=7;i++)
            for(int j=0;j<=3;j++)
            {
                firChess = chessboard.getChessComponents()[i][j];
                if (firChess.isReversal() && firChess.getChessColor().equals(chessboard.getCurrentColor()))//可选中
                {
                    if(!firChess.isSelected())
                        chessboard.clickController.onClick(firChess);
                    ArrayList<shift> ar = new ArrayList<>();
                    if (!(firChess instanceof CannonChessComponent))
                    {
                        if (i + up.sx >= 0) ar.add(up);if (i + down.sx <= 7) ar.add(down);
                        if (j + left.sy >= 0) ar.add(left);if (j + right.sy <= 3) ar.add(right);
                        Collections.shuffle(ar);
                        for (int k = 0; k < ar.size(); k++)
                        {
                            if (chessboard.getChessComponents()[i + ar.get(k).sx][j + ar.get(k).sy].score>maxScore  &&
                                    chessboard.clickController.handleSecond(chessboard.getChessComponents()[i + ar.get(k).sx][j + ar.get(k).sy]))
                            {
                                maxScore=chessboard.getChessComponents()[i + ar.get(k).sx][j + ar.get(k).sy].score;
                                fromChess=firChess;
                                toChess=chessboard.getChessComponents()[i + ar.get(k).sx][j + ar.get(k).sy];
                            }
                        }
                    }
                    else if (firChess instanceof CannonChessComponent)
                    {
                        ar.add(up); ar.add(down);   ar.add(left);   ar.add(right);
                        Collections.shuffle(ar);
                        for (int k = 0; k < ar.size(); k++)
                        {
                            int nx = i + ar.get(k).sx, ny = j + ar.get(k).sy;
                            while (nx >= 0 && nx <= 7 && ny >= 0 && ny <= 3)
                            {
                                if (chessboard.getChessComponents()[nx][ny].score>maxScore &&
                                        chessboard.getChessComponents()[nx][ny].getChessColor().equals(chessboard.mePlayer.getColor().equals(Color.RED)?ChessColor.RED:ChessColor.BLACK)
                                && chessboard.clickController.handleSecond(chessboard.getChessComponents()[nx][ny]))
                                {
                                    maxScore=chessboard.getChessComponents()[nx][ny].score;
                                    fromChess=firChess;
                                    toChess=chessboard.getChessComponents()[nx][ny];
                                }
                                nx += ar.get(k).sx;
                                ny += ar.get(k).sy;
                            }
                        }
                    }
                    if(firChess.isSelected())
                        chessboard.clickController.onClick(firChess);//取消选取
                }
            }
        if(maxScore>0)
        {
            chessboard.clickController.onClick(fromChess);
            try {sleep(500);} catch (InterruptedException ex) {}
            chessboard.clickController.onClick(toChess);
        }
        else
            playMode0();
    }
    private void handlePlay()
    {
        canListenToMe=false;
        mainFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {sleep(500);} catch (InterruptedException ex) {}
        if(difficultyMode==0)
            playMode0();
        else if(difficultyMode==1)
            playMode1();
        canListenToMe=true;
        mainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    public static void setDifficultyMode(int diffMode) {
        difficultyMode=diffMode;
    }
    @Override
    public void run() {
        if(computerStop)
            return;
        withdrawButton.setEnabled(false);
        while (true)
        {
            if (stop || computerStop) break;
            if(chessboard.getCurrentColor()!=(chessboard.mePlayer.getColor().equals(Color.RED)? ChessColor.RED:ChessColor.BLACK))
                handlePlay();
            try {sleep(20);} catch (InterruptedException ex) {}
        }
        if(!chessboard.ope.empty())
            withdrawButton.setEnabled(true);
    }
}