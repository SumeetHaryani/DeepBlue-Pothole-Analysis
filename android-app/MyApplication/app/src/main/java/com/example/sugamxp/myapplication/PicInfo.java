package com.example.sugamxp.myapplication;

public class PicInfo {
    private String date;
    private int number_of_potholes;
    private String pic_url;
    private double pothole_area;
    private String pothole_name;
    private String uid;
    private String status;
    private String location_add;
    private double latitude;
    private double longitude;
    public PicInfo(String date, int number_of_potholes, String pic_url, double pothole_area,
                   String pothole_name, String uid, String status, String location_add
                    ,double longitude, double latitude) {
        this.date = date;
        this.number_of_potholes = number_of_potholes;
        this.pic_url = pic_url;
        this.pothole_area = pothole_area;
        this.pothole_name = pothole_name;
        this.uid = uid;
        this.status = status;
        this.location_add = location_add;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public PicInfo() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumber_of_potholes() {
        return number_of_potholes;
    }

    public void setNumber_of_potholes(int number_of_potholes) {
        this.number_of_potholes = number_of_potholes;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public double getPothole_area() {
        return pothole_area;
    }

    public void setPothole_area(double pothole_area) {
        this.pothole_area = pothole_area;
    }

    public String getPothole_name() {
        return pothole_name;
    }

    public void setPothole_name(String pothole_name) {
        this.pothole_name = pothole_name;
    }

    public String getUserid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation_add() {
        return location_add;
    }

    public void setLocation_add(String location_add) {
        this.location_add = location_add;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "PicInfo{" +
                "date='" + date + '\'' +
                ", number_of_potholes=" + number_of_potholes +
                ", pic_url='" + pic_url + '\'' +
                ", pothole_area=" + pothole_area +
                ", pothole_name='" + pothole_name + '\'' +
                ", uid='" + uid + '\'' +
                ", status='" + status + '\'' +
                ", location_add='" + location_add + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
