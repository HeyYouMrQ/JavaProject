package view;

import chessComponent.*;
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

    public static final int shiftConst1=20;
    public static JPanel gamePanel=new JPanel(),menuPanel=new JPanel();//todo
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

        addMenuLabel();
        addPVCButton();
        addPVPButton();
        addMenuExitButton();
        addMenuMinimize();
    }
    private void addMenuLabel() {//todo
    /*    statusLabel = new JLabel("RED's TURN");
        statusLabel.setLocation(WIDTH * 3 / 5 + shiftConst1, HEIGHT / 20);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(statusLabel);
     */
    }
    public static void contendFirstInPVC()
    {
        String[] options={"Yes","No"};
        int choice=JOptionPane.showOptionDialog(JOptionPane.getRootFrame(),"Do you want to start first?","From Judge"
                ,JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[0]);
        if(choice==0)
            Chessboard.mePlayer=Chessboard.redPlayer;
        else
            Chessboard.mePlayer=Chessboard.blackPlayer;
    }
    private void addPVCButton() {
        JButton button = new JButton("Player vs Computer");
        button.setLocation(WIDTH * 2 / 5 , HEIGHT *4/ 20);
        button.setSize(220, 60);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        button.setBackground(Color.LIGHT_GRAY);//todo
        menuPanel.add(button);

        button.addActionListener(e -> {
            Chessboard.mode=0;
            chessboard.initAllChessOnBoard();
            addCapturingBoard();
            ChessGameFrame.repaintAll();
            menuPanel.setEnabled(false);
            menuPanel.setVisible(false);
            setContentPane(gamePanel);
            gamePanel.setEnabled(true);
            gamePanel.setVisible(true);
        });
    }
    public static void contendFirstInPVP()
    {
        //todo
    }
    private void addPVPButton() {//todo
        JButton button = new JButton("Player vs Player");
        button.setLocation(WIDTH * 2 / 5 , HEIGHT *8/ 20);
        button.setSize(220, 60);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        button.setBackground(Color.LIGHT_GRAY);//todo
        menuPanel.add(button);

        button.addActionListener(e -> {
        //    setContentPane(gamePanel);
        });
    }
    private void addMenuExitButton() {
        JButton button = new JButton("EXIT");
        button.addActionListener((e) -> {
            String[] options={"Yes","Cancel"};
            int choice=JOptionPane.showOptionDialog(JOptionPane.getRootFrame(),"Confirm to exit?","From Judge"
                    ,JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[1]);
            if(choice==0){//菜单 todo
                System.exit(0);
            }
        });
        button.setLocation(WIDTH * 2 / 5, HEIGHT *12 / 20);
        button.setSize(220, 60);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        menuPanel.add(button);
    }
    private void addMenuMinimize() {
        JButton button = new JButton("MINIMIZE");
        button.addActionListener((e) -> {
            setExtendedState(JFrame.ICONIFIED);
        });
        button.setLocation(WIDTH * 2 / 5, HEIGHT *16 / 20);
        button.setSize(220, 60);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        menuPanel.add(button);
    }
    private void loadGamePanel()
    {
        gamePanel.setBackground(Color.WHITE);
        gamePanel.setLayout(null);
        addGameLabel();
        addGameLoadButton();
        addCheatingModeButton();
        addRestartButton();
        addWithdrawButton();
        addGameEscapeButton();
        addGameMinimize();
        addChessboard();
    }
    /**
     * 在游戏窗体中添加棋盘
     */
    private void addChessboard() {
        chessboard = new Chessboard(CHESSBOARD_SIZE/2,CHESSBOARD_SIZE);
        gameController = new GameController(chessboard);
        chessboard.setLocation(WIDTH / 5, HEIGHT / 10);
        gamePanel.add(chessboard);
    }
    private void addCapturingBoard()
    {//player:0红1黑
        capturingBoardHe = new CapturingBoard(CHESSBOARD_SIZE/8,CHESSBOARD_SIZE*7/8,
                mePlayer.getColor().equals(Color.RED)?0:1);
        capturingBoardMe = new CapturingBoard(CHESSBOARD_SIZE/8,CHESSBOARD_SIZE*7/8,
                mePlayer.getColor().equals(Color.RED)?1:0);

        capturingBoardHe.setLocation(WIDTH/5 - CHESSBOARD_SIZE/8, HEIGHT / 10);
        capturingBoardMe.setLocation(WIDTH/5 + CHESSBOARD_SIZE/2, HEIGHT / 10);//自己放在右边

        gamePanel.add(capturingBoardHe);
        gamePanel.add(capturingBoardMe);
    }
    /**
     * 在游戏窗体中添加标签
     */
    private void addGameLabel() {
        statusLabel = new JLabel("RED's TURN");
        statusLabel.setLocation(WIDTH * 4 / 5 + shiftConst1, HEIGHT / 20);
        statusLabel.setSize(200, 60);
        statusLabel.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(statusLabel);

        scoreOfBlack = new JLabel(String.format("BLACK's points: %d", Chessboard.blackPlayer.getCurrentScore()));
        scoreOfBlack.setLocation(WIDTH * 4 / 5 + shiftConst1, HEIGHT *2/ 20);
        scoreOfBlack.setSize(200, 60);
        scoreOfBlack.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(scoreOfBlack);

        scoreOfRed = new JLabel(String.format("RED's points: %d", Chessboard.redPlayer.getCurrentScore()));
        scoreOfRed.setLocation(WIDTH * 4 / 5 + shiftConst1, HEIGHT *3/ 20);
        scoreOfRed.setSize(200, 60);
        scoreOfRed.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(scoreOfRed);
    }

    public static JLabel getStatusLabel() {
        return statusLabel;
    }

    private void addGameLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(WIDTH * 4 / 5 + shiftConst1, HEIGHT *5/ 20);
        button.setSize(180, 60);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        button.setBackground(Color.LIGHT_GRAY);
        gamePanel.add(button);

        button.addActionListener(e -> {
            System.out.println("Click load");
            String path = JOptionPane.showInputDialog(this, "Input Path here");
            gameController.loadGameFromFile(path);
        });
    }

    public static JButton cheatingButton = new JButton("Cheating Mode: OFF");
    private void addCheatingModeButton() {//todo
        cheatingButton.addActionListener((e) -> {
            if(cheatingButton.getText().equals("Cheating Mode: OFF")) {
                cheatingButton.setText("Cheating Mode: ON");
                cheatingButton.repaint();
                isCheatingMode=true;
            }
            else {
                cheatingButton.setText("Cheating Mode: OFF");
                cheatingButton.repaint();
                isCheatingMode=false;
            }
        });
        cheatingButton.setLocation(WIDTH * 4 / 5 + shiftConst1, HEIGHT *7 / 20);
        cheatingButton.setSize(230, 60);
        cheatingButton.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(cheatingButton);
    }

    private void addRestartButton() {
        JButton button = new JButton("RESTART");
        button.addActionListener((e) -> {
            String[] options={"Menu","Restart!","Cancel"};
            int choice=JOptionPane.showOptionDialog(JOptionPane.getRootFrame(),"Confirm to restart?"
                    ,"From Judge",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[1]);
            if(choice==0){//菜单
                ChessGameFrame.gamePanel.setEnabled(false);
                ChessGameFrame.gamePanel.setVisible(false);
                Handler.mainFrame.setContentPane(ChessGameFrame.menuPanel);
                ChessGameFrame.menuPanel.setEnabled(true);
                ChessGameFrame.menuPanel.setVisible(true);
            }
            else if(choice==1){
                chessboard.initAllChessOnBoard();//restart!
                ChessGameFrame.repaintAll();
            }
        });
        button.setLocation(WIDTH * 4 / 5 + shiftConst1, HEIGHT *9 /20 );
        button.setSize(180, 60);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(button);
    }

    public static JButton withdrawButton = new JButton("Cheating Mode: OFF");
    private void withdraw()
    {//todo
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

            boolean meIsEaten=(capturingIsMe.peek()==1);
            CapturingBoard change=meIsEaten?capturingBoardHe:capturingBoardMe;
            change.capturingChesses[capturingLabel.peek()].num--;
        }
        Chessboard.setCurrentColor(Chessboard.getCurrentColor().equals(ChessColor.RED)?ChessColor.BLACK:ChessColor.RED);
        ChessGameFrame.getStatusLabel().setText(String.format("%s's TURN", Chessboard.getCurrentColor().getName()));
        ope.pop();
        firCom.pop();   firCol.pop();   firX.pop(); firY.pop();
        secCom.pop();   secCol.pop();   secX.pop(); secY.pop(); firCannonSecRev.pop();
        capturingIsMe.pop();    capturingLabel.pop();
        repaintAll();
        if(ope.empty())
            withdrawButton.setEnabled(false);
    }
    private void addWithdrawButton() {
        withdrawButton.setText("withdraw");
        withdrawButton.setEnabled(false);
        withdrawButton.addActionListener((e) -> {
            withdraw();
        });
        withdrawButton.setLocation(WIDTH * 4 / 5 + shiftConst1, HEIGHT *11 / 20);
        withdrawButton.setSize(180, 60);
        withdrawButton.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(withdrawButton);
    }

    private void addGameEscapeButton() {
        JButton button = new JButton("ESCAPE");
        button.addActionListener((e) -> {
            String[] options={"Yes","Cancel"};
            int choice=JOptionPane.showOptionDialog(JOptionPane.getRootFrame(),"Confirm to escape from this game?","From Judge"
                    ,JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[1]);
            if(choice==0){//菜单
                gamePanel.setEnabled(false);
                gamePanel.setVisible(false);
                setContentPane(menuPanel);
                menuPanel.setEnabled(true);
                menuPanel.setVisible(true);
            }
        });
        button.setLocation(WIDTH * 4 / 5 + shiftConst1, HEIGHT *13 / 20);
        button.setSize(180, 60);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(button);
    }
    private void addGameMinimize() {
        JButton button = new JButton("MINIMIZE");
        button.addActionListener((e) -> {
            setExtendedState(JFrame.ICONIFIED);
        });
        button.setLocation(WIDTH * 4 / 5 + shiftConst1, HEIGHT *15 / 20);
        button.setSize(180, 60);
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