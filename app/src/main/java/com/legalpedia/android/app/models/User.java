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
public class User {
    @Id
    private Long id;
    private String username;
    private String password;
    private String secret;
    private int status;
    private int role;
    private int isemailverified;
    private int isphoneverified;
    private Date createdate;

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSecret() {
        return this.secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getRole() {
        return this.role;
    }
    public void setRole(int role) {
        this.role = role;
    }
    public int getIsphoneverified() {
        return isphoneverified;
    }

    public void setIsphoneverified(int isphoneverified) {
        this.isphoneverified = isphoneverified;
    }



    @Generated(hash = 832918899)
    public User(Long id, String username, String password, String secret,
            int status, int role, int isemailverified, int isphoneverified,
            Date createdate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.secret = secret;
        this.status = status;
        this.role = role;
        this.isemailverified = isemailverified;
        this.isphoneverified = isphoneverified;
        this.createdate = createdate;
    }
    @Generated(hash = 586692638)
    public User() {
    }

    public int getIsemailverified() {
        return isemailverified;
    }

    public void setIsemailverified(int isemailverified) {
        this.isemailverified = isemailverified;
    }

    public Date getCreatedate() {
        return this.createdate;
    }
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }



}
