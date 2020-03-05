package Log;

import Interfaces.Log;
import utils.FileUtil;

public class PlayLog implements Log {

    //玩家ID
    private String name1;

    public PlayLog() {

    }
    @Override
    public void InputLog(boolean flag) {
        FileUtil.WriteToFile(name1, "PlayLog.txt", flag);
    }

    @Override
    public void ClearLog() {
        FileUtil.ClearFile("PlayLog.txt");
    }
    public String getName1() {
        return name1;
    }
    public void setName1(String name1) {
        this.name1 = name1;
    }


}
