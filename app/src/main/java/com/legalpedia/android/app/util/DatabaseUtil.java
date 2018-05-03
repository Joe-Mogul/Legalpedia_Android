package com.legalpedia.android.app.util;

import android.database.sqlite.SQLiteStatement;
import android.text.Html;
import android.util.Log;

import com.legalpedia.android.app.models.Articles;
import com.legalpedia.android.app.models.ArticlesDao;
import com.legalpedia.android.app.models.Category;
import com.legalpedia.android.app.models.CategoryDao;
import com.legalpedia.android.app.models.Courts;
import com.legalpedia.android.app.models.CourtsDao;
import com.legalpedia.android.app.models.DaoMaster;
import com.legalpedia.android.app.models.Dictionary;
import com.legalpedia.android.app.models.DictionaryDao;
import com.legalpedia.android.app.models.Judgements;
import com.legalpedia.android.app.models.JudgementsDao;
import com.legalpedia.android.app.models.Laws;
import com.legalpedia.android.app.models.LawsDao;
import com.legalpedia.android.app.models.Maxims;
import com.legalpedia.android.app.models.MaximsDao;
import com.legalpedia.android.app.models.Precedence;
import com.legalpedia.android.app.models.PrecedenceDao;
import com.legalpedia.android.app.models.Principles;
import com.legalpedia.android.app.models.PrinciplesDao;
import com.legalpedia.android.app.models.Publications;
import com.legalpedia.android.app.models.PublicationsDao;
import com.legalpedia.android.app.models.Ratio;
import com.legalpedia.android.app.models.RatioDao;
import com.legalpedia.android.app.models.Rules;
import com.legalpedia.android.app.models.RulesDao;
import com.legalpedia.android.app.models.Sections;
import com.legalpedia.android.app.models.SectionsDao;
import com.legalpedia.android.app.models.Subcategory;
import com.legalpedia.android.app.models.SubcategoryDao;
import com.legalpedia.android.app.models.Subjects;
import com.legalpedia.android.app.models.SubjectsDao;
import com.legalpedia.android.app.models.Summary;
import com.legalpedia.android.app.models.SummaryDao;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by adebayoolabode on 5/7/17.
 */

public class DatabaseUtil {


    private static Database database=null;


    public static void dumpArticles(List hashlist, List<Articles> articlesList){
        String title="";
        String content="";

        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("title")) {
                    title = String.valueOf(hash.get(key));
                }
                if(key.equals("content")) {
                    content = String.valueOf(hash.get(key));
                }
            }


        }

        if(!title.equals("")) {
            Articles article = new Articles();
            article.setTitle(title);
            article.setContent(content);
            articlesList.add(article);
        }
    }

    public static void dumpCourts(List hashlist,List<Courts> listCourt){
        String title="";
        String name="";
        long id =0;
        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("id")) {
                    id = Long.parseLong(String.valueOf(hash.get(key)));
                }
                if(key.equals("title")) {
                    title = String.valueOf(hash.get(key));
                }
                if(key.equals("name")) {
                    name = String.valueOf(hash.get(key));
                }
            }

        }
        if(!title.equals("")) {
            Courts court = new Courts();
            court.setId(id);
            court.setTitle(title);
            court.setName(name);
            listCourt.add(court);
        }
    }

    public static void dumpCategory(List hashlist,List<Category> listCategory){
        String name = "";
        String description = "";
        String iconurl = "";
        int weightindex  = 0;
        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("name")) {
                    name = String.valueOf(hash.get(key));
                }
                if(key.equals("description")) {
                    description = String.valueOf(hash.get(key));
                }
                if(key.equals("iconurl")) {
                    iconurl = String.valueOf(hash.get(key));
                }
                if(key.equals("weightindex")) {
                    weightindex = Integer.valueOf(String.valueOf(hash.get(key)));
                }
            }

        }
        if(!name.equals("")) {
            Category category = new Category();
            category.setDescription(description);
            category.setName(name);
            category.setIconurl(iconurl);
            category.setWeightindex(weightindex);
            listCategory.add(category);
        }
    }


    public static void dumpDictionary(List hashlist,List<Dictionary> listDictionary){
        String title = "";
        String content = "";
        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("title")) {
                    title = String.valueOf(hash.get(key));
                }
                if(key.equals("content")) {
                    content = String.valueOf(hash.get(key));
                }
            }

        }
        if(!title.equals("")) {
            Dictionary dictionary = new Dictionary();
            dictionary.setTitle(title);
            dictionary.setContent(content);
            listDictionary.add(dictionary);
        }
    }

    public static void dumpMaxims(List hashlist,List<Maxims> listMaxims){
        String title = "";
        String content = "";
        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("title")) {
                    title = String.valueOf(hash.get(key));
                }
                if(key.equals("content")) {
                    content = String.valueOf(hash.get(key));
                }
            }

        }
        if(!title.equals("")) {
            Maxims maxims = new Maxims();
            maxims.setTitle(title);
            maxims.setContent(content);
            listMaxims.add(maxims);
        }
    }

    public static void dumpPrinciples(List hashlist,List<Principles> principlesList){
        String name = "";
        int subjectid = 0;
        long id=0L;
        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("id")) {
                    id = Long.valueOf(String.valueOf(hash.get(key)));
                }
                if(key.equals("name")) {
                    name = String.valueOf(hash.get(key));
                }
                if(key.equals("subjectid")) {
                    subjectid = Integer.valueOf(String.valueOf(hash.get(key)));
                }
            }

        }
        if(!name.equals("")) {
            Principles principles = new Principles();
            principles.setId(id);
            principles.setSubjectid(subjectid);
            principles.setName(name);
            principlesList.add(principles);
        }
    }


    public static void dumpPrecedence(List hashlist,List<Precedence> precedenceList){
        String category = "";
        String title = "";
        String content="";
        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("category")) {
                    category = String.valueOf(hash.get(key));
                }
                if(key.equals("title")) {
                    title = String.valueOf(hash.get(key));
                }
                if(key.equals("content")) {
                    content = String.valueOf(hash.get(key));
                }
            }

        }
        if(!title.equals("")) {
            Precedence precedence = new Precedence();
            precedence.setCategory(category);
            precedence.setTitle(title);
            precedence.setContent(content);
            precedenceList.add(precedence);
        }
    }

    public static void dumpLaws2(List hashlist,List<Laws> lawsList){
        String title= "";
        String description= "";
        String number= "";
        Date date = new Date();
        Date postdate = new Date();
        Date updatedate = new Date();

        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("number")) {
                    number = String.valueOf(hash.get(key));
                }
                if(key.equals("title")) {
                    title = String.valueOf(hash.get(key));
                }
                if(key.equals("description")) {
                    description = String.valueOf(hash.get(key));
                }

                if(key.equals("date")) {
                    date = new Date();
                    try {
                        if(String.valueOf(hash.get(key)).length()>2) {
                            try {
                                date = new SimpleDateFormat("MMM d, yyyy").parse(String.valueOf(hash.get(key)));

                            }
                            catch(Exception ex){
                                date = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(hash.get(key)));
                            }
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
                if(key.equals("postdate")) {
                    postdate = new Date();
                    try {
                        postdate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(hash.get(key)));
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
                if(key.equals("updatedate")) {
                    updatedate = new Date();
                    try {

                        updatedate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(hash.get(key)));
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }


                }

            }

        }
        if(!title.equals("")) {
            Laws laws = new Laws();
            laws.setTitle(title);
            laws.setDescription(description);
            laws.setNumber(number);
            laws.setDate(date);
            laws.setPostdate(postdate);
            laws.setUpdatedate(updatedate);
            lawsList.add(laws);
        }
    }


    public static void dumpPublication(List hashlist,List<Publications> publicationList){
        String title="";
        String content="";
        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("title")) {
                    title = String.valueOf(hash.get(key));
                }
                if(key.equals("content")) {
                    content = String.valueOf(hash.get(key));
                }

            }

        }
        if(!title.equals("")) {
            Publications publication = new Publications();
            publication.setTitle(title);
            publication.setContent(content);
            publicationList.add(publication);
        }

    }

    public static void dumpRatio(List hashlist,List<Ratio> ratioList){
        long id = 0L;
        String title="";
        String content="";

        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("id")) {
                    id = Long.parseLong(String.valueOf(hash.get(key)));
                }
                if(key.equals("title")) {
                    title = String.valueOf(hash.get(key));
                }
                if(key.equals("content")) {
                    content = String.valueOf(hash.get(key));
                }
            }

        }
        if(!title.equals("")) {
            Ratio ratio = new Ratio();
            ratio.setId(id);
            ratio.setTitle(title);
            ratio.setContent(content);
            ratioList.add(ratio);
        }

    }


    public static void dumpRules(List hashlist,List<Rules> rulesList){
        String name="";
        String type="";
        String section ="";
        String title="";
        String content="";

        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();


            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("name")) {
                    name = String.valueOf(hash.get(key));
                }
                if(key.equals("type")) {
                    type = String.valueOf(hash.get(key));
                }
                if(key.equals("section")) {
                    section = String.valueOf(hash.get(key));
                }
                if(key.equals("title")) {
                    title = String.valueOf(hash.get(key));
                }
                if(key.equals("content")) {
                    content = String.valueOf(hash.get(key));
                }

            }
        }
        if(!name.equals("")) {
            Rules rules = new Rules();
            rules.setName(name);
            rules.setType(type);
            rules.setSection(section);
            rules.setTitle(title);
            rules.setContent(content);
            rulesList.add(rules);
        }

    }


    public static void dumpSection2(List hashlist,List<Sections> sectionsList){
        int lawid=0;
        String partschedule="";
        String title="";
        String body="";
        int weightindex=0;
        Date postdate= new Date();
        Date updatedate= new Date();


        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("partschedule")) {
                    partschedule = String.valueOf(hash.get(key));
                }
                if(key.equals("lawid")) {
                    lawid = Integer.valueOf(String.valueOf(hash.get(key)));
                }
                if(key.equals("body")) {
                    body = String.valueOf(hash.get(key));
                }
                if(key.equals("title")) {
                    title = String.valueOf(hash.get(key));
                }
                if(key.equals("weightindex")) {
                    weightindex = Integer.valueOf(String.valueOf(hash.get(key)));
                }
                if(key.equals("postdate")) {
                    try {
                        postdate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(hash.get(key)));
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
                if(key.equals("updatedate")) {
                    try{
                        updatedate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(hash.get(key)));
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                }

            }

        }
        if(!title.equals("")) {
            Sections sections = new Sections();
            sections.setLawid(lawid);
            sections.setPartschedule(partschedule);
            sections.setTitle(title);
            sections.setBody(body);
            sections.setUpdatedate(updatedate);
            sections.setPostdate(postdate);
            sections.setWeightindex(weightindex);
            sectionsList.add(sections);
        }

    }



    public static void dumpLaws(List hashlist, List<Laws> lawsList){
        String title= "";
        String cover= "";
        String sid= "";
        String description= "";
        String number= "";
        Date date = null;
        Date postdate = new Date();
        Date updatedate = new Date();
        long id=0L;
        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("id")) {
                    sid = String.valueOf(hash.get(key));
                    id = Long.valueOf(sid);
                }

                if(key.equals("number")) {
                    number = String.valueOf(hash.get(key));
                }
                if(key.equals("title")) {
                    title = String.valueOf(hash.get(key));
                }
                if(key.equals("cover")) {
                    cover = String.valueOf(hash.get(key));
                }
                if(key.equals("description")) {
                    description = String.valueOf(hash.get(key));
                }

                if(key.equals("date")) {
                    //date=String.valueOf(hash.get(key));
                    try {
                        //"March 2, 1985"
                        date = new SimpleDateFormat("MMMMM dd, yyyy").parse(String.valueOf(hash.get(key)));
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
                if(key.equals("postdate")) {
                    postdate = new Date();
                    try {
                        postdate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(hash.get(key)));
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
                if(key.equals("updatedate")) {
                    updatedate = new Date();
                    try {

                        updatedate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(hash.get(key)));
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }


                }


            }



        }
        if(!title.equals("")) {
            Laws laws = new Laws();
            laws.setId(id);
            laws.setSid(sid);
            laws.setTitle(title);
            laws.setDescription(description);
            laws.setNumber(number);
            laws.setDate(date);
            laws.setPostdate(postdate);
            laws.setUpdatedate(updatedate);
            lawsList.add(laws);
        }

    }

    public static void dumpSection(List hashlist,List<Sections> sectionList){
        int lawid=0;
        String partschedule="";
        String title="";
        String sid="";
        String body="";
        int weightindex=0;
        Date postdate= new Date();
        Date updatedate= new Date();
        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("id")) {
                    sid = String.valueOf(hash.get(key));
                }
                if(key.equals("partschedule")) {
                    partschedule = String.valueOf(hash.get(key));
                }
                if(key.equals("lawid")) {
                    lawid = Integer.valueOf(String.valueOf(hash.get(key)));
                }
                if(key.equals("body")) {
                    body = String.valueOf(hash.get(key));
                }
                if(key.equals("title")) {
                    title = String.valueOf(hash.get(key));
                }
                if(key.equals("weightindex")) {
                    weightindex = Integer.valueOf(String.valueOf(hash.get(key)));
                }
                if(key.equals("postdate")) {
                    try {
                        postdate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(hash.get(key)));
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
                if(key.equals("updatedate")) {
                    try{
                        updatedate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(hash.get(key)));
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                }

            }

        }

        if(!title.equals("")) {
            Sections sections = new Sections();
            sections.setSid(sid);
            sections.setLawid(lawid);
            sections.setPartschedule(partschedule);
            sections.setTitle(title);
            sections.setBody(body);
            sections.setUpdatedate(updatedate);
            sections.setPostdate(postdate);
            sections.setWeightindex(weightindex);
            sectionList.add(sections);
        }



    }




    public static void dumpSubCategory(List hashlist,List<Subcategory> subcategoryList){
        int catid=0;
        String name = "";
        String description = "";
        String iconurl = "";
        int weightindex  = 0;

        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("name")) {
                    name = String.valueOf(hash.get(key));
                }
                if(key.equals("description")) {
                    description = String.valueOf(hash.get(key));
                }
                if(key.equals("iconurl")) {
                    iconurl = String.valueOf(hash.get(key));
                }
                if(key.equals("weightindex")) {
                    weightindex = Integer.valueOf(String.valueOf(hash.get(key)));
                }
                if(key.equals("catid")) {
                    catid = Integer.valueOf(String.valueOf(hash.get(key)));
                }

            }
        }

        if(!name.equals("")) {
            Subcategory subcategory = new Subcategory();
            subcategory.setCatid(catid);
            subcategory.setDescription(description);
            subcategory.setName(name);
            subcategory.setIconurl(iconurl);
            subcategory.setWeightindex(weightindex);
            subcategoryList.add(subcategory);
        }
    }


    public static void dumpSubjects(List hashlist,List<Subjects> subjectList){
        int subjectid=0;
        String name="";

        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {

                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("id")) {
                    subjectid = Integer.valueOf(String.valueOf(hash.get(key)));
                }
                if(key.equals("name")) {
                    name = String.valueOf(hash.get(key));
                }
            }

        }
        if(!name.equals("")) {
            long id=Long.valueOf(String.valueOf(subjectid));
            Subjects subjects = new Subjects();
            subjects.setId(id);
            subjects.setSid(subjectid);
            subjects.setName(name);
            subjectList.add(subjects);
        }
    }

    public static void dumpSummary(List hashlist,List<Summary> summaryList){
        long id =0L;
        String area_of_law="";
        String case_title="";
        String summary_of_facts="";
        String held="";
        String issue="";
        String ratio="";
        String cases_cited="";
        String statutes_cited="";
        String subject_matter="";
        int principleid=0;
        int caseid=0;
        int courtid=0;
        Date judgement_date=null;
        String l_citation="";
        String o_citations="";
        String sitting_at="";
        String suit_number="";
        String coram="";
        String party_a_type="";
        String party_a_names="";
        String party_b_type="";
        String party_b_names="";
        Date date=null;
        String category_tags="";
        Date date_posted= new Date();
        Date date_updated=new Date();

        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("id")) {

                    id = Long.parseLong(String.valueOf(hash.get(key)));
                   // Log.d("Summary",suit_number);
                }
                if(key.equals("suit_number")) {

                    suit_number = String.valueOf(hash.get(key));
                    // Log.d("Summary",suit_number);
                }
                if(key.equals("area_of_law")) {
                    area_of_law = String.valueOf(hash.get(key));
                }
                if(key.equals("case_title")) {
                    case_title = String.valueOf(hash.get(key));
                }

                if(key.equals("summary_of_facts")) {
                    summary_of_facts = String.valueOf(hash.get(key));
                }
                if(key.equals("held")) {
                    held = String.valueOf(hash.get(key));
                }
                if(key.equals("issue")) {
                    issue = String.valueOf(hash.get(key));
                }
                if(key.equals("ratio")) {
                    ratio = String.valueOf(hash.get(key));
                }
                if(key.equals("subject_matter")) {
                    subject_matter = String.valueOf(hash.get(key));
                }
                if(key.equals("cases_cited")) {
                    cases_cited = String.valueOf(hash.get(key));
                }
                if(key.equals("statutes_cited")) {
                    statutes_cited = String.valueOf(hash.get(key));
                }
                if(key.equals("principleid")) {
                    principleid = Integer.valueOf(String.valueOf(hash.get(key)));
                }
                if(key.equals("caseid")) {
                    caseid = Integer.valueOf(String.valueOf(hash.get(key)));
                }
                if(key.equals("courtid")) {
                    courtid = Integer.valueOf(String.valueOf(hash.get(key)));
                }
                if(key.equals("judgement_date")) {
                    try {

                        if(!String.valueOf(hash.get(key)).equals("null") && !String.valueOf(hash.get(key)).equals("\"")) {
                            String judgedate = removeTags(String.valueOf(hash.get(key))).toString();
                            judgement_date = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy").parse(judgedate);
                        }
                    }
                    catch(Exception ex){
                        Log.d("Invalid Date",hash.get(key).toString());
                        //ex.printStackTrace();
                    }
                }
                if(key.equals("l_citation")) {
                    l_citation = String.valueOf(hash.get(key));
                }
                if(key.equals("o_citations")) {
                    o_citations = String.valueOf(hash.get(key));
                }
                if(key.equals("sitting_at")) {
                    sitting_at = String.valueOf(hash.get(key));
                }
                if(key.equals("coram")) {
                    coram = String.valueOf(hash.get(key));
                }
                if(key.equals("party_a_type")) {
                    party_a_type = String.valueOf(hash.get(key));
                }
                if(key.equals("party_a_names")) {
                    party_a_names = String.valueOf(hash.get(key));
                }
                if(key.equals("party_b_type")) {
                    party_b_type = String.valueOf(hash.get(key));
                }
                if(key.equals("party_b_names")) {
                    party_b_names = String.valueOf(hash.get(key));
                }
                if(key.equals("date")) {
                    try {
                        if(!String.valueOf(hash.get(key)).equals("null") && !String.valueOf(hash.get(key)).equals("\"")) {
                            date = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(hash.get(key)));
                        }
                    }
                    catch(Exception ex){
                        Log.d("Invalid Date",hash.get(key).toString());
                        //ex.printStackTrace();
                    }
                }
                if(key.equals("category_tags")) {
                    category_tags = String.valueOf(hash.get(key));
                }
                if(key.equals("date_posted")) {
                    date_posted = new Date();
                    try {
                        if(!String.valueOf(hash.get(key)).equals("null") && !String.valueOf(hash.get(key)).equals("\"")) {
                            date_posted = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(hash.get(key)));
                        }
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
                if(key.equals("date_updated")) {
                    date_updated= new Date();
                    try{
                        if(!String.valueOf(hash.get(key)).equals("null")) {
                            date_updated = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(hash.get(key)));
                        }
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }

        }
        if(!case_title.equals("")) {

            Summary summary = new Summary();
            summary.setId(id);
            summary.setArea_of_law(area_of_law);
            summary.setCase_title(case_title);
            summary.setSummary_of_facts(summary_of_facts);
            summary.setHeld(held);
            summary.setIssue(issue);
            summary.setRatio(ratio);
            summary.setCases_cited(cases_cited);
            summary.setStatutes_cited(statutes_cited);
            summary.setSubject_matter(subject_matter);
            summary.setPrincipleid(principleid);
            summary.setCaseid(caseid);
            summary.setCourtid(courtid);
            summary.setJudgement_date(judgement_date);
            summary.setL_citation(l_citation);
            summary.setO_citations(o_citations);
            summary.setSitting_at(sitting_at);
            summary.setCoram(coram);
            summary.setSuit_number(suit_number);
            summary.setParty_a_type(party_a_type);
            summary.setParty_a_names(party_a_names);
            summary.setParty_b_type(party_b_type);
            summary.setParty_b_names(party_b_names);
            summary.setDate(date);
            summary.setCategory_tags(category_tags);
            summary.setDate_posted(date_posted);
            summary.setDate_updated(date_updated);
            summaryList.add(summary);
        }

    }

    public static void dumpJudgements(List hashlist,List<Judgements> judgementList){
        long id= 0L;
        String counsel="";
        String category_tags="";
        Date date_posted = new Date();
        Date date_updated= new Date();
        int summaryid=0;
        String jdgement="";

        Iterator itr = hashlist.iterator();
        while (itr.hasNext()) {
            HashMap hash = (HashMap) itr.next();
            Iterator it = hash.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = String.valueOf(hash.get(key));
                if(key.equals("id")) {
                    id = Long.valueOf(String.valueOf(hash.get(key)));
                }
                if(key.equals("summaryid")) {
                    summaryid = Integer.valueOf(String.valueOf(hash.get(key)));
                }
                if(key.equals("judgement")) {
                    jdgement = String.valueOf(hash.get(key));
                }
                if(key.equals("counsel")) {
                    counsel = String.valueOf(hash.get(key));
                }
                if(key.equals("category_tags")) {
                    category_tags = String.valueOf(hash.get(key));
                }
                if(key.equals("date_posted")) {
                    date_posted = new Date();
                    try {
                        if(!String.valueOf(hash.get(key)).equals("null")) {
                            date_posted = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(hash.get(key)));
                        }
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
                if(key.equals("date_updated")) {
                    date_updated= new Date();
                    try{
                        if(!String.valueOf(hash.get(key)).equals("null")) {
                            date_updated = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(hash.get(key)));
                        }
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                }

            }

        }
        if(!jdgement.equals("")){
            Judgements judgement = new Judgements();
            judgement.setId(id);
            judgement.setSummaryid(summaryid);
            judgement.setJudgement(jdgement);
            judgement.setCounsel(counsel);
            judgement.setCategory_tags(category_tags);
            judgement.setDate_posted(date_posted);
            judgement.setDate_updated(date_updated);
            judgementList.add(judgement);
        }

    }




    public static Database getDB(DaoMaster dm){
        if(database==null){
            database = dm.getDatabase();
        }

        return database;
    }



    public void bulkInsertJudgements(List<Judgements> judgements,DaoMaster dm){
        Database db = getDB(dm);

        String sql = "INSERT INTO "+ JudgementsDao.TABLENAME +" VALUES (?,?,?);";
            DatabaseStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (Judgements judge : judgements) {
                statement.clearBindings();
                 statement.execute();
            }
            db.setTransactionSuccessful();
            db.endTransaction();


    }


    public static String removeTags(String in)
    {
        int index=0;
        int index2=0;
        while(index!=-1)
        {
            index = in.indexOf("<");
            index2 = in.indexOf(">", index);
            if(index!=-1 && index2!=-1)
            {
                in = in.substring(0, index).concat(in.substring(index2+1, in.length()));
            }
        }
        return in;
    }
}
