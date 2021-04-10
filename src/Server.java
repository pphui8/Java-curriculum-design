import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

/* ********************************************
 * 版权所有 (C)2020,JinHui
 *
 * 文件名称：Server.java
 * 内容摘要：网络服务端可运行程序
 * 其它说明：无
 * 当前版本：V1.0
 * 作    者：JinHui
 * 完成日期：2020/11/30
 *
 * 修改记录：添加了计时器和排序功能
 * 修改日期：2020/12/4
 * 版本号：V2.3
 * 修改人：JinHui
 * 修改内容：添加了计时器和排序功能
 ***********************************************/
public class Server {
    //储存用的数据结构
    private static final List<Data> list = new ArrayList<>();
    //文件路径
    private static final String path = System.getProperty("user.dir") + "\\src\\rank.txt";
    //计时器，计时什么时候排序
    Timer timer;
    //主函数
    public static void main(String[] args) {
        init();
        //Test();
        try {
            server_run();
            exit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /* **********************************************
     * 功能描述：测试数据的存取是否正常
     * 输入参数：无
     * 输出参数：储存的玩家数据
     * 返回值：无
     * 其它说明：从rank.txt中读取
     * **********************************************/
    private static void Test() {
        init();
        sort();
        for(Data d : list) System.out.println(d.point);
        try {
            exit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /* **********************************************
     * 功能描述：初始化游戏玩家的数据
     * 输入参数：无
     * 输出参数：储存的玩家数据
     * 返回值：无
     * 其它说明：从rank.txt中读取
     * **********************************************/
    private static void init() {
        try {
            BufferedReader fr = new BufferedReader(new FileReader(new File(path)));
            for(int i = 0; i < 100; i++) {
                String s1 = fr.readLine();
                String s2 = fr.readLine();
                String s3 = fr.readLine();
                list.add(new Data(s1, Integer.parseInt(s2), s3));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    /* **********************************************
     * 功能描述：退出前的保存函数
     * 输入参数：无
     * 输出参数：玩家数据
     * 返回值：无
     * 其它说明：数据写入rank.txt，不存在会在当前文件夹下创建一个，抛出异常Exception
     * **********************************************/
    private static void exit() throws Exception{
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
        Data data;
        for(int i = 0; i < list.toArray().length; i++) {
            data = list.get(i);
            bufferedWriter.write(data.name);
            bufferedWriter.newLine();
            bufferedWriter.write(Integer.toString(data.point));
            bufferedWriter.newLine();
            bufferedWriter.write(data.time);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }
    /* **********************************************
     * 功能描述：对顺序表进行排序的方法
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：按分数高低进行排序
     * **********************************************/
    private static void sort() {
        Collections.sort(list);
    }
    /* **********************************************
     * 功能描述：网络开始运行
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：无
     * **********************************************/
    private static void server_run() throws Exception{
        //创建服务器
        ServerSocket server = new ServerSocket(5000);
        while(true) {
            Socket socket = server.accept();
            //设置输出流
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            //设置输入流
            DataInputStream input = new DataInputStream(socket.getInputStream());
            //读取游戏端传来的三条数据
            Data tmp = new Data();
            String s = (String) input.readUTF();
            tmp.name = s;
            s = (String)input.readUTF();
            tmp.point = Integer.parseInt(s);
            s = (String)input.readUTF();
            tmp.time = s;
            System.out.println(tmp.name + " " + tmp.point + " " + tmp.time);
            list.add(tmp);
            //传给客户端游戏数据
            for (int i = 0; i < 20; ++i) {
                tmp = list.get(i);
                output.writeUTF(tmp.name + " " + tmp.point + " " + tmp.time);
                output.flush();
            }
            output.writeUTF("end");
            output.flush();
            output.flush();
            socket.close();
        }
    }
    /* **********************************************
     * 功能描述：内部类data，即玩家数据类型
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：无
     * **********************************************/
    private static class Data implements Comparable<Data>{
        public String name;
        public int point;
        public String time;
        public Data(String name, int point, String time) {
            this.name = name;
            this.point = point;
            this.time = time;
        }
        public Data() { }
        @Override
        public int compareTo(Data D) {
            return (this.point - D.point);
        }
    }
}