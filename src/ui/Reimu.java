/* ********************************************
 * 版权所有 (C)2020,JinHui
 *
 * 文件名称：reimu.java
 * 内容摘要：记录主角基本信息
 * 其它说明：无
 * 当前版本：V1.0
 * 作    者：JinHui
 * 完成日期：2020/11/30
 ***********************************************/
package ui;

public class Reimu {
    //人物当前的位置
    public static int x;
    public static int y;
    //人物图像的大小
    public static final int PIC_X = 31;
    public static final int PIC_Y = 49;
    //图片文件名
    public static String name = null;

    /***********************************************
     * 功能描述：移动reimu到指定位置
     * 输入参数：指定的坐标x, y
     * 输出参数：当前reimu的坐标
     * 返回值：无
     * 其它说明：无
     ***********************************************/
    public void move(int x, int y) {
        Reimu.x = x;
        Reimu.y = y;
    }
}
