package AllView;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HelpView extends Pane{

    private Stage stage;
    private Image image;
    private VBox vBox;

    public HelpView(Stage stage) {

        vBox = new VBox();
        vBox.setPrefSize(500,600);
        image = new Image("image/Help_View.jpg");
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

        String[] labelname = {" (1)对局双方各执一色棋子。" ,
                " (2)空棋盘开局。" ,
                " (3)黑先、白后,交替下子,每次只能下一子。" };
        for(int i = 0; i < 3; i ++) {
            Label label = new Label(labelname[i]);
            label.setWrapText(true);                    //换行
            label.setTextFill(Color.YELLOW);    //字体颜色
            label.setFont(new Font("Cambria", 28));
            vBox.getChildren().add(label);
        }
        vBox.setLayoutX(150);
        vBox.setLayoutY(400);
        vBox.getChildren().add(button);
        getChildren().add(vBox);
    }

}
