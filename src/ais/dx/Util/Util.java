package ais.dx.Util;

import ais.dx.Config.UserConfig;
import ais.dx.ReadCSV.Point;
import ais.dx.ReadCSV.Position;
import ais.dx.ReadCSV.Vector;

import java.util.ArrayList;

/**
 *  Copyright (C) mica
 *  Filename: Util.java
 *
 * @description 一些常用的工具类
 * @author mica
 * @Date 2019.4.27
 * @version 1.0
 */

public class Util {
    /***
     * 判断给定的年份是否是闰年
     * @param year 待判断的年份
     * @return 返回一个boolean对象，true代表该年份是闰年，否则不是
     * */
    public static boolean isLeapYear(int year) {
        if (year % 100 != 0) {
            if (year % 4 == 0) {
                return true;
            }
            return false;
        } else {
            if (year % 400 == 0) {
                return true;
            }
            return false;
        }
    }

    /**
     * 返回给定年份的每个月的天数
     * @param year 待计算的年数
     * @return 返回一个int型数组，其中下标表示月份，数组值代表该年该月的天数
     * **/
    public static int[] getDay(int year) {
        int[] months = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if (isLeapYear(year)) {
            months[2] = 29;
        }
        return months;
    }

    /**
     * 判断给定的时间点是否满足用户的POI
     * @param  year     待处理的年份
     * @param  month    待处理的月份
     * @param  day      待处理的天
     * @param  hour     待处理的小时
     * @param  minute   待处理的分钟
     * @return 如返回true表示满足用户的输入，否则不满足
     * */
    public static boolean isPOI(int year, int month, int day, int hour, int minute) {
        // 如果持续的分钟不超过60，那么直接进行判断就好
        if (UserConfig.USER_START_MINUTE + UserConfig.DURATION_TIME < 60) {
            if (year == UserConfig.USER_START_YEAR && month == UserConfig.USER_START_MONTH &&
                    day == UserConfig.USER_START_DAY && hour == UserConfig.USER_START_HOUR &&
                    minute >= UserConfig.USER_START_MINUTE && minute < UserConfig.USER_START_MINUTE + UserConfig.DURATION_TIME) {
                return true;
            }
            return false;
        } else {
            // 如果持续的分钟超过60，这个时候需要进行分钟归零时钟加一的操作
            if (UserConfig.USER_START_HOUR + 1 < 24) {
                // 其中以下的cond_1表示的是在未变化之前的分钟的范围
                // cond_2表示的是在时钟变化之后的分钟的范围
                boolean cond_1 = (UserConfig.USER_START_HOUR == hour && minute >= UserConfig.USER_START_MINUTE && minute < 60);
                boolean cond_2 = (UserConfig.USER_START_HOUR + 1 == hour && minute >= 0 && minute < UserConfig.USER_START_MINUTE + UserConfig.DURATION_TIME - 60);

                if (year == UserConfig.USER_START_YEAR && month == UserConfig.USER_START_MONTH &&
                        day == UserConfig.USER_START_DAY && cond_1 || cond_2) {
                    return true;
                }
                return false;
            } else {
                if (UserConfig.USER_START_DAY + 1 <= Util.getDay(year)[UserConfig.USER_START_MONTH]) {
                    boolean cond_1 = (UserConfig.USER_START_DAY == day && hour == UserConfig.USER_START_HOUR && minute >= UserConfig.USER_START_MINUTE && minute < 60);
                    boolean cond_2 = (UserConfig.USER_START_DAY + 1 == day && hour == 0 && minute >= 0 && minute < UserConfig.USER_START_MINUTE + UserConfig.DURATION_TIME - 60);

                    if (year == UserConfig.USER_START_YEAR && month == UserConfig.USER_START_MONTH && cond_1 || cond_2) {
                        return true;
                    }
                    return false;
                } else {
                    if (UserConfig.USER_START_MONTH + 1 <= 12) {
                        boolean cond_1 = (UserConfig.USER_START_MONTH == month && UserConfig.USER_START_DAY == day && hour == UserConfig.USER_START_HOUR && minute >= UserConfig.USER_START_MINUTE && minute < 60);
                        boolean cond_2 =  (UserConfig.USER_START_MONTH + 1 == month && day == 1 && hour == 0 && minute >= 0 && minute < UserConfig.USER_START_MINUTE + UserConfig.DURATION_TIME - 60);

                        if (year == UserConfig.USER_START_YEAR && cond_1 || cond_2) {
                            return true;
                        }

                        return false;
                    } else {
                        boolean cond_1 = (year == UserConfig.USER_START_YEAR && UserConfig.USER_START_MONTH == month && UserConfig.USER_START_DAY == day && hour == UserConfig.USER_START_HOUR && minute >= UserConfig.USER_START_MINUTE && minute < 60);
                        boolean cond_2 = (year == UserConfig.USER_START_YEAR + 1 && month == 1 && day == 1 && hour == 0 && minute >= 0 && minute < UserConfig.USER_START_MINUTE + UserConfig.DURATION_TIME - 60);

                        if (cond_1 || cond_2) {
                            return true;
                        }

                        return false;
                    }
                }
            }
        }
    }

    /**
     * 计算形成点集的面积，并计算出对应的区域的面积
     * @param points
     * @return 返回表示传入的点集是空，否则返回正常的面积
     * **/
    public static double calArea(ArrayList<Position> points) {
        if (points == null) {
            return 0.0;
        }
        double area = new Double(0);

        for (int startIndex = 0, endIndex = 1; endIndex < points.size(); startIndex = endIndex ++) {
            Vector vectorStart = new Vector(new Position(), points.get(startIndex));
            Vector vectorEnd   = new Vector(new Position(), points.get(endIndex));

            area += vectorStart.crossProduct(vectorEnd) * 0.5;
        }
        return area * 9101160000.085981;
    }
}
