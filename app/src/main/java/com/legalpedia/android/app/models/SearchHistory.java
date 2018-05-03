package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 12/13/16.
 */
@Entity( createInDb = false)
public class SearchHistory {
    @Id
    private Long id;
    private String query;
    private String view;

    @Generated(hash = 1646704985)
    public SearchHistory(Long id, String query, String view) {
        this.id = id;
        this.query = query;
        this.view = view;
    }

    @Generated(hash = 1905904755)
    public SearchHistory() {
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }





}
