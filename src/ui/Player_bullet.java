package ui;

/* ********************************************
 * 版权所有 (C)2020,JinHui
 *
 * 文件名称：Player_bullet.java
 * 内容摘要：子弹信息存储类
 * 其它说明：无
 * 当前版本：V1.0
 * 作    者：JinHui
 * 完成日期：2020/11/30
 * **********************************************/
public class Player_bullet {
    //子弹的图片
    public static final int BULLET_PIC_X = 12;
    public static final int BULLET_PIC_Y = 18;
    //子弹的位置
    public int x;
    public int y;
    //子弹类型1：左，2：中，3：右
    public int bullet_type;
    public Player_bullet(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.bullet_type = type;
    }
    //
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
