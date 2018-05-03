package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 12/22/16.
 */
@Entity( createInDb = false)
public class Annotations {
    @Id
    private Long id;
    private int uid;
    private int resource;
    private String title;
    private String content;
    private int resourceid;
    private String comment;






    @Generated(hash = 400644835)
    public Annotations(Long id, int uid, int resource, String title, String content,
            int resourceid, String comment) {
        this.id = id;
        this.uid = uid;
        this.resource = resource;
        this.title = title;
        this.content = content;
        this.resourceid = resourceid;
        this.comment = comment;
    }

    @Generated(hash = 1906870294)
    public Annotations() {
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


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
