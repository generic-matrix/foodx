package com.project.foodx.usermodule.entity;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Food {
    private String id;
    private String title;
    private String[] ingredients;
    private String[] instructions;
    private Rating[] rating;
    private Location location;

    public Food() {
        super();
    }

    public Food(String json){
        try {
            JSONObject obj = new JSONObject(json);
            this.id=obj.getString("_id");
            JSONObject source= obj.getJSONObject("_source");
            this.title=source.getString("title");
            JSONArray ingredients =source.getJSONArray("ingredients");
            List<String> list = new ArrayList<String>();
            for(int i = 0; i < ingredients.length(); i++){
                list.add(ingredients.getString(i));
            }
            this.ingredients=list.toArray(new String[0]);
            JSONArray instructions =source.getJSONArray("instructions");
            list = new ArrayList<String>();
            for(int i = 0; i < instructions.length(); i++){
                list.add(instructions.getString(i));
            }
            this.instructions=list.toArray(new String[0]);
            JSONObject location =source.getJSONObject("location");
            this.location= new Location(location.getDouble("lat"),location.getDouble("lon"));

            if(!source.has("rating")){
                this.rating=null;
            }else {
                JSONArray rating =source.getJSONArray("rating");
                Rating[] rating_list= new Rating[rating.length()];
                for (int i = 0; i < rating.length(); i++) {
                    JSONObject RatingObj=rating.getJSONObject(i);
                    rating_list[i]=new Rating(RatingObj.getString("user_id"),RatingObj.getString("user_name"),RatingObj.getString("rating_id"),RatingObj.getString("message"),RatingObj.getString("star"));
                }
                this.rating=rating_list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Food(String id,String title,String[] ingredients,String[] instructions,Rating[] rating,Location location) {
        super();
        this.id = id;
        this.title = title;
        this.ingredients= ingredients;
        this.instructions= instructions;
        this.rating = rating;
        this.location= location;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setInstructions(String[] instructions) {
        this.instructions = instructions;
    }

    public String[] getInstructions() {
        return instructions;
    }

    public void setRating(Rating[] rating) {
        this.rating = rating;
    }

    public Rating[] getRating() {
        return rating;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
