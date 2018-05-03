package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 12/13/16.
 */
@Entity( createInDb = true)
public class UpdateRequest {
    @Id
    private Long id;
    private String resourceid;
    private String filename;
    private String filesize;
    private String updatedate;


    @Generated(hash = 242078048)
    public UpdateRequest(Long id, String resourceid, String filename,
            String filesize, String updatedate) {
        this.id = id;
        this.resourceid = resourceid;
        this.filename = filename;
        this.filesize = filesize;
        this.updatedate = updatedate;
    }

    @Generated(hash = 1219545242)
    public UpdateRequest() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceid() {
        return resourceid;
    }

    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
    }
}
