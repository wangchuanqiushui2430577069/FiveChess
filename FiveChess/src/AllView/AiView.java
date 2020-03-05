package AllView;

import AllChess.Chess;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.AiStack;
import utils.BoardUtil;
import utils.LoginUtil;
import java.util.Optional;
import java.util.Stack;

public class AiView extends Pane {

    private Stage stage;

    private Image image;
    //画布
    private Canvas canvas;
    //画笔
    private GraphicsContext gc;
    //按钮名称
    private final static String[] Select = {"悔棋","重开","返回","先后手"};

    private HBox hbox = null;
    private VBox vbox = null;

    //悔棋中间栈
    private Stack<Chess> nowStack;

    public AiView(Stage stage) {
        this.stage = stage;
        canvas = new Canvas(650, 650);
        this.gc = canvas.getGraphicsContext2D();	//获得GraphicsContext2D进行渲染
        hbox = new HBox();
        /*
        AiStack.ClearStack();
        BoardUtil.setChess(16);
        BoardUtil.setAIFirst(false);
        BoardUtil.setAlign(70);
        BoardUtil.setCellLen(40);
        BoardUtil.setHeight(560);
        BoardUtil.setWidth(560);
        BoardUtil.initChess();
        */
        TextInputDialog dialog = new TextInputDialog("***");
        dialog.setTitle("玩家登录窗口");
        dialog.setHeaderText("登录");
        dialog.setContentText("玩家ID");
        // 传统的获取输入值的方法
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            LoginUtil.name1 = result.get(); 	//保存玩家ID
        } else {
            LoginUtil.name1 = "***";			//为输入任何字符
        }
        // lambda表达式获取输入值
        //result.ifPresent(value -> info.setText(info.getText() + "\nbtn8(lambda) 输入了" + value));

        //添加按钮并注册事件
        for(int i = 0; i < 4; i++) {
            Button button = new Button(Select[i]);
            button.setPrefSize(70, 20);
            switch (i) {
                case 0:
                    button.setOnAction(e -> {
                        /*
                         * 悔棋
                         */

                        // TODO 封装成一个方法
                        nowStack = new Stack<>();
                        Chess  ai,play;
                        //逆置原栈
                        AiStack.ReverseStack();
                        while(!AiStack.stack.empty()) {
                            nowStack.push(AiStack.OutPutStack());
                        }

                        if(!nowStack.empty()) {		//防止栈空报错
                            ai = nowStack.pop();
                        } else {
                            ai = null;
                        }

                        if(ai == null) { 			//玩家先手却没下
                            //重新开始
                            Clearchess();
                            AiStack.ClearStack();
                        } else {
                            Clearchess();

                            if(!nowStack.empty()) {		//防止栈空报错
                                play = nowStack.pop();
                            } else {
                                play = null;
                            }
                            if(play == null) {	//电脑先手下了一步，而玩家没下
                                AiStack.ClearStack();
                            } else {
                                while(!nowStack.empty()) {
                                    ai = nowStack.pop(); 		//电脑下的棋出栈
                                    if(!nowStack.empty()) {		//防止栈空报错
                                        play = nowStack.pop();	//玩家下的棋出栈
                                    } else {
                                        play = null;
                                    }
                                    if(ai != null) {
                                        ai.play();
                                        ai.paintChess(gc);
                                    }
                                    if(play != null) {
                                        play.play();
                                        play.paintChess(gc);
                                    }
                                }

                            }

                        }
                        //逆置原栈
                        AiStack.ReverseStack();
                        //棋盘为空。电脑重新下一步棋
                        if(BoardUtil.ChessPaneEmpty() && BoardUtil.isAIFirst()) {
                            int x = 4+(int)(Math.random()*6);
                            int y = 4+(int)(Math.random()*6);
                            Chess newAi = new Chess(x, y, 'B');
                            newAi.play();				//人机随机下一步
                            newAi.paintChess(gc);		//将电脑下的棋子设置颜色标志
                        }
                    });
                    break;
                case 1:
                    button.setOnAction(e -> {
                        /*
                         * 重开
                         */
                        Clearchess();			        //重绘，并将重置棋子标志

                    });
                    break;
                case 2:
                    button.setOnAction(e -> {

                        /*
                         * 返回
                         */
                        BoardUtil.setAIFirst(false);	//重置先手
                        MainView main_View = new MainView();
                        try {
                            main_View.start(stage);	//返回主界面
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    });
                    break;
                default :
                    button.setOnAction(e -> {
                        /*
                         * 改变先后手
                         */
                        //如果是玩家先手，且棋盘上并未下棋，则改为电脑先手
                        if(!BoardUtil.isAIFirst() && BoardUtil.ChessPaneEmpty()) {
                            BoardUtil.setAIFirst(true);
                            //现在电脑先手
                            int x = 4+(int)(Math.random()*6);
                            int y = 4+(int)(Math.random()*6);
                            Chess newAi = new Chess(x, y, 'B');
                            newAi.play();				//人机随机下一步
                            newAi.paintChess(gc);		//将电脑下的棋子设置颜色标志
                            BoardUtil.setCurrentSide('W');
                        }
                    });
                    break;
            }
            hbox.getChildren().add(button);
        }

        //画界面
        draw(gc);

        vbox = new VBox(hbox,canvas);
        image = new Image("image/AIplay.jpg");
        getChildren().add(new ImageView(image));
        getChildren().add(vbox);
    }

    public Canvas getCanvas() {
        return this.canvas;
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
            gic.setLineWidth(2);		//解决重绘出现重叠问题
            //画横线
            gic.strokeLine(align, y * cell + align, width + align, y * cell + align);
            //画竖线
            gic.strokeLine(y * cell + align, align, y * cell + align, height + align);

        }
        gic.stroke();

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

    /**
     * 重绘界面
     */
    public void Clearchess() {

        BoardUtil.initChess();		            //重置棋盘的落子标志
        BoardUtil.setAIFirst(false);	        //重置先手
        AiStack.ClearStack();                   //重置栈
        gc.clearRect(0,0,650,650);				//清屏
        draw(gc);                               //重绘棋盘
        vbox = new VBox(hbox, canvas);
        getChildren().add(vbox);
    }

    //注册鼠标事件
//    public void SetMouse(AiControl playAction) {
//        canvas.setOnMouseClicked(playAction);
//    }


}
