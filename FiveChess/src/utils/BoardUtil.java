package utils;

import AllChess.Chess;

/**
 * 棋盘工具类
 */
public class BoardUtil {


    public static char[][] chess;		//下棋标志数组    16
    public static double width;         //棋子宽度  560
    public static double height;        //棋子高度  560
    public static double cellLen; 	    //画棋子的缩放倍率  40
    public static char currentSide; 	//玩家所下棋的颜色
    public static double align;         //棋盘距窗口边框左、上的距离  70
    public static boolean AIFirst;	    //人机先手标志    false
    public static Chess Aichess;        //人机刚下在棋盘上的棋子
    public static Chess Playchess;      //玩家刚下在棋盘上的棋子
    public static Chess Play2chess;     //玩家 2 刚下在棋盘上的棋子

    /**
     * 判断棋盘是否空
     * @return
     */
    public static boolean ChessPaneEmpty() {
        for(int i = 0; i <= 15; i++) {
            for(int j = 0 ;j <= 15; j++) {
                if(chess[i][j] != '0')
                    return false;
            }
        }
        return true;

    }

    /**
     * 对棋盘进行初始化
     */
    public static void initChess() {
        for(int i = 0; i <= 15; i++)
            for(int j = 0; j <= 15; j++)
                chess[i][j] = '0';
    }

    public static void setPlaychess(Chess playchess) { Playchess = playchess; }

    public static void setPlay2chess(Chess play2chess) { Play2chess = play2chess; }

    public static void setAichess(Chess aichess) { Aichess = aichess; }

    public static void setChess(int n) { chess = new char[n][n]; }

    public static void setWidth(double width) { BoardUtil.width = width; }

    public static void setHeight(double height) { BoardUtil.height = height; }

    public static void setCellLen(double cellLen) { BoardUtil.cellLen = cellLen; }

    public static void setCurrentSide(char currentSide) { BoardUtil.currentSide = currentSide; }

    public static void setAlign(double align) { BoardUtil.align = align; }

    public static boolean isAIFirst() { return AIFirst; }

    public static void setAIFirst(boolean AIFirst) { BoardUtil.AIFirst = AIFirst; }


    public static double getWidth() { return width; }

    public static double getHeight() { return height; }

    public static double getCellLen() { return cellLen; }

    public static double getAlign() { return align; }


}
