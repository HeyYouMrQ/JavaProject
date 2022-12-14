package player;

import java.awt.*;

public class Players {
    private  int currentScore=0;
    private  int totalScore;//积分榜
    private  Color color;
    public Players(Color color) {
        this.color = color;
    }
    public Players() {this.color=Color.RED;}//默认
    public Color getColor() {
        return color;
    }
    public void setColor(Color color)
    {
        this.color=color;
    }
    public int getCurrentScore() {return currentScore;}
    public void setCurrentScore(int sC) {currentScore = sC;}

    public int getTotalScore() {return totalScore;}
    public void setTotalScore(int tS) {totalScore = tS;}
}