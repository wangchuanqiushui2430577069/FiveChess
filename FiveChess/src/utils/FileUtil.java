package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 向文件追加写入内容
     *
     * @param name
     * @param FileName
     * @param flag
     */
    public static void WriteToFile(String name, String FileName, boolean flag) {
        String date = sdf.format(new Date()).toString(); //获取当前时间
        FileWriter fw = null;
        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            File f = new File(FileName);
            fw = new FileWriter(f, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        //追加内容
        if(flag)
            pw.println( "玩家： " + name + "     Win     "  + "   " + date);
        else
            pw.println( "玩家： " + name + "     Fail     "  + "   "  + date);

        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空文件内容
     *
     * @param FileName
     */
    public static void ClearFile(String FileName) {

        File file = new File(FileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
