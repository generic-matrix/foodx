package com.project.foodx.usermodule.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class AlexaUser {
    private String user_id;
    private String name;
    private String email;
    private String token;
    private String error;
    private String error_description;

    public AlexaUser() {
        super();
    }
    public AlexaUser(String json){
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
            if(obj.has("user_id")) {
                this.user_id = obj.getString("user_id");
            }
            if(obj.has("name")) {
                this.name = obj.getString("name");
            }
            if(obj.has("email")) {
                this.email = obj.getString("email");
            }
            if(obj.has("error_description")) {
                this.error_description = obj.getString("error_description");
            }
            if(obj.has("error")) {
                this.error = obj.getString("error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public AlexaUser(String user_id, String name,String email) {
        super();
        this.user_id = user_id;
        this.name = name;
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getError() {
        return error;
    }

    public String getError_description() {
        return error_description;
    }
}
