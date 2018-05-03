package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 11/7/16.
 */

@Entity( createInDb = false)
public class Principles {
    @Id
    private Long id;
    private int subjectid;
    private String name;

    /*@Keep
    public Principles() {
    }*/


    @Generated(hash = 261281626)
    public Principles(Long id, int subjectid, String name) {
        this.id = id;
        this.subjectid = subjectid;
        this.name = name;
    }

    @Generated(hash = 2139103567)
    public Principles() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(int subjectid) {
        this.subjectid = subjectid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
