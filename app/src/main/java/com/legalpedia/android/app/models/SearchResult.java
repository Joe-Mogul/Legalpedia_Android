package com.legalpedia.android.app.models;

import java.util.List;

/**
 * Created by adebayoolabode on 6/23/17.
 */

public class SearchResult implements Comparable<SearchResult>{

    private String title;
    private String description;
    private String resourceid;
    private String resource;
    private String ratioheader;
    private String year;



    private String query;
    private List<String> tokens;


    public SearchResult(String title, String description, String resourceid, String resource,String ratioheader,String year,String query,List<String> tokens) {
        this.title = title;
        this.description = description;
        this.resourceid = resourceid;
        this.resource = resource;
        this.ratioheader = ratioheader;
        this.year = year;
        this.query = query;
        this.tokens = tokens;
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

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }


    public String getResourceid() {
        return resourceid;
    }

    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    public String getRatioheader() {
        return ratioheader;
    }

    public void setRatioheader(String ratioheader) {
        this.ratioheader = ratioheader;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }


    public int compareTo(SearchResult o)
    {
        if(o.ratioheader.length()<=0){
            return 1;
        }else{
            return ratioheader.compareToIgnoreCase(o.ratioheader);
        }

    }



}
