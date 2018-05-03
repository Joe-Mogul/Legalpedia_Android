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
public class Account {
    @Id
    private Long id;
    private int uid;
    private String accounttype;
    private Date lastrenewdate;
    private Date nextrenewdate;
    private Date createdate;
    @Generated(hash = 109273520)
    public Account(Long id, int uid, String accounttype, Date lastrenewdate,
            Date nextrenewdate, Date createdate) {
        this.id = id;
        this.uid = uid;
        this.accounttype = accounttype;
        this.lastrenewdate = lastrenewdate;
        this.nextrenewdate = nextrenewdate;
        this.createdate = createdate;
    }
    @Generated(hash = 882125521)
    public Account() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getUid() {
        return this.uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public String getAccounttype() {
        return this.accounttype;
    }
    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }
    public Date getLastrenewdate() {
        return this.lastrenewdate;
    }
    public void setLastrenewdate(Date lastrenewdate) {
        this.lastrenewdate = lastrenewdate;
    }
    public Date getNextrenewdate() {
        return this.nextrenewdate;
    }
    public void setNextrenewdate(Date nextrenewdate) {
        this.nextrenewdate = nextrenewdate;
    }
    public Date getCreatedate() {
        return this.createdate;
    }
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }






}
