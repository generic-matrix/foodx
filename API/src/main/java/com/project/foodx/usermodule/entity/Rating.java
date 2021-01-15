package com.project.foodx.usermodule.entity;

public class Rating {
    private String user_id;
    private String user_name;
    private String rating_id;
    private String message;
    private String star;

    public Rating() {
        super();
    }

    public Rating(String user_id,String user_name,String rating_id,String message, String star) {
        super();
        this.user_id = user_id;
        this.user_name = user_name;
        this.rating_id=rating_id;
        this.message = message;
        this.star= star;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getStar() {
        return star;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setRating_id(String rating_id) {
        this.rating_id = rating_id;
    }

    public String getRating_id() {
        return rating_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }
}
