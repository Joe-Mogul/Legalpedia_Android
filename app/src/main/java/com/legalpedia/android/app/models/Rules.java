package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 12/22/16.
 */
@Entity( createInDb = false)
public class Rules {
    @Id
    private Long id;
    private String name;
    private String type;
    private String section;
    private String title;
    private String content;

    @Generated(hash = 1343037195)
    public Rules(Long id, String name, String type, String section, String title,
            String content) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.section = section;
        this.title = title;
        this.content = content;
    }

    @Generated(hash = 1692174665)
    public Rules() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
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



}
