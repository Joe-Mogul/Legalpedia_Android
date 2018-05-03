package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 12/22/16.
 */
@Entity( createInDb = false)
public class Tasks {
    @Id
    private Long id;
    private String name;
    private String assignee;
    private String description;
    private String due;
    private String status;
    private String completed;
    private String case_title;
    private String notes;

    @Generated(hash = 1812995867)
    public Tasks(Long id, String name, String assignee, String description,
            String due, String status, String completed, String case_title,
            String notes) {
        this.id = id;
        this.name = name;
        this.assignee = assignee;
        this.description = description;
        this.due = due;
        this.status = status;
        this.completed = completed;
        this.case_title = case_title;
        this.notes = notes;
    }

    @Generated(hash = 1908996543)
    public Tasks() {
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

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getCase_title() {
        return case_title;
    }

    public void setCase_title(String case_title) {
        this.case_title = case_title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }



}
