package com.example.sugamxp.myapplication;

public class UploadInfo {
    private String fname;
    private String key;
    private String date;

    public UploadInfo(String fname, String key, String date) {
        this.fname = fname;
        this.key = key;
        this.date = date;
    }

    public UploadInfo() {
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
