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
    private double relativeAngle;

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

    public double distance(Position position) {
        double deltaLon = this.longitude - position.getLongitude();
        double deltaLat = this.latitude - position.getLatitude();

        return Math.sqrt(deltaLon * deltaLon + deltaLat * deltaLat);
    }

    public double getRelativeAngle() {
        return relativeAngle;
    }

    public void setRelativeAngle(double relativeAngle) {
        this.relativeAngle = relativeAngle;
    }
}