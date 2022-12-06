package chessComponent;

import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;
import view.Chessboard;

import java.awt.*;

public class CannonChessComponent extends ChessComponent {

    public CannonChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor chessColor, ClickController clickController, int size) {
        super(chessboardPoint, location, chessColor, clickController, size);
        if (this.getChessColor() == ChessColor.RED) {
            name = "炮";
        } else {
            name = "砲";
        }
        super.score=5;
    }
    private boolean screenOK(SquareComponent[][] chessboard,ChessboardPoint destination)
    {
        if(destination.getX()!=this.getChessboardPoint().getX() && destination.getY()!=this.getChessboardPoint().getY()) return false;
        int cntNoneEmpty=0;
        if(destination.getX()==this.getChessboardPoint().getX())
        {
            for(int i=Math.min(destination.getY(),this.getChessboardPoint().getY())+1;i<=Math.max(destination.getY(),this.getChessboardPoint().getY())-1;i++)
            {
                if(!(chessboard[this.getChessboardPoint().getX()][i] instanceof EmptySlotComponent))
                    cntNoneEmpty++;
            }
        }
        else if(destination.getY()==this.getChessboardPoint().getY())
        {
            for(int i=Math.min(destination.getX(),this.getChessboardPoint().getX())+1;i<=Math.max(destination.getX(),this.getChessboardPoint().getX())-1;i++)
            {
                if(!(chessboard[i][this.getChessboardPoint().getY()] instanceof EmptySlotComponent))
                    cntNoneEmpty++;
            }
        }
        if(cntNoneEmpty==1) return true;
        else return false;
    }
    @Override
    public boolean canMoveTo(SquareComponent[][] chessboard, ChessboardPoint destination) {
        SquareComponent destinationChess = chessboard[destination.getX()][destination.getY()];
        if(! destinationChess.isReversal)
            return (!(destinationChess instanceof EmptySlotComponent)) && screenOK(chessboard,destination);
        else//翻开了
            return (!(destinationChess instanceof EmptySlotComponent)) && screenOK(chessboard,destination)
                    && destinationChess.getChessColor() != Chessboard.getCurrentColor();
        //todo: complete this method
    }
}
