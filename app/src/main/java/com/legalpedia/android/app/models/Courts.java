package com.legalpedia.android.app.models;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 12/22/16.
 */
@Entity( createInDb = false)
public class Courts {


    @Id
    private Long id;
    private String title;
    private String name;
    @Generated(hash = 1717662971)
    public Courts(Long id, String title, String name) {
        this.id = id;
        this.title = title;
        this.name = name;
    }

    @Generated(hash = 1011554979)
    public Courts() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
