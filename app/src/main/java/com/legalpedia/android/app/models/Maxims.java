package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 11/7/16.
 */

@Entity( createInDb = false)
public class Maxims {
    @Id
    private Long id;
    private String title;
    private String content;
    @Generated(hash = 1806838467)
    public Maxims(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
    @Generated(hash = 694340064)
    public Maxims() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }


}
