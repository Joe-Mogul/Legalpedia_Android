package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;


/**
 * Created by adebayoolabode on 12/13/16.
 */
@Entity( createInDb = false)
public class Subcategory {
    @Id
    private Long id;
    private int catid;
    private String name;
    private String description;



    public void setWeightindex(int weightindex) {
        this.weightindex = weightindex;
    }

    private String iconurl;
    private int weightindex;



    @Generated(hash = 1751375031)
    public Subcategory(Long id, int catid, String name, String description,
            String iconurl, int weightindex) {
        this.id = id;
        this.catid = catid;
        this.name = name;
        this.description = description;
        this.iconurl = iconurl;
        this.weightindex = weightindex;
    }

    @Generated(hash = 1953416725)
    public Subcategory() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
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

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public int getWeightindex() {
        return weightindex;
    }



}
