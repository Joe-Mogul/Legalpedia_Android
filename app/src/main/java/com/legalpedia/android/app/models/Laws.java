package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;


/**
 * Created by adebayoolabode on 11/7/16.
 */
@Entity( createInDb = false)
public class Laws {

    @Id
    private Long id;
    private String sid;
    private String title;
    private String number;
    private String description;
    private Date date;
    private Date postdate;
    private Date updatedate;

    @Generated(hash = 550152492)
    public Laws(Long id, String sid, String title, String number,
            String description, Date date, Date postdate, Date updatedate) {
        this.id = id;
        this.sid = sid;
        this.title = title;
        this.number = number;
        this.description = description;
        this.date = date;
        this.postdate = postdate;
        this.updatedate = updatedate;
    }
    @Generated(hash = 93936268)
    public Laws() {
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNumber() {
        return this.number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Date getPostdate() {
        return this.postdate;
    }
    public void setPostdate(Date postdate) {
        this.postdate = postdate;
    }
    public Date getUpdatedate() {
        return this.updatedate;
    }
    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }





}
