package AllView;

import Controller.AiControl;
import Controller.PlayerControl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import utils.AiStack;
import utils.BoardUtil;

public class MainView extends Application{

    private Pane stack;
    private VBox vBox;
    private Image image;
    private static final String[] choose = {"人机对战","玩家对战","游戏日志","游戏简介","退出游戏"};

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setResizable(false);
        stack = new Pane();
        vBox = new VBox();
        image = new Image("/image/k1.gif");
        stack.getChildren().add(new ImageView(image));

        //游戏前的配置
        AiStack.ClearStack();
        BoardUtil.setChess(16);
        BoardUtil.setAIFirst(false);
        BoardUtil.setAlign(70);
        BoardUtil.setCellLen(40);
        BoardUtil.setHeight(560);
        BoardUtil.setWidth(560);
        BoardUtil.initChess();

        String url1 = this.getClass().getClassLoader().getResource("music/back.mp3").toExternalForm();
        Media media = new Media(url1);
        MediaPlayer mPlayer = new MediaPlayer(media);
        mPlayer.play();
        mPlayer.setCycleCount(200);

        for(int i = 0; i < 5; i++) {
            Button button = new Button(choose[i]);
            button.setPrefSize(100, 45);
            switch (i) {
                case 0:
                    button.setOnAction(e -> {
                        /*
                         * 人机对战界面
                         */
                        mPlayer.pause();
                        AiView AIPane = new AiView(primaryStage);
                        AiControl playAction = new AiControl(AIPane.getCanvas());
                        AIPane.getCanvas().setOnMouseClicked(playAction);//添加鼠标事件
                        Scene scene = new Scene(AIPane,700,700);
                        primaryStage.setScene(scene);
                        primaryStage.setTitle("人机对战");
                        primaryStage.show();
                    });
                    break;
                case 1:
                    button.setOnAction(e -> {
                        /*
                         * 玩家对战界面
                         */
                        mPlayer.pause();
                        PlayerView PlayPane = new PlayerView(primaryStage);
                        PlayerControl playerControl = new PlayerControl(PlayPane.getCanvas());
                        PlayPane.getCanvas().setOnMouseClicked(playerControl);
                        //  注册事件
                        Scene scene = new Scene(PlayPane,700,700);
                        primaryStage.setScene(scene);
                        primaryStage.setTitle("玩家对战");
                        primaryStage.show();
                    });
                    break;
                case 2:
                    button.setOnAction(e -> {
                        /*
                         * 玩家日志界面
                         */
                        mPlayer.pause();
                        LogView LogPane = new LogView(primaryStage);
                        Scene scene = new Scene(LogPane,700,700);
                        primaryStage.setScene(scene);
                        primaryStage.setTitle("游戏日志");
                        primaryStage.show();
                    });
                    break;
                case 3:
                    button.setOnAction(e -> {
                        /*
                         * 游戏简介界面
                         */
                        mPlayer.pause();
                        HelpView helpPane = new HelpView(primaryStage);
                        Scene scene = new Scene(helpPane,700,700);
                        primaryStage.setScene(scene);
                        primaryStage.setTitle("游戏简介");
                        primaryStage.show();
                    });
                    break;
                default:
                    button.setOnAction(e -> {
                        System.exit(0);
                    });

            }
            vBox.setLayoutX(400);
            vBox.setLayoutY(250);
            vBox.getChildren().add(button);

        }
        stack.getChildren().add(vBox);
        Scene scene = new Scene(stack,700,700);
        primaryStage.setScene(scene);
        //窗口标题
        primaryStage.setTitle("五子棋");
        primaryStage.show();

    }

}