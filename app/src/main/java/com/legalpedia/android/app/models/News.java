package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created by adebayoolabode on 11/7/16.
 */
@Entity( createInDb = false)
public class News {

    @Id
    private Long id;
    private String title;
    private String content;
    private Date postdate;

    @Generated(hash = 1724660025)
    public News(Long id, String title, String content, Date postdate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.postdate = postdate;
    }
    @Generated(hash = 1579685679)
    public News() {
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
    public Date getPostdate() {
        return postdate;
    }

    public void setPostdate(Date postdate) {
        this.postdate = postdate;
    }




}
