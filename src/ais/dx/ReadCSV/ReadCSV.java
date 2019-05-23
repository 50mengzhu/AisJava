package ais.dx.ReadCSV;

import ais.dx.Config.UserConfig;
import ais.dx.Util.Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *  Copyright (C) mica
 *  Filename: ReadCSV.java
 *
 * read from the csv file and store the valid data
 * @author mica
 * 2019.4.27
 * @version 1.0
 */
public class ReadCSV {
    private ArrayList<Point> pointSet;
    private String csvName;
    private double averageSOG;
    private HashMap<String, ArrayList<Point>> shipClassifyByMMSI;
    private double minLatitude;
    private Point minLatPoint;


    public ReadCSV(String filename) {
        pointSet = new ArrayList<>();
        csvName = filename;
        averageSOG = 0.0;
        shipClassifyByMMSI = new HashMap<>();
        minLatitude = 180.0;
        minLatPoint = new Point();
    }

    /***
     * 从一个csv文件中将数据解析出来
     * 而将数据直接存储在本类的私有成员当中
     * @throws java.io.FileNotFoundException 当文件不存在时候抛出
     * @throws java.io.IOException 也可能在读文件的时候抛出io异常
     * */
    public void readData() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvName));
            reader.readLine();

            String line = null;

            while ((line = reader.readLine()) != null) {
                String[] shipAttr = line.split(",");

                String  MMSI        = shipAttr[3];
                double  longitude   = Double.parseDouble(shipAttr[8]);
                double  latitude    = Double.parseDouble(shipAttr[9]);
                double  SOG         = Double.parseDouble(shipAttr[6]);
                double  COG         = Double.parseDouble(shipAttr[10]);
                int     year        = Integer.parseInt(shipAttr[15]);
                int     month       = Integer.parseInt(shipAttr[16]);
                int     day         = Integer.parseInt(shipAttr[17]);
                int     hour        = Integer.parseInt(shipAttr[18]);
                int     minute      = Integer.parseInt(shipAttr[19]);
                int     second      = Integer.parseInt(shipAttr[20]);

                // 如果经纬度超标，直接舍弃
                if (longitude > UserConfig.USER_LONGITUDE_HIGH ||
                        longitude < UserConfig.USER_LONGITUDE_LOW ||
                        latitude > UserConfig.USER_LATITUDE_HIGH ||
                        latitude < UserConfig.USER_LATITUDE_LOW) {
                    continue;
                }

                if (!Util.isPOI(year, month, day, hour, minute)) {
                    continue;
                }

                int count = pointSet.size();
                averageSOG = (count * averageSOG + SOG) / (count + 1);

                Point point = new Point(MMSI, new Position(longitude, latitude), SOG, COG, year, month, day, hour, minute, second);
                if (pointSet.contains(point)) {
                    continue;
                }
                // 寻找最小的纬度，以及包含纬度的点
                if (Double.compare(minLatitude, latitude) > 0) {
                    minLatitude = latitude;
                    minLatPoint = point;
                }

                // 按照船只的MMSI号码将船只进行分类
                ArrayList<Point> points = new ArrayList<>();
                points.add(point);
                if (shipClassifyByMMSI.put(MMSI, points) != null) {
                    shipClassifyByMMSI.get(MMSI).add(point);
                }

                pointSet.add(point);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Point> getPointSet() {
        return pointSet;
    }

    public Point getMinLatPoint() {
        return minLatPoint;
    }

    public double getAverageSOG() {
        return averageSOG;
    }

    public HashMap<String, ArrayList<Point>> getShipClassifyByMMSI() {
        return shipClassifyByMMSI;
    }
}
