import media.MusicStuff;
import view.Handler;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        String filepath = "./resource/关大洲-清平乐.wav";
        MusicStuff musicObject = new MusicStuff();
        musicObject.playMusic(filepath);
        SwingUtilities.invokeLater(() -> {
            Handler.mainFrame.initChessGameFrame();
            Handler.mainFrame.setVisible(true);
        });
    }
}
//问题:存档&读档(如何检测错误？？？) ，网络
/**
 * 未吃掉的棋子为灰色
 * 棋子的移动做成多帧动画
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