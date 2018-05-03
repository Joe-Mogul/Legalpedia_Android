package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

/**
 * Created by adebayoolabode on 12/22/16.
 */
@Entity( createInDb = false)
public class Notes {
    @Id
    private Long id;
    private String title;
    private String content;
    private int uid;
    private int resource;
    private int resourceid;
    private int isprivate;
    private int cancomment;
    private Date editdate;
    private Date createdate;



    @Generated(hash = 270054044)
    public Notes(Long id, String title, String content, int uid, int resource,
            int resourceid, int isprivate, int cancomment, Date editdate,
            Date createdate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.uid = uid;
        this.resource = resource;
        this.resourceid = resourceid;
        this.isprivate = isprivate;
        this.cancomment = cancomment;
        this.editdate = editdate;
        this.createdate = createdate;
    }

    @Generated(hash = 1409607808)
    public Notes() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsprivate() {
        return isprivate;
    }

    public void setIsprivate(int isprivate) {
        this.isprivate = isprivate;
    }
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public int getResourceid() {
        return resourceid;
    }

    public void setResourceid(int resourceid) {
        this.resourceid = resourceid;
    }
    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getEditdate() {
        return editdate;
    }

    public void setEditdate(Date editdate) {
        this.editdate = editdate;
    }

    public int getCancomment() {
        return this.cancomment;
    }

    public void setCancomment(int cancomment) {
        this.cancomment = cancomment;
    }


}
