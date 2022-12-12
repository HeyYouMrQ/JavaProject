package view;

import chessComponent.*;
import computerPlayer.ComputerPlayer;
import controller.GameController;
import model.ChessColor;
import model.ChessboardPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

import static view.Chessboard.*;

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
    private final int WIDTH;
    private final int HEIGHT;
    public final int CHESSBOARD_SIZE;

    private GameController gameController;
    private static JLabel statusLabel;
    private static JLabel scoreOfBlack;
    private static JLabel scoreOfRed;
    public static Chessboard chessboard;
    public static CapturingBoard capturingBoardMe, capturingBoardHe;//capturingBoardMe:我捕获的棋子(所以与我的颜色相反！)
    public static int menuMode;//0人机1玩家对战
    public static ComputerPlayer computerPlayer;
    public static JPanel gamePanel=new JPanel(),menuPanel=new JPanel();//todo
    public static PanelComponent PVCButtonsPanel=new PanelComponent();
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

        loadMenuPanel();
        loadGamePanel();
        setContentPane(menuPanel);
    }
    private void loadMenuPanel()
    {
        menuPanel.setBackground(Color.RED);
        menuPanel.setLayout(null);
        menuPanel.setVisible(true);
        addMenuLabel();
        addPVCButton();
        addPVPButton();
        addMenuExitButton();
        addMenuMinimize();
    }
    private void addMenuLabel() {//todo
    /*    statusLabel = new JLabel("RED's TURN");
        statusLabel.setLocation(WIDTH * 3 / 5, HEIGHT / 20);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(statusLabel);
     */
    }
    public static void contendFirstInPVC()
    {
        String[] options={"是","否"};
        int choice=JOptionPane.showOptionDialog(JOptionPane.getRootFrame(),"你想要先手吗？","裁判问："
                ,JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
        if(choice==0)
            Chessboard.mePlayer=Chessboard.redPlayer;
        else
            Chessboard.mePlayer=Chessboard.blackPlayer;
    }
    private void addPVCButton() {
        JButton button = new JButton("人机对战");
        button.setLocation(WIDTH * 2 / 5 , HEIGHT *4/ 20);
        button.setSize(WIDTH/5, HEIGHT /12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        button.setBackground(Color.LIGHT_GRAY);
        menuPanel.add(button);

        button.addActionListener(e -> {
            menuMode =0;
            chessboard.initAllChessOnBoard();
            addCapturingBoard();
            ChessGameFrame.repaintAll();

            menuPanel.setEnabled(false);
            menuPanel.setVisible(false);
            PVCButtonsPanel.setEnabled(true);
            PVCButtonsPanel.setVisible(true);
            gamePanel.setEnabled(true);
            gamePanel.setVisible(true);
            gamePanel.add(PVCButtonsPanel);
            setContentPane(gamePanel);
            computerPlayer=new ComputerPlayer();
            computerPlayer.start();
        });
    }
    public static void contendFirstInPVP()
    {
        //todo
    }
    private void addPVPButton() {//todo
        JButton button = new JButton("玩家对战");
        button.setLocation(WIDTH * 2 / 5 , HEIGHT *8/ 20);
        button.setSize(WIDTH/5, HEIGHT /12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        button.setBackground(Color.LIGHT_GRAY);//todo
        menuPanel.add(button);

        button.addActionListener(e -> {
        //    setContentPane(gamePanel);
        });
    }
    private void addMenuExitButton() {
        JButton button = new JButton("退出");
        button.addActionListener((e) -> {
            String[] options={"是","取消"};
            int choice=JOptionPane.showOptionDialog(JOptionPane.getRootFrame(),"确定退出吗","裁判问："
                    ,JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[1]);
            if(choice==0){//菜单 todo
                System.exit(0);
            }
        });
        button.setLocation(WIDTH * 2 / 5, HEIGHT *12 / 20);
        button.setSize(WIDTH/5, HEIGHT /12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        menuPanel.add(button);
    }
    private void addMenuMinimize() {
        JButton button = new JButton("最小化窗口");
        button.addActionListener((e) -> {
            setExtendedState(JFrame.ICONIFIED);
        });
        button.setLocation(WIDTH * 2 / 5, HEIGHT *16 / 20);
        button.setSize(WIDTH/5, HEIGHT /12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        menuPanel.add(button);
    }
    private void loadGamePanel()
    {
        gamePanel.setBackground(Color.WHITE);
        gamePanel.setLayout(null);
        gamePanel.setVisible(true);
        addGameLabel();

        addGameEscapeButton();
        addGameMinimize();
        addChessboard();
        loadPVCButtons();
    }
    /**
     * 在游戏窗体中添加棋盘
     */
    private void addChessboard() {
        chessboard = new Chessboard(CHESSBOARD_SIZE/2,CHESSBOARD_SIZE);
        gameController = new GameController(chessboard);
        chessboard.setLocation(WIDTH / 5, HEIGHT / 5);
        gamePanel.add(chessboard);
    }
    private void addCapturingBoard()
    {//player:0红1黑
        capturingBoardHe = new CapturingBoard(CHESSBOARD_SIZE/8,CHESSBOARD_SIZE*7/8,
                mePlayer.getColor().equals(Color.RED)?0:1);
        capturingBoardMe = new CapturingBoard(CHESSBOARD_SIZE/8,CHESSBOARD_SIZE*7/8,
                mePlayer.getColor().equals(Color.RED)?1:0);

        capturingBoardHe.setLocation(WIDTH/5 - CHESSBOARD_SIZE/8, HEIGHT / 4);
        capturingBoardMe.setLocation(WIDTH/5 + CHESSBOARD_SIZE/2, HEIGHT / 4);//自己放在右边

        gamePanel.add(capturingBoardHe);
        gamePanel.add(capturingBoardMe);
    }
    /**
     * 在游戏窗体中添加标签
     */
    private void addGameLabel() {
        statusLabel = new JLabel("轮到红方了");
        statusLabel.setLocation(WIDTH / 5, HEIGHT / 40);
        statusLabel.setSize(WIDTH/6, HEIGHT /12);
        statusLabel.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(statusLabel);

        scoreOfBlack = new JLabel(String.format("黑方的分数是： %d", Chessboard.blackPlayer.getCurrentScore()));
        scoreOfBlack.setLocation(WIDTH / 5, HEIGHT *3/ 40);
        scoreOfBlack.setSize(WIDTH/6, HEIGHT /12);
        scoreOfBlack.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(scoreOfBlack);

        scoreOfRed = new JLabel(String.format("红方的分数是： %d", Chessboard.redPlayer.getCurrentScore()));
        scoreOfRed.setLocation(WIDTH / 5, HEIGHT *5/ 40);
        scoreOfRed.setSize(WIDTH/6, HEIGHT /12);
        scoreOfRed.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(scoreOfRed);
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

        loadGameLoadButton();
        loadCheatingModeButton();
        loadRestartButton();
        loadWithdrawButton();
        loadComputerPlayerButton();
        loadSetDifficultyButton();
    }
    private void loadGameLoadButton() {
        JButton button = new JButton("载入");
        button.setLocation(0, 0);
        button.setSize(WIDTH*11/60, HEIGHT/12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        button.setBackground(Color.LIGHT_GRAY);
        PVCButtonsPanel.add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            String path = JOptionPane.showInputDialog(this, "输入路径：");
            gameController.loadGameFromFile(path);
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
        PVCButtonsPanel.add(cheatingButton);
    }

    private void loadRestartButton() {
        JButton button = new JButton("重开");
        button.addActionListener((e) -> {
            String[] options={"重开","取消"};
            int choice=JOptionPane.showOptionDialog(JOptionPane.getRootFrame(),"确定重开吗？"
                    ,"裁判问：",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[1]);
            if(choice==0){
                ComputerPlayer.stop=true;
                chessboard.initAllChessOnBoard();//restart!
                ChessGameFrame.repaintAll();
                computerPlayer=new ComputerPlayer();
                computerPlayer.start();
            }
        });
        button.setLocation(0, HEIGHT *2/10 );
        button.setSize(WIDTH*11/60, HEIGHT/12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        PVCButtonsPanel.add(button);
    }

    public static JButton withdrawButton = new JButton("作弊模式：关");
    private void withdraw()
    {
        SquareComponent firChess = chessboard.getChessComponents()[firX.peek()][firY.peek()];
        if(ope.peek()==1)//翻棋子的
            firChess.setReversal(false);
        else if(ope.peek()==2)//走棋子的 0-6:将士相车马兵炮7:空
        {
            SquareComponent secChess = chessboard.getChessComponents()[secX.peek()][secY.peek()];
            if(firCom.peek()==0)
            firChess = new GeneralChessComponent(new ChessboardPoint(firX.peek(),firY.peek()), chessboard.calculatePoint(firX.peek(),firY.peek())
                    , firCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(firCom.peek()==1)
            firChess = new AdvisorChessComponent(new ChessboardPoint(firX.peek(),firY.peek()), chessboard.calculatePoint(firX.peek(),firY.peek())
                    , firCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(firCom.peek()==2)
            firChess = new MinisterChessComponent(new ChessboardPoint(firX.peek(),firY.peek()), chessboard.calculatePoint(firX.peek(),firY.peek())
                    , firCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(firCom.peek()==3)
            firChess = new ChariotChessComponent(new ChessboardPoint(firX.peek(),firY.peek()), chessboard.calculatePoint(firX.peek(),firY.peek())
                    , firCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(firCom.peek()==4)
            firChess = new HorseChessComponent(new ChessboardPoint(firX.peek(),firY.peek()), chessboard.calculatePoint(firX.peek(),firY.peek())
                    , firCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(firCom.peek()==5)
            firChess = new SoldierChessComponent(new ChessboardPoint(firX.peek(),firY.peek()), chessboard.calculatePoint(firX.peek(),firY.peek())
                    , firCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(firCom.peek()==6)
            firChess = new CannonChessComponent(new ChessboardPoint(firX.peek(),firY.peek()), chessboard.calculatePoint(firX.peek(),firY.peek())
                    , firCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            //else if(firCom.peek()==7) first 必无空棋子
            //firChess = new EmptySlotComponent(firChess.getChessboardPoint(), firChess.getLocation(), chessboard.clickController, chessboard.CHESS_SIZE);

            if(secCom.peek()==0)
                secChess = new GeneralChessComponent(new ChessboardPoint(secX.peek(),secY.peek()), chessboard.calculatePoint(secX.peek(),secY.peek())
                        , secCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(secCom.peek()==1)
                secChess = new AdvisorChessComponent(new ChessboardPoint(secX.peek(),secY.peek()), chessboard.calculatePoint(secX.peek(),secY.peek())
                        , secCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(secCom.peek()==2)
                secChess = new MinisterChessComponent(new ChessboardPoint(secX.peek(),secY.peek()), chessboard.calculatePoint(secX.peek(),secY.peek())
                        , secCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(secCom.peek()==3)
                secChess = new ChariotChessComponent(new ChessboardPoint(secX.peek(),secY.peek()), chessboard.calculatePoint(secX.peek(),secY.peek())
                        , secCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(secCom.peek()==4)
                secChess = new HorseChessComponent(new ChessboardPoint(secX.peek(),secY.peek()), chessboard.calculatePoint(secX.peek(),secY.peek())
                        , secCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(secCom.peek()==5)
                secChess = new SoldierChessComponent(new ChessboardPoint(secX.peek(),secY.peek()), chessboard.calculatePoint(secX.peek(),secY.peek())
                        , secCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(secCom.peek()==6)
                secChess = new CannonChessComponent(new ChessboardPoint(secX.peek(),secY.peek()), chessboard.calculatePoint(secX.peek(),secY.peek())
                        , secCol.peek()==0?ChessColor.RED:ChessColor.BLACK , chessboard.clickController, chessboard.CHESS_SIZE);
            else if(secCom.peek()==7)
                secChess = new EmptySlotComponent(secChess.getChessboardPoint(), secChess.getLocation(), chessboard.clickController, chessboard.CHESS_SIZE);

            chessboard.putChessOnBoard(firChess);
            chessboard.putChessOnBoard(secChess);

            firChess.setReversal(true);
            if(firCannonSecRev.peek()==1)
                secChess.setReversal(false);
            else
                secChess.setReversal(true);

            if(secCol.peek()==0)
                Chessboard.blackPlayer.setCurrentScore(Chessboard.blackPlayer.getCurrentScore()-secChess.score);
            else
                Chessboard.redPlayer.setCurrentScore(Chessboard.redPlayer.getCurrentScore()-secChess.score);
            ChessGameFrame.getScoreOfBlack().setText(String.format("BLACK's points: %d", Chessboard.blackPlayer.getCurrentScore()));
            ChessGameFrame.getScoreOfRed().setText(String.format("RED's points: %d", Chessboard.redPlayer.getCurrentScore()));

            if(capturingLabel.peek()!=7)
            {
                boolean meIsEaten=(capturingIsMe.peek()==1);
                CapturingBoard change=meIsEaten?capturingBoardHe:capturingBoardMe;
                change.capturingChesses[capturingLabel.peek()].num--;
            }
        }
        Chessboard.setCurrentColor(Chessboard.getCurrentColor().equals(ChessColor.RED)?ChessColor.BLACK:ChessColor.RED);
        ChessGameFrame.getStatusLabel().setText(String.format("轮到%s方了", Chessboard.getCurrentColor().getName()));
        ope.pop();
        firCom.pop();   firCol.pop();   firX.pop(); firY.pop();
        secCom.pop();   secCol.pop();   secX.pop(); secY.pop(); firCannonSecRev.pop();
        capturingIsMe.pop();    capturingLabel.pop();
        repaintAll();
        if(ope.empty())
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
        PVCButtonsPanel.add(withdrawButton);
    }
    private void loadComputerPlayerButton()//是否开启
    {
        JButton button = new JButton("开");
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
        PVCButtonsPanel.add(button);
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
        PVCButtonsPanel.add(button);
    }
    private void addGameEscapeButton() {
        JButton button = new JButton("逃跑");
        button.addActionListener((e) -> {
            String[] options={"是","取消"};
            int choice=JOptionPane.showOptionDialog(JOptionPane.getRootFrame(),"确定要逃离这局游戏吗？","裁判问："
                    ,JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[1]);
            if(choice==0){//菜单
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
        gamePanel.add(button);
    }
    private void addGameMinimize() {
        JButton button = new JButton("最小化窗口");
        button.addActionListener((e) -> {
            setExtendedState(JFrame.ICONIFIED);
        });
        button.setLocation(WIDTH * 4 / 5, HEIGHT *15 / 20);
        button.setSize(WIDTH*11/60, HEIGHT/12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(button);
    }
    public static void repaintAll()
    {
        capturingBoardHe.repaint();
        capturingBoardMe.repaint();
        withdrawButton.repaint();
        chessboard.repaint();
        statusLabel.repaint();
        scoreOfBlack.repaint();
        scoreOfRed.repaint();
        cheatingButton.repaint();
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