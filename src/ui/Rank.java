package ui;

import java.awt.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/* ********************************************
 * 版权所有 (C)2020,JinHui
 *
 * 文件名称：rank.java
 * 内容摘要：显示世界排名，并将User信息上传
 * 其它说明：项目要求联网
 * 当前版本：V1.0
 * 作    者：JinHui
 * 完成日期：2020/11/30
 * 其他说明：端口号为5000
 * **********************************************/
public class Rank {
    //新建分数榜窗口
    static JFrame rank_frame = new JFrame();
    //申明窗口大小
    private static final int RANK_FRAME_WIDTH = 300;
    private static final int RANK_FRAME_HEIGHT = 400;
    //创建文本域写服务器端传来的数据
    static JTextArea rankArea = new JTextArea();
    //设置为滚动面板
    static JScrollPane rankPane = new JScrollPane(rankArea);
    //设置刷新按钮
    static JButton refresh = new JButton("刷新");
    //游戏数据是否发送过，避免发送太多次
    private static boolean is_sent = false;
    public static void init() {
        //设置窗口属性
        rank_frame.setSize(RANK_FRAME_WIDTH, RANK_FRAME_HEIGHT);
        rank_frame.setLocationRelativeTo(null);
        rank_frame.setVisible(true);
        //设置文本域属性
        rankArea.setPreferredSize(new Dimension(290, 350));
        rankArea.setLocation(10, 10);
        rankArea.setEditable(false);
        //组装窗口组件
        rank_frame.add(rankPane);
        rank_frame.add(refresh, BorderLayout.SOUTH);
        //按钮注册监听器
        refresh.addActionListener(e -> {
            //如果用户没有登陆
            if(!User.is_login) {
                User.user_name = JOptionPane.showInputDialog(rank_frame, "请先输入用户名", "touhou", JOptionPane.INFORMATION_MESSAGE);
                //当输入了空置或点了取消
                if(User.user_name != null && !Objects.equals(User.user_name, "")) {
                    User.is_login = true;
                }
            }
            else {
                //if(is_end("end")) rankArea.append("aaa");
                rank_run();
            }
        });
    }
    /* **********************************************
     * 功能描述：开始运行
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：网络启动方法
     * **********************************************/
    private static void rank_run() {
        //重置文本域中的信息
        rankArea.setText("");
        rankArea.append(User.user_name + " " + User.high_point);
        rankArea.append("\n");
        //设置输入流
        DataInputStream input;
        //设置输出流
        DataOutputStream output;
        try {
            //计数器
            int cnt = 0;
            //指定服务器ip, 端口号
            Socket socket = new Socket("localhost", 5000);
            //设置输入输出类
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            //设置游戏数据只发送一次，避免数据冗余
            if(!is_sent) {
                //获取电脑当前时间，并发送玩家数据给客户端
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                output.writeUTF(User.user_name);
                output.flush();
                output.writeUTF("" + User.high_point);
                output.flush();
                output.writeUTF(df.format(new Date()));
                is_sent = true;
            }
            //收取来自服务器端的数据，写到文本域中
            String s;
            do {
                cnt++;
                rankArea.append(cnt + " ");
                s = (String)input.readUTF();
                rankArea.append(s + "\n");
            } while(!is_end(s));
            //关闭
            output.flush();
            output.close();
            input.close();
            socket.close();
        } catch(ConnectException ce) {  //连接不成功时的处理
            rankArea.append("连接失败，请检查网络设置\n");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    /* **********************************************
     * 功能描述：判断是否读到结尾
     * 输入参数：无
     * 输出参数：无
     * 返回值：boolean
     * 其它说明：读到"end"就返回true，其他false
     * **********************************************/
    private static boolean is_end(String s) {
        return s.startsWith("end");
    }
}