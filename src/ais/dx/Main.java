package ais.dx;

import ais.dx.Config.UserConfig;
import ais.dx.Congest.Density;
import ais.dx.DB.DB;
import ais.dx.Process.KNN;
import ais.dx.ReadCSV.Point;
import ais.dx.ReadCSV.Position;
import ais.dx.ReadCSV.ReadCSV;
import ais.dx.Util.Util;
import sun.security.krb5.internal.crypto.Des;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        ReadCSV reader = new ReadCSV("src/ais/dx/Data/position.csv");

        reader.readData();
        System.out.println(reader.getPointSet().size());
        reader.toFile("fuck");

        KNN knn = new KNN();
        reader.readFile("fuck.csv");
        ArrayList<Position> dataSet = reader.getLonlatSet();

        System.out.println("size fuck !!!:: " + dataSet.size());

        long startTime = System.currentTimeMillis();
        int param = 1526;
        int result = knn.concaveHull(dataSet, param);

        while (result == KNN.RECALCULATE) {
            if (knn.getAlphaConvexHull() == null) {
                System.out.println("idot");
            } else {
                System.out.println("fucking size: " + knn.getAlphaConvexHull().size());
            }
            knn.clear();
            result = knn.concaveHull(dataSet, ++ param);
        }
        ArrayList<Position> points = knn.getAlphaConvexHull();

        System.out.println(param + " \t" + result);
        if (points == null) {
            System.out.println("stupid! you got null objects!");
        } else {
            System.out.println(points.size());

            System.out.println("***************longitude************");
            for (Position position : points) {
                System.out.println(position.getLongitude());
            }
            System.out.println("***************latitude************");
            for (Position position : points) {
                System.out.println(position.getLatitude());
            }
/******
 *  判断最后的拥堵情况
 *
        double area = Util.calArea(points);
        System.out.println("真实的计算密度" + reader.getShipClassifyByMMSI().size() / area);

        ArrayList<String > ships = new ArrayList<>(reader.getShipClassifyByMMSI().keySet());

        int[] lengths = DB.getShipInfo(ships);

        double turnDensity = Density.density(7, 1, lengths);
        double congDensity = Density.density(3, 1, lengths);



        System.out.println("转折密度：" + turnDensity);
        System.out.println("阻塞密度：" + congDensity);

        System.out.println("素的: " + reader.getAverageSOG());


        System.out.println("**************");
        int counnnn = 0;
        for (String ship : ships) {
            System.out.println(ship + "\t" + lengths[counnnn ++]);
 */
        }
        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间



//        HashMap<String, ArrayList<Point>> points = reader.getShipClassifyByMMSI();
//
//        Iterator iterator = points.entrySet().iterator();
//
//        while (iterator.hasNext()) {
//            HashMap.Entry<String, ArrayList<Point>> point = (HashMap.Entry<String, ArrayList<Point>>) iterator.next();
//            Object key = point.getKey();
//            ArrayList<Point> value = point.getValue();
//            System.out.println(key);
//            for (Point po : value) {
//                System.out.println(po.getDay() + "\t" + po.getMinute());
//            }
//        }
//
//        System.out.println(DB.getShipInfo("412700104"));
    }
}
