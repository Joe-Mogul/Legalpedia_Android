package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 7/4/17.
 */
@Entity( createInDb = false)
public class BugReport {

    @Id
    private Long id;
    private String title;
    private String description;
    private Date createdate;


    @Generated(hash = 132852542)
    public BugReport(Long id, String title, String description, Date createdate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdate = createdate;
    }

    @Generated(hash = 1647080932)
    public BugReport() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

}
