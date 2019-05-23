package ais.dx.Config;
/**
 *  Copyright (C) mica
 *  Filename: UserConfig.java
 *
 * @description 用户输入的感兴趣的区域POI(Point Of Interest)
 * @author mica
 * @Date 2019.4.27
 * @version 1.0
 */

public class UserConfig {
    // 由用户设置拥堵的具体区域
    public static double USER_LONGITUDE_LOW  = 118.05;
    public static double USER_LONGITUDE_HIGH = 118.616;
    public static double USER_LATITUDE_LOW   = 24.0882;
    public static double USER_LATITUDE_HIGH  = 24.6755;

    // 由用户设定具体的拥堵时间
    public static int   USER_START_YEAR      = 2018;
    public static int   USER_START_MONTH     = 12;
    public static int   USER_START_DAY       = 21;
    public static int   USER_START_HOUR      = 11;
    public static int   USER_START_MINUTE    = 47;

    // 用户设定持续时间
    public static int   DURATION_TIME        = 5;
}
