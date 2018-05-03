package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 12/22/16.
 */
@Entity( createInDb = false)
public class Researchrequest {


    @Id
    private Long id;
    private String title;
    private String description;
    private String researchdetail;
    private String recommendedcitations;
    private int status;
    private String message;
    private String comment;
    private Date expecteddate;
    private Date createdate;
    private Date updatedate;








    @Generated(hash = 1224328216)
    public Researchrequest(Long id, String title, String description,
            String researchdetail, String recommendedcitations, int status,
            String message, String comment, Date expecteddate, Date createdate,
            Date updatedate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.researchdetail = researchdetail;
        this.recommendedcitations = recommendedcitations;
        this.status = status;
        this.message = message;
        this.comment = comment;
        this.expecteddate = expecteddate;
        this.createdate = createdate;
        this.updatedate = updatedate;
    }

    @Generated(hash = 424761763)
    public Researchrequest() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResearchdetail() {
        return researchdetail;
    }

    public void setResearchdetail(String researchdetail) {
        this.researchdetail = researchdetail;
    }

    public String getRecommendedcitations() {
        return recommendedcitations;
    }

    public void setRecommendedcitations(String recommendedcitations) {
        this.recommendedcitations = recommendedcitations;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getExpecteddate() {
        return expecteddate;
    }

    public void setExpecteddate(Date expecteddate) {
        this.expecteddate = expecteddate;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }


}
