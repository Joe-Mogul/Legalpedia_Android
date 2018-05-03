package com.legalpedia.android.app.models;

/**
 * Created by adebayoolabode on 6/23/17.
 */

public class SubjectsStat {
    private int id;
    private String title;
    private String count;

    public SubjectsStat(int id,String title, String count) {
        this.id = id;
        this.title = title;
        this.count = count;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
