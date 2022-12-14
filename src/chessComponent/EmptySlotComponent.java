package chessComponent;

import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;
import view.Chessboard;

import java.awt.*;

/**
 * 这个类表示棋盘上的空棋子的格子
 */
public class EmptySlotComponent extends SquareComponent {

    public EmptySlotComponent(ChessboardPoint chessboardPoint, Point location, ClickController listener, int size) {
        super(chessboardPoint, location, ChessColor.NONE, listener, size);
        super.score=0;
        super.label=7;
    }

    @Override
    public boolean canMoveTo(Chessboard chessboard, SquareComponent[][] sqcs, ChessboardPoint destination) {
        return false;
    }

}
