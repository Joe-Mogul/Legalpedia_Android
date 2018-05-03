package com.legalpedia.android.app.models;



import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 12/13/16.
 */
@Entity( createInDb = false)
public class Category {
    @Id
    private Long id;
    private Integer weightindex;
    private String name;
    private String description;
    private String iconurl;



    @Generated(hash = 522264606)
    public Category(Long id, Integer weightindex, String name, String description,
            String iconurl) {
        this.id = id;
        this.weightindex = weightindex;
        this.name = name;
        this.description = description;
        this.iconurl = iconurl;
    }

    @Generated(hash = 1150634039)
    public Category() {
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getWeightindex() {
        return weightindex;
    }

    public void setWeightindex(Integer weightindex) {
        this.weightindex = weightindex;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }
}
