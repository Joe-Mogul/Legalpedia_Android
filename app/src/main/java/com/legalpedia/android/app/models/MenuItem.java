package com.legalpedia.android.app.models;

/**
 * Created by adebayoolabode on 9/27/17.
 */

public class MenuItem {
    int id;
    String name;
    int resourceid;

    public MenuItem(int id,int resourceid, String name) {
        this.id = id;
        this.resourceid = resourceid;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResourceid() {
        return resourceid;
    }

    public void setResourceid(int resourceid) {
        this.resourceid = resourceid;
    }




}
