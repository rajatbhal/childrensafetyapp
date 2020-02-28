package com.example.locationtracker;

/**
 * Created by RIDHHI on 3/23/2017.
 */

class MarkerData {
    public String getFuserid() {
        return fuserid;
    }

    public void setFuserid(String fuserid) {
        this.fuserid = fuserid;
    }

    String fuserid;

    double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    double longitude;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    String snippet;

    public int getIconResID() {
        return IconResID;
    }

    public void setIconResID(int iconResID) {
        IconResID = iconResID;
    }

    int IconResID;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
