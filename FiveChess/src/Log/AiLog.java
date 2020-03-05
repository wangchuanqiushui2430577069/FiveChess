package Log;

import Interfaces.Log;
import utils.FileUtil;

/**
 * 日志实体类
 * @author AIPlay
 *
 */
public class AiLog implements Log {

    //玩家ID
    private String name1;
    //电脑
    private String name2;

    public AiLog(String name1) {
        this.name1 = name1;
        this.name2 = "人机二代";
    }

    public AiLog() {
        this.name2 = "人机二代";
    }


    //调用文件工具类方法
    @Override
    public void InputLog(boolean flag) {
        FileUtil.WriteToFile(name1, "AiLog.txt", flag);
    }

    @Override
    public void ClearLog() {
        FileUtil.ClearFile("AiLog.txt");
    }

    //构造器与修改器
    public String getName1() { return name1; }

    public void setName1(String name1) { this.name1 = name1; }

    public String getName2() { return name2; }

    public void setName2(String name2) { this.name2 = name2; }
}
