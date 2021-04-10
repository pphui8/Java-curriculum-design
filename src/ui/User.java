package ui;

import java.io.*;
import java.util.Objects;

/* ********************************************
 * 版权所有 (C)2020,JinHui
 *
 * 文件名称：User.java
 * 内容摘要：收录游戏数据，方便存取
 * 其它说明：存储文件对象为config.txt
 * 当前版本：V1.0
 * 作    者：JinHui
 * 完成日期：2020/11/30
 *
 * 修改记录：修改文件读取方法
 * 修改日期：2020/11/25
 * 版本号：V2.0
 * 修改人：JinHui
 * 修改内容：加入了一个判断是否登陆的变量，使high_point可以本地存储
 * **********************************************/
public class User {
    //网络用静态常量
    public static boolean is_login = false;
    public static String user_name = null;
    public static int high_point = 0;
    //游戏用静态常量
    public static long tmp_point = 0;
    //储存玩家数据的文件位置
    private static final String path = System.getProperty("user.dir") + "\\src\\config.txt";
    //游戏的难度
    public static final int EASY = 30;
    public static final int NORMAL = 20;
    public static final int HARD = 10;
    /* **********************************************
     * 功能描述：初始化游戏数据
     * 输入参数：游戏各项数据
     * 输出参数：从config.txt进行数据读取
     * 返回值：无
     * 其它说明：当config.txt为空时，即没有数据
     * **********************************************/
    public void init() {
        try {
            BufferedReader fr = new BufferedReader(new FileReader(new File(path)));
            String tmp = fr.readLine();
            //玩家未登录
            if(Objects.equals(tmp, "false")) {
                is_login = false;
            }
            //玩家已登录
            else {
                is_login = true;
                tmp = fr.readLine();
                user_name = tmp;
            }
            tmp = fr.readLine();
            high_point = Integer.parseInt(tmp);
            fr.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /* **********************************************
     * 功能描述：退出时的数据保存
     * 输入参数：无
     * 输出参数：游戏数据
     * 返回值：无
     * 其它说明：向config.txt输出游戏数据
     * **********************************************/
    public void exit() {
        if(is_login){
            try {
                BufferedWriter wr = new BufferedWriter(new FileWriter(path));
                if(is_login) {
                    wr.write("true");
                    wr.newLine();
                    wr.write(user_name);
                }
                else {
                    wr.write("false");
                }
                wr.newLine();
                wr.write(String.valueOf(high_point));
                wr.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
