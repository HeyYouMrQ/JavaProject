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
//问题:网络
/**
 *yrger
 * 待实现：
 * 对话框、背景美化
 * 字体美化：隶书
 * 修复一下stop标签
 * 新增交换颜色功能
 * 联机时需要输入昵称，注册用户
 * 阴间音效
 * 人机、联机时使用wait_cursor
 * 局域网对战(计时)
 * 把作弊模式的颜色换换(1.RGB 2.brighter())
 * 打包程序
 */