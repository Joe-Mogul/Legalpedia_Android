package com.legalpedia.android.app.models;



import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;


/**
 * Created by adebayoolabode on 11/7/16.
 */

@Entity( createInDb = false)
public class Dictionary {
    @Id
    private Long id;
    private String title;
    private String content;
    @Generated(hash = 619142279)
    public Dictionary(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
    @Generated(hash = 487998537)
    public Dictionary() {
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
