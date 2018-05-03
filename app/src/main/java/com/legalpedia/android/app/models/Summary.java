package com.legalpedia.android.app.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by adebayoolabode on 12/22/16.
 */

@Entity( createInDb = false)
public class Summary {
    @Id
    private Long id;
    private String case_title;
    private String area_of_law;
    private String summary_of_facts;
    private String held;
    private String issue;
    private String ratio;
    private String cases_cited;
    private String statutes_cited;
    private String subject_matter;
    private int principleid;
    private int caseid;
    private int courtid;
    private Date judgement_date;
    private String l_citation;
    private String o_citations;
    private String sitting_at;
    @Property(nameInDb = "suit_number")
    private String suit_number;
    private String coram;
    private String party_a_type;
    private String party_a_names;
    private String party_b_type;
    private String party_b_names;
    private Date date;
    private String category_tags;
    private Date date_posted;
    private Date date_updated;


    @Generated(hash = 1535167678)
    public Summary(Long id, String case_title, String area_of_law,
            String summary_of_facts, String held, String issue, String ratio,
            String cases_cited, String statutes_cited, String subject_matter,
            int principleid, int caseid, int courtid, Date judgement_date,
            String l_citation, String o_citations, String sitting_at,
            String suit_number, String coram, String party_a_type,
            String party_a_names, String party_b_type, String party_b_names,
            Date date, String category_tags, Date date_posted, Date date_updated) {
        this.id = id;
        this.case_title = case_title;
        this.area_of_law = area_of_law;
        this.summary_of_facts = summary_of_facts;
        this.held = held;
        this.issue = issue;
        this.ratio = ratio;
        this.cases_cited = cases_cited;
        this.statutes_cited = statutes_cited;
        this.subject_matter = subject_matter;
        this.principleid = principleid;
        this.caseid = caseid;
        this.courtid = courtid;
        this.judgement_date = judgement_date;
        this.l_citation = l_citation;
        this.o_citations = o_citations;
        this.sitting_at = sitting_at;
        this.suit_number = suit_number;
        this.coram = coram;
        this.party_a_type = party_a_type;
        this.party_a_names = party_a_names;
        this.party_b_type = party_b_type;
        this.party_b_names = party_b_names;
        this.date = date;
        this.category_tags = category_tags;
        this.date_posted = date_posted;
        this.date_updated = date_updated;
    }

    @Generated(hash = 1461598545)
    public Summary() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCase_title() {
        return case_title;
    }

    public void setCase_title(String case_title) {
        this.case_title = case_title;
    }

    public String getArea_of_law() {
        return area_of_law;
    }

    public void setArea_of_law(String area_of_law) {
        this.area_of_law = area_of_law;
    }

    public String getSummary_of_facts() {
        return summary_of_facts;
    }

    public void setSummary_of_facts(String summary_of_facts) {
        this.summary_of_facts = summary_of_facts;
    }

    public String getHeld() {
        return held;
    }

    public void setHeld(String held) {
        this.held = held;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getCases_cited() {
        return cases_cited;
    }

    public void setCases_cited(String cases_cited) {
        this.cases_cited = cases_cited;
    }

    public String getStatutes_cited() {
        return statutes_cited;
    }

    public void setStatutes_cited(String statutes_cited) {
        this.statutes_cited = statutes_cited;
    }

    public String getSubject_matter() {
        return subject_matter;
    }

    public void setSubject_matter(String subject_matter) {
        this.subject_matter = subject_matter;
    }

    public int getPrincipleid() {
        return principleid;
    }

    public void setPrincipleid(int principleid) {
        this.principleid = principleid;
    }

    public int getCaseid() {
        return caseid;
    }

    public void setCaseid(int caseid) {
        this.caseid = caseid;
    }

    public int getCourtid() {
        return courtid;
    }

    public void setCourtid(int courtid) {
        this.courtid = courtid;
    }



    public Date getJudgement_date() {
        return judgement_date;
    }

    public void setJudgement_date(Date judgement_date) {
        this.judgement_date = judgement_date;
    }

    public String getL_citation() {
        return l_citation;
    }

    public void setL_citation(String l_citation) {
        this.l_citation = l_citation;
    }

    public String getO_citations() {
        return o_citations;
    }

    public void setO_citations(String o_citations) {
        this.o_citations = o_citations;
    }

    public String getSitting_at() {
        return sitting_at;
    }

    public void setSitting_at(String sitting_at) {
        this.sitting_at = sitting_at;
    }

    public String getSuit_number() {
        return suit_number;
    }

    public void setSuit_number(String suit_number) {
        this.suit_number = suit_number;
    }

    public String getCoram() {
        return coram;
    }

    public void setCoram(String coram) {
        this.coram = coram;
    }

    public String getParty_a_type() {
        return party_a_type;
    }

    public void setParty_a_type(String party_a_type) {
        this.party_a_type = party_a_type;
    }

    public String getParty_a_names() {
        return party_a_names;
    }

    public void setParty_a_names(String party_a_names) {
        this.party_a_names = party_a_names;
    }

    public String getParty_b_type() {
        return party_b_type;
    }

    public void setParty_b_type(String party_b_type) {
        this.party_b_type = party_b_type;
    }

    public String getParty_b_names() {
        return party_b_names;
    }

    public void setParty_b_names(String party_b_names) {
        this.party_b_names = party_b_names;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
