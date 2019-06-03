package ais.dx.Util;

import ais.dx.ReadCSV.Point;
import ais.dx.ReadCSV.Position;

import java.util.ArrayList;

/**
 *  Copyright (C) mica
 *  Filename: MaxHeap.java
 *
 * @description 大顶堆类
 * @author mica
 * @Date 2019.4.29
 * @version 1.0
 */
public class MaxHeap {
    Position currentPosition;
    private ArrayList<Position> data;

    public MaxHeap(Position curPosition) {
        data = new ArrayList<>();
        currentPosition = curPosition;
    }

    public MaxHeap(ArrayList<Position> array, Position curPosition) {
        this.currentPosition = curPosition;
        this.data = array;
        buildMaxHeap();
    }

    /**
     * 将一个数组调整成为一个大顶堆
     * 从后向前调整
     * **/
    private void buildMaxHeap() {
        for (int i = (data.size() / 2) - 1; i >= 0; -- i) {
            heapify(i);
        }
    }

    /**
     * @param index 调整时的下标
     * 将数组的左右儿子全部调整为一个大顶堆
     * **/
    private void heapify(int index) {
        int leftIndex       = ((index + 1) << 1) - 1;
        int rightIndex      = (index + 1) << 1;
        int biggestIndex    = index;

        if (leftIndex < data.size() && Double.compare(currentPosition.distance(data.get(leftIndex)), currentPosition.distance(data.get(biggestIndex))) > 0) {
            biggestIndex = leftIndex;
        }
        if (rightIndex < data.size() && Double.compare(currentPosition.distance(data.get(rightIndex)), currentPosition.distance(data.get(biggestIndex))) > 0) {
            biggestIndex = rightIndex;
        }

        if (index == biggestIndex) {
            return;
        }

        swap(index, biggestIndex);
        heapify(biggestIndex);
    }

    /**
     * 交换两个下标对应的元素的值
     * @param i
     * @param j
     * **/
    private void swap(int i, int j) {
        Position temp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, temp);
    }

    /**
     * @param dataSet 待查找的数据集
     * @param k 待查找的数据的个数
     * **/
    public ArrayList<Position> topMinK(ArrayList<Position> dataSet, int  k) {
        if (k >= dataSet.size()) {
            return dataSet;
        }

        data = new ArrayList<>(k);
        for (int i = 0; i < k; ++ i) {
            data.add(dataSet.get(i));
        }

        buildMaxHeap();

        for (int i = k; i < dataSet.size(); ++ i) {
            double root = currentPosition.distance(data.get(0));

            if (Double.compare(root, currentPosition.distance(dataSet.get(i))) > 0) {
                data.set(0, dataSet.get(i));
                heapify(0);
            }
        }

        return data;
    }
}
