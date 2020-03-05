package Controller;

import AllChess.Chess;
import AllChess.PlayerChess;
import AllView.PlayerView;
import Interfaces.Log;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import utils.AiStack;
import utils.BoardUtil;
import Log.PlayLog;
import utils.JudgeUtil;
import utils.LoginUtil;
import java.io.OutputStream;
import java.net.Socket;

public class PlayerControl implements EventHandler<MouseEvent> {

    private Chess Playchess;
    private Log Playlog;
    private GraphicsContext gc;
    private Canvas canvas;


    public PlayerControl(Canvas CAnvas) {
        canvas = CAnvas;
        gc = canvas.getGraphicsContext2D();
        Playchess = new PlayerChess();
        Playlog = new PlayLog();

    }
    @Override
    public void handle(MouseEvent e) {

        //初始化音频设备
        String url1 = this.getClass().getClassLoader().getResource("music/dianji.mp3").toExternalForm();
        Media media = new Media(url1);
        MediaPlayer mPlayer = new MediaPlayer(media);
        if (PlayerView.start == false) {
            return;
        }
        double cellLen = BoardUtil.getCellLen();
        double Align = BoardUtil.getAlign();
        //定位棋子落点
        int ex = (int)((e.getX() - Align + cellLen/2)/cellLen);
        int ey = (int)((e.getY() - Align + cellLen/2)/cellLen);
        //System.out.println("(" + ex + " , " + ey + ")");// 在棋盘上点击鼠标时在控制台输出坐标信息,方便测试
        if (e.getX() >= 55 && e.getY() >= 55 && ex < 15 && ey < 15) {// 控制鼠标点击位于棋盘内部

            if (PlayerView.one == true) {                          // 轮到一方落子时,落子判断胜负,并传送鼠标点击的坐标

                Playchess.setX(ex);
                Playchess.setY(ey);

                if(PlayerView.me) {
                    Playchess.setCurrentSide('B');
                } else {
                    Playchess.setCurrentSide('W');
                }
                boolean flag = Playchess.play();
                //将play方法中对BoardUtil的操作重置
                BoardUtil.chess[Playchess.getX()][Playchess.getY()] = '0';
                if(flag) {
                    mPlayer.play();
                    PlayerView.doit(Playchess);	//重新对BoardUtil进行操作
                    Playchess.paintChess(gc);

                    sendXY(ex, ey);// 先传送坐标再判断胜负
                    if (PlayerView.VSmyself == false) {// 联机模式下才进行此次胜负判断
                        if(JudgeUtil.judgeGame(ex, ey, Playchess.getCurrentSide())) {       //判定胜负
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("五子棋游戏");
                            alert.setContentText("游戏结束," + ((Playchess.getCurrentSide()
                                    =='B') ? "黑方" : "白方") + "获胜!");
                            alert.showAndWait();

                            BoardUtil.initChess();	    						//重置棋盘
                            AiStack.ClearStack();       						//重置栈
                            BoardUtil.setAIFirst(false);
                            gc.clearRect(0,0,700,700);							//清屏
                            PlayerView.draw(gc);        						//重绘棋盘
                            canvas.setOnMouseClicked(new PlayerControl(canvas));    //重新注册事件

                            //TODO 游戏即将重新开始，设置一个提示框
                            PlayerView.me = true;

                            if(Playlog instanceof PlayLog) {
                                ((PlayLog)Playlog).setName1(LoginUtil.name2);
                            }
                            Playlog.InputLog(true);
                        }

                    }
                    PlayerView.one = false;					// 换到对方落子
                } else {
                    return ;
                }

            }


        }

    }

    private  void sendXY(int xSend, int ySend) {                                        // 传送鼠标点击的坐标
        try {                                                                           // 问题在于每次传送坐标都要重新创建一个socket对象,不知道怎么改进
            Socket socket = new Socket(PlayerView.inetAddress, PlayerView.serverPort);
            OutputStream os = socket.getOutputStream();
            String strr = xSend + "-" + ySend;                                          // 使用-符号连接坐标,并以字符串形式发送到另一服务器
            os.write(strr.getBytes());
            socket.shutdownOutput();                                                    // 及时发送位置信息,不用closs()是保持通信一直连接
            //释放资源
            os.close();
            socket.close();
        } catch (Exception ex) {
            System.out.println("对方已下线");
            System.exit(0);
            ex.printStackTrace();
        }
    }

}
