package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;


/**
 * Created by adebayoolabode on 11/7/16.
 */

@Entity( createInDb = false)
public class Subjects {
    @Id
    private Long id;


    private int sid;
    private String name;



    @Generated(hash = 390297276)
    public Subjects(Long id, int sid, String name) {
        this.id = id;
        this.sid = sid;
        this.name = name;
    }
    @Generated(hash = 1488062786)
    public Subjects() {
    }



    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }







}
