package controller;

import chessComponent.EmptySlotComponent;
import chessComponent.SquareComponent;
import computerPlayer.ComputerPlayer;
import media.MyDialog;
import model.ChessColor;
import player.Players;
import view.CapturingBoard;
import view.ChessGameFrame;
import view.Chessboard;
import view.Handler;

import java.awt.*;

import static media.BufferedPictures.WithdrawOnIcon;
import static view.ChessGameFrame.*;

public class ClickController {
    private Chessboard chessboard;
    private SquareComponent first;
    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }
    private boolean isComputerOperating()//如果是机器正在操作，则返回真
    {
        if(!ComputerPlayer.stop && ChessGameFrame.menuMode==0 && chessboard.getCurrentColor()!=(chessboard.mePlayer.getColor().equals(Color.RED)?
                ChessColor.RED:ChessColor.BLACK))
            return true;
        else return false;
    }
    public void recordWithdraw(SquareComponent fir)
    {
        chessboard.ope.push(1);
        chessboard.firCom.push(-1);/*0-6:将士相车马兵炮 7:空 */  chessboard.firCol.push(-1);
        chessboard.firX.push(fir.getChessboardPoint().getX()); chessboard.firY.push(fir.getChessboardPoint().getY());
        chessboard.secCom.push(-1);   chessboard.secCol.push(-1);   chessboard.secX.push(-1); chessboard.secY.push(-1); chessboard.firCannonSecRev.push(-1);
        chessboard.capturingIsMe.push(-1);chessboard.capturingLabel.push(-1);
        if(ComputerPlayer.stop && chessboard==ChessGameFrame.chessboard)
        {
            withdrawButton.setEnabled(true);
            withdrawButton.setIcon(WithdrawOnIcon);
        }
    }
    public void recordWithdraw(SquareComponent fir,SquareComponent sec,int rev,int meIsEaten,int label)
    {
        chessboard.ope.push(2);
        chessboard.firCom.push(fir.label);/*0-6:将士相车马兵炮 7:空 */  chessboard.firCol.push(fir.getChessColor().equals(ChessColor.RED)?0:1);//0红1黑
        chessboard.firX.push(fir.getChessboardPoint().getX()); chessboard.firY.push(fir.getChessboardPoint().getY());
        chessboard.secCom.push(sec.label);   chessboard.secCol.push(sec.getChessColor().equals(ChessColor.RED)?0:1);
        chessboard.secX.push(sec.getChessboardPoint().getX()); chessboard.secY.push(sec.getChessboardPoint().getY());
        chessboard.firCannonSecRev.push(rev);
        chessboard.capturingIsMe.push(meIsEaten);chessboard.capturingLabel.push(label);
        if(ComputerPlayer.stop && chessboard==ChessGameFrame.chessboard)
        {
            withdrawButton.setEnabled(true);
            withdrawButton.setIcon(WithdrawOnIcon);
        }
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
                if(menuMode==1)
                    client.work(1,squareComponent.getChessboardPoint().getX(),
                            squareComponent.getChessboardPoint().getY(),0,0);
                recordWithdraw(squareComponent);
                squareComponent.setReversal(true);
                //System.out.printf("onClick to reverse a chess [%d,%d]\n", squareComponent.getChessboardPoint().getX(), squareComponent.getChessboardPoint().getY());
                squareComponent.repaint();
                chessboard.clickController.swapPlayer();
                return 2;
            }
            else if(squareComponent.isReversal() && squareComponent.getChessColor() .equals(chessboard.getCurrentColor()))
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
                if(menuMode==1)
                    client.work(2,first.getChessboardPoint().getX(), first.getChessboardPoint().getY()
                            ,squareComponent.getChessboardPoint().getX(), squareComponent.getChessboardPoint().getY());
                if(squareComponent.label!=7)
                {
                    ChessColor eatenColor=squareComponent.getChessColor();
                    boolean meIsEaten=chessboard.mePlayer.getColor().equals(eatenColor.equals(ChessColor.RED)?Color.RED:Color.BLACK);
                    CapturingBoard change=meIsEaten?chessboard.capturingBoardHe:chessboard.capturingBoardMe;
                    change.capturingChesses[squareComponent.label].num++;
                    change.capturingChesses[squareComponent.label].repaint();
                    recordWithdraw(first,squareComponent,squareComponent.isReversal()?0:1,meIsEaten?1:0,squareComponent.label);
                }
                else
                    recordWithdraw(first,squareComponent,squareComponent.isReversal()?0:1,0,squareComponent.label);
                first.addScoreToPlayer(chessboard,squareComponent);
                //repaint in swap chess method.
                chessboard.swapChessComponents(first, squareComponent);
                chessboard.clickController.swapPlayer();
                first.setSelected(false);
                first = null;
                return 3;
            }
            else if(!isComputerOperating() && !squareComponent.isReversal() && !(squareComponent instanceof EmptySlotComponent))
            {//翻过来(为了观感体验，机器不允许这样)
                if(menuMode==1)
                    client.work(1,squareComponent.getChessboardPoint().getX(),
                            squareComponent.getChessboardPoint().getY(),0,0);
                recordWithdraw(squareComponent);
                first.setSelected(false);
                first.repaint();
                first = null;
                squareComponent.setReversal(true);
                //System.out.printf("onClick to reverse a chess [%d,%d]\n", squareComponent.getChessboardPoint().getX(), squareComponent.getChessboardPoint().getY());
                squareComponent.repaint();
                chessboard.clickController.swapPlayer();
                return 2;
            }
            else if (!isComputerOperating() && squareComponent.isReversal() && squareComponent.getChessColor() == chessboard.getCurrentColor())
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
        return first.canMoveTo(chessboard,chessboard.getChessComponents(), squareComponent.getChessboardPoint());
    }
    private void hasWinner(Players pl)
    {
        if(menuMode==1)
        {
            MyDialog.showDialog("游戏结束！",(pl.getColor().equals(Color.BLACK)? "黑方":"红方")+" 赢了！");
            gamePanel.setEnabled(false);
            gamePanel.setVisible(false);
            menuPanel.setEnabled(true);
            menuPanel.setVisible(true);
            Handler.mainFrame.setContentPane(menuPanel);
            return;
        }
        ComputerPlayer.stop=true;
        int choice= MyDialog.confirmDialog("游戏结束！",(pl.getColor().equals(Color.BLACK)? "黑方":"红方")+" 赢了！","菜单","重开");
        if(choice==1){//菜单
            gamePanel.setEnabled(false);
            gamePanel.setVisible(false);
            PVCButtonsPanel.setEnabled(false);
            PVCButtonsPanel.setVisible(false);
            menuPanel.setEnabled(true);
            menuPanel.setVisible(true);
            gamePanel.remove(PVCButtonsPanel);
            Handler.mainFrame.setContentPane(ChessGameFrame.menuPanel);
        }
        else
        {
            ComputerPlayer.stop=true;
            chessboard.initAllChessOnBoard(0);//restart!
            ChessGameFrame.repaintAll();
            ComputerPlayer.stop=false;
            computerPlayer=new ComputerPlayer();
            computerPlayer.start();
        }
    }
    private void ckeckWinner() {
        if(chessboard.blackPlayer.getCurrentScore()>=60)
            hasWinner(chessboard.blackPlayer);
        else if(chessboard.redPlayer.getCurrentScore()>=60)
            hasWinner(chessboard.redPlayer);
    }
    public void swapPlayer() {
        chessboard.setCurrentColor(chessboard.getCurrentColor() == ChessColor.BLACK ? ChessColor.RED : ChessColor.BLACK);
        if(this.chessboard==ChessGameFrame.chessboard)
        {
            ChessGameFrame.getStatusLabel().setText(String.format("轮到%s方了", chessboard.getCurrentColor().getName()));
            ChessGameFrame.getScoreOfBlack().setText(String.format("黑方的分数是： %d", chessboard.blackPlayer.getCurrentScore()));
            ChessGameFrame.getScoreOfRed().setText(String.format("红方的分数是： %d", chessboard.redPlayer.getCurrentScore()));
        }
        ckeckWinner();
    }
}