import view.Handler;

import javax.swing.*;
public class Main {

    public static void main(String[] args) {
        //String filepath ="关大洲-清平乐.wav";
        //MusicStuff musicObject = new MusicStuff();
        //musicObject.playMusic(filepath);
        SwingUtilities.invokeLater(() -> {Handler.mainFrame.setVisible(true);});
    }
}
//问题:人机贪心模式下翻棋的思路，棋子的移动做成多帧动画，存档&读档(如何检测错误？？？)
/**
 * 待实现：
 * 强制注册用户
 * 棋子的移动做成多帧动画
 * 存档&读档(如何检测错误？？？)
 * 阴间音效
 * 人机、联机时使用wait_cursor
 * 局域网对战(计时)
 * 把作弊模式的颜色换换(1.RGB 2.brighter())
 * 打包程序
 */