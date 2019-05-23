package ais.dx.DB;

import java.sql.*;

/**
 *  Copyright (C) mica
 *  Filename: DB.java
 *
 * @description 使用数据库存储船只的信息
 * @author mica
 * @Date 2019.4.28
 * @version 1.0
 */

public class DB {
    /**
     * 通过jdbc连接数据库，从而获得该船只得长度信息
     * @param MMSI 船只得编号
     * @return 如果查询成功就返回得是一条船得长度，否则返回零
     * */
    public static int getShipInfo(String MMSI) {
        int length = 0;
        try {
            // 加载类名，然后进行数据库的链接
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ais?characterEncoding=utf8&serverTimezone=UTC", "root", "");

            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("select Length from ship_info where MMSI = " + MMSI);

            while (resultSet.next()) {
                length = Integer.parseInt(resultSet.getString(1));
            }

            resultSet.close();

            statement.close();

            conn.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return length;
    }
}
