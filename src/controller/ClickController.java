package controller;

import chessComponent.EmptySlotComponent;
import chessComponent.SquareComponent;
import computerPlayer.ComputerPlayer;
import model.ChessColor;
import player.Players;
import view.CapturingBoard;
import view.ChessGameFrame;
import view.Chessboard;
import view.Handler;

import javax.swing.*;
import java.awt.*;

import static view.ChessGameFrame.*;
import static view.Chessboard.*;

public class ClickController {
    private final Chessboard chessboard;
    private SquareComponent first;
    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }
    private boolean isComputerOperating()//如果是机器正在操作，则返回真
    {
        if(ChessGameFrame.menuMode==0 && Chessboard.getCurrentColor()!=(mePlayer.getColor().equals(Color.RED)?
                ChessColor.RED:ChessColor.BLACK))
            return true;
        else return false;
    }
    public void recordWithdraw(SquareComponent fir)
    {
        ope.push(1);
        firCom.push(null);/*0-6:将士相车马兵炮 7:空 */  firCol.push(null);
        firX.push(fir.getChessboardPoint().getX()); firY.push(fir.getChessboardPoint().getY());
        secCom.push(null);   secCol.push(null);   secX.push(null); secY.push(null); firCannonSecRev.push(null);
        capturingIsMe.push(null);capturingLabel.push(null);
        if(ComputerPlayer.stop)
            withdrawButton.setEnabled(true);
    }
    public void recordWithdraw(SquareComponent fir,SquareComponent sec,int rev,int meIsEaten,int label)
    {
        ope.push(2);
        firCom.push(fir.label);/*0-6:将士相车马兵炮 7:空 */  firCol.push(fir.getChessColor().equals(ChessColor.RED)?0:1);//0红1黑
        firX.push(fir.getChessboardPoint().getX()); firY.push(fir.getChessboardPoint().getY());
        secCom.push(sec.label);   secCol.push(sec.getChessColor().equals(ChessColor.RED)?0:1);
        secX.push(sec.getChessboardPoint().getX()); secY.push(sec.getChessboardPoint().getY());
        firCannonSecRev.push(rev);
        capturingIsMe.push(meIsEaten);capturingLabel.push(label);
        if(ComputerPlayer.stop)
            withdrawButton.setEnabled(true);
    }
    public boolean canChangeCursor(SquareComponent squareComponent)
    {
        if(first!=null && handleSecond(squareComponent))
            return true;
        return false;
    }
    public  int onClick(SquareComponent squareComponent) {//操作不成功返回0，选中返回1，翻开返回2，吃掉返回3，取消返回4
        if(first==null)
        {
            if(!squareComponent.isReversal() && !(squareComponent instanceof EmptySlotComponent))
            {//翻开
                recordWithdraw(squareComponent);
                squareComponent.setReversal(true);
                System.out.printf("onClick to reverse a chess [%d,%d]\n", squareComponent.getChessboardPoint().getX(), squareComponent.getChessboardPoint().getY());
                squareComponent.repaint();
                chessboard.clickController.swapPlayer();
                return 2;
            }
            else if(squareComponent.isReversal() && squareComponent.getChessColor() .equals(Chessboard.getCurrentColor()))
            {//选定同颜色的已翻开的
                squareComponent.setSelected(true);
                first = squareComponent;
                first.repaint();
                return 1;
            }
        }
        else if(first!=null)
        {
            if (first == squareComponent)
            { // 再次点击取消选取
                squareComponent.setSelected(false);
                first.repaint();
                first = null;
                return 4;
            }
            else if (handleSecond(squareComponent))
            {//能吃的、能移动到空格子的就做
                if(squareComponent.label!=7)
                {
                    ChessColor eatenColor=squareComponent.getChessColor();
                    boolean meIsEaten=mePlayer.getColor().equals(eatenColor.equals(ChessColor.RED)?Color.RED:Color.BLACK);
                    CapturingBoard change=meIsEaten?capturingBoardHe:capturingBoardMe;
                    change.capturingChesses[squareComponent.label].num++;
                    change.capturingChesses[squareComponent.label].repaint();
                    recordWithdraw(first,squareComponent,squareComponent.isReversal()?0:1,meIsEaten?1:0,squareComponent.label);
                }
                else
                    recordWithdraw(first,squareComponent,squareComponent.isReversal()?0:1,0,squareComponent.label);
                first.addScoreToPlayer(squareComponent);
                //repaint in swap chess method.
                chessboard.swapChessComponents(first, squareComponent);
                chessboard.clickController.swapPlayer();
                first.setSelected(false);
                first = null;
                return 3;
            }
            else if(!isComputerOperating() && !squareComponent.isReversal() && !(squareComponent instanceof EmptySlotComponent))
            {//翻过来(为了观感体验，机器不允许这样)
                recordWithdraw(squareComponent);
                first.setSelected(false);
                first.repaint();
                first = null;
                squareComponent.setReversal(true);
                System.out.printf("onClick to reverse a chess [%d,%d]\n", squareComponent.getChessboardPoint().getX(), squareComponent.getChessboardPoint().getY());
                squareComponent.repaint();
                chessboard.clickController.swapPlayer();
                return 2;
            }
            else if (!isComputerOperating() && squareComponent.isReversal() && squareComponent.getChessColor() == Chessboard.getCurrentColor())
            {//换选(为了观感体验，机器不允许这样)
                squareComponent.setSelected(true);
                first.setSelected(false);
                first.repaint();
                squareComponent.repaint();
                first = squareComponent;
                return 1;
            }
        }
        return 0;
    }
    /**
     * @param squareComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */
    public boolean handleSecond(SquareComponent squareComponent) {//空棋子的isReversal是false！
        return first.canMoveTo(chessboard.getChessComponents(), squareComponent.getChessboardPoint());
    }
    private void hasWinner(Players pl)
    {
        ComputerPlayer.stop=true;
        String[] options={"菜单","重开"};
        int choice=JOptionPane.showOptionDialog(JOptionPane.getRootFrame()
                ,(pl.getColor().equals(Color.BLACK)? "黑方":"红方")+" 赢了！"
                ,"游戏结束！",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[1]);
        if(choice==0){//菜单
            gamePanel.setEnabled(false);
            gamePanel.setVisible(false);
            PVCButtonsPanel.setEnabled(false);
            PVCButtonsPanel.setVisible(false);
            menuPanel.setEnabled(true);
            menuPanel.setVisible(true);
            gamePanel.remove(PVCButtonsPanel);
            Handler.mainFrame.setContentPane(ChessGameFrame.menuPanel);
        }
        else {
            chessboard.initAllChessOnBoard();//restart!
            ChessGameFrame.repaintAll();
            computerPlayer=new ComputerPlayer();
            computerPlayer.start();
        }
    }
    private void ckeckWinner() {
        if(Chessboard.blackPlayer.getCurrentScore()>=60)
            hasWinner(Chessboard.blackPlayer);
        else if(Chessboard.redPlayer.getCurrentScore()>=60)
            hasWinner(Chessboard.redPlayer);
    }
    public void swapPlayer() {
        Chessboard.setCurrentColor(Chessboard.getCurrentColor() == ChessColor.BLACK ? ChessColor.RED : ChessColor.BLACK);
        ChessGameFrame.getStatusLabel().setText(String.format("轮到%s方了", Chessboard.getCurrentColor().getName()));
        ChessGameFrame.getScoreOfBlack().setText(String.format("黑方的分数是： %d", Chessboard.blackPlayer.getCurrentScore()));
        ChessGameFrame.getScoreOfRed().setText(String.format("红方的分数是： %d", Chessboard.redPlayer.getCurrentScore()));
        ckeckWinner();
    }
}