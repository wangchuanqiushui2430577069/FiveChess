package utils;

public class JudgeUtil {

    /**
     * 判断游戏是否结束
     * true代表结束游戏 ,false代表继续游戏
     * @param row
     * @param col
     * @param chessColor
     * @return
     */
    public static boolean judgeGame (int row,int col,char chessColor) {
        int level = level(row,col,chessColor);
        int vertical = vertical(row,col,chessColor);
        int left = leftOblique(row,col,chessColor);
        int right = rightOblique(row,col,chessColor);
        //changeSide(); //此处修改
        if(level >= 5 || vertical >= 5 || left >= 5 || right >= 5) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查棋子在竖直线上是否连成五子
     * @return
     */
    public static int level (int row, int col, char chessColor) {
        int line = 1;
        int i ;
        if (row == 0) {
            for (i = row+1; !overArea(i,col); i++) {
                if (BoardUtil.chess[i][col] == chessColor)
                    line++;
                else
                    break;
            }
        } else if (row == 14) {
            for (i = row-1;  !overArea(i,col); i--) {
                if (BoardUtil.chess[i][col] == chessColor)
                    line++;
                else
                    break;
            }
        } else {
            for (i = row-1; !overArea(i,col); i--) {
                if (BoardUtil.chess[i][col] == chessColor)
                    line++;
                else
                    break;
            }
            for (i = row+1; !overArea(i,col); i++) {
                if (BoardUtil.chess[i][col] == chessColor)
                    line++;
                else
                    break;
            }
        }
        return line;
    }

    /**
     * 检查棋子在水平线上是否连成五子
     * @param row
     * @param col
     * @param chessColor
     * @return
     */
    public static int vertical (int row, int col, char chessColor) {
        int line = 1;
        int j;
        if(col == 0) {
            for (j = col+1;!overArea(row,j); j++) {
                if (BoardUtil.chess[row][j] == chessColor)
                    line++;
                else
                    break;
            }

        } else if (col == 14) {
            for (j = col-1; !overArea(row,j); j--) {
                if (BoardUtil.chess[row][j] == chessColor)
                    line++;
                else
                    break;
            }

        } else {
            for (j = col-1; !overArea(row,j); j--) {
                if (BoardUtil.chess[row][j] == chessColor)
                    line++;
                else
                    break;
            }

            for (j = col+1; !overArea(row,j); j++) {
                if (BoardUtil.chess[row][j] == chessColor)
                    line++;
                else
                    break;
            }
        }
        return line;
    }

    /**
     * 检查左倾斜线上的棋子是否连成五子
     * @param row
     * @param col
     * @param chessColor
     * @return
     */
    public static int leftOblique (int row, int col, char chessColor) {
        int line = 1, i, j;
        //int i = row - 1, j = col - 1;
        if ( col == 0 || row == 0) {
            for ( i = row+1, j = col+1; !overArea(i,j); i++, j++) {
                if (BoardUtil.chess[i][j] == chessColor)
                    line++;
                else
                    break;
            }
        } else if ( col == 14 || row == 14 ) {
            if (row > 0 && col > 0) {
                for (i = row - 1, j = col - 1; !overArea(i,j); i--, j--) {
                    if (BoardUtil.chess[i][j] == chessColor)
                        line++;
                    else
                        break;
                }
            } else if (row == 0 || col == 0) {
                return line;
            }

        } else {
            for (i = row - 1, j = col - 1; !overArea(i,j); i--, j--) {
                if (BoardUtil.chess[i][j] == chessColor)
                    line++;
                else
                    break;
            }
            for (i = row + 1, j = col + 1; !overArea(i,j); i++, j++) {
                if (BoardUtil.chess[i][j] == chessColor)
                    line++;
                else
                    break;
            }
        }
        return line;
    }

    /**
     * 检查右倾斜线上的棋子是否连成五子
     * @param row
     * @param col
     * @param chessColor
     * @return
     */
    public static int rightOblique (int row, int col, char chessColor) {
        int line = 1, i, j;
        //int i = row - 1, j = col + 1;
        if ( col == 0 || row == 14 ) {
            if (row > 0) {
                for (i = row - 1, j = col + 1;  !overArea(i,j); i--, j++) {
                    if (BoardUtil.chess[i][j] == chessColor)
                        line++;
                    else
                        break;
                }
            } else {
                return line;
            }
        } else if ( col == 14 || row == 0 ) {
            if (col > 0) {
                for (i = row + 1, j = col - 1; !overArea(i,j); i++, j--) {
                    if (BoardUtil.chess[i][j] == chessColor)
                        line++;
                    else
                        break;
                }
            } else {
                return line;
            }
        } else {
            for (i = row - 1, j = col + 1; !overArea(i,j); i--, j++) {
                if (BoardUtil.chess[i][j] == chessColor)
                    line++;
                else
                    break;
            }
            for (i = row + 1, j = col - 1; !overArea(i,j); i++, j--) {
                if (BoardUtil.chess[i][j] == chessColor)
                    line++;
                else
                    break;
            }
        }
        return line;
    }

    public static boolean overArea (int x, int y) {

        if (x >= 0 && x < 15 && y >= 0 && y < 15) {
            return false;
        } else {
            return true;
        }
    }

}
