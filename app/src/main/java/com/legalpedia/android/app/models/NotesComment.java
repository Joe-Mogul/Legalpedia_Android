package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 7/27/17.
 */
@Entity( createInDb = false)
public class NotesComment {
    @Id
    private Long id;
    private int uid;
    private int noteid;
    private String comment;
    private Date createdate;


    @Generated(hash = 1101440754)
    public NotesComment(Long id, int uid, int noteid, String comment,
            Date createdate) {
        this.id = id;
        this.uid = uid;
        this.noteid = noteid;
        this.comment = comment;
        this.createdate = createdate;
    }

    @Generated(hash = 439325203)
    public NotesComment() {
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

    public int getNoteid() {
        return noteid;
    }

    public void setNoteid(int noteid) {
        this.noteid = noteid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }


}
