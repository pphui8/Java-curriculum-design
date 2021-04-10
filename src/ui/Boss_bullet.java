package ui;
/* ********************************************
 * 版权所有 (C)2020,JinHui
 *
 * 文件名称：Boss_bullet.java
 * 内容摘要：boss子弹信息存储类
 * 其它说明：无
 * 当前版本：V1.0
 * 作    者：JinHui
 * 完成日期：2020/11/30
 * **********************************************/
public class Boss_bullet {
    //子弹的大小
    public static final int BULLET_PIC_X = 20;
    public static final int BULLET_PIC_Y = 20;
    //子弹的位置
    public int x;
    public int y;
    //子弹的类型
    public int type;

    /***********************************************
     * 功能描述：Boss子弹的创建算法
     * 输入参数：x, y：子弹坐标
     * 输出参数：子弹位置被创建
     * 返回值：无
     * 其它说明：无
     ***********************************************/
    public Boss_bullet(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /***********************************************
     * 功能描述：Boss子弹的移动算法
     * 输入参数：x, y：子弹坐标
     * 输出参数：子弹位置被修改
     * 返回值：无
     * 其它说明：无
     ***********************************************/
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
