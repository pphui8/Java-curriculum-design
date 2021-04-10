package ui;
/* ********************************************
 * 版权所有 (C)2020,JinHui
 *
 * 文件名称：start.java
 * 内容摘要：游戏启动项目文件
 * 其它说明：无
 * 当前版本：V1.0
 * 作    者：JinHui
 * 完成日期：2020/11/30
 * **********************************************/
public class start {
    public static void main(String[] args) {
        //从config中加载数据文件
        new User().init();
        //打开游戏窗口
        new Wel_frame().init();
        //test_user();
    }
    /* **********************************************
     * 功能描述：User的测试代码，测试User内部的类是否在正常运行
     * 输入参数：无
     * 输出参数：无
     * 返回值：无
     * 其它说明：初始化界面参数
     * **********************************************/
    private static void test_user() {
        //config.txt文件测试代码块
        User p = new User();
        p.init();
        System.out.printf("%s %d\n", User.user_name, User.high_point);
        System.out.println(User.is_login);
        User.user_name = "湫月汐";
        User.is_login = true;
        p.exit();
    }
}