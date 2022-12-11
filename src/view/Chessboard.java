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
import java.util.List;
import java.util.Stack;

import static view.ChessGameFrame.*;

/**
 * 这个类表示棋盘组建，其包含：
 * SquareComponent[][]: 4*8个方块格子组件
 */
public class Chessboard extends JComponent {

    private static final int ROW_SIZE = 8;
    private static final int COL_SIZE = 4;

    private final SquareComponent[][] squareComponents = new SquareComponent[ROW_SIZE][COL_SIZE];
    public final ClickController clickController = new ClickController(this);
    public final int CHESS_SIZE;
    //todo: you can change the initial player
    //all chessComponents in this chessboard are shared only one model controller

    private static ChessColor currentColor;
    public static Players redPlayer=new Players(Color.RED),blackPlayer=new Players(Color.BLACK);//红方先手
    public static Stack<Integer> ope=new Stack<>(),firCom=new Stack<>(),firCol=new Stack<>(),firX=new Stack<>(),firY=new Stack<>()
            ,secCom=new Stack<>(),secCol=new Stack<>(),secX=new Stack<>(),secY=new Stack<>(),firCannonSecRev=new Stack<>();//ope=1,翻棋子,仅用fir;=2,走棋子,fir与sec都用;
    // firCannonSecRev若first为炮吃了未翻开的棋子，则为1
    public static Stack<Integer> capturingIsMe=new Stack<>(),capturingLabel=new Stack<>();
    public static boolean isCheatingMode=false;
    public static Players mePlayer;
    public static int menuMode;//0人机1玩家对战
    public Chessboard(int width, int height) {
        setLayout(null); // Use absolute layout.
        setSize(width + 2, height);
        CHESS_SIZE = (height - 6) / 8;
        SquareComponent.setSpacingLength(CHESS_SIZE / 12);
        System.out.printf("chessboard [%d * %d], chess size = %d\n", width, height, CHESS_SIZE);
    }

    public SquareComponent[][] getChessComponents() {
        return squareComponents;
    }

    public static ChessColor getCurrentColor() {
        return currentColor;
    }

    public  static void setCurrentColor(ChessColor cC) {
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

    private class initType
    {
        public int chessType,player;//前者0-6:将士相车马兵炮;后者0-1:红黑
        public initType(int chessType,int player)
        {
            this.chessType=chessType;
            this.player=player;
        }
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
        System.out.println(tmpList.size());
        Collections.shuffle(tmpList);
        for(int i=0;i<ROW_SIZE;i++)
            for(int j=0;j<COL_SIZE;j++)
                ret[i][j]=tmpList.get(i*COL_SIZE+j);
        return ret;
    }
    public void initAllChessOnBoard() {
        ope.clear();
        firCom.clear();   firCol.clear();   firX.clear(); firY.clear();
        secCom.clear();   secCol.clear();   secX.clear(); secY.clear();
        firCannonSecRev.clear();
        capturingIsMe.clear();    capturingLabel.clear();
        withdrawButton.setEnabled(false);
        currentColor = ChessColor.RED;
        redPlayer.setCurrentScore(0);
        blackPlayer.setCurrentScore(0);
        cheatingButton.setText("Cheating Mode: OFF");
        isCheatingMode=false;
        ChessGameFrame.getStatusLabel().setText(String.format("%s's TURN", Chessboard.getCurrentColor().getName()));
        ChessGameFrame.getScoreOfBlack().setText(String.format("BLACK's points: %d", Chessboard.blackPlayer.getCurrentScore()));
        ChessGameFrame.getScoreOfRed().setText(String.format("RED's points: %d", Chessboard.redPlayer.getCurrentScore()));
        if(menuMode ==0)
            ChessGameFrame.contendFirstInPVC();
        else if(menuMode ==1)
            ChessGameFrame.contendFirstInPVP();
        if(capturingBoardHe!=null)
            capturingBoardHe.initAllCapturingChessOnBoard(mePlayer.getColor().equals(Color.RED) ?0:1);
        if(capturingBoardMe!=null)
            capturingBoardMe.initAllCapturingChessOnBoard(mePlayer.getColor().equals(Color.RED)?1:0);

        initType[][] randomizedComponents=initRandomizedChessOnBoard();
        for (int i = 0; i < squareComponents.length; i++) {
            for (int j = 0; j < squareComponents[i].length; j++) {
                ChessColor color=randomizedComponents[i][j].player==0? ChessColor.RED : ChessColor.BLACK;//0红1黑
                SquareComponent squareComponent;
                if (randomizedComponents[i][j].chessType == 0)//0-6:将士相车马兵炮
                    squareComponent = new GeneralChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
                else if(randomizedComponents[i][j].chessType == 1)
                    squareComponent = new AdvisorChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
                else if(randomizedComponents[i][j].chessType == 2)
                    squareComponent = new MinisterChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
                else if(randomizedComponents[i][j].chessType == 3)
                    squareComponent = new ChariotChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
                else if(randomizedComponents[i][j].chessType == 4)
                    squareComponent = new HorseChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
                else if(randomizedComponents[i][j].chessType == 5)
                    squareComponent = new SoldierChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
                else //==6
                    squareComponent = new CannonChessComponent(new ChessboardPoint(i, j), calculatePoint(i, j), color, clickController, CHESS_SIZE);
                squareComponent.setVisible(true);
                putChessOnBoard(squareComponent);
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
    public void loadGame(List<String> chessData) {//todo
        chessData.forEach(System.out::println);
    }
}
