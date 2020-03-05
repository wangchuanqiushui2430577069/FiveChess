package AllView;

import AllChess.Chess;
import AllChess.PlayerChess;
import Controller.PlayerControl;
import Interfaces.Log;
import Log.PlayLog;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.AiStack;
import utils.BoardUtil;
import utils.JudgeUtil;
import utils.LoginUtil;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;

public class PlayerView extends Pane implements  Runnable{

    private Stage stage;

    private Image image;
    //画布
    private Canvas canvas;
    //画笔
    private GraphicsContext gc;
    private Log Playlog;
    private HBox hbox = null;
    private VBox vbox = null;

    public static boolean me;        		// 初始化为己方先落黑子
    public static boolean one = true;       // 控制联机双方每次只能一个人下子
    public static boolean start = false;    // 默认打开游戏不能下子,需先选择游戏模式

    public static InetAddress inetAddress;  // 客户端确认服务端的IP地址
    static String UsernameOrIp;             // 联机对方主机名或IP地址

    // 客户端和服务端端口号一致则为单机版五子棋
    static int clientPort;                  // 设置客户端端口
    public static int serverPort;           // 设置服务端端口
    public static boolean VSmyself;         // 选择单机模式,则程序只判断一次胜负,防止对局结束后无法落子,若联机模式,则判断两次胜负

    static Thread t;                        // 声明一个线程

    public PlayerView(Stage stage) {

        t = new Thread(this);       //初始化一个线程
        this.stage = stage;
        canvas = new Canvas(650, 650);
        this.gc = canvas.getGraphicsContext2D();	//获得GraphicsContext2D进行渲染
        Playlog = new PlayLog();

        TextInputDialog log = new TextInputDialog("***");
        log.setTitle("玩家登录窗口");
        log.setHeaderText("登录");
        log.setContentText("玩家ID");
        // 传统的获取输入值的方法
        Optional<String> results = log.showAndWait();
        if (results.isPresent()) {
            LoginUtil.name2 = results.get(); 	//保存玩家ID
        } else {
            LoginUtil.name2 = "***";			//为输入任何字符
        }

        TextInputDialog dialog = new TextInputDialog("localhost");
        dialog.setTitle("联机窗口");
        dialog.setHeaderText("联机对方主机名或IP地址");
        dialog.setContentText("IP");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            UsernameOrIp = result.get();        //保存对方主机或IP地址
        } else {
            UsernameOrIp = "localhost" ;		//默认为本地主机IP,即单机
        }

        TextInputDialog dialog2 = new TextInputDialog("8887");  //默认值
        dialog2.setTitle("联机窗口");
        dialog2.setHeaderText("输入端口号：1024 — 65535");
        dialog2.setContentText("端口号");
        Optional<String> result2 = dialog2.showAndWait();
        if (result2.isPresent()) {
            //TODO  用户输入的可能不是数字字符，待处理
            clientPort = Integer.parseInt(result2.get());       //输入一个端口号
        } else {
            clientPort = 8887 ;
        }

        TextInputDialog dialog3 = new TextInputDialog("8887");  //默认值
        dialog3.setTitle("联机窗口");
        dialog3.setHeaderText("输入对方端口号：1024 — 65535");
        dialog3.setContentText("对方端口号");
        Optional<String> result3 = dialog3.showAndWait();
        if (result3.isPresent()) {
            //TODO  用户输入的可能不是数字字符，待处理
            serverPort = Integer.parseInt(result3.get());        //输入对方端口号
        } else {
            serverPort = 8887 ;
        }

        try {
            inetAddress = InetAddress.getByName(UsernameOrIp);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (inetAddress != null) {

            start = true;                           //设置游戏开始
            if (clientPort == serverPort) {         // 若主机名或IP为自己电脑(或不填写)且两个端口一样,则为单机模式
                VSmyself = true;                    //开启单机模式
            } else {
                VSmyself = false;                   // 联机模式,设置单机模式为false
            }
            t.start();                              // 运行线程，实时接受对方传来的消息
        }

        Button button = new Button("返回");
        button.setOnAction(e->{
            MainView main_View = new MainView();
            try {
                main_View.start(stage);	//返回主界面
            } catch (Exception e1) {
                t.interrupt();         //中止线程
                e1.printStackTrace();
            }

        });
        button.setPrefSize(70, 20);

        hbox = new HBox(button);

        image = new Image("image/Player.jpg");
        getChildren().add(new ImageView(image));
        vbox = new VBox(hbox, canvas);
        getChildren().add(vbox);

    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    @Override
    public void run() {          	// 使用线程,端口号一致可进行单机游戏
        BoardUtil.initChess();   	//游戏开启,重绘棋盘
        me = true;
        draw(gc);
        receive();               	//开启服务端接收数据
    }

    public void receive() {			// 服务端接收对方发来的坐标
        try {
            ServerSocket serverSocket = new ServerSocket(clientPort);// 从该端口接收数据
            ///等待客户连接的时间不超过120秒
            //serverSocket.setSoTimeout(120000);
            //即使操作系统还没释放端口，同一个主机上的其他进程还可以立刻重用该端口.
            serverSocket.setReuseAddress(true);
            //初始化音频设备
            String url1 = this.getClass().getClassLoader().getResource("music/dianji.mp3").toExternalForm();
            Media media = new Media(url1);
            MediaPlayer mPlayer = new MediaPlayer(media);
            BoardUtil.Play2chess = new PlayerChess();
            Socket socket = null;
            InputStream is = null;
            byte[] bys = null;
            int len = 0;
            while (true) {

                socket = serverSocket.accept();
                is = socket.getInputStream();
                bys = new byte[1024];
                len = is.read(bys);

                //当服务器响应客户端时，需要在响应信息输完后加入 socket.shutdownOutput()
                socket.shutdownInput();

                String strReceive = new String(bys, 0, len);	//可以在后面加一个 ",utf-8"
                String[] strs = strReceive.split("-");      	// 使用"-"分割字符串,得到坐标
                BoardUtil.Play2chess.setX(Integer.parseInt(strs[0]));
                BoardUtil.Play2chess.setY(Integer.parseInt(strs[1]));
                if(me) {
                    BoardUtil.Play2chess.setCurrentSide('B');
                } else {
                    BoardUtil.Play2chess.setCurrentSide('W');
                }
                doit(BoardUtil.Play2chess);
                mPlayer.play();
                BoardUtil.Play2chess.paintChess(gc);

                // 得到坐标后,重绘棋盘并判断胜方
                if(JudgeUtil.judgeGame(Integer.parseInt(strs[0]), Integer.parseInt(strs[1]), BoardUtil.Play2chess.getCurrentSide())) {

                    //在UI线程之外不能直接操控UI组件
                    //将参数runnable将要执行的代码，交给FX application thread线程执行
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("五子棋游戏");
                            alert.setContentText("游戏结束," + ((BoardUtil.Play2chess.getCurrentSide()
                                    =='B') ? "黑方" : "白方") + "获胜!");
                            alert.showAndWait();
                            //TODO 游戏即将重新开始，设置一个提示框
                        }
                    });
                    if(Playlog instanceof PlayLog) {
                        ((PlayLog)Playlog).setName1(LoginUtil.name2);
                    }
                    Playlog.InputLog(false);   								//玩家输了
                    BoardUtil.initChess();	    							//重置棋盘
                    AiStack.ClearStack();       							//重置栈
                    BoardUtil.setAIFirst(false);
                    gc.clearRect(0,0,700,700);								//清屏
                    draw(gc);        										//重绘棋盘
                    canvas.setOnMouseClicked(new PlayerControl(canvas));    //重新注册事件
                    me = true;
                }
                one = true;                                         		// 己方不能落子,棋权交给对方

                socket.shutdownOutput();
                try {
                    //如果不设置等待时间，服务端已经关闭了，客户端还没有来得及读取这个响应，从而报错
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //释放资源
                is.close();
                socket.close();

            }
        } catch (IOException e) {
            t.interrupt();         //中止线程
            e.printStackTrace();
        }


    }

    public static void doit(Chess play) {

        if (BoardUtil.chess[play.getX()][play.getY()] == '0') {		// 点击位置无棋子
            if (me == true) {
                BoardUtil.chess[play.getX()][play.getY()] = 'B';	// 黑子
                me = false;// 换为白子落子
            } else if (me == false) {
                BoardUtil.chess[play.getX()][play.getY()] = 'W';	// 白子
                me = true;// 换为黑子落子
            }
        }
    }


    /**
     * 绘制棋盘
     */
    public static void draw(GraphicsContext gic) {

        double cell = BoardUtil.getCellLen();
        double align = BoardUtil.getAlign();
        double width = BoardUtil.getWidth();
        double height = BoardUtil.getHeight();

        //填充棋盘颜色
        gic.setFill(Color.BURLYWOOD);
        gic.fillRect(align - 15, align - 15, width + 30, height + 30);

        for(int y = 1; y < 15; y++) {
            gic.setLineWidth(2);    //设置线条宽度，解决重绘出现重叠问题
            //画横线
            gic.strokeLine(align, y * cell + align, width + align, y * cell + align);
            //画竖线
            gic.strokeLine(y * cell + align, align, y * cell + align, height + align);
        }

        /**
         * 天元(7,7),四个星位(3,3),(3,11),(11,3),(11,11)
         **/
        for(int i = 3; i <= 14; i += 4)
            for(int j = 3; j <= 14;) {
                gic.setFill(Color.BLACK);
                //画天元
                if(i == 7) {
                    j = 7;
                    gic.strokeOval(i * cell + align - 4, j * cell + align - 4, 8, 8);
                    gic.fillOval(i * cell + align - 4, j * cell + align - 4, 8, 8);
                    break;
                }
                //画星位
                else {
                    gic.strokeOval(i * cell + align - 4, j * cell + align - 4, 8, 8);
                    gic.fillOval(i * cell + align - 4, j * cell + align - 4, 8, 8);
                    j += 8;
                }
            }

        //边框加粗
        gic.setLineWidth(3.0f);
        gic.strokeRect(align, align, width, height);

    }

}
