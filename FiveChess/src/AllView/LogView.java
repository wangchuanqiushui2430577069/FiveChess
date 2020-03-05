package AllView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LogView extends Pane{

    private Stage stage;
    private Image image;
    private VBox vBox;
    private HBox hBox;
    ListView<String> list = null;		//下滑列表
    ListView<String> list2 = null;
    ObservableList<String> mylist = null;	//监听列表
    ObservableList<String> mylist2 = null;

    public LogView(Stage stage) {

        list = new ListView<>();
        list.setEditable(false);	//设置成不可编辑
        list.setPrefSize(700, 200);
        list2 = new ListView<>();
        list2.setEditable(false);	//设置成不可编辑
        list2.setPrefSize(700, 200);

        hBox = new HBox();
        vBox = new VBox();
        hBox.setPrefHeight(100);
        image = new Image("image/hero.jpg");
        getChildren().add(new ImageView(image));
        this.stage = stage;
        Button button = new Button("返回首页");
        button.setPrefSize(90, 40);
        button.setOnAction(e->{
            MainView main_View = new MainView();
            try {
                main_View.start(stage);//返回主界面
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        Button button2 = new Button("清空日志");
        button2.setPrefSize(90, 40);
        button2.setOnAction(e->{
            try {
                //TODO 刷新日志界面
                ClearAiLog();		//清空日志文件
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        hBox.getChildren().add(button);
        hBox.getChildren().add(button2);
        vBox.getChildren().add(hBox);

        mylist = FXCollections.observableArrayList("								"
                + "对战电脑日志");
        //mylist.remove(0);
        File file = new File("AiLog.txt");		// 配置的文件放在包下不易进行修改
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        String line = null;
        try {
            while ((line = br.readLine()) != null ) {
                mylist.add(line);
            }
        } catch (IOException e1) {

            e1.printStackTrace();
        }
        list.setItems(mylist);

        mylist2 = FXCollections.observableArrayList("								"
                + "对战玩家日志");
        File file2 = new File("PlayLog.txt");		// 配置的文件放在包下不易进行修改
        BufferedReader br2 = null;
        try {
            br2 = new BufferedReader(new FileReader(file2));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        String line2 = null;
        try {
            while ((line2 = br2.readLine()) != null ) {
                mylist2.add(line2);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        list2.setItems(mylist2);


        vBox.getChildren().add(list);
        vBox.getChildren().add(list2);
        getChildren().add(vBox);

    }

    private void ClearAiLog() throws Exception {
        Class<?> clazz = null;
        Class<?> clazz2 = null;
        try {
            clazz = Class.forName("Log.AiLog");  //运用反射机制
            Method method = clazz.getMethod("ClearLog");
            method.invoke(clazz.newInstance());
            clazz2 = Class.forName("Log.PlayLog");  //运用反射机制
            Method method2 = clazz2.getMethod("ClearLog");
            method2.invoke(clazz2.newInstance());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
