package com.example.sugamxp.myapplication;

public class Info {
    private float sensor_width;
    private float sensor_height;
    private float focal_length;

    public Info(float sensor_width, float sensor_height, float focal_length) {
        this.sensor_width = sensor_width;
        this.sensor_height = sensor_height;
        this.focal_length = focal_length;
    }

    public Info() {
    }

    public float getSensor_width() {
        return sensor_width;
    }

    public void setSensor_width(float sensor_width) {
        this.sensor_width = sensor_width;
    }

    public float getSensor_height() {
        return sensor_height;
    }

    public void setSensor_height(float sensor_height) {
        this.sensor_height = sensor_height;
    }

    public float getFocal_length() {
        return focal_length;
    }

    public void setFocal_length(float focal_length) {
        this.focal_length = focal_length;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
