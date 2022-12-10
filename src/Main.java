import view.Handler;
import javax.swing.*;
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {Handler.mainFrame.setVisible(true);});
    }
}
//
/**
 * 待实现：
 * 人机对战 (多线程 阻塞 计时)
 * 强制注册用户
 * 棋子的移动做成多帧动画
 * 存档&读档(如何检测错误？？？)
 * 阴间音效
 * 人机、联机时使用wait_cursor
 * 局域网对战
 * 把作弊模式的颜色换换(1.RGB 2.brighter())
 * 打包程序
 */