package view;

import model.ChessColor;

import javax.swing.*;
import java.awt.*;
public class CapturingChess extends JComponent {
    private static final Color squareColor = new Color(250, 220, 190);
    protected static final Font CHESS_FONT = new Font("宋体", Font.BOLD, 36);
    protected static final Font NUM_FONT = new Font("宋体", Font.BOLD, 20);
    private static int spacingLength;
    private ChessColor chessColor;
    public int label;//0-6:将士相车马兵炮 7:空
    public int num=0;//个数
    public CapturingChess(int label, Point location, ChessColor chessColor, int size)
    {
        this.label=label;
        setLocation(location);
        setSize(size, size);
        this.chessColor = chessColor;
        this.label=label;
    }
    public ChessColor getChessColor() {
        return chessColor;
    }

    @Override
    protected void paintComponent(Graphics g) {//0-6:将士相车马兵炮
        g.setColor(Color.LIGHT_GRAY);
        boolean numIsZero=(num==0);
        String name="";
        if(chessColor.equals(ChessColor.RED))
        {
            if(label==0)    name="帥";
            else if(label==1)   name="仕";
            else if(label==2)   name="相";
            else if(label==3)   name="俥";
            else if(label==4)   name="傌";
            else if(label==5)   name="兵";
            else if(label==6)   name="炮";
        }
        else if(chessColor.equals(ChessColor.BLACK))
        {
            if(label==0)    name="將";
            else if(label==1)   name="士";
            else if(label==2)   name="象";
            else if(label==3)   name="車";
            else if(label==4)   name="馬";
            else if(label==5)   name="卒";
            else if(label==6)   name="砲";
        }
        spacingLength=3;//todo
        super.paintComponent(g);
        if(!numIsZero)
            g.setColor(Color.CYAN);//圆形实心棋子
        g.fillOval(spacingLength, spacingLength, this.getWidth() - 2 * spacingLength, this.getHeight() - 2 * spacingLength);
        if(!numIsZero)
            g.setColor(Color.DARK_GRAY);//圆形空心边框
        g.drawOval(spacingLength, spacingLength, this.getWidth() - 2 * spacingLength,this.getHeight() - 2 * spacingLength);
        if(!numIsZero)
            g.setColor(this.getChessColor().getColor());//字
        else
            g.setColor(Color.GRAY);
        g.setFont(CHESS_FONT);
        g.drawString(name, this.getWidth() / 4, this.getHeight() * 2 / 3);
        g.setColor(Color.LIGHT_GRAY);

        if(!numIsZero)
            g.setColor(Color.WHITE);//实心小圈
        g.fillOval(this.getWidth()/12, this.getWidth()/12, this.getWidth()/4, this.getHeight()/4);
        if(!numIsZero)
            g.setColor(Color.DARK_GRAY);//空心小圆
        g.drawOval(this.getWidth()/12, this.getWidth()/12, this.getWidth()/4, this.getHeight()/4);
        if(!numIsZero)
            g.setColor(Color.RED);//字
        else
            g.setColor(Color.GRAY);
        g.setFont(NUM_FONT);
        g.drawString(String.format("%d",num), this.getWidth() / 7, this.getHeight()*7/24 );
    }
}