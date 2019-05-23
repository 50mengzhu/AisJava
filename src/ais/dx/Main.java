package ais.dx;

import ais.dx.Config.UserConfig;
import ais.dx.Process.KNN;
import ais.dx.ReadCSV.Point;
import ais.dx.ReadCSV.Position;
import ais.dx.ReadCSV.ReadCSV;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        ReadCSV reader = new ReadCSV("src/ais/dx/Data/position.csv");

        reader.readData();
        System.out.println(reader.getPointSet().size());

        KNN knn = new KNN();
        ArrayList<Point> dataSet = reader.getPointSet();

        ////////
        File outFile = new File("date" + UserConfig.USER_START_DAY + UserConfig.USER_START_HOUR + UserConfig.USER_START_MINUTE +  ".txt");
        if (!outFile.exists()) {
            outFile.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(outFile.getName(), true);
        for (Point point : dataSet) {
            fileWriter.write(point.getPosition().getLongitude() + "," + point.getPosition().getLatitude() + "\n");
        }

        fileWriter.close();
        System.out.println("Almost Done!!");
        ////////

        int param = 1747;
        int result = knn.concaveHull(dataSet, param);

        while (result == KNN.RECALCULATE) {
            if (knn.getAlphaHull() == null) {
                System.out.println("idot");
            } else {
                System.out.println("fucking size: " + knn.getAlphaHull().size());
            }
            knn.clear();
            result = knn.concaveHull(dataSet, ++ param);
        }
        ArrayList<Point> points = knn.getAlphaHull();

        System.out.println(param + " \t" + result);
        if (points == null) {
            System.out.println("stupid! you got null objects!");
        } else {

            File dataOutFile = new File("out" + UserConfig.USER_START_DAY + UserConfig.USER_START_HOUR + UserConfig.USER_START_MINUTE +  ".txt");
            if (!dataOutFile.exists()) {
                dataOutFile.createNewFile();
            }

            FileWriter dataFileWriter = new FileWriter(dataOutFile.getName(), true);

            System.out.println(points.size());
//            for (Point point : points) {
////                System.out.println(point.getPosition().getLongitude());
//                Position startPos = point.getPosition();
//                Position endPos = point.
//                dataFileWriter.write("[" + point.getPosition().getLongitude() + "," + point.getPosition().getLatitude() + "],");
//            }
            Position startPos = new Position();
            Position endPos = points.get(0).getPosition();
            for (int i = 1; i < points.size(); ++ i) {
                startPos = endPos;
                endPos = points.get(i).getPosition();
                dataFileWriter.write("[" + startPos.getLongitude() + "," + startPos.getLatitude() + "]," + "[" + endPos.getLongitude() + "," + endPos.getLatitude() + "]\n");
            }
            dataFileWriter.close();
//            System.out.println("*******");
//            for (Point point : points) {
//                System.out.println(point.getPosition().getLatitude());
//            }
        }

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
