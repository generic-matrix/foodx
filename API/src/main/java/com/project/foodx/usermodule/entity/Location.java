package com.project.foodx.usermodule.entity;

public class Location {
    private double lat;
    private double lng;

    public Location() {
        super();
    }

    public Location(double lat,double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLng() {
        return lng;
    }
}
