package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 8/6/17.
 */
@Entity( createInDb = false)
public class Subscription {
    @Id
    private Long id;
    private int uid;
    private Date expirydate;
    private Date lastrenewdate;
    @Generated(hash = 1775424582)
    public Subscription(Long id, int uid, Date expirydate, Date lastrenewdate) {
        this.id = id;
        this.uid = uid;
        this.expirydate = expirydate;
        this.lastrenewdate = lastrenewdate;
    }

    @Generated(hash = 1800298428)
    public Subscription() {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Date getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(Date expirydate) {
        this.expirydate = expirydate;
    }

    public Date getLastrenewdate() {
        return lastrenewdate;
    }

    public void setLastrenewdate(Date lastrenewdate) {
        this.lastrenewdate = lastrenewdate;
    }


}
