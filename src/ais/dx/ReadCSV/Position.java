package ais.dx.ReadCSV;

/**
 *  Copyright (C) mica
 *  Filename: Position.java
 *
 * longitude & latitude
 * @author mica
 * 2019.4.27
 * @version 1.0
 */
public class Position {
    private double longitude;
    private double latitude;

    public Position() {
        this(0, 0);
    }

    public Position(Position position){
        longitude = position.getLongitude();
        latitude = position.getLatitude();
    }

    public Position(double lon, double lat){
        longitude = lon;
        latitude = lat;
    }

    public double getLongitude(){
        return longitude;
    }

    public double getLatitude(){
        return latitude;
    }
}