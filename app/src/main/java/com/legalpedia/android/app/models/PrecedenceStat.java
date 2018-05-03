package com.legalpedia.android.app.models;

/**
 * Created by adebayoolabode on 6/23/17.
 */

public class PrecedenceStat {

    private String title;
    private String count;

    public PrecedenceStat(String title, String count) {
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


}
