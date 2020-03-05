package AllChess;

import utils.AiStack;
import utils.BoardUtil;

public class AiChess extends Chess {

    public AiChess(int x, int y) {
        super(x, y);
        currentSide = 'W';
    }

    /**
     * 无参构造
     */
    public AiChess() {
        super();
    }

    /**
     * 重写 play 方法
     * @param x
     * @param y
     * @return
     */
    public boolean play() {
        if(!overArea(x,y) && BoardUtil.chess[x][y]=='0') {
            if(BoardUtil.AIFirst) {
                //电脑先
                BoardUtil.chess[x][y] = 'B';
                AiStack.stack.push(new Chess(x, y, 'B'));//将电脑下的棋也入栈
            } else {
                BoardUtil.chess[x][y] = 'W';
                AiStack.stack.push(new Chess(x, y, 'W'));
            }
            return true;
        }
        else {
            return false;
        }
    }

}
