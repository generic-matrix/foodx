package com.project.foodx.usermodule.entity;

public class AutoComplete {

    private String id;
    private String name;

    public AutoComplete() {
        super();
    }

    public AutoComplete(String id, String name) {
        super();
        this.id = id;
        this.name = name;
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
}
