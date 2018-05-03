package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 11/7/16.
 */
@Entity( createInDb = false)
public class Precedence {
    @Id
    private Long id;
    private String category;
    private String title;
    private String content;
    @Generated(hash = 130688971)
    public Precedence(Long id, String category, String title, String content) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
    }
    @Generated(hash = 482740821)
    public Precedence() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCategory() {
        return this.category;
    }
    public void setCategory(String category) {
        this.category = category;
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
