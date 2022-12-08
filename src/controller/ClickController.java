package controller;

import chessComponent.EmptySlotComponent;
import chessComponent.SquareComponent;
import model.ChessColor;
import player.Players;
import view.ChessGameFrame;
import view.Chessboard;

import javax.swing.*;
import java.awt.*;

import static view.ChessGameFrame.*;

public class ClickController {
    private final Chessboard chessboard;
    private SquareComponent first;
    public ClickController(Chessboard chessboard) {
        this.chessboard = chessboard;
    }
    public void recordWithdraw(SquareComponent fir)
    {
        ope.push(1);
        firCom.push(null);/*0-6:将士相车马兵炮 7:空 */  firCol.push(null);//0黑1红
        firX.push(fir.getChessboardPoint().getX()); firY.push(fir.getChessboardPoint().getY());
        secCom.push(null);   secCol.push(null);   secX.push(null); secY.push(null); firCannonSecRev.push(null);
        withdrawButton.setEnabled(true);
    }
    public void recordWithdraw(SquareComponent fir,SquareComponent sec,int rev)
    {
        ope.push(2);
        firCom.push(fir.label);/*0-6:将士相车马兵炮 7:空 */  firCol.push(fir.getChessColor()==ChessColor.BLACK?0:1);//0黑1红
        firX.push(fir.getChessboardPoint().getX()); firY.push(fir.getChessboardPoint().getY());
        secCom.push(sec.label);   secCol.push(sec.getChessColor().equals(ChessColor.BLACK)?0:1);
        secX.push(sec.getChessboardPoint().getX()); secY.push(sec.getChessboardPoint().getY());
        if(rev==1)
            firCannonSecRev.push(1);
        else
            firCannonSecRev.push(0);
        withdrawButton.setEnabled(true);
    }
    public void onClick(SquareComponent squareComponent) {
        if(first==null)
        {
            if(!squareComponent.isReversal() && !(squareComponent instanceof EmptySlotComponent))
            {//翻开
                recordWithdraw(squareComponent);
                squareComponent.setReversal(true);
                System.out.printf("onClick to reverse a chess [%d,%d]\n", squareComponent.getChessboardPoint().getX(), squareComponent.getChessboardPoint().getY());
                squareComponent.repaint();
                chessboard.clickController.swapPlayer();
            }
            else if(squareComponent.isReversal() && squareComponent.getChessColor() == Chessboard.getCurrentColor())
            {//选定同颜色的已翻开的
                squareComponent.setSelected(true);
                first = squareComponent;
                first.repaint();
            }
        }
        else if(first!=null)
        {
            if (first == squareComponent)
            { // 再次点击取消选取
                squareComponent.setSelected(false);
                first.repaint();
                first = null;
            }
            else if (handleSecond(squareComponent))
            {//能吃的、能移动到空格子的就做
                if(squareComponent.isReversal()==false)
                    recordWithdraw(first,squareComponent,1);
                else
                    recordWithdraw(first,squareComponent,0);
                first.addScoreToPlayer(squareComponent);
                //repaint in swap chess method.
                chessboard.swapChessComponents(first, squareComponent);
                chessboard.clickController.swapPlayer();
                first.setSelected(false);
                first = null;
            }
            else if(!squareComponent.isReversal() && !(squareComponent instanceof EmptySlotComponent))
            {//翻过来
                recordWithdraw(squareComponent);
                first.setSelected(false);
                first.repaint();
                first = null;
                squareComponent.setReversal(true);
                System.out.printf("onClick to reverse a chess [%d,%d]\n", squareComponent.getChessboardPoint().getX(), squareComponent.getChessboardPoint().getY());
                squareComponent.repaint();
                chessboard.clickController.swapPlayer();
            }
            else if (squareComponent.isReversal() && squareComponent.getChessColor() == Chessboard.getCurrentColor())
            {//换选
                squareComponent.setSelected(true);
                first.setSelected(false);
                first.repaint();
                squareComponent.repaint();
                first = squareComponent;
            }
        }
    }
    /**
     * @param squareComponent first棋子目标移动到的棋子second
     * @return first棋子是否能够移动到second棋子位置
     */
    private boolean handleSecond(SquareComponent squareComponent) {//空棋子的isReversal是false！
        return first.canMoveTo(chessboard.getChessComponents(), squareComponent.getChessboardPoint());
    }
    private void hasWinner(Players pl)
    {
        Object[] options={"Menu","Restart!"};
        int choice=JOptionPane.showOptionDialog(JOptionPane.getRootFrame()
                ,pl.getColor()==Color.BLACK? "BLACK":"RED" +" has won!"
                ,"Game over!",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[1]);
        if(choice==0){//菜单 todo

        }
        else {
            chessboard.initAllChessOnBoard();//restart!
            ChessGameFrame.repaintAll();
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
        ChessGameFrame.getStatusLabel().setText(String.format("%s's TURN", Chessboard.getCurrentColor().getName()));
        ChessGameFrame.getScoreOfBlack().setText(String.format("BLACK's points: %d", Chessboard.blackPlayer.getCurrentScore()));
        ChessGameFrame.getScoreOfRed().setText(String.format("RED's points: %d", Chessboard.redPlayer.getCurrentScore()));
        ckeckWinner();
    }
}