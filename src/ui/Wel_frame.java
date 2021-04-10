package ui;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/* ********************************************
 * 版权所有 (C)2020,JinHui
 *
 * 文件名称：Wel_frame.java
 * 内容摘要：游戏欢迎界面
 * 其它说明：调用Wel_frame方法打开界面
 * 当前版本：V2.0
 * 作    者：JinHui
 * 完成日期：2020/11/30
 *
 * 修改记录：修改界面排列方式
 * 修改日期：2020/11/25
 * 版本号：V2.0
 * 修改人：JinHui
 * 修改内容：将从前的项目排列由之前的Layeredpane改为画布内排列，鼠标控制改为键鼠结合使用
 *
 * 修改记录：修正了鼠标点击窗口关闭时无法正确的关闭页面的bug
 * 修改日期：2020/11/25
 * 版本号：V2.1
 * 修改人：JinHui
 * 修改内容：frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
 *         添加了该行代码，窗口不会被默认关闭了
 *
 * 修改记录：完善了窗口监听机制
 * 修改日期：2020/11/25
 * 版本号：V2.2
 * 修改人：JinHui
 * 修改内容：增加了一个监听窗口是否是活动的监听器，窗口被重新打开时会显示正确的内容了
 *
 * 修改记录：把画布修改为了JPanel类，解决了界面闪烁问题
 * 修改日期：2020/12/4
 * 版本号：V2.3
 * 修改人：JinHui
 * 修改内容：把游戏修改为了JPanel类，解决了界面闪烁问题
 * **********************************************/
public class Wel_frame {
    //创建窗口
    public static JFrame frame = new JFrame("touhouDemo");
    //计时器，用来延迟打开窗口
    Timer timer = new Timer();
    //窗口属性（大小）
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 668;
    //是否选择了某一项的数组
    private static boolean is_start = true;
    private static boolean is_setting = false;
    private static boolean is_rank = false;
    private static boolean is_exit = false;
    //config.txt文件读写操作使用的类
    User user = new User();
    //dummy的表示数
    //private static int dummy = 0;
    //设置标识球的大小
    private static final int CHOICE_SIZE = 16;
    //新建画布
    canvas backgroundCanvas = new canvas();
    //是否是加载状态
    private static boolean loading = false;
    //动态获取图像文件目录
    private static final String path = System.getProperty("user.dir") + "\\src\\Wel_Frame";
    //使用哈希图存放文件索引
    public static HashMap<String, BufferedImage> pic = new HashMap<>();
    //创建图片文件
    static {
        //读取该目录下的所有图片文件
        File[] files = new File(path).listFiles();
        //读取图片并存储与哈希图
        try {
            assert files != null;
            for(File file : files) {
                pic.put(file.getName(), ImageIO.read(file));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    /* **********************************************
     * 功能描述：初始化游戏欢迎界面
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：初始化界面参数
     * **********************************************/
    public void init() {
        //设置画布属性
        backgroundCanvas.setPreferredSize(new Dimension(192, 108));
        //把画布加入窗口
        frame.add(backgroundCanvas);
        //设置窗口的基本属性
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //焦点聚焦于画布
        backgroundCanvas.requestFocus();
        /* **********************************************
         * 功能描述：给画布注册监听器
         * 输入参数：无
         * 输出参数：无
         * 返回值：无
         * 其它说明：注册后监听的键值为UP, DOWN, ENTER
         * **********************************************/
        backgroundCanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keycode = e.getKeyCode();
                //System.out.println(keycode);
                //UP
                if(keycode == KeyEvent.VK_UP) {
                    if(is_exit) {
                        is_exit = false;
                        is_rank = true;
                        backgroundCanvas.repaint();
                    } else if(is_rank) {
                        is_rank = false;
                        is_setting = true;
                        backgroundCanvas.repaint();
                    } else if(is_setting) {
                        is_setting = false;
                        is_start = true;
                        backgroundCanvas.repaint();
                    }
                }
                //DOWN
                else if(keycode == KeyEvent.VK_DOWN) {
                    if(is_start) {
                        is_start = false;
                        is_setting = true;
                        backgroundCanvas.repaint();
                    } else if(is_setting) {
                        is_setting = false;
                        is_rank = true;
                        backgroundCanvas.repaint();
                    } else if(is_rank) {
                        is_rank = false;
                        is_exit = true;
                        backgroundCanvas.repaint();
                    }
                }
                //ENTER
                else if(keycode == KeyEvent.VK_ENTER) {
                    //start
                    if(is_start) {
                        loading = true;
                        backgroundCanvas.repaint();
                        timer.schedule(new task(), 3000);
                    }
                    //setting
                    else if(is_setting) {
                        Setting.init();
                    }
                    //rank
                    else if(is_rank) {
                        Rank.init();
                    }
                    //exit
                    else if(is_exit) {
                        //弹出是否退出游戏的对话框
                        int result = JOptionPane.showConfirmDialog(frame, "退出游戏", "touhouDemo", JOptionPane.OK_CANCEL_OPTION);
                        if(result == JOptionPane.YES_OPTION) {
                            //保存游戏数据文件
                            user.exit();
                            System.exit(0);
                        }
                    }
                }
            }
        });
        /* **********************************************
         * 功能描述：给窗口的退出键注册监听器
         * 输入参数：无
         * 输出参数：无
         * 返回值：无
         * 其它说明：退出前多加一个询问, 同时对右上和系统菜单的关闭按钮注册监听器
         * **********************************************/
        frame.addWindowListener(new WindowAdapter() {
            //从右上角关闭时调用
            @Override
            public void windowClosed(WindowEvent e) {
                //弹出是否返回主页游戏的对话框
                int result = JOptionPane.showConfirmDialog(frame, "退出游戏", "touhouDemo", JOptionPane.OK_CANCEL_OPTION);
                if(result == JOptionPane.YES_OPTION) {
                    //保存游戏数据文件
                    user.exit();
                    System.exit(0);
                }
            }
            //从系统菜单中关闭时调用
            @Override
            public void windowClosing(WindowEvent e) {
                //弹出是否返回主页游戏的对话框
                int result = JOptionPane.showConfirmDialog(frame, "退出游戏", "touhouDemo", JOptionPane.OK_CANCEL_OPTION);
                if(result == JOptionPane.YES_OPTION) {
                    //保存游戏数据文件
                    user.exit();
                    System.exit(0);
                }
            }
            //窗口被重新调用时要重置一部分变量
            @Override
            public void windowActivated(WindowEvent e) {
                is_start = true;
                is_setting = false;
                is_rank = false;
                is_exit = false;
                loading = false;
                //刷新页面
                backgroundCanvas.repaint();
            }
        });
    }
    /* **********************************************
     * 功能描述：设置计时器用的task，实现delay后要执行的操作
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：图片来源于哈希图pic，读取自Wel_Frame包中
     * **********************************************/
    private static class task extends TimerTask {
        @Override
        public void run() {
            frame.setVisible(false);
            new game().init();
        }
    }
    /* **********************************************
     * 功能描述：重写画布类，以达到界面效果
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：图片来源于哈希图pic，读取自Wel_Frame包中
     * **********************************************/
    private static class canvas extends JPanel {
        @Override
        public void paint(Graphics g) {
            g.drawImage(pic.get("bk.png"), 0, 0, FRAME_WIDTH, FRAME_HEIGHT, null);
            if(!loading) {
                if(is_start) g.drawImage(pic.get("choice.png"), 650 + CHOICE_SIZE, 50, 50, 50, null);
                if(is_setting) g.drawImage(pic.get("choice.png"), 650 + CHOICE_SIZE, 100, 50, 50, null);
                if(is_rank) g.drawImage(pic.get("choice.png"), 650 + CHOICE_SIZE, 150, 50, 50, null);
                if(is_exit) g.drawImage(pic.get("choice.png"), 650 + CHOICE_SIZE, 200, 50, 50, null);
                g.drawImage(pic.get("start.png"), 400, 50, 250, 51, null);
                g.drawImage(pic.get("setting.png"), 400, 100, 167, 50, null);
                g.drawImage(pic.get("rank.png"), 400, 150, 120, 50, null);
                g.drawImage(pic.get("exit.png"), 400, 200, 100, 50, null);
            }
            else {
                g.drawImage(pic.get("loading.png"), 800, 400, 256, 256, null);
                /*switch(dummy) {
                    case 0 -> g.drawImage(pic.get("dummy.png"), 1056, 460, 4, 4, null);
                    case 1 -> {
                        g.drawImage(pic.get("dummy.png"), 1056, 460, 4, 4, null);
                        g.drawImage(pic.get("dummy.png"), 1066, 460, 4, 4, null);
                    }
                    case 2 -> {
                        g.drawImage(pic.get("dummy.png"), 1056, 460, 4, 4, null);
                        g.drawImage(pic.get("dummy.png"), 1066, 460, 4, 4, null);
                        g.drawImage(pic.get("dummy.png"), 1076, 460, 4, 4, null);
                    }
                }*/
            }
        }
    }
}