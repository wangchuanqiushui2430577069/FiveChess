package Controller;

import AllChess.AiChess;
import AllChess.Chess;
import AllChess.PlayerChess;
import AllView.AiView;
import Interfaces.Log;
import Log.AiLog;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import utils.AiStack;
import utils.BoardUtil;
import utils.JudgeUtil;
import utils.LoginUtil;

public class AiControl implements EventHandler<MouseEvent> {


    private Chess Aichess;
    private Chess Playchess;
    private Log Ailog;
    private GraphicsContext gc;
    private Canvas canvas;
    private Ai AI;


    public AiControl() {
        Aichess = new AiChess();
        Playchess = new PlayerChess();
        Ailog = new AiLog();
        AI = new Ai();
    }
    public AiControl(Canvas CAnvas) {
        canvas = CAnvas;
        gc = canvas.getGraphicsContext2D();
        Aichess = new AiChess();
        Playchess = new PlayerChess();
        Ailog = new AiLog();
        AI = new Ai();

    }

    @Override
    public void handle(MouseEvent event) {
        double cellLen = BoardUtil.getCellLen();
        double Align = BoardUtil.getAlign();

        //初始化音频设备
        String url1 = this.getClass().getClassLoader().getResource("music/dianji.mp3").toExternalForm();
        Media media = new Media(url1);
        MediaPlayer mPlayer = new MediaPlayer(media);

        //定位棋子落点
        int x = (int)((event.getX() - Align + cellLen/2)/cellLen);
        int y = (int)((event.getY() - Align + cellLen/2)/cellLen);

        if(event.getX() >= 55 && event.getY() >= 55 && x < 15 && y < 15) { //没越界
            Playchess.setX(x);
            Playchess.setY(y);
            if(BoardUtil.isAIFirst()) {
                Playchess.setCurrentSide('W');
            } else {
                Playchess.setCurrentSide('B');
            }
            boolean flag = Playchess.play();            //可以在该处落子
            boolean flag2 = false;                      //输赢标志
            if(flag) {
                mPlayer.play();							//播放音效
                Playchess.paintChess(gc);				//绘制棋子
                if(JudgeUtil.judgeGame(x, y, Playchess.getCurrentSide())) {       //判定胜负
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("五子棋游戏");
                    alert.setContentText("游戏结束,"+"玩家"+"获胜!");
                    alert.showAndWait();

                    if(Ailog instanceof AiLog) {
                        ((AiLog)Ailog).setName1(LoginUtil.name1);
                    }
                    Ailog.InputLog(true);	//记录胜负信息

                    BoardUtil.initChess();	    //重置棋盘
                    AiStack.ClearStack();       //重置栈
                    BoardUtil.setAIFirst(false);
                    gc.clearRect(0,0,700,700);	//清屏
                    AiView.draw(gc);                        //重绘棋盘
                    canvas.setOnMouseClicked(new AiControl(canvas));    //重新注册事件

                    flag2 = true;
                    if(BoardUtil.isAIFirst()) {
                        int a = 4+(int)(Math.random()*6);
                        int b = 4+(int)(Math.random()*6);
                        Aichess.setX(a);
                        Aichess.setY(b);
                        Aichess.setCurrentSide('B');
                        if(Aichess.play()) {    //人机随机下一步
                            Aichess.paintChess(gc);
                        }
                        //BoardUtil.setCurrentSide('W');
                    }
                }
                if(!flag2) {  //玩家未赢

                    AI.ReStart();       //更新评分表
                    AI.JudgeBest();     //人机思考如何下棋
                    Aichess.setX(AI.getX());
                    Aichess.setY(AI.getY());
                    if(BoardUtil.isAIFirst()) {
                        Aichess.setCurrentSide('B');
                    } else {
                        Aichess.setCurrentSide('W');
                    }
                    if(Aichess.play()) {    //画出电脑所下棋子
                        Aichess.paintChess(gc);

                    }

                    //判断胜负
                    if(JudgeUtil.judgeGame(AI.getX(), AI.getY(), BoardUtil.isAIFirst()?'B':'W')) {

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("五子棋游戏");
                        alert.setContentText("游戏结束,"+"电脑"+"获胜!");
                        alert.showAndWait();

                        if(Ailog instanceof AiLog) {
                            ((AiLog)Ailog).setName1(LoginUtil.name1);
                        }
                        Ailog.InputLog(false);	            //记录胜负信息

                        BoardUtil.initChess();                  //重置棋盘的落子标志
                        gc.clearRect(0,0,700,700);	//清屏
                        AiView.draw(gc);                        //重绘棋盘
                        AiStack.ClearStack();                   //重置栈
                        BoardUtil.setAIFirst(false);            //默认玩家先手
                        canvas.setOnMouseClicked(new AiControl(canvas));    //重新注册事件

                        if(BoardUtil.isAIFirst()) {
                            int a = 4+(int)(Math.random()*6);
                            int b = 4+(int)(Math.random()*6);
                            Aichess.setX(a);
                            Aichess.setY(b);
                            Aichess.setCurrentSide('B');
                            if(Aichess.play()) {    //人机随机下一步
                                Aichess.paintChess(gc);
                            }
                        }
                    }
                }
            }
        }
    }


}
