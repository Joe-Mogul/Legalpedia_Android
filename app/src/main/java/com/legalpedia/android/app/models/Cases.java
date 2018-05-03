package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by adebayoolabode on 12/22/16.
 */
@Entity( createInDb = false)
public class Cases {
    @Id
    private Long id;
    private String name;
    private String title;
    private String suit_number;
    private String client;
    private String judges;
    private String court;
    private String subject;
    private String facts;
    private String commenced;


    @Generated(hash = 934842449)
    public Cases(Long id, String name, String title, String suit_number,
            String client, String judges, String court, String subject,
            String facts, String commenced) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.suit_number = suit_number;
        this.client = client;
        this.judges = judges;
        this.court = court;
        this.subject = subject;
        this.facts = facts;
        this.commenced = commenced;
    }

    @Generated(hash = 1976734171)
    public Cases() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSuit_number() {
        return suit_number;
    }

    public void setSuit_number(String suit_number) {
        this.suit_number = suit_number;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getJudges() {
        return judges;
    }

    public void setJudges(String judges) {
        this.judges = judges;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFacts() {
        return facts;
    }

    public void setFacts(String facts) {
        this.facts = facts;
    }

    public String getCommenced() {
        return commenced;
    }

    public void setCommenced(String commenced) {
        this.commenced = commenced;
    }


}
