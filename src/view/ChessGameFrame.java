package view;

import chessComponent.*;
import computerPlayer.ComputerPlayer;
import controller.GameController;
import media.ImageComponent;
import media.MyDialog;
import model.ChessColor;
import model.ChessboardPoint;
import pvp.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    public static boolean computerStop=true,computerEasy=true;
    public static JLayeredPane gamePanel=new JLayeredPane(),menuPanel=new JLayeredPane()
            ,pvpPlayerListPanel=new JLayeredPane();
    public static JLayeredPane PVCButtonsPanel=new JLayeredPane();
    public static String user=null,oppoUser=null,ip=null;
    public static Client client=null;
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
        String[] options={"是","否"};
        int choice=MyDialog.confirmDialog("裁判问：","你想要先手吗？","是","否");
        if(choice==1)
            chessboard.mePlayer=chessboard.redPlayer;
        else
            chessboard.mePlayer=chessboard.blackPlayer;
    }
    private static void initPVP()
    {
        String[] elements={"mSMs","Ccvs","vVhh","MGSS","HCaa","VsSs","mHAs","cASg"};
        for(int i=0;i<=7;i++)//初始棋盘：红大写黑小写，gamvhsce将士相车马兵炮空,v for vehicle
        {
            for(int j=0;j<=3;j++)
            {
                boolean isRED=Character.isUpperCase(elements[j].charAt(0));
                char tmp=Character.toLowerCase(elements[j].charAt(0));
                switch (tmp)
                {
                    case 'g':
                        chessboard.initComponents[i][j].chessType=0;
                        break;
                    case 'a':
                        chessboard.initComponents[i][j].chessType=1;
                        break;
                    case 'm':
                        chessboard.initComponents[i][j].chessType=2;
                        break;
                    case 'v':
                        chessboard.initComponents[i][j].chessType=3;
                        break;
                    case 'h':
                        chessboard.initComponents[i][j].chessType=4;
                        break;
                    case 's':
                        chessboard.initComponents[i][j].chessType=5;
                        break;
                    case 'c':
                        chessboard.initComponents[i][j].chessType=6;
                        break;
                }
                chessboard.initComponents[i][j].player=(isRED?0:1);
            }
        }
        chessboard.initAllChessOnBoard(1);
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

        button.addActionListener(e -> {//todo 配套服务没写
            ip=MyDialog.textDialog("联机小精灵","输入服务器端ip:");
            client=new Client(ip);
            /*if(!serverExists(ip)) todo 查一下服务器是否存在？
            {
                MyDialog.showDialog("联机小精灵","服务器不存在！");
                return;
            }*/
            if((user=MyDialog.userDialog())==null)
                return;
            menuMode =1;
            menuPanel.setEnabled(false);
            menuPanel.setVisible(false);
            pvpPlayerListPanel.setVisible(true);
            setContentPane(pvpPlayerListPanel);
            client.send(5,user);
            loadPVPPlayerListPanel();
            initPVP();//todo 偷懒惹
            addCapturingBoard();
            ChessGameFrame.repaintAll();
           // PVPButtonsPanel.setEnabled(true);
           // PVPButtonsPanel.setVisible(true);
           // gamePanel.add(PVPButtonsPanel,JLayeredPane.MODAL_LAYER);
            gamePanel.setEnabled(true);
            gamePanel.setVisible(true);
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
    private void loadPVPPlayerListPanel()
    {
        pvpPlayerListPanel.setSize(WIDTH,HEIGHT);
        pvpPlayerListPanel.setLocation(0,0);
        pvpPlayerListPanel.setLayout(null);
        pvpPlayerListPanel.setVisible(true);

        JButton button = new JButton("连接");
        button.setSize(WIDTH/5,HEIGHT/5);
        button.setLocation(WIDTH*2/3,HEIGHT*2/3);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                client.link();
            }
        });
        pvpPlayerListPanel.add(button,JLayeredPane.MODAL_LAYER);

        client.user_list.setSize(WIDTH/2,HEIGHT/2);
        client.user_list.setLocation(WIDTH/10,HEIGHT/10);
        pvpPlayerListPanel.add(client.user_list,JLayeredPane.MODAL_LAYER);

        JComponent imageComponent = new ImageComponent(MENU);// create an instance of ImageComponent
        imageComponent.setSize(WIDTH,HEIGHT);
        imageComponent.setLocation(0, 0); // set absolute location
        pvpPlayerListPanel.add(imageComponent,JLayeredPane.FRAME_CONTENT_LAYER);
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
        JComponent imageComponent = new ImageComponent(GameBG);// create an instance of ImageComponent
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
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setLocation(WIDTH / 5, HEIGHT / 40);
        statusLabel.setSize(WIDTH/6, HEIGHT /12);
        statusLabel.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(statusLabel,JLayeredPane.MODAL_LAYER);

        scoreOfBlack = new JLabel(String.format("黑方的分数是： %d", chessboard.blackPlayer.getCurrentScore()));
        scoreOfBlack.setForeground(Color.BLACK);
        scoreOfBlack.setLocation(WIDTH / 5, HEIGHT *3/ 40);
        scoreOfBlack.setSize(WIDTH/6, HEIGHT /12);
        scoreOfBlack.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(scoreOfBlack,JLayeredPane.MODAL_LAYER);

        scoreOfRed = new JLabel(String.format("红方的分数是： %d", chessboard.redPlayer.getCurrentScore()));
        scoreOfRed.setForeground(Color.BLACK);
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
        JButton button = new JButton();//载入
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        Image image =Toolkit.getDefaultToolkit().getImage("./resource/loadin.png").getScaledInstance(WIDTH /12,HEIGHT/12,Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(image));

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
        JButton button = new JButton();//存档
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        Image image =Toolkit.getDefaultToolkit().getImage("./resource/save.png").getScaledInstance(WIDTH /12,HEIGHT/12,Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(image));

        button.setLocation(WIDTH/10, 0);
        button.setSize(WIDTH/12, HEIGHT/12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        button.setBackground(Color.LIGHT_GRAY);
        PVCButtonsPanel.add(button,JLayeredPane.MODAL_LAYER);

        button.addActionListener(e -> {
            gameController.saveGameToFile();
        });
    }

    public static JButton cheatingButton = new JButton();//"作弊模式：关"
    private void loadCheatingModeButton() {
        cheatingButton.addActionListener((e) -> {
            if(!isCheatingMode) {
            //    cheatingButton.setText("作弊模式：开");
                cheatingButton.setIcon(CheatOnIcon);
                cheatingButton.repaint();
                isCheatingMode=true;
            }
            else {
            //    cheatingButton.setText("作弊模式：关");
                cheatingButton.setIcon(CheatOffIcon);
                cheatingButton.repaint();
                isCheatingMode=false;
            }
        });
        cheatingButton.setContentAreaFilled(false);
        cheatingButton.setBorderPainted(false);
        cheatingButton.setIcon(CheatOffIcon);

        cheatingButton.setLocation(0, HEIGHT /10);
        cheatingButton.setSize(WIDTH/6, HEIGHT/12);
        cheatingButton.setFont(new Font("宋体", Font.BOLD, 20));
        PVCButtonsPanel.add(cheatingButton,JLayeredPane.MODAL_LAYER);
    }

    private void loadRestartButton() {
        JButton button = new JButton();//"重开"
        button.addActionListener((e) -> {
            int choice=MyDialog.confirmDialog("裁判问：","确定重开吗？","重开","取消");
            if(choice==1){
                ComputerPlayer.stop=true;
                chessboard.initAllChessOnBoard(0);//restart!
                ChessGameFrame.repaintAll();
                ComputerPlayer.stop=false;
                computerPlayer=new ComputerPlayer();
                computerPlayer.start();
            }
        });
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        Image image =Toolkit.getDefaultToolkit().getImage("./resource/restart.png").getScaledInstance(WIDTH/6,HEIGHT/12,Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(image));

        button.setLocation(0, HEIGHT *2/10 );
        button.setSize(WIDTH/6, HEIGHT/12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        PVCButtonsPanel.add(button,JLayeredPane.MODAL_LAYER);
    }

    public static JButton withdrawButton = new JButton();//悔棋
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
        {
            withdrawButton.setEnabled(false);
            withdrawButton.setIcon(WithdrawOffIcon);
        }
    }
    private void loadWithdrawButton() {
        withdrawButton.setContentAreaFilled(false);//悔棋
        withdrawButton.setBorderPainted(false);
        withdrawButton.setIcon(WithdrawOffIcon);
        withdrawButton.setEnabled(false);
        withdrawButton.addActionListener((e) -> {
            withdraw();
        });
        withdrawButton.setLocation(0, HEIGHT *3 / 10);
        withdrawButton.setSize(WIDTH/6, HEIGHT/12);
        withdrawButton.setFont(new Font("宋体", Font.BOLD, 20));
        PVCButtonsPanel.add(withdrawButton,JLayeredPane.MODAL_LAYER);
    }
    private void loadComputerPlayerButton()//是否开启
    {
        JButton button = new JButton();//"关"
        button.setContentAreaFilled(false);//悔棋
        button.setBorderPainted(false);
        button.setIcon(PcOffIcon);
        button.addActionListener((e) -> {
            if(!computerStop) {
                button.setIcon(PcOffIcon);
                button.repaint();
                computerStop=true;
                ComputerPlayer.stop=true;
            }
            else {
                button.setIcon(PcOnIcon);
                button.repaint();
                computerStop=false;
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
        JButton button = new JButton();//"简单"
        button.addActionListener((e) -> {
            if(computerEasy) {
                button.setIcon(DifficultIcon);
                button.repaint();
                computerEasy=false;
                ComputerPlayer.setDifficultyMode(1);
            }
            else {
                button.setIcon(EasyIcon);
                button.repaint();
                computerEasy=true;
                ComputerPlayer.setDifficultyMode(0);
            }
        });
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setIcon(EasyIcon);
        button.setLocation(WIDTH/10, HEIGHT *4/10);
        button.setSize(WIDTH/12, HEIGHT/12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        PVCButtonsPanel.add(button,JLayeredPane.MODAL_LAYER);
    }
    private void addGameEscapeButton() {
        JButton button = new JButton();//逃跑
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        Image image =Toolkit.getDefaultToolkit().getImage("./resource/escape.png").getScaledInstance(WIDTH/6,HEIGHT/12,Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(image));
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
        button.setSize(WIDTH/6, HEIGHT/12);
        button.setFont(new Font("宋体", Font.BOLD, 20));
        gamePanel.add(button,JLayeredPane.MODAL_LAYER);
    }
    private void addGameMinimize() {
        JButton button = new JButton();//"最小化窗口"
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        Image image =Toolkit.getDefaultToolkit().getImage("./resource/mingame.png").getScaledInstance(WIDTH/6,HEIGHT/12,Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(image));
        button.addActionListener((e) -> {
            setExtendedState(JFrame.ICONIFIED);
        });
        button.setLocation(WIDTH * 4 / 5, HEIGHT *15 / 20);
        button.setSize(WIDTH/6, HEIGHT/12);
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