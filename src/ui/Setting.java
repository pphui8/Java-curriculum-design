package ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/* ********************************************
 * 版权所有 (C)2020,JinHui
 *
 * 文件名称：Setting.java
 * 内容摘要：实现设置面板以及设置的功能
 * 其它说明：无
 * 当前版本：无
 * 作    者：JinHui
 * 完成日期：2020/11/30
 * **********************************************/
public class Setting{
    //新建窗口
    private static JFrame set_frame = new JFrame("setting");
    //申明窗口大小
    private static final int SET_FRAME_WIDTH = 300;
    private static final int SET_FRAME_HEIGHT = 400;
    //新建一个下拉条
    //JComboBox language = new JComboBox<>();
    //下拉列表框的内容
    JLabel chinese;
    JLabel english;
    JLabel japanese;

    /* **********************************************
     * 功能描述：打开设置窗口，实现设置功能
     * 输入参数：指定的坐标x, y
     * 输出参数：当前reimu的坐标
     * 返回值：无
     * 其它说明：无
     ***********************************************/
    public static void init() {
        /*给下拉条添加组件
        language.add("chinese");
        language.add("english");
        language.add("japanese");
        //给下拉条注册监听器
        language.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getItemSelectable() != null) {

                }
            }
        });
        //把下拉条放入窗口中
        set_frame.add(language);*/
        //设置窗口属性
        set_frame.setResizable(false);
        set_frame.setSize(SET_FRAME_WIDTH, SET_FRAME_HEIGHT);
        set_frame.setLocationRelativeTo(null);
        set_frame.setVisible(true);
        set_frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                //弹出是否返回主页游戏的对话框
                int result = JOptionPane.showConfirmDialog(set_frame, "返回主页", "touhouDemo", JOptionPane.OK_CANCEL_OPTION);
                if(result == JOptionPane.YES_OPTION) {
                    super.windowClosed(e);
                }
            }
        });
    }
}