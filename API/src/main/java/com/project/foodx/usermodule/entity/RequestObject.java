package com.project.foodx.usermodule.entity;


public class RequestObject {
    private String food_id;
    private String user_name;
    private String message;
    private String star;

    RequestObject(String food_id,String user_name,String message,String star) throws Exception {
        if(food_id==null || user_name==null || message==null || star==null){
            throw new Exception("Malformed Input");
        }
        if(Float.parseFloat(star)>5 || Float.parseFloat(star)<0){
            throw new Exception("Star Value is invalid ");
        }
        this.food_id=food_id;
        this.user_name=user_name;
        this.message=message;
        this.star=star;
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

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }
}
