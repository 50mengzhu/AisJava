package ais.dx.Process;

import ais.dx.ReadCSV.Point;
import ais.dx.ReadCSV.Position;
import ais.dx.Util.MaxHeap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ais.dx.ReadCSV.Vector;

import static java.lang.Math.PI;

/**
 *  Copyright (C) mica
 *  Filename: KNN.java
 *
 * @description 使用KNN算法计算出点集的α凸包
 * @author mica
 * @Date 2019.4.27
 * @version 1.0
 */
public class KNN {
    private ArrayList<Position> alphaConvexHull;
    public static int SUCCESS               = 1;
    public static int RECALCULATE           = 2;
    public static int POINT_NUM_NOT_ENOUGH  = -1;

    public KNN() {
        alphaConvexHull = new ArrayList<>();
    }

    /**
     * 清空结果的所有数据
     * **/
    public void clear() {
        alphaConvexHull.clear();
    }

    public ArrayList<Position> getAlphaConvexHull() {
        return alphaConvexHull;
    }


    /**
     * @param originPositionSet 用于计算α凸包的原始点集
     * @param k 是KNN方法中的参数k，表示采用k个邻居的方式
     * @return
     * -1 所给定的点集不能形成闭合的多边形（形成环至少需要3个点形成三角形的闭合）
     *  0 所给定的点集正好形成一个闭合的三角形
     *  1 计算出包含所有点的α凸包，这是最令人满意的返回结果
     *  2 返回这个表示需要堆参数增加
     * **/
    public int concaveHull(ArrayList<Position> originPositionSet, int k) {
        ArrayList<Position> positionSet = (ArrayList<Position>) originPositionSet.clone();
        // 这里需要保证k一定大于3
        int paramK = Math.max(k, 3);
        // 点集的数量需要足够
        if (positionSet.size() < 3) {
            alphaConvexHull.clear();
            return POINT_NUM_NOT_ENOUGH;
        }
        if (positionSet.size() == 3) {
            alphaConvexHull = (ArrayList<Position>) alphaConvexHull.clone();
            return SUCCESS;
        }
        // 或者是保证k个邻居能够被找到
        paramK = Math.min(paramK, positionSet.size());
        // 选出第一个点插入返回的点集中
//        Point firstPoint = minPoint;
        Position firstPoint = findMinYPoint(positionSet);
        alphaConvexHull.add(firstPoint);
        Position currentPoint = firstPoint;
        // 将第一个点加入结果之后，就把它从原来的点集中删除
        positionSet.remove(firstPoint);

        double preAngle = 0.0;
        // step实际上就是用来控制下面这个循环的
        int step = 1;
        while (((currentPoint != firstPoint) || (step == 1)) && (positionSet.size() > 0)) {
            // 为了防止最终的包围的点集在k个之前就形成包围圈，因此在3个之后才将firstPoint重新添加回来
            // 第一次想的在k次之后把第一个点添加回来的思路是错的，这样会导致如果没在k之前形成凸包环，那么就一直形成不了
            // 就会导致一直不能停止的死循环
            if (step == 4) {
                positionSet.add(firstPoint);
            }
            ArrayList<Position> nearestKPoints = nearestPoints(positionSet, currentPoint, paramK);
            ArrayList<Position> sortedPoints = sortByAngle(nearestKPoints, currentPoint, preAngle);
            // 从以上排好序的点中选择第一个不在内部的点
            boolean its = true;
            int i = 0, lastPoint = 0, j = 0;
            while (its && (i < sortedPoints.size())) {
                // 判断是否已经和第一个相连并成环，实际上使用这个是用来控制凸包集合中的第一段是否需要和目的线段进行相交判断
                if (sortedPoints.get(i) == firstPoint) {
                    lastPoint = 1;
                } else {
                    lastPoint = 0;
                }
                // 无条件添加第二、三个凸包元素。不然的话没办法进行相交判断
                j = alphaConvexHull.size() - 2;
                its = false;
                while (!its && (j > lastPoint)) {
                    its = intersect(currentPoint, sortedPoints.get(i), alphaConvexHull.get(j), alphaConvexHull.get(j - 1));
                    -- j;
                }
                // 只有在相交的情况之下才进行对已有点的自加，否则会引起超出索引的错误
                if (its) {
                    ++ i;
                }
            }
            // 当目前的k值找出的所有的点和当前点形成的线段都和α凸包的边有交点，那么就说明我们的参数选择太小了需要增加
            if (its) {
                return RECALCULATE;
            }
            // 将满足条件的点添加到我们的α凸包中
            currentPoint = sortedPoints.get(i);
            alphaConvexHull.add(currentPoint);
            // 并更新目前的基准角度
            preAngle = calAngle(alphaConvexHull.get(step), alphaConvexHull.get(step - 1));
            // 移除已经添加过的点
            positionSet.remove(currentPoint);
            ++ step;
        }
        // 当形成一个α凸包之后，我们需要判断是否所有的点全部都已经被计算出来的α凸包包含
        boolean allInside = true;
        int i = positionSet.size() - 1;
        while (allInside && i >= 0) {
            allInside = pointInPoly(positionSet.get(i), alphaConvexHull);
            -- i;
        }
        // 如果还有在剩余的点在计算出来的α凸包外，那么我们需要对参数进行增加
        if (!allInside) {
            return RECALCULATE;
        }
        // 否则完成对α凸包的求解
        return SUCCESS;
    }

    /**
     * 本函数的作用就是寻找点集中纬度最低的一点
     * @param positionSet 待查找的点集
     * @return 返回的是纬度最低的哪一点
     * **/
    private Position findMinYPoint(ArrayList<Position> positionSet) {
        double minLatitude = 180.0;
        Position minYPoint = new Position();

        for (Position position : positionSet) {
            if (Double.compare(position.getLatitude(), minLatitude) < 0) {
                minLatitude = position.getLatitude();
                minYPoint = position;
            }
        }
        return minYPoint;
    }

    /**
     * 计算点集中与当前点的距离最小的k个
     * @param currentPosition
     * @param positionSet
     * @param k
     * @return 返回的是点集中距离当前点最近的k个
     * */
    private ArrayList<Position> nearestPoints(ArrayList<Position> positionSet, Position currentPosition, int k) {
        MaxHeap maxHeap = new MaxHeap(currentPosition);
        return maxHeap.topMinK(positionSet, k);
    }

    /**
     * 根据找出的k个邻近的点与当前点的角度按照从大到小的方式进行排序
     * @param currentPosition 当前的点
     * @param nearestPosition 邻近点的集合
     * @param preAngle 之前的角度
     * @return 返回拍好序的邻近点的集合
     * */
    private ArrayList<Position> sortByAngle(ArrayList<Position> nearestPosition, Position currentPosition, double preAngle) {
        ArrayList<Position> sortedPsitionSet = new ArrayList<>();

        for (Position position : nearestPosition) {
            double diffAngle = calRelationAngle(currentPosition, position, preAngle);
            position.setRelativeAngle(diffAngle);
            sortedPsitionSet.add(position);
        }

        // 实现使用相对角度来进行对Point中的排序
        Collections.sort(sortedPsitionSet, new Comparator<Position>() {
            @Override
            public int compare(Position o1, Position o2) {
                return Double.compare(o2.getRelativeAngle(), o1.getRelativeAngle());
            }
        });

        return sortedPsitionSet;
    }

    /**
     * 判断两个线段是否相交
     * @param pointA
     * @param pointB
     * @param pointC
     * @param pointD
     * @return 返回的结果是true说明两个线段相交，返回false说明两线段是不相交的
     *  */
    private boolean intersect(Position pointA, Position pointB, Position pointC, Position pointD) {
        Vector vectorAB = new Vector(pointA, pointB);
        Vector vectorAC = new Vector(pointA, pointC);
        Vector vectorAD = new Vector(pointA, pointD);

        Vector vectorCA = new Vector(pointC, pointA);
        Vector vectorCB = new Vector(pointC, pointB);
        Vector vectorCD = new Vector(pointC, pointD);

        // 计算向量的叉乘，若两点在线段两边那么向量叉乘结果是负的
        double result_1 = vectorAC.crossProduct(vectorAB) * vectorAD.crossProduct(vectorAB);
        double result_2 = vectorCA.crossProduct(vectorCD) * vectorCB.crossProduct(vectorCD);
//        double e = 1e-12;

        return (Double.compare(result_1, 0.0) < 0) && (Double.compare(result_2, 0.0) < 0);
//        return (result_1 < e) && (result_2 < e);
    }

    /***
     * 使用向量点乘的方式计算角度，如果是在一二象限，那么返回正的角度，若在三四象限则返回负的角度
     * @param point1
     * @param point2
     * @return 返回这两个点和水平线构成的角度
     * */
    private double calAngle(Position point2, Position point1) {
        Vector vectorStd = new Vector(1, 0);
        Vector vector = new Vector(point1, point2);

        int coef = -1;
        if (vector.crossProduct(vectorStd) < 1e-8) {
            coef = 1;
        }
        double dotResult = vector.dotProduct(vectorStd);
        // 注意这里由于Math.acos函数返回的角度是在0~PI之间，所以我直接省略了取绝对值的函数
        return coef * Math.acos(dotResult / (vector.norm() * vectorStd.norm()));
    }

    /**
     * 判断一个点是否在我们给定的点集包围的区域内
     * @param pointSet 给定的点集
     * @param point 待测试点
     * @return 返回为true则表示在点击包围的多边形之中，否则在多边形区域之外
     * ***/
    private boolean pointInPoly(Position point, ArrayList<Position> pointSet) {
        int startPoint = 0, endPoint = pointSet.size() - 1;
        boolean result = false;

        double pointX = point.getLongitude();
        double pointY = point.getLatitude();

        // 这个是根据引射线法计算
        for (; startPoint < pointSet.size(); endPoint = startPoint ++) {
            double cond = (pointSet.get(endPoint).getLongitude() - pointSet.get(startPoint).getLongitude()) * (pointY - pointSet.get(startPoint).getLatitude()) / (pointSet.get(endPoint).getLatitude() - pointSet.get(startPoint).getLatitude()) + pointSet.get(startPoint).getLongitude();
            if (((Double.compare(pointSet.get(startPoint).getLatitude(), pointY) > 0 ) != (Double.compare(pointSet.get(endPoint).getLatitude(), pointY) > 0))
                    && (Double.compare(cond, pointX) >= 0)) {
                if (Double.compare(cond, pointX) == 0) {
                    return true;
                }
                result = !result;
            }
//            // 特殊情况之一：测试点刚好和图形顶点处
//            if (point.equals(pointSet.get(startPoint)) || point.equals(pointSet.get(endPoint))) {
//                return true;
//            }
//            if ((Double.compare(pointY, pointSet.get(endPoint).getPosition().getLatitude()) >= 0) && (Double.compare(pointY, pointSet.get(startPoint).getPosition().getLatitude())) < 0
//                || ((Double.compare(pointY, pointSet.get(startPoint).getPosition().getLatitude())) >= 0) && (Double.compare(pointY, pointSet.get(endPoint).getPosition().getLatitude()) < 0)) {
//                double predictX = pointSet.get(startPoint).getPosition().getLongitude() + (pointY - pointSet.get(startPoint).getPosition().getLatitude()) / Vector.slope(pointSet.get(startPoint), pointSet.get(endPoint));
//                // 特殊情况之二：测试点刚好在图形的边上
//                if (Double.compare(pointX, predictX) == 0) {
//                    return true;
//                }
//                if (Double.compare(pointX, predictX) < 0) {
//                    result = !result;
//                }
//            }
        }
        return result;
    }

    /**
     * 计算由起止点构成的向量与preAngle的相对角度
     * @param startPoint
     * @param endPoint
     * @param preAngle
     * @return  其中向量构成的夹角在preAngle顺时针方向则角度为正，否则为负
     * **/
    private double calRelationAngle(Position startPoint, Position endPoint, double preAngle) {
        Vector vector = new Vector(startPoint, endPoint);
        // 将基准角preAngle归一化，如果在一四象限那么横坐标为正，否则为负
        int stdX = 1;
        if ((preAngle > PI / 2 && preAngle < PI) || (preAngle > - PI && preAngle < - PI / 2)) {
            stdX = -1;
        }

        Vector vectorStd = new Vector(stdX, stdX * Math.tan(preAngle));

        double dotResult = vector.dotProduct(vectorStd);
        double crossResult = vector.crossProduct(vectorStd);

        int coef = -1;
        if (crossResult >= 0) {
            coef = 1;
        }
        return coef * Math.acos(dotResult / (vector.norm() *vectorStd.norm()));
    }
}
