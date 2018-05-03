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
public class Sections {
    @Id
    private Long id;
    private int lawid;
    private String sid;
    private String partschedule;
    private String title;
    private String body;
    private int weightindex;
    private Date postdate;
    private Date updatedate;







    @Generated(hash = 1473453159)
    public Sections(Long id, int lawid, String sid, String partschedule,
            String title, String body, int weightindex, Date postdate,
            Date updatedate) {
        this.id = id;
        this.lawid = lawid;
        this.sid = sid;
        this.partschedule = partschedule;
        this.title = title;
        this.body = body;
        this.weightindex = weightindex;
        this.postdate = postdate;
        this.updatedate = updatedate;
    }

    @Generated(hash = 1855077620)
    public Sections() {
    }







    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLawid() {
        return lawid;
    }

    public void setLawid(int lawid) {
        this.lawid = lawid;
    }

    public String getPartschedule() {
        return partschedule;
    }

    public void setPartschedule(String partschedule) {
        this.partschedule = partschedule;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    public int getWeightindex() {
        return weightindex;
    }

    public void setWeightindex(int weightindex) {
        this.weightindex = weightindex;
    }

    public Date getPostdate() {
        return postdate;
    }

    public void setPostdate(Date postdate) {
        this.postdate = postdate;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }



}
