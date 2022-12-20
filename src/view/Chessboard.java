package view;


import chessComponent.*;
import controller.ClickController;
import model.ChessColor;
import model.ChessboardPoint;
import player.Players;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import static controller.GameController.*;
import static media.BufferedPictures.*;
import static view.ChessGameFrame.*;
import static view.Handler.mainFrame;

/**
 * 这个类表示棋盘组建，其包含：
 * SquareComponent[][]: 4*8个方块格子组件
 */
public class Chessboard extends JComponent {

    private static final int ROW_SIZE = 8;
    private static final int COL_SIZE = 4;

    private final SquareComponent[][] squareComponents = new SquareComponent[ROW_SIZE][COL_SIZE];
    public ClickController clickController = new ClickController(this);
    public int CHESS_SIZE;
    //todo: you can change the initial player
    //all chessComponents in this chessboard are shared only one model controller
    public initType[][] initComponents =new initType[ROW_SIZE][COL_SIZE];
    private ChessColor currentColor=ChessColor.RED;
    public Players redPlayer=new Players(Color.RED),blackPlayer=new Players(Color.BLACK);//红方先手
    public Stack<Integer> ope=new Stack<>(),firCom=new Stack<>(),firCol=new Stack<>(),firX=new Stack<>(),firY=new Stack<>()
            ,secCom=new Stack<>(),secCol=new Stack<>(),secX=new Stack<>(),secY=new Stack<>(),firCannonSecRev=new Stack<>();//ope=1,翻棋子,仅用fir;=2,走棋子,fir与sec都用;
    // firCannonSecRev若first为炮吃了未翻开的棋子，则为1
    public Stack<Integer> capturingIsMe=new Stack<>(),capturingLabel=new Stack<>();
    public static boolean isCheatingMode=false;
    public Players mePlayer=new Players();
    public CapturingBoard capturingBoardMe=new CapturingBoard(mainFrame.CHESSBOARD_SIZE/8,mainFrame.CHESSBOARD_SIZE*7/8, mePlayer.getColor().equals(Color.RED)?1:0)
            , capturingBoardHe= new CapturingBoard(mainFrame.CHESSBOARD_SIZE/8,mainFrame.CHESSBOARD_SIZE*7/8,
            mePlayer.getColor().equals(Color.RED)?0:1);
    public static boolean canListenToMe=true;
    public Chessboard(int width, int height) {
        setLayout(null); // Use absolute layout.
        setSize(width + 2, height);
        CHESS_SIZE = (height - 6) / 8;
        SquareComponent.setSpacingLength(CHESS_SIZE / 12);
        //System.out.printf("chessboard [%d * %d], chess size = %d\n", width, height, CHESS_SIZE);
        for(int i=0;i<ROW_SIZE;i++)//分配内存
            for(int j=0;j<COL_SIZE;j++)
                initComponents[i][j]=new initType();
    }

    public SquareComponent[][] getChessComponents() {
        return squareComponents;
    }

    public ChessColor getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(ChessColor cC) {
        currentColor = cC;
    }

    /**
     * 将SquareComponent 放置在 ChessBoard上。里面包含移除原有的component及放置新的component
     * @param squareComponent
     */
    public void putChessOnBoard(SquareComponent squareComponent) {
        int row = squareComponent.getChessboardPoint().getX(), col = squareComponent.getChessboardPoint().getY();
        if (squareComponents[row][col] != null) {
            remove(squareComponents[row][col]);
        }
        add(squareComponents[row][col] = squareComponent);
    }

    /**
     * 交换chess1 chess2的位置
     * @param chess1
     * @param chess2
     */
    public void swapChessComponents(SquareComponent chess1, SquareComponent chess2) {
        // Note that chess1 has higher priority, 'destroys' chess2 if exists.
        if (!(chess2 instanceof EmptySlotComponent)) {
            remove(chess2);
            add(chess2 = new EmptySlotComponent(chess2.getChessboardPoint(), chess2.getLocation(), clickController, CHESS_SIZE));
        }
        chess1.swapLocation(chess2);
        int row1 = chess1.getChessboardPoint().getX(), col1 = chess1.getChessboardPoint().getY();
        squareComponents[row1][col1] = chess1;
        int row2 = chess2.getChessboardPoint().getX(), col2 = chess2.getChessboardPoint().getY();
        squareComponents[row2][col2] = chess2;

        //只重新绘制chess1 chess2，其他不变
        chess1.repaint();
        chess2.repaint();
    }

    public static class initType
    {
        public int chessType,player;//前者0-6:将士相车马兵炮;后者0-1:红黑
        public initType(int chessType,int player)
        {
            this.chessType=chessType;
            this.player=player;
        }
        public initType() {}
    }
    private initType[][] initRandomizedChessOnBoard()
    {
        initType[][] ret=new initType[ROW_SIZE][COL_SIZE];
        ArrayList<initType> tmpList=new ArrayList<>();
        for(int i=0;i<=1;i++)//顺序添加
        {
            tmpList.add(new initType(0,i));
            for(int j=0;j<2;j++)    tmpList.add(new initType(1,i));
            for(int j=0;j<2;j++)    tmpList.add(new initType(2,i));
            for(int j=0;j<2;j++)    tmpList.add(new initType(3,i));
            for(int j=0;j<2;j++)    tmpList.add(new initType(4,i));
            for(int j=0;j<5;j++)    tmpList.add(new initType(5,i));
            for(int j=0;j<2;j++)    tmpList.add(new initType(6,i));
        }
        //System.out.println(tmpList.size());
        Collections.shuffle(tmpList);
        for(int i=0;i<ROW_SIZE;i++)
            for(int j=0;j<COL_SIZE;j++)
                ret[i][j]=tmpList.get(i*COL_SIZE+j);
        return ret;
    }
    public void addCapturingBoard()
    {//player:0红1黑
        if(capturingBoardHe!=null)
            gamePanel.remove(capturingBoardHe);
        if(capturingBoardMe!=null)
            gamePanel.remove(capturingBoardMe);
        capturingBoardHe = new CapturingBoard(mainFrame.CHESSBOARD_SIZE/8,mainFrame.CHESSBOARD_SIZE*7/8,
                mePlayer.getColor().equals(Color.RED)?0:1);
        capturingBoardMe = new CapturingBoard(mainFrame.CHESSBOARD_SIZE/8,mainFrame.CHESSBOARD_SIZE*7/8,
                mePlayer.getColor().equals(Color.RED)?1:0);

        capturingBoardHe.setLocation(mainFrame.WIDTH/5 - mainFrame.CHESSBOARD_SIZE/8, mainFrame.HEIGHT / 4);
        capturingBoardMe.setLocation(mainFrame.WIDTH/5 + mainFrame.CHESSBOARD_SIZE/2, mainFrame.HEIGHT / 4);//自己放在右边

        if(this==ChessGameFrame.chessboard)
        {
            gamePanel.add(capturingBoardHe);
            gamePanel.add(capturingBoardMe);
        }
    }
    public void initAllChessOnBoard(int mode) {//0重开1按指定的initComponents初始化但不载入
        if(mode==0)
        {
            ope.clear();
            firCom.clear();   firCol.clear();   firX.clear(); firY.clear();
            secCom.clear();   secCol.clear();   secX.clear(); secY.clear();
            firCannonSecRev.clear();
            capturingIsMe.clear();    capturingLabel.clear();
            withdrawButton.setEnabled(false);
            withdrawButton.setIcon(WithdrawOffIcon);
            cheatingButton.setIcon(CheatOffIcon);
            isCheatingMode=false;
            currentColor = ChessColor.RED;
            redPlayer.setCurrentScore(0);
            blackPlayer.setCurrentScore(0);
            if(menuMode ==0)
                ChessGameFrame.contendFirstInPVC(0);
            initComponents =initRandomizedChessOnBoard();
        }
        if(menuMode ==1)
            ChessGameFrame.contendFirstInPVP();
        addCapturingBoard();
        if(mode==1 && menuMode!=1)
        {
            for(int i=0;i<=6;i++)
            {
                capturingBoardMe.getCapturingChesses()[i].num= aimCapturingboardMeNum[i];
                capturingBoardHe.getCapturingChesses()[i].num= aimCapturingboardHeNum[i];
            }
        }
        for (int i = 0; i < squareComponents.length; i++)
        {
            for (int j = 0; j < squareComponents[i].length; j++)
            {
                ChessColor color= initComponents[i][j].player==0? ChessColor.RED : ChessColor.BLACK;//0红1黑
                SquareComponent squareComponent;
                if (initComponents[i][j].chessType == 0)//0-6:将士相车马兵炮
                    squareComponent = new GeneralChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
                else if(initComponents[i][j].chessType == 1)
                    squareComponent = new AdvisorChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
                else if(initComponents[i][j].chessType == 2)
                    squareComponent = new MinisterChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
                else if(initComponents[i][j].chessType == 3)
                    squareComponent = new ChariotChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
                else if(initComponents[i][j].chessType == 4)
                    squareComponent = new HorseChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
                else if(initComponents[i][j].chessType == 5)
                    squareComponent = new SoldierChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
                else if(initComponents[i][j].chessType == 6)
                    squareComponent = new CannonChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
                else// if(arrangedComponents[i][j].chessType == 7)
                    squareComponent = new EmptySlotComponent(new ChessboardPoint(i, j), calculatePoint(i, j), clickController, CHESS_SIZE);

                squareComponent.setVisible(true);
                putChessOnBoard(squareComponent);
                if(mode==1 && menuMode!=1)
                    squareComponent.setReversal(isRever[i][j]);
            }
        }
    }
    /**
     * 绘制棋盘格子
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    /**
     * 将棋盘上行列坐标映射成Swing组件的Point
     * @param row 棋盘上的行
     * @param col 棋盘上的列
     * @return
     */
    public Point calculatePoint(int row, int col) {
        return new Point(col * CHESS_SIZE + 3, row * CHESS_SIZE + 3);
    }
    /**
     * 通过GameController调用该方法
     * @param chessData
     */
}
