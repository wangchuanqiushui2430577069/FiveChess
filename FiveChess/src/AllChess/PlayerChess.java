package AllChess;

import javafx.scene.control.Alert;
import utils.AiStack;
import utils.BoardUtil;

public class PlayerChess extends Chess {

    public PlayerChess(int x, int y) {
        super(x, y);
        currentSide = 'B';
    }

    /**
     * 无参构造
     */
    public PlayerChess() {
        super();
    }



    /**
     * 重写 play 方法
     * @param x
     * @param y
     * @return
     */
    public boolean play() {
        if(!overArea(x,y) && BoardUtil.chess[x][y] == '0') {
            BoardUtil.chess[x][y] = currentSide;
            AiStack.stack.push(new Chess(x, y, currentSide));//将下棋信息入栈
            return true;
        } else {
            //提示框，该处不可以下
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("警告");
            alert.setContentText("棋子不可以下在此处！");
            alert.showAndWait();
            return false;
        }
    }


}
