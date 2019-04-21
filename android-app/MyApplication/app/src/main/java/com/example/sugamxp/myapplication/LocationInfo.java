package com.example.sugamxp.myapplication;

public class LocationInfo {
    private String uid;
    private String pothole_name;
    private double latitude;
    private double longitude;

    public LocationInfo(String uid, String pothole_name, double latitude, double longitude) {
        this.uid = uid;
        this.pothole_name = pothole_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationInfo() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPothole_name() {
        return pothole_name;
    }

    public void setPothole_name(String pothole_name) {
        this.pothole_name = pothole_name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LocationInfo{" +
                "uid='" + uid + '\'' +
                ", pothole_name='" + pothole_name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
