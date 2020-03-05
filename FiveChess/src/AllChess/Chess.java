package AllChess;

import Interfaces.TryPlay;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import utils.AiStack;
import utils.BoardUtil;

public class Chess implements TryPlay {

    protected int x;
    protected int y;
    protected char currentSide;

    public Chess(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Chess(int x, int y, char currentSide) {
        this.x = x;
        this.y = y;
        this.currentSide = currentSide;
    }

    //未越界
    public boolean overArea(int x, int y) {

        if(x >= 0 && x < 15 && y >= 0 && y < 15)
            return false;
        else
            return true;
    }

    /**
     * 绘制棋子
     */
    public void paintChess(GraphicsContext gc) {

        double cell;
        cell = BoardUtil.getCellLen();
        double align = BoardUtil.getAlign();

        if(!overArea(x, y)) {  	 // 未越界
            if(getCurrentSide() == 'B')
                gc.setFill(Color.BLACK);	//	黑方先
            else
                gc.setFill(Color.WHITE);	//	白方先
            gc.strokeOval(align + x*cell - cell/2, align + y*cell - cell/2, cell, cell);
            gc.fillOval(align + x*cell - cell/2, align + y*cell - cell/2, cell, cell);
            BoardUtil.chess[x][y] = getCurrentSide();  //设置该处棋子颜色
        }
    }

    @Override
    public boolean play() {
        if(!overArea(x,y) && BoardUtil.chess[x][y] == '0') {
            BoardUtil.chess[x][y] = currentSide;
            AiStack.stack.push(new Chess(x, y, currentSide));//将下棋信息入栈
            return true;
        } else {
            return false;
        }
    }

    public Chess() { }

    public char getCurrentSide() { return currentSide; }

    public void setCurrentSide(char currentSide) { this.currentSide = currentSide; }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
