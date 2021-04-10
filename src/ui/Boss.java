/* ********************************************
 * 版权所有 (C)2020,JinHui
 *
 * 文件名称：Boss.java
 * 内容摘要：记录Boss的基本信息
 * 其它说明：无
 * 当前版本：V1.0
 * 作    者：JinHui
 * 完成日期：2020/11/30
 ***********************************************/
package ui;
public class Boss {
    //人物当前的位置
    public static int x;
    public static int y;
    //人物图像的大小
    public static final int PIC_X = 77;
    public static final int PIC_Y = 110;
    //图片文件名
    public static String name;

    /***********************************************
     * 功能描述：修改Boss的位置
     * 输入参数：Boss移动的目标位置x, y
     * 输出参数：当前Boss的位置坐标
     * 返回值：无
     * 其它说明：无
     ***********************************************/
    public void move(int x, int y) {
        Boss.x = x;
        Boss.y = y;
    }
}
