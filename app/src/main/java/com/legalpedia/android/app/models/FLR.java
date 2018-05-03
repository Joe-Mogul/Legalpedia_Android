package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by adebayoolabode on 11/7/16.
 */
@Entity( createInDb = false)
public class FLR {

    @Id
    private Long id;
    private String title;
    private String content;


    @Generated(hash = 2083625613)
    public FLR(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
    @Generated(hash = 62827871)
    public FLR() {
    }


    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }


}
