package ui;

import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.*;
/* ********************************************
 * 版权所有 (C)2020,JinHui
 *
 * 文件名称：game.java
 * 内容摘要：游戏主体界面
 * 其它说明：无
 * 当前版本：V1.0
 * 作    者：JinHui
 * 完成日期：2020/11/30
 *
 * 修改记录：修正了鼠标点击窗口关闭时无法正确的关闭页面的bug
 * 修改日期：2020/11/25
 * 版本号：V2.1
 * 修改人：JinHui
 * 修改内容：frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
 *         添加了该行代码，使窗口在关闭时不会停止整个程序的运行而是正确的打开主页面
 *
 * 修改记录：把游戏画布修改为了JPanel类，解决了界面闪烁问题
 * 修改日期：2020/12/4
 * 版本号：V2.3
 * 修改人：JinHui
 * 修改内容：把游戏画布修改为了JPanel类，解决了界面闪烁问题
 *
 * 修改记录：增强游戏可玩性
 * 修改日期：2020/12/12
 * 版本号：V2.3
 * 修改人：JinHui
 * 修改内容：游戏现在可运行，有分数等。
 * **********************************************/
public class game {
    //新建窗口
    JFrame game_frame = new JFrame("touhou game");
    //申明窗口大小
    private static final int GAME_FRAME_WIDTH = 1014;
    private static final int GAME_FRAME_HEIGHT = 750;
    //申明游戏和计分板面板大小
    private static final int GAME_WIDTH = 700;
    private static final int GAME_HEIGHT = 750;
    private static final int BOARD_WIDTH = 314;
    private static final int BOARD_HEIGHT = 750;
    //设置难易度, 也就是帧数
    public static int Hard_Rank = User.EASY;
    //设置玩家分数器
    private static int point = 0;
    //计分的面板
    JLabel Point_Label = new JLabel();
    //新建游戏面板
    MyJPanel game_panel = new MyJPanel();
    //新建分数画布
    Board_Canvas board_canvas = new Board_Canvas();
    //设置主人公移动速度
    private static final int Speed_x = 18;
    private static final int Speed_y = 18;
    //BOSS的移动速度
    private static int BOSS_SPEED = 3;
    //主人公子弹的移动速度
    private static final int PLAYER_BULLET_SPEED = 20;
    private static final int PLAYER_BULLET_HRI_SPEED = 5;
    //BOSS公子弹的移动速度
    private static final int BOSS_BULLET_SPEED = 6;
    //主人公子弹的修正参数
    private static final int PLAYER_BULLET_FIX = 7;
    //Boss子弹修正参数
    private static final int BOSS_BULLET_FIX = 0;
    //申明计时器
    Timer timer;
    Timer player_timer;
    Timer Boss_timer;
    //建立玩家弹药库
    static java.util.List<Player_bullet> playerBullets;
    //建立boss的弹药库
    static java.util.List<Boss_bullet> bossBullets;
    //动态获取图像文件目录
    private static final String path_bk = System.getProperty("user.dir") + "\\src\\bk";
    private static final String path_boss = System.getProperty("user.dir") + "\\src\\boss";
    private static final String path_player = System.getProperty("user.dir") + "\\src\\player";
    //使用哈希图存放文件索引
    private static final HashMap<String, BufferedImage> game_bk = new HashMap<>();
    private static final HashMap<String, BufferedImage> game_boss = new HashMap<>();
    private static final HashMap<String, BufferedImage> game_player = new HashMap<>();
    //创建图片文件
    static {
        //读取该目录下的所有图片文件
        File[] files_bk = new File(path_bk).listFiles();
        File[] files_boss = new File(path_boss).listFiles();
        File[] files_player = new File(path_player).listFiles();
        //读取图片并存储与哈希图
        try {
            assert files_bk != null;
            assert files_boss != null;
            assert files_player != null;
            for(File file : files_bk) {
                //System.out.println(file.getName());
                game_bk.put(file.getName(), ImageIO.read(file));
            }
            for(File file : files_boss) {
                //System.out.println(file.getName());
                game_boss.put(file.getName(), ImageIO.read(file));
            }
            for(File file : files_player) {
                //System.out.println(file.getName());
                game_player.put(file.getName(), ImageIO.read(file));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /***********************************************
     * 功能描述：初始化游戏面板，被调用后开始游戏进程
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：无
     ***********************************************/
    public void init() {
        //重置静态变量
        point = 0;
        Point_Label.setFont(new Font("仿宋", Font.PLAIN, 26));
        //把分数板加到窗口北边
        Point_Label.setText("POINT:" + point);
        game_frame.add(Point_Label, BorderLayout.NORTH);
        //设置游戏画布的属性
        game_panel.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        //把game画布加入box左边(中间）
        game_frame.add(game_panel, BorderLayout.CENTER);
        //设置计分板画布的属性
        board_canvas.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        //把bord画布加入box右边
        game_frame.add(board_canvas, BorderLayout.EAST);
        //设置窗口属性
        game_frame.setSize(GAME_FRAME_WIDTH, GAME_FRAME_HEIGHT);
        game_frame.setLocationRelativeTo(null);
        game_frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        game_frame.setVisible(true);
        //给画布注册监听器
        game_panel.requestFocus();
        add_listener(game_panel);
        add_listener(board_canvas);
        //初始化玩家及boss的位置
        Reimu.name = "player.png";
        Reimu.x = 350;
        Reimu.y = 680;
        Boss.name = "boss.png";
        Boss.x = 350;
        Boss.y = 150;
        game_panel.repaint();
        //给窗口注册监听器
        game_frame.addWindowListener(new WindowAdapter() {
            //从右上角关闭时调用
            @Override
            public void windowClosing(WindowEvent e) {
                //更新游戏最高分
                if(point > User.high_point) User.high_point = point;
                Wel_frame.frame.setVisible(true);
                game_frame.setVisible(false);
                super.windowClosing(e);
            }
            //从系统菜单中关闭时调用
            @Override
            public void windowClosed(WindowEvent e) {
                //更新游戏最高分
                if(point > User.high_point) User.high_point = point;
                Wel_frame.frame.setVisible(true);
                game_frame.setVisible(false);
                super.windowClosing(e);
            }
        });
        //重置计时器
        timer = null;
        player_timer = null;
        Boss_timer = null;
        fight();
    }
    /***********************************************
     * 功能描述：游戏开始打斗
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：无
     ***********************************************/
    private void fight() {
        //重置一部分静态区的的属性
        playerBullets = null;
        bossBullets = null;
        playerBullets = new LinkedList<>();
        bossBullets = new LinkedList<>();
        //重设任务和Boss的位置
        Reimu.x = 350;
        Reimu.y = 680;
        Boss.x = 350;
        Boss.y = 150;
        //重置静态变量
        point = 0;
        //计时器开始
        timer = new Timer(Hard_Rank, task);
        timer.start();
        //玩家发射子弹的计时器
        player_timer = new Timer(80, player_task);
        player_timer.start();
        //Boss发射子弹的计时器
        Boss_timer = new Timer(500, Boss_task);
        Boss_timer.start();
    }
    /***********************************************
     * 功能描述：boss发射一颗子弹
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：boss的子弹库的表被增加了一颗子弹
     ***********************************************/
    private void boss_shoot() {
        //创建子弹
        //中
        Boss_bullet bullet1 = new Boss_bullet(Boss.x + BOSS_BULLET_FIX, Boss.y, 1);
        //左
        Boss_bullet bullet2 = new Boss_bullet(Boss.x + BOSS_BULLET_FIX, Boss.y, 2);
        //右
        Boss_bullet bullet3 = new Boss_bullet(Boss.x + BOSS_BULLET_FIX, Boss.y, 3);
        //子弹加入弹药库
        bossBullets.add(bullet1);
        bossBullets.add(bullet2);
        bossBullets.add(bullet3);
    }
    /***********************************************
     * 功能描述：发射一颗子弹
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：子弹库的表被增加了一颗子弹
     ***********************************************/
    private void shoot() {
        //创建子弹
        //中
        Player_bullet bullet1 = new Player_bullet(Reimu.x - PLAYER_BULLET_FIX, Reimu.y, 2);
        //右
        Player_bullet bullet2 = new Player_bullet(Reimu.x, Reimu.y, 1);
        //左
        Player_bullet bullet3 = new Player_bullet(Reimu.x - PLAYER_BULLET_FIX * 2, Reimu.y, 3);
        //子弹加入弹药库
        playerBullets.add(bullet1);
        playerBullets.add(bullet2);
        playerBullets.add(bullet3);
    }
    /***********************************************
     * 功能描述：给面板添加一个键盘监听器
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：监听键值为上下左右
     ***********************************************/
    private void add_listener(JPanel panel) {
        //给组件添加一个键盘监听器
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keycode = e.getKeyCode();
                //System.out.println(keycode);
                if(keycode == KeyEvent.VK_UP) {
                    if(Reimu.y - 40 > 0) Reimu.y -= Speed_y;
                }
                else if(keycode == KeyEvent.VK_DOWN) {
                    if(Reimu.y + 40 < GAME_HEIGHT - 10) Reimu.y += Speed_y;
                }
                else if(keycode == KeyEvent.VK_LEFT) {
                    if(Reimu.x - 40 > 0) Reimu.x -= Speed_x;
                }
                else if(keycode == KeyEvent.VK_RIGHT) {
                    if(Reimu.x + 40 < GAME_WIDTH) Reimu.x += Speed_x;
                }
                else if(keycode == KeyEvent.VK_ESCAPE) {
                    //更新游戏最高分
                    if(point > User.high_point) User.high_point = point;
                    Wel_frame.frame.setVisible(true);
                    game_frame.setVisible(false);
                }
            }
        });

    }
    /***********************************************
     * 功能描述：给面板添加一个键盘监听器
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：监听键值为上下左右，与上一个内部类为重写关系
     ***********************************************/
    private void add_listener(Canvas canvas) {
        //给组件添加一个键盘监听器
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                int keycode = e.getKeyCode();
                //System.out.println(keycode);
                if(keycode == KeyEvent.VK_UP) {
                    if(Reimu.y - 40 > 0) Reimu.y -= Speed_y;
                }
                else if(keycode == KeyEvent.VK_DOWN) {
                    if(Reimu.y + 40 < GAME_HEIGHT) Reimu.y += Speed_y;
                }
                else if(keycode == KeyEvent.VK_LEFT) {
                    if(Reimu.x - 40 > 0) Reimu.x -= Speed_x;
                }
                else if(keycode == KeyEvent.VK_RIGHT) {
                    if(Reimu.x + 40 < GAME_WIDTH) Reimu.x += Speed_x;
                }
                else if(keycode == KeyEvent.VK_ESCAPE) {
                    Point_Label.setText("游戏暂停!");
                    //更新游戏最高分
                    if(point > User.high_point) User.high_point = point;
                    Wel_frame.frame.setVisible(true);
                    game_frame.setVisible(false);
                }
            }
        });

    }
    /***********************************************
     * 功能描述：重新开始时调用的方法
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：无
     ***********************************************/
    private void restart() {
        //重设任务和Boss的位置
        Reimu.x = 350;
        Reimu.y = 680;
        Boss.x = 350;
        Boss.y = 150;
        //重置静态变量
        point = 0;
        playerBullets.clear();
        bossBullets.clear();
        Point_Label.setText("POINT:" + point);
        //重新开始计时器
        timer.start();
        player_timer.start();
        Boss_timer.start();
    }

    /* **********************************************
     * 功能描述：计时器用的监听器，用于实现稳定的帧率
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：是一个监听器对象
     ***********************************************/
    ActionListener task = e -> {
        //玩家子弹自动移动
        for(int i = 0; i < playerBullets.toArray().length; ++i) {
            Player_bullet player_bullet = playerBullets.get(i);
            if(player_bullet.y + Player_bullet.BULLET_PIC_Y / 2 > GAME_HEIGHT) {
                //超限时删除此子弹
                playerBullets.remove(i);
            }
            //碰到Boss时删除此子弹，加一分，重绘label的分值
            else if(player_bullet.x > Boss.x - Boss.PIC_X / 2 && player_bullet.x < Boss.x + Boss.PIC_X / 2 && player_bullet.y > Boss.y - Boss.PIC_Y / 2 && player_bullet.y < Boss.y + Boss.PIC_Y / 2) {
                playerBullets.remove(i);
                point++;
                Point_Label.setText("POINT:" + point);
            }
            else {
                switch(player_bullet.bullet_type) {
                    case 1 -> player_bullet.move(player_bullet.x - PLAYER_BULLET_HRI_SPEED, player_bullet.y - PLAYER_BULLET_SPEED);
                    case 2 -> player_bullet.move(player_bullet.x, player_bullet.y - PLAYER_BULLET_SPEED);
                    case 3 -> player_bullet.move(player_bullet.x + PLAYER_BULLET_HRI_SPEED, player_bullet.y - PLAYER_BULLET_SPEED);
                }
            }
        }
        //Boss子弹自动移动
        for(int i = 0; i < bossBullets.toArray().length; ++i) {
            Boss_bullet boss_bullet = bossBullets.get(i);
            //超限时删除此子弹
            if(boss_bullet.y > GAME_FRAME_HEIGHT) {
                bossBullets.remove(i);
            }
            //Boss子弹碰到玩家后游戏结束
            else if(boss_bullet.x > Reimu.x - Reimu.PIC_X / 2 && boss_bullet.x < Reimu.x + Reimu.PIC_X / 2 && boss_bullet.y > Reimu.y - Reimu.PIC_Y / 2 && boss_bullet.y < Reimu.y + Reimu.PIC_Y / 2) {
                Point_Label.setText("游戏结束!");
                game_panel.repaint();
                timer.stop();
                if(point > User.high_point) {
                    int res = JOptionPane.showConfirmDialog(game_frame, "高分！" + point + " 重新开始?", "游戏结束", JOptionPane.OK_CANCEL_OPTION);
                    if(res == JOptionPane.OK_OPTION) {
                        //更新最高分
                        User.high_point = point;
                        restart();
                    } else {
                        //更新游戏最高分
                        User.high_point = point;
                        Wel_frame.frame.setVisible(true);
                        game_frame.setVisible(false);
                    }
                }
                else {
                    int res = JOptionPane.showConfirmDialog(game_frame, "分数" + point + " 重新开始?", "游戏结束", JOptionPane.OK_CANCEL_OPTION);
                    if(res == JOptionPane.OK_OPTION) {
                        restart();
                    } else {
                        Wel_frame.frame.setVisible(true);
                        game_frame.setVisible(false);
                    }
                }
            }
            else {
                switch(boss_bullet.type) {
                    case 1 -> boss_bullet.move(boss_bullet.x - PLAYER_BULLET_HRI_SPEED, boss_bullet.y + BOSS_BULLET_SPEED);
                    case 2 -> boss_bullet.move(boss_bullet.x, boss_bullet.y + BOSS_BULLET_SPEED);
                    case 3 -> boss_bullet.move(boss_bullet.x + PLAYER_BULLET_HRI_SPEED, boss_bullet.y + BOSS_BULLET_SPEED);
                }
            }
        }
        //Boss自动移动, 到边界时自动换方向
        if(Boss.x <= 0 || Boss.x >= (GAME_WIDTH - Boss.PIC_X / 2)) {
            BOSS_SPEED = -BOSS_SPEED;
        }
        Boss.x += BOSS_SPEED;
        //重绘页面
        game_panel.repaint();
    };
    /* **********************************************
     * 功能描述：timer类所使用的监听器
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：调用方法发射一次子弹
     ***********************************************/
    ActionListener player_task = e -> {
        //发射一次子弹
        shoot();
    };
    /* **********************************************
     * 功能描述：timer类所使用的监听器
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：调用方法发射一次子弹
     ***********************************************/
    ActionListener Boss_task = e -> {
        //发射一次子弹
        boss_shoot();
    };

    /* **********************************************
     * 功能描述：重写面板，实现游戏内容画面
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：无
     ***********************************************/
    private static class MyJPanel extends JPanel {
        @Override
        //画背景
        public void paint(Graphics g) {
            g.drawImage(game_bk.get("world.png"), 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
            //绘画Boss
            g.drawImage(game_boss.get(Boss.name), Boss.x - Boss.PIC_X / 2, Boss.y - Boss.PIC_Y / 2, Boss.PIC_X, Boss.PIC_Y, null);
            //绘画玩家
            g.drawImage(game_player.get(Reimu.name), Reimu.x - Reimu.PIC_X / 2, Reimu.y - Reimu.PIC_Y / 2, Reimu.PIC_X, Reimu.PIC_Y, null);
            //画子弹
            for(Player_bullet player_bullet : playerBullets) {
                g.drawImage(game_player.get("bullet.png"), player_bullet.x, player_bullet.y, Player_bullet.BULLET_PIC_X, Player_bullet.BULLET_PIC_Y, null);
            }
            for(Boss_bullet bossbullets : bossBullets) {
                g.drawImage(game_boss.get("bullet.png"), bossbullets.x - Boss_bullet.BULLET_PIC_X / 2, bossbullets.y - Boss_bullet.BULLET_PIC_Y / 2, Boss_bullet.BULLET_PIC_X, Boss_bullet.BULLET_PIC_Y, null);
            }
        }
    }
    /***********************************************
     * 功能描述：重写画布类，实现计分板内容画面
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：无
     ***********************************************/
    private static class Board_Canvas extends Canvas {
        @Override
        public void paint(Graphics gx) {
            gx.drawImage(game_bk.get("right.png"), 0, 0, BOARD_WIDTH, BOARD_HEIGHT, null);
        }
    }
}