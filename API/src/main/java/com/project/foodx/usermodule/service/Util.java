package com.project.foodx.usermodule.service;

import org.json.JSONArray;
import org.json.JSONObject;

public class Util {

    private float AvgRating=0;
    private int RatingCount=0;

    public String setRandomId(){
        long unixTime =System.currentTimeMillis() / 1000L;
        return String.valueOf(unixTime);
    }

    public void GetAvgRating(Object obj){
        AvgRating=0;
        RatingCount=0;
        try{
            if(obj!=null) {
                JSONArray jsonArray = new JSONArray(obj.toString());
                float total_rating=0;
                RatingCount=jsonArray.length();
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    total_rating=total_rating+Float.parseFloat(object.getString("star"));
                }
                AvgRating=total_rating/RatingCount;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public float getAvgRating() {
        return this.AvgRating;
    }

    public int getRatingCount() {
        return this.RatingCount;
    }
}
