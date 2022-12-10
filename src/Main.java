import view.ChessGameFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        ChessGameFrame mainFrame = new ChessGameFrame(720,720);
        SwingUtilities.invokeLater(() -> {mainFrame.setVisible(true);});
    }
}
//
/**
 * 待实现：
 * 展示捕获的棋子
 * 棋子的移动做成多帧动画
 * 存档&读档(如何检测错误？？？)
 * 阴间音效
 * 人机对战
 * 人机、联机时使用wait_cursor
 * 局域网对战
 * 打包程序
 */