package com.project.foodx.usermodule.entity;


public class SnapShot {

    private String id;
    private String name;
    private float avg_rating;
    private int rating_count;

    public SnapShot() {
        super();
    }

    public SnapShot(String id, String name,float avg_rating,int rating_count) {
        super();
        this.id = id;
        this.name = name;
        this.avg_rating = avg_rating;
        this.rating_count = rating_count;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRating(float rating) {
        this.avg_rating = rating;
    }

    public float getRating() {
        return avg_rating;
    }

    public void setRating_count(int rating_count) {
        this.rating_count = rating_count;
    }

    public int getRating_count() {
        return rating_count;
    }
}
