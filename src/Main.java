import view.ChessGameFrame;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        ChessGameFrame mainFrame = new ChessGameFrame(720,720);
        SwingUtilities.invokeLater(() -> {mainFrame.setVisible(true);});
    }
}//bug：无法真正重开（it's xxx's turn 在重开后不对，cheating mode按钮也没变回去）
//半成品：cheating mode
/**
 * 待实现：
 * 悔棋（栈多步？）
 * 存档&读档
 * 打包程序
 */