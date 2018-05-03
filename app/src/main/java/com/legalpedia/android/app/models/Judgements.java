package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 12/22/16.
 */
@Entity( createInDb = false)
public class Judgements {
    @Id
    private Long id;
    private int summaryid;
    private String judgement;
    private String counsel;
    private String category_tags;
    private Date date_posted;
    private Date date_updated;

    @Generated(hash = 458244582)
    public Judgements(Long id, int summaryid, String judgement, String counsel,
            String category_tags, Date date_posted, Date date_updated) {
        this.id = id;
        this.summaryid = summaryid;
        this.judgement = judgement;
        this.counsel = counsel;
        this.category_tags = category_tags;
        this.date_posted = date_posted;
        this.date_updated = date_updated;
    }

    @Generated(hash = 1172373343)
    public Judgements() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSummaryid() {
        return summaryid;
    }

    public void setSummaryid(int summaryid) {
        this.summaryid = summaryid;
    }

    public String getJudgement() {
        return judgement;
    }

    public void setJudgement(String judgement) {
        this.judgement = judgement;
    }

    public String getCounsel() {
        return counsel;
    }

    public void setCounsel(String counsel) {
        this.counsel = counsel;
    }

    public String getCategory_tags() {
        return category_tags;
    }

    public void setCategory_tags(String category_tags) {
        this.category_tags = category_tags;
    }

    public Date getDate_posted() {
        return date_posted;
    }

    public void setDate_posted(Date date_posted) {
        this.date_posted = date_posted;
    }

    public Date getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(Date date_updated) {
        this.date_updated = date_updated;
    }
}
