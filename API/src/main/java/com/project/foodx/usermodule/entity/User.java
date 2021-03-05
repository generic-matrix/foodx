package com.project.foodx.usermodule.entity;


import com.project.foodx.usermodule.service.Util;

public class User {

    //unique key
    private String id;
    private String email;
    private String password;
    private String name;

    public User() {
        super();
    }

    public User(String email, String password,String name) {
        super();
        this.email = email;
        this.password = password;
        this.name= name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id=id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRandomId() {
        this.id=new Util().setRandomId();
    }
}