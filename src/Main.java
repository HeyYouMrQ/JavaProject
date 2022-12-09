import view.ChessGameFrame;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        ChessGameFrame mainFrame = new ChessGameFrame(720,720);
        SwingUtilities.invokeLater(() -> {mainFrame.setVisible(true);});
    }
}
//半成品：cheating mode
/**
 * 待实现：
 * cheatingMode
 * 展示捕获的棋子
 * 存档&读档
 * 打包程序
 */