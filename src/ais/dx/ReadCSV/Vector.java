package ais.dx.ReadCSV;

/**
 *  Copyright (C) mica
 *  ClassName: Vector.java
 *
 * @description 就是构造一个数学中的二维向量坐标类，主要实现向量叉乘和点乘
 * @author mica
 * @Date 2019.4.27
 * @version 1.0
 */
public class Vector {
    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Point startPoint, Point endPoint) {
        this(endPoint.getPosition().getLongitude() - startPoint.getPosition().getLongitude(), endPoint.getPosition().getLatitude() - startPoint.getPosition().getLatitude());
    }

    public Vector(Position startPosition, Position endPosition) {
        this(endPosition.getLongitude() - startPosition.getLongitude(), endPosition.getLatitude() - startPosition.getLatitude());
    }

    /**
     * 计算两个向量的叉乘结果
     * @param vector
     * @return 返回叉乘的结果
     * */
    public double crossProduct(Vector vector) {
        return this.x * vector.getY() - this.y * vector.getX();
    }

    /**
     * 实现两个向量的点乘
     * @param vector
     * @return 返回点乘结果
     * */
    public double dotProduct(Vector vector) {
        return this.x * vector.getX() + this.y * vector.getY();
    }

    /**
     * 计算向量的模
     * @return 返回向量的模
     * */
    public double norm() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * 计算两点的斜率
     * @param point1
     * @param point2
     * @return 若两个经度相同那么返回NaN，否则返回斜率
     * **/
    public static double slope(Point point1, Point point2) {
        if (Double.compare(point1.getPosition().getLongitude(), point2.getPosition().getLongitude()) == 0) {
            return Float.NaN;
        } else {
            return (point1.getPosition().getLatitude() - point2.getPosition().getLatitude()) / (point1.getPosition().getLongitude() - point2.getPosition().getLongitude());
        }
    }

    public static double slope(Position position1, Position position2) {
        if (Double.compare(position1.getLongitude(), position2.getLongitude()) == 0) {
            return Float.NaN;
        } else {
            return (position1.getLatitude() - position2.getLatitude()) / (position1.getLongitude() - position2.getLongitude());
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}