package view;

import chessComponent.SquareComponent;
import model.ChessColor;

import javax.swing.*;
import java.awt.*;

public class CapturingBoard extends JComponent {
    private static final int ROW_SIZE = 7;
    public final CapturingChess[] capturingChesses = new CapturingChess[ROW_SIZE];
    public final int CAPTURING_SIZE;

    public CapturingBoard(int width, int height, int player) {//player:0红1黑
        setLayout(null); // Use absolute layout.
        setSize(width, height);
        CAPTURING_SIZE = height / 7;
        SquareComponent.setSpacingLength(CAPTURING_SIZE / 15);
        initAllCapturingChessOnBoard(player);
    }
    public CapturingChess[] getChessComponents() {
        return capturingChesses;
    }

    public void putChessOnBoard(CapturingChess capturingChess) {
        int row = capturingChess.label;
        if (capturingChesses[row] != null) {
            remove(capturingChesses[row]);
        }
        add(capturingChesses[row] = capturingChess);
    }

    private class initType
    {
        public int chessType,player;//前者0-6:将士相车马兵炮;后者0-1:红黑
        public initType(int chessType,int player)
        {
            this.chessType=chessType;
            this.player=player;
        }
    }
    public void initAllCapturingChessOnBoard(int player) {//player:0红1黑
        initType[] components=new initType[ROW_SIZE];
        for(int i=0;i<=6;i++)//顺序添加
            components[i]=new initType(i,player);
        for(int i=0;i<=6;i++)
        {
            ChessColor color=components[i].player==0? ChessColor.RED : ChessColor.BLACK;//0红1黑
            CapturingChess capturingChess;
            capturingChess =new CapturingChess(i,calculatePoint(i),color, CAPTURING_SIZE) ;
            capturingChess.setVisible(true);
            putChessOnBoard(capturingChess);
        }
    }
    /**
     * 绘制棋盘格子
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private Point calculatePoint(int row) {
        return new Point(0, row * CAPTURING_SIZE);
    }
}