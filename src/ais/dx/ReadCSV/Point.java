package ais.dx.ReadCSV;

import javafx.geometry.Pos;

/**
 *  Copyright (C) mica
 *  Filename: Point.java
 *
 * @description position and attributions of ship
 * @author mica
 * @Date 2019.4.27
 * @version 1.0
 */

public class Point {
    private String MMSI;
    // longitude & latitude
    private Position position;
    // speed over ground
    private double SOG;
    // course over ground
    private double COG;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    private double relativeAngle;
    private double minDistance;

    public Point() {
        this.MMSI = "0000000";
        this.position =  new Position(0.0, 0.0);
        this.SOG =  0.0;
        this.COG =  0.0;
        this.year =  1970;
        this.month =  1;
        this.day =  1;
        this.hour = 0;
        this.minute = 0;
        this.second = 0;
        this.relativeAngle = 0.0;
    }

    public Point(String MMSI, Position position, double SOG, double COG, int year, int month, int day, int hour, int minute, int second) {
        this.MMSI = MMSI;
        this.position = position;
        this.SOG = SOG;
        this.COG = COG;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.relativeAngle = 0.0;
    }

    public String getMMSI() {
        return MMSI;
    }

    public Position getPosition() {
        return position;
    }

    public double getCOG() {
        return COG;
    }

    public double getSOG() {
        return SOG;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getMonth() {
        return month;
    }

    public int getSecond() {
        return second;
    }

    public int getYear() {
        return year;
    }

    public double getRelativeAngle() {
        return relativeAngle;
    }

    public void setRelativeAngle(double relativeAngle) {
        this.relativeAngle = relativeAngle;
    }

    public double distance(Point point) {
        double deltaX = this.getPosition().getLongitude() - point.getPosition().getLongitude();
        double deltaY = this.getPosition().getLatitude() - point.getPosition().getLatitude();

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && obj instanceof Point) {
            Point point = (Point) obj;
            return Double.compare(this.position.getLongitude(), point.getPosition().getLongitude()) == 0
                    && Double.compare(this.position.getLatitude(), point.getPosition().getLatitude()) == 0;
        } else {
            return false;
        }
    }
}

