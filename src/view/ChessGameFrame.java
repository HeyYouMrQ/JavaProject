package view;

import chessComponent.*;
import computerPlayer.ComputerPlayer;
import controller.GameController;
import media.ImageComponent;
import media.MyDialog;
import model.ChessColor;
import model.ChessboardPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

import static view.Chessboard.isCheatingMode;
import static media.BufferedPictures.*;

/**
 * 这个类表示游戏窗体，窗体上包含：
 * 1 Chessboard: 棋盘
 * 2 JLabel:  标签
 * 3 JButton： 按钮
 */
public class ChessGameFrame extends JFrame {
    @Override
    public void setExtendedState(int state) {
        if(state==JFrame.ICONIFIED)
            super.setExtendedState(JFrame.ICONIFIED);
        else if(state==JFrame.MAXIMIZED_BOTH)
            super.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    public final int WIDTH;
    public final int HEIGHT;
    public final int CHESSBOARD_SIZE;

    private GameController gameController;
    private static JLabel statusLabel;
    private static JLabel scoreOfBlack;
    private static JLabel scoreOfRed;
    public static Chessboard chessboard;
    //capturingBoardMe:我捕获的棋子(所以与我的颜色相反！)
    public static int menuMode;//0人机1玩家对战
    public static ComputerPlayer computerPlayer;
    public static JLayeredPane gamePanel=new JLayeredPane(),menuPanel=new JLayeredPane();//新建一个分层器
    public static JLayeredPane PVCButtonsPanel=new JLayeredPane();
    @Override
    public Container getContentPane() {
        return super.getContentPane();
    }
    public static JLabel getScoreOfBlack() {
        return scoreOfBlack;
    }
    public static JLabel getScoreOfRed() {
        return scoreOfRed;
    }
    public ChessGameFrame(int width,int height) {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);//不要工具栏
        this.setResizable(false);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        setTitle("暗棋"); //设置标题
        //this.WIDTH=width;
        //this.HEIGHT=height;
        this.WIDTH=Toolkit.getDefaultToolkit().getScreenSize().width;//直接设为最大尺寸
        this.HEIGHT=Toolkit.getDefaultToolkit().getScreenSize().height;
        this.CHESSBOARD_SIZE=HEIGHT*4/5;
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
    }
    public void initChessGameFrame()
    {
        loadMenuPanel();
        loadGamePanel();
        setContentPane(menuPanel);
    }
    private void loadMenuPanel()
    {
        menuPanel.setLayout(null);
        menuPanel.setVisible(true);

        addMenuLabel();
        addPVCButton();
        addPVPButton();
        addMenuExitButton();
        addMenuMinimize();
        addMenuPicture();
    }
    private void addMenuPicture()
    {
        JComponent imageComponent = new ImageComponent(MENU);// create an instance of ImageComponent
        imageComponent.setSize(WIDTH,HEIGHT);
        imageComponent.setLocation(0, 0); // set absolute location
        menuPanel.add(imageComponent,JLayeredPane.FRAME_CONTENT_LAYER);
    }
    private void addMenuLabel() {//todo
    /*    statusLabel = new JLabel("RED's TURN");
        statusLabel.setLocation(WIDTH * 3 / 5, HEIGHT / 20);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(statusLabel);
     */
    }
    public static void contendFirstInPVC(int mode)////0重开1载入
    {
        String[] options={"是","否"};
        int choice=MyDialog.confirmDialog("裁判问：",mode==0?"你想要先手吗？":"你想要红方吗？","是","否");
        if(choice==1)
            chessboard.mePlayer=chessboard.redPlayer;
        else
            chessboard.mePlayer=chessboard.blackPlayer;
    }
    private void addPVCButton() {
        JButton button = new JButton();
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        Image image =Toolkit.getDefaultToolkit().getImage("./resource/pvc.png").getScaledInstance(WIDTH/5,HEIGHT/12,Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(image));
        button.setLocation(WIDTH * 2 / 5 , HEIGHT *4/ 20);
        button.setSize(WIDTH/5, HEIGHT /12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        //button.setBackground(Color.LIGHT_GRAY);
        button.repaint();
        menuPanel.add(button,JLayeredPane.MODAL_LAYER);

        button.addActionListener(e -> {
            menuMode =0;
            chessboard.initAllChessOnBoard(0);
            addCapturingBoard();
            ChessGameFrame.repaintAll();

            menuPanel.setEnabled(false);
            menuPanel.setVisible(false);
            PVCButtonsPanel.setEnabled(true);
            PVCButtonsPanel.setVisible(true);
            gamePanel.add(PVCButtonsPanel,JLayeredPane.MODAL_LAYER);
            gamePanel.setEnabled(true);
            gamePanel.setVisible(true);
            setContentPane(gamePanel);
        });
    }
    public static void contendFirstInPVP()
    {
        //todo
    }
    private void addPVPButton() {//todo
        JButton button = new JButton();
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        Image image =Toolkit.getDefaultToolkit().getImage("./resource/pvp.png").getScaledInstance(WIDTH/5,HEIGHT/12,Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(image));
        button.setLocation(WIDTH * 2 / 5 , HEIGHT *8/ 20);
        button.setSize(WIDTH/5, HEIGHT /12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        button.setBackground(Color.LIGHT_GRAY);//todo
        menuPanel.add(button,JLayeredPane.MODAL_LAYER);

        button.addActionListener(e -> {
        //    setContentPane(gamePanel);
        });
    }
    private void addMenuExitButton() {
        JButton button = new JButton();
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        Image image =Toolkit.getDefaultToolkit().getImage("./resource/exit.png").getScaledInstance(WIDTH/5,HEIGHT/12,Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(image));
        button.addActionListener((e) -> {
            int choice=MyDialog.confirmDialog("裁判问：","确定退出吗？","是","取消");
            if(choice==1){
                System.exit(0);
            }
        });
        button.setLocation(WIDTH * 2 / 5, HEIGHT *12 / 20);
        button.setSize(WIDTH/5, HEIGHT /12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        menuPanel.add(button,JLayeredPane.MODAL_LAYER);
    }
    private void addMenuMinimize() {
        JButton button = new JButton();
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        Image image =Toolkit.getDefaultToolkit().getImage("./resource/min.png").getScaledInstance(WIDTH/5,HEIGHT/12,Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(image));
        button.addActionListener((e) -> {
            setExtendedState(JFrame.ICONIFIED);
        });
        button.setLocation(WIDTH * 2 / 5, HEIGHT *16 / 20);
        button.setSize(WIDTH/5, HEIGHT /12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        menuPanel.add(button,JLayeredPane.MODAL_LAYER);
    }
    private void loadGamePanel()
    {
        addChessboard();
        gamePanel.setBackground(Color.WHITE);
        gamePanel.setLayout(null);
        gamePanel.setVisible(true);
        addGameLabel();

        addGameEscapeButton();
        addGameMinimize();
        loadPVCButtons();
        addGameBackground();
    }
    private void addGameBackground()
    {
        JComponent imageComponent = new ImageComponent(MENU);// create an instance of ImageComponent
        imageComponent.setSize(WIDTH,HEIGHT);
        imageComponent.setLocation(0, 0); // set absolute location
        gamePanel.add(imageComponent,JLayeredPane.FRAME_CONTENT_LAYER);
    }
    /**
     * 在游戏窗体中添加棋盘
     */
    private void addChessboard() {
        chessboard = new Chessboard(CHESSBOARD_SIZE/2,CHESSBOARD_SIZE);
        gameController = new GameController();
        chessboard.setLocation(WIDTH / 5, HEIGHT / 5);
        gamePanel.add(chessboard);
    }
    private void addCapturingBoard()
    {//player:0红1黑
        chessboard.capturingBoardHe = new CapturingBoard(CHESSBOARD_SIZE/8,CHESSBOARD_SIZE*7/8,
                chessboard.mePlayer.getColor().equals(Color.RED)?0:1);
        chessboard.capturingBoardMe = new CapturingBoard(CHESSBOARD_SIZE/8,CHESSBOARD_SIZE*7/8,
                chessboard.mePlayer.getColor().equals(Color.RED)?1:0);

        chessboard.capturingBoardHe.setLocation(WIDTH/5 - CHESSBOARD_SIZE/8, HEIGHT / 4);
        chessboard.capturingBoardMe.setLocation(WIDTH/5 + CHESSBOARD_SIZE/2, HEIGHT / 4);//自己放在右边

        gamePanel.add(chessboard.capturingBoardHe);
        gamePanel.add(chessboard.capturingBoardMe);
    }
    /**
     * 在游戏窗体中添加标签
     */
    private void addGameLabel() {
        statusLabel = new JLabel("轮到红方了");
        statusLabel.setLocation(WIDTH / 5, HEIGHT / 40);
        statusLabel.setSize(WIDTH/6, HEIGHT /12);
        statusLabel.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(statusLabel,JLayeredPane.MODAL_LAYER);

        scoreOfBlack = new JLabel(String.format("黑方的分数是： %d", chessboard.blackPlayer.getCurrentScore()));
        scoreOfBlack.setLocation(WIDTH / 5, HEIGHT *3/ 40);
        scoreOfBlack.setSize(WIDTH/6, HEIGHT /12);
        scoreOfBlack.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(scoreOfBlack,JLayeredPane.MODAL_LAYER);

        scoreOfRed = new JLabel(String.format("红方的分数是： %d", chessboard.redPlayer.getCurrentScore()));
        scoreOfRed.setLocation(WIDTH / 5, HEIGHT *5/ 40);
        scoreOfRed.setSize(WIDTH/6, HEIGHT /12);
        scoreOfRed.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(scoreOfRed,JLayeredPane.MODAL_LAYER);
    }

    public static JLabel getStatusLabel() {
        return statusLabel;
    }

    public void loadPVCButtons()
    {
        PVCButtonsPanel.setBackground(Color.WHITE);
        PVCButtonsPanel.setLayout(null);
        PVCButtonsPanel.setSize(WIDTH/5,HEIGHT/2);
        PVCButtonsPanel.setLocation(WIDTH * 4 / 5, HEIGHT *2/ 20);

        loadInButton();
        loadOutButton();
        loadCheatingModeButton();
        loadRestartButton();
        loadWithdrawButton();
        loadComputerPlayerButton();
        loadSetDifficultyButton();
    }
    private void loadInButton() {
        JButton button = new JButton("载入");
        button.setLocation(0, 0);
        button.setSize(WIDTH /12, HEIGHT/12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        button.setBackground(Color.LIGHT_GRAY);
        PVCButtonsPanel.add(button,JLayeredPane.MODAL_LAYER);

        button.addActionListener(e -> {
            gameController.loadGameFromFile();
        });
    }
    private void loadOutButton() {
        JButton button = new JButton("存档");
        button.setLocation(WIDTH/10, 0);
        button.setSize(WIDTH/12, HEIGHT/12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        button.setBackground(Color.LIGHT_GRAY);
        PVCButtonsPanel.add(button,JLayeredPane.MODAL_LAYER);

        button.addActionListener(e -> {
            gameController.saveGameToFile();
        });
    }

    public static JButton cheatingButton = new JButton("作弊模式：关");
    private void loadCheatingModeButton() {
        cheatingButton.addActionListener((e) -> {
            if(cheatingButton.getText().equals("作弊模式：关")) {
                cheatingButton.setText("作弊模式：开");
                cheatingButton.repaint();
                isCheatingMode=true;
            }
            else {
                cheatingButton.setText("作弊模式：关");
                cheatingButton.repaint();
                isCheatingMode=false;
            }
        });
        cheatingButton.setLocation(0, HEIGHT /10);
        cheatingButton.setSize(WIDTH*11/60, HEIGHT/12);
        cheatingButton.setFont(new Font("宋体", Font.BOLD, 20));
        PVCButtonsPanel.add(cheatingButton,JLayeredPane.MODAL_LAYER);
    }

    private void loadRestartButton() {
        JButton button = new JButton("重开");
        button.addActionListener((e) -> {
            int choice=MyDialog.confirmDialog("裁判问：","确定重开吗？","重开","取消");
            if(choice==1){
                ComputerPlayer.stop=true;
                chessboard.initAllChessOnBoard(0);//restart!
                ChessGameFrame.repaintAll();
                computerPlayer=new ComputerPlayer();
                computerPlayer.start();
            }
        });
        button.setLocation(0, HEIGHT *2/10 );
        button.setSize(WIDTH*11/60, HEIGHT/12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        PVCButtonsPanel.add(button,JLayeredPane.MODAL_LAYER);
    }

    public static JButton withdrawButton = new JButton("作弊模式：关");
    private void withdraw()
    {
        SquareComponent firChess = chessboard.getChessComponents()[chessboard.firX.peek()][chessboard.firY.peek()];
        if(chessboard.ope.peek()==1)//翻棋子的
            firChess.setReversal(false);
        else if(chessboard.ope.peek()==2)//走棋子的 0-6:将士相车马兵炮7:空
        {
            SquareComponent secChess = chessboard.getChessComponents()[chessboard.secX.peek()][chessboard.secY.peek()];
            if(chessboard.firCom.peek()==0)
            firChess = new GeneralChessComponent(new ChessboardPoint(chessboard.firX.peek(),chessboard.firY.peek()), chessboard.calculatePoint(chessboard.firX.peek(),chessboard.firY.peek())
                    , chessboard.firCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(chessboard.firCom.peek()==1)
            firChess = new AdvisorChessComponent(new ChessboardPoint(chessboard.firX.peek(),chessboard.firY.peek()), chessboard.calculatePoint(chessboard.firX.peek(),chessboard.firY.peek())
                    , chessboard.firCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(chessboard.firCom.peek()==2)
            firChess = new MinisterChessComponent(new ChessboardPoint(chessboard.firX.peek(),chessboard.firY.peek()), chessboard.calculatePoint(chessboard.firX.peek(),chessboard.firY.peek())
                    , chessboard.firCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(chessboard.firCom.peek()==3)
            firChess = new ChariotChessComponent(new ChessboardPoint(chessboard.firX.peek(),chessboard.firY.peek()), chessboard.calculatePoint(chessboard.firX.peek(),chessboard.firY.peek())
                    , chessboard.firCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(chessboard.firCom.peek()==4)
            firChess = new HorseChessComponent(new ChessboardPoint(chessboard.firX.peek(),chessboard.firY.peek()), chessboard.calculatePoint(chessboard.firX.peek(),chessboard.firY.peek())
                    , chessboard.firCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(chessboard.firCom.peek()==5)
            firChess = new SoldierChessComponent(new ChessboardPoint(chessboard.firX.peek(),chessboard.firY.peek()), chessboard.calculatePoint(chessboard.firX.peek(),chessboard.firY.peek())
                    , chessboard.firCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(chessboard.firCom.peek()==6)
            firChess = new CannonChessComponent(new ChessboardPoint(chessboard.firX.peek(),chessboard.firY.peek()), chessboard.calculatePoint(chessboard.firX.peek(),chessboard.firY.peek())
                    , chessboard.firCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            //else if(firCom.peek()==7) first 必无空棋子
            //firChess = new EmptySlotComponent(firChess.getChessboardPoint(), firChess.getLocation(), chessboard.clickController, chessboard.CHESS_SIZE);

            if(chessboard.secCom.peek()==0)
                secChess = new GeneralChessComponent(new ChessboardPoint(chessboard.secX.peek(),chessboard.secY.peek()), chessboard.calculatePoint(chessboard.secX.peek(),chessboard.secY.peek())
                        , chessboard.secCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(chessboard.secCom.peek()==1)
                secChess = new AdvisorChessComponent(new ChessboardPoint(chessboard.secX.peek(),chessboard.secY.peek()), chessboard.calculatePoint(chessboard.secX.peek(),chessboard.secY.peek())
                        , chessboard.secCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(chessboard.secCom.peek()==2)
                secChess = new MinisterChessComponent(new ChessboardPoint(chessboard.secX.peek(),chessboard.secY.peek()), chessboard.calculatePoint(chessboard.secX.peek(),chessboard.secY.peek())
                        , chessboard.secCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(chessboard.secCom.peek()==3)
                secChess = new ChariotChessComponent(new ChessboardPoint(chessboard.secX.peek(),chessboard.secY.peek()), chessboard.calculatePoint(chessboard.secX.peek(),chessboard.secY.peek())
                        , chessboard.secCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(chessboard.secCom.peek()==4)
                secChess = new HorseChessComponent(new ChessboardPoint(chessboard.secX.peek(),chessboard.secY.peek()), chessboard.calculatePoint(chessboard.secX.peek(),chessboard.secY.peek())
                        , chessboard.secCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(chessboard.secCom.peek()==5)
                secChess = new SoldierChessComponent(new ChessboardPoint(chessboard.secX.peek(),chessboard.secY.peek()), chessboard.calculatePoint(chessboard.secX.peek(),chessboard.secY.peek())
                        , chessboard.secCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(chessboard.secCom.peek()==6)
                secChess = new CannonChessComponent(new ChessboardPoint(chessboard.secX.peek(),chessboard.secY.peek()), chessboard.calculatePoint(chessboard.secX.peek(),chessboard.secY.peek())
                        , chessboard.secCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(chessboard.secCom.peek()==7)
                secChess = new EmptySlotComponent(secChess.getChessboardPoint(), secChess.getLocation(), chessboard.clickController, chessboard.CHESS_SIZE);

            chessboard.putChessOnBoard(firChess);
            chessboard.putChessOnBoard(secChess);

            firChess.setReversal(true);
            if(chessboard.firCannonSecRev.peek()==1)
                secChess.setReversal(false);
            else
                secChess.setReversal(true);

            if(chessboard.secCol.peek()==0)
                chessboard.blackPlayer.setCurrentScore(chessboard.blackPlayer.getCurrentScore()-secChess.score);
            else
                chessboard.redPlayer.setCurrentScore(chessboard.redPlayer.getCurrentScore()-secChess.score);
            ChessGameFrame.getScoreOfBlack().setText(String.format("BLACK's points: %d", chessboard.blackPlayer.getCurrentScore()));
            ChessGameFrame.getScoreOfRed().setText(String.format("RED's points: %d", chessboard.redPlayer.getCurrentScore()));

            if(chessboard.capturingLabel.peek()!=7)
            {
                boolean meIsEaten=(chessboard.capturingIsMe.peek()==1);
                CapturingBoard change=meIsEaten?chessboard.capturingBoardHe:chessboard.capturingBoardMe;
                change.capturingChesses[chessboard.capturingLabel.peek()].num--;
            }
        }
        chessboard.setCurrentColor(chessboard.getCurrentColor().equals(ChessColor.RED)?ChessColor.BLACK:ChessColor.RED);
        ChessGameFrame.getStatusLabel().setText(String.format("轮到%s方了", chessboard.getCurrentColor().getName()));
        chessboard.ope.pop();
        chessboard.firCom.pop();   chessboard.firCol.pop();   chessboard.firX.pop(); chessboard.firY.pop();
        chessboard.secCom.pop();   chessboard.secCol.pop();   chessboard.secX.pop(); chessboard.secY.pop(); chessboard.firCannonSecRev.pop();
        chessboard.capturingIsMe.pop();    chessboard.capturingLabel.pop();
        repaintAll();
        if(chessboard.ope.empty())
            withdrawButton.setEnabled(false);
    }
    private void loadWithdrawButton() {
        withdrawButton.setText("悔棋");
        withdrawButton.setEnabled(false);
        withdrawButton.addActionListener((e) -> {
            withdraw();
        });
        withdrawButton.setLocation(0, HEIGHT *3 / 10);
        withdrawButton.setSize(WIDTH*11/60, HEIGHT/12);
        withdrawButton.setFont(new Font("宋体", Font.BOLD, 20));
        PVCButtonsPanel.add(withdrawButton,JLayeredPane.MODAL_LAYER);
    }
    private void loadComputerPlayerButton()//是否开启
    {
        JButton button = new JButton("关");
        button.addActionListener((e) -> {
            if(button.getText().equals("开")) {
                button.setText("关");
                button.repaint();
                ComputerPlayer.stop=true;
            }
            else {
                button.setText("开");
                button.repaint();
                ComputerPlayer.stop=false;
                computerPlayer=new ComputerPlayer();
                computerPlayer.start();
            }
        });
        button.setLocation(0, HEIGHT *4/10);
        button.setSize(WIDTH /12, HEIGHT/12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        PVCButtonsPanel.add(button,JLayeredPane.MODAL_LAYER);
    }
    private void loadSetDifficultyButton()
    {
        JButton button = new JButton("简单");
        button.addActionListener((e) -> {
            if(button.getText().equals("简单")) {
                button.setText("困难");
                button.repaint();
                ComputerPlayer.setDifficultyMode(1);
            }
            else {
                button.setText("简单");
                button.repaint();
                ComputerPlayer.setDifficultyMode(0);
            }
        });
        button.setLocation(WIDTH/10, HEIGHT *4/10);
        button.setSize(WIDTH/12, HEIGHT/12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        PVCButtonsPanel.add(button,JLayeredPane.MODAL_LAYER);
    }
    private void addGameEscapeButton() {
        JButton button = new JButton("逃跑");
        button.addActionListener((e) -> {
            int choice=MyDialog.confirmDialog("裁判问：","确定要逃离这局游戏吗？","是","取消");
            if(choice==1){//菜单
                gamePanel.setEnabled(false);
                gamePanel.setVisible(false);
                PVCButtonsPanel.setEnabled(false);
                PVCButtonsPanel.setVisible(false);
                menuPanel.setEnabled(true);
                menuPanel.setVisible(true);
                gamePanel.remove(PVCButtonsPanel);
                setContentPane(ChessGameFrame.menuPanel);
            }
        });
        button.setLocation(WIDTH * 4 / 5, HEIGHT *13 / 20);
        button.setSize(WIDTH*11/60, HEIGHT/12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(button,JLayeredPane.MODAL_LAYER);
    }
    private void addGameMinimize() {
        JButton button = new JButton("最小化窗口");
        button.addActionListener((e) -> {
            setExtendedState(JFrame.ICONIFIED);
        });
        button.setLocation(WIDTH * 4 / 5, HEIGHT *15 / 20);
        button.setSize(WIDTH*11/60, HEIGHT/12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(button,JLayeredPane.MODAL_LAYER);
    }
    public static void repaintAll()
    {
        chessboard.capturingBoardHe.repaint();
        chessboard.capturingBoardMe.repaint();
        withdrawButton.repaint();
        chessboard.repaint();
        statusLabel.repaint();
        scoreOfBlack.repaint();
        scoreOfRed.repaint();
        cheatingButton.repaint();
        getStatusLabel().setText(String.format("轮到%s方了", chessboard.getCurrentColor().getName()));
        getScoreOfBlack().setText(String.format("黑方的得分是： %d", chessboard.blackPlayer.getCurrentScore()));
        getScoreOfRed().setText(String.format("红方的得分是 %d", chessboard.redPlayer.getCurrentScore()));
    }
    @Override
    protected void processWindowEvent(WindowEvent e)
    {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_ICONIFIED) {setExtendedState(JFrame.ICONIFIED);}
        if (e.getID() == WindowEvent.WINDOW_DEICONIFIED) {setExtendedState(JFrame.MAXIMIZED_BOTH);}
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {System.exit(0);}
    }
}