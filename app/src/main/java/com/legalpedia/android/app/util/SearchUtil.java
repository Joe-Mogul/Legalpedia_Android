package com.legalpedia.android.app.util;

import android.util.Log;

import com.legalpedia.android.app.models.Ratio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by adebayoolabode on 7/19/17.
 */

public class SearchUtil {
    private static String queryresult;
    private static String[] commonWords = {"the","court","judgement","of","to","and","a","in","is","it","you","that","he","was","for","on","are","with","as","i"};
    private static List <String>stopwords = new ArrayList<String>(Arrays.asList(commonWords));
    private static int maxratios=20;
    //private static int maxratios=2;

    public static boolean wordNotFound(String textq){
        boolean wordfound = false;
        for(String stopword : stopwords) {
            stopword = stopword.trim();
            if (stopword.equals(textq)){

                wordfound = true;
                break;
            }

        }

        return wordfound;
    }


    public static String findParagraph(String source, List<String> filters, String paragraphSeparator)
    {
        String result="";
        if(source.length()>200) {
           result = source.substring(0, 200);
            result = result+"....";
        }
        else if(source.length()>0) {
           result ="";
            String pattern = "[A-Z](?i)[^.?!]*?\\b(";
            int i = 0;
            int total = filters.size();
            try {
                for (String searchText : filters) {
                    if (i < total - 1) {
                        pattern = pattern + searchText + "|";
                    } else {
                        pattern = pattern + searchText;
                    }

                }
                pattern = pattern + ")\\b[^.?!]*[.?!]";
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(source);
                int x = 0;
                while (m.find()) {
                    if (x < 2) {
                        result = result + " " + m.group();
                    } else {
                        break;
                    }
                    x++;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            result = result+"....";
        }
        else{
           result ="Not Available";
        }
            return result;
   }

    public static String findParagraph2(String source, List<String> filters, String paragraphSeparator)
    {

        String result =source.substring(0,500);
        for(String searchText : filters) {

            final int locationOfSearchTerm = source.indexOf(searchText);
            if (locationOfSearchTerm == -1) return null;

            int paragraphEnd = source.indexOf(paragraphSeparator, locationOfSearchTerm + searchText.length());

            //if we didn't find an end of a paragraph, we want to go the end
            if (paragraphEnd == -1) paragraphEnd = searchText.length();

            int paragraphStart = source.lastIndexOf(paragraphSeparator, locationOfSearchTerm);

            //if we didn't find a start of a paragraph, we want to go the beginning
            if (paragraphStart == -1) paragraphStart = 0;
                try {
                    result = searchText.substring(paragraphStart, paragraphEnd - 1);
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
        }

        return result;
    }



    public static String generateGeneralQuery(String querytext,String offset,List<Ratio> ratiolist,List<String> filtered){



        String generalquery="select case_title,a._id,held as description,d.name as courts,c.title as ratioheader,a.date as year from summary a,ratio c,courts d " +
                "where a.courtid=d._id ";
        int i =0;
        int begin =0;
        int end = 0;
        if(ratiolist.size() > 0) {
            String ratioquery ="";
            ratioquery = ratioquery + " AND (";
            List<Ratio> sublist = ratiolist;
            if (ratiolist.size() > maxratios) {
                begin = Integer.parseInt(offset);
                if(begin>ratiolist.size()){
                    begin=ratiolist.size();
                }
                end = begin + maxratios;
                if(end>ratiolist.size()){
                    end=ratiolist.size();
                }
                Log.d("Begin",String.valueOf(begin));
                Log.d("End",String.valueOf(end));
                if(ratiolist.size()>begin) {
                    sublist = ratiolist.subList(begin, end);
                }else{
                    sublist = new ArrayList<Ratio>();
                }
            }
            Log.d("Sublist", String.valueOf(sublist.size()));

            for (Ratio r : sublist) {
                if (i < sublist.size() - 1) {
                   // ratioquery = ratioquery + " (a.ratio like '%," + String.valueOf(r.getId()) + "%' AND c._id="+String.valueOf(r.getId())+" ) OR ";
                    ratioquery = ratioquery + " (a.ratio like '%[" + String.valueOf(r.getId()) + "]%' AND c._id="+String.valueOf(r.getId())+" ) OR ";
                } else {
                   // ratioquery = ratioquery + " (a.ratio like '%," + String.valueOf(r.getId()) + "%'  AND c._id="+String.valueOf(r.getId())+" ) OR  ";
                    ratioquery = ratioquery + " (a.ratio like '%[" + String.valueOf(r.getId()) + "]%'  AND c._id="+String.valueOf(r.getId())+" )  ";
                }
                i++;
            }
            if(ratiolist.size()>begin) {
                generalquery = generalquery + ratioquery + ") ";
            }
        }

        /**
        if(filtered.size()>0) {
            String filterquery = "";
            if(ratiolist.size()>0) {
                filterquery = filterquery + "  (";
            }
            else{
                filterquery = filterquery + " OR (";
            }
            int j = 0;
            for (String filterstring : filtered) {
                if (j < filtered.size() - 1) {
                    filterquery = filterquery + " (a.ratio like '%" + filterstring + "%') OR ";
                } else {
                    filterquery = filterquery + " (a.ratio like '%" + filterstring + "%' )  ";
                }
                j++;
            }

                generalquery = generalquery + filterquery + ") OR ";


        }
        */

        /**String[] textfilter = querytext.split(" ");
        List <String> searchlist = new ArrayList<String>(Arrays.asList(textfilter));
        List<String> filtered= new ArrayList<String>();
        for(String textq : searchlist){
            textq = textq.trim();
            if(!wordNotFound(textq)){
                if(!filtered.contains(textq)){
                    filtered.add(textq);
                    System.out.println("Adding "+textq);
                }
            }

        }
        generalquery = generalquery+ "((c.title like '%"+querytext+"%' OR judgement like '%"+querytext+"%' OR content like '%"+querytext+"') OR ";
         int totalfiltered = filtered.size();
        i = 0;
         for(String s : filtered) {
         if(i<totalfiltered-1) {
         generalquery = generalquery + "(c.title like '%" + s + "%' OR judgement like '%" + s + "%' OR  content like '%" + s + "%') OR ";
         }
         else{
         generalquery = generalquery + "(c.title like '%" + s + "%' OR judgement like '%" + s + "%' OR content like '%" + s + "%'))";
         }
         i++;
         }
         */

        generalquery = generalquery+ " AND c.title like '%"+querytext+"%'";


        generalquery =generalquery+"  order by date  desc";
        return generalquery;
    }



    public static String generateGeneralQuery(String querytext,String offset,List<String> filtered){

        /**
         * String generalquery="select a._id,case_title,judgement as description,d.name as courts,c.title as ratioheader,a.date as year from summary a,judgements b,ratio c,courts d " +
         "where c._id=a.ratio AND a._id=b.summaryid AND a.courtid=d._id AND ";
         */

        String generalquery="select case_title,a._id,held as description,d.name as courts,c.title as ratioheader,a.date as year from summary a,ratio c,courts d " +
                "where  a.courtid=d._id AND ";





         generalquery = generalquery+ "((c.title like '%"+querytext+"%' OR judgement like '%"+querytext+"%' OR content like '%"+querytext+"') OR ";
         int totalfiltered = filtered.size();
         int i = 0;
         for(String s : filtered) {
         if(i<totalfiltered-1) {
         generalquery = generalquery + "(c.title like '%" + s + "%' OR judgement like '%" + s + "%' OR  content like '%" + s + "%') OR ";
         }
         else{
         generalquery = generalquery + "(c.title like '%" + s + "%' OR judgement like '%" + s + "%' OR content like '%" + s + "%')";
         }
         i++;
         }
        //generalquery = generalquery+ "((c.title like '%"+querytext+"%' OR judgement like '%"+querytext+"%' OR content like '%"+querytext+"%') OR ";
        generalquery = generalquery+ " c.title like '%"+querytext+"%'";


        //generalquery =generalquery+") order by date,d._id desc";
        generalquery =generalquery+" order by d._id,date LIMIT 200";
        return generalquery;
    }


    public static String fetchRatios(String searchtext,boolean useToken){
        String querytext="";
        if(useToken) {
            String[] textfilter = searchtext.split(" ");
            List<String> searchlist = new ArrayList<String>(Arrays.asList(textfilter));
            List<String> filtered = new ArrayList<String>();
            for (String textq : searchlist) {
                textq = textq.trim();
                if (!wordNotFound(textq)) {
                    if (!filtered.contains(textq)) {
                        filtered.add(textq);
                        //System.out.println("Adding "+textq);
                    }
                }

            }
            querytext = "SELECT _id,title,content from ratio WHERE title like '%" + searchtext + "%' OR content like '%" + searchtext + "%' OR ";
            int totalfiltered = filtered.size();
            int i = 0;
            for (String s : filtered) {
                if (i < totalfiltered - 1) {
                    querytext = querytext + "(title like '%" + s + "%'  OR content like '%" + s + "%') OR ";
                } else {
                    querytext = querytext + "(title like '%" + s + "%'  OR content like '%" + s + "%')";
                }
                i++;
            }
        }else{
            querytext="SELECT _id,title,content from ratio WHERE title like '%"+searchtext+"%' OR content like '%"+searchtext+"%'  ORDER BY title";
        }
        return querytext;
    }
    public static String generateGeneralQuery2(String querytext,String offset,List<Ratio> ratiolist,List<String> filtered){

        /**
         * String generalquery="select a._id,case_title,judgement as description,d.name as courts,c.title as ratioheader,a.date as year from summary a,judgements b,ratio c,courts d " +
         "where c._id=a.ratio AND a._id=b.summaryid AND a.courtid=d._id AND ";
         */

        String generalquery="select case_title,a._id,held as description,d.name as courts,c.title as ratioheader,a.date as year from summary a,ratio c,courts d " +
                "where  a.courtid=d._id  ";


        int i =0;
        int begin =0;
        int end = 0;
        if(ratiolist.size() > 0) {
            String ratioquery ="";
            ratioquery = ratioquery + " AND (";
            List<Ratio> sublist = ratiolist;
            if (ratiolist.size() > maxratios) {
                begin = Integer.parseInt(offset);
                if(begin>ratiolist.size()){
                    begin=ratiolist.size();
                }
                end = begin + maxratios;
                if(end>ratiolist.size()){
                    end=ratiolist.size();
                }
                Log.d("Begin",String.valueOf(begin));
                Log.d("End",String.valueOf(end));
                if(ratiolist.size()>begin) {
                    sublist = ratiolist.subList(begin, end);
                }else{
                    sublist = new ArrayList<Ratio>();
                }
            }
            Log.d("Sublist", String.valueOf(sublist.size()));


            for (Ratio r : sublist) {
                if (i < sublist.size() - 1) {
                    ratioquery = ratioquery + " (a.ratio like '%[" + String.valueOf(r.getId()) + "]%' AND c._id="+String.valueOf(r.getId())+" ) OR ";
                } else {
                    ratioquery = ratioquery + " (a.ratio like '%[" + String.valueOf(r.getId()) + "]%'  AND c._id="+String.valueOf(r.getId())+" )  ";
                }
                i++;
            }
            if(ratiolist.size()>begin) {
                generalquery = generalquery + ratioquery + ") AND ";
            }
        }



        /**generalquery = generalquery+ "((c.title like '%"+querytext+"%' OR judgement like '%"+querytext+"%' OR content like '%"+querytext+"') OR ";
        int totalfiltered = filtered.size();
        int i = 0;
        for(String s : filtered) {
            if(i<totalfiltered-1) {
                generalquery = generalquery + "(c.title like '%" + s + "%' OR judgement like '%" + s + "%' OR  content like '%" + s + "%') OR ";
            }
            else{
                generalquery = generalquery + "(c.title like '%" + s + "%' OR judgement like '%" + s + "%' OR content like '%" + s + "%')";
            }
        i++;
        }*/
         //generalquery = generalquery+ "((c.title like '%"+querytext+"%' OR judgement like '%"+querytext+"%' OR content like '%"+querytext+"%') OR ";


        int totalfiltered = filtered.size();

        i = 0;
        if(totalfiltered>0) {
            String filterquery="";
            filterquery = filterquery+ "(";
            filterquery = filterquery +" ";
            for (String s : filtered) {
                if (i < totalfiltered - 1) {
                    filterquery = filterquery + "(c.title like '%" + s + "%') OR ";

                    //generalquery = generalquery + "(c.title like '%" + s + "%' OR judgement like '%" + s + "%' OR  content like '%" + s + "%') OR ";
                } else {
                    filterquery = filterquery + "(c.title like '%" + s + "%') ";
                    //generalquery = generalquery + "(c.title like '%" + s + "%' OR judgement like '%" + s + "%' OR content like '%" + s + "%')";
                }
                i++;
            }
            generalquery =generalquery+ " "+filterquery+") ";
        }
        generalquery =generalquery+" order by date,d._id desc LIMIT 200";
        //generalquery =generalquery+") order by d._id,date desc LIMIT "+offset+",100";
        return generalquery;
    }


    public static String generateGeneralQuery3(String searchtext,String offset,List<Ratio> ratiolist,List<String> filtered){

        /**String generalquery="select a._id,case_title,held as description,d.name as courts,'' as ratioheader,a.date as year  from judgements c " +
                " LEFT JOIN summary a " +
                " ON c.summaryid=a._id " +
                " LEFT JOIN courts d " +
                " ON a.courtid =d._id where a.case_title like '%"+searchtext+"%' OR c.judgement like '%"+searchtext+"%'  ";
         */
        String generalquery="select a._id,case_title,held as description,d.name as courts,'' as ratioheader,a.date as year,ratio  from summary a,judgements c,courts d" +
               " WHERE c.summaryid=a._id " +
                 " AND a.courtid =d._id AND (a.case_title like '%"+searchtext+"%' OR c.judgement like '%"+searchtext+"%')  ";
        int i =0;
        int begin =0;
        int end = 0;
        if(ratiolist.size() > 0) {
            String ratioquery ="";
            ratioquery = ratioquery + "(";
            List<Ratio> sublist = ratiolist;
            if (ratiolist.size() > maxratios) {
                begin = Integer.parseInt(offset);
                if(begin>ratiolist.size()){
                    begin=ratiolist.size();
                }
                end = begin + maxratios;
                if(end>ratiolist.size()){
                    end=ratiolist.size();
                }
                Log.d("Begin",String.valueOf(begin));
                Log.d("End",String.valueOf(end));
                if(ratiolist.size()>begin) {
                    sublist = ratiolist.subList(begin, end);
                }else{
                    sublist = new ArrayList<Ratio>();
                }
            }
            Log.d("Sublist", String.valueOf(sublist.size()));

            for (Ratio r : sublist) {
                if (i < sublist.size() - 1) {
                    ratioquery = ratioquery + " (a.ratio like '%[" + String.valueOf(r.getId()) + "]%' AND c._id="+String.valueOf(r.getId())+" ) OR ";
                } else {
                    ratioquery = ratioquery + " (a.ratio like '%[" + String.valueOf(r.getId()) + "]%'  AND c._id="+String.valueOf(r.getId())+" )  ";
                }
                i++;
            }
            if(ratiolist.size()>begin) {
                //generalquery = generalquery + " AND "+ratioquery + ") ";
            }
        }



        int totalfiltered = filtered.size();

        i = 0;
        if(totalfiltered>0) {
            String filterquery="";
            filterquery = filterquery+ " OR (";
            filterquery = filterquery +" ";
            for (String s : filtered) {
                if (i < totalfiltered - 1) {
                    filterquery = filterquery + "(c.judgement like '%" + s + "%') OR ";

                    //generalquery = generalquery + "(c.title like '%" + s + "%' OR judgement like '%" + s + "%' OR  content like '%" + s + "%') OR ";
                } else {
                    filterquery = filterquery + "(c.judgement like '%" + s + "%')";
                    //generalquery = generalquery + "(c.title like '%" + s + "%' OR judgement like '%" + s + "%' OR content like '%" + s + "%')";
                }
                i++;
            }
            //generalquery =generalquery+ " "+filterquery+") order by date,d._id desc LIMIT 0,1000";
        }


    return generalquery + "    LIMIT 1000";
    }

    public static String generateJudgementuery(String querytext,String courtid,String offset,List<String> filtered){


        String generalquery="select a._id,case_title,judgement as description,d.name as courts,c.title as ratioheader,a.date as year from summary a,judgements b,ratio c,courts d " +
                "where c._id=a.ratio AND a._id=b.summaryid AND a.courtid=d._id AND ";





        generalquery = generalquery+ "((judgement like '%"+querytext+"%' OR c.title like '%"+querytext+"%' OR content like '%"+querytext+"') OR ";
        int totalfiltered = filtered.size();
        int i = 0;
        for(String s : filtered) {
            if(i<totalfiltered-1) {
                generalquery = generalquery + "(judgement like '%" + s + "%' OR c.title like '%" + s + "%' OR content like '%" + s + "%') OR ";
            }
            else{
                generalquery = generalquery + "(judgement like '%" + s + "%' OR c.title like '%" + s + "%' OR content like '%" + s + "%')";
            }
            i++;
        }
        //generalquery =generalquery+") order by date,d._id desc";
        generalquery =generalquery+") order by d._id,date desc LIMIT "+offset+",100";
        return generalquery;
    }




    public static String generateLawsQuery(String querytext,String offset,List<String> filtered){


        String generalquery="select b._id,a.title as title,a.description as description,b.title as sectiontitle,'' as ratioheader,date from laws a,sections b where a.sid=b.lawid AND" +
                " (a.title like '%"+querytext+"%' OR a.description like '%"+querytext+"%' OR b.title like '%"+querytext+"%' OR b.partschedule like '%"+querytext+"%' OR b.body like '%"+querytext+"%') AND ";






        int totalfiltered = filtered.size();
        int i = 0;
        for(String s : filtered) {
            if(i<totalfiltered-1) {
                generalquery = generalquery + " (a.title like '%"+s+"%' OR a.description like '%"+s+"%' OR b.title like '%"+s+"%' OR b.partschedule like '%"+s+"%' OR b.body like '%"+s+"%') OR ";
            }
            else{
                generalquery = generalquery + "  (a.title like '%"+s+"%' OR a.description like '%"+s+"%' OR b.title like '%"+s+"%' OR b.partschedule like '%"+s+"%' OR b.body like '%"+s+"%') ";
            }
            i++;
        }
        //generalquery =generalquery+") order by date,d._id desc";
        generalquery =generalquery+" order by a._id,date  LIMIT 200";
        return generalquery;
    }


    public static String generatePrecedenceQuery(String querytext,String offset,List<String> filtered){

        String generalquery="select a._id,a.title as title,a.content as description,'' as sectiontitle,'' as ratioheader,'' as date from precedence a WHERE " +
                " (a.title like '%"+querytext+"%' OR a.content like '%"+querytext+"%') AND ";






        int totalfiltered = filtered.size();
        int i = 0;
        for(String s : filtered) {
            if(i<totalfiltered-1) {
                generalquery = generalquery + " (a.title like '%"+s+"%' OR a.content like '%"+s+"%') OR ";
            }
            else{
                generalquery = generalquery + "  (a.title like '%"+s+"%' OR a.content like '%"+s+"%') ";
            }
            i++;
        }
        //generalquery =generalquery+") order by date,d._id desc";
        generalquery =generalquery+" order by a._id  LIMIT 200";
        return generalquery;
    }


    public static String generateRulesQuery(String querytext,String offset,List<String> filtered){

        String generalquery="select a._id,a.title as title,a.content as description,'' as sectiontitle,'' as ratioheader,'' as date from rules a WHERE " +
                " (a.title like '%"+querytext+"%' OR a.content like '%"+querytext+"%') AND ";






        int totalfiltered = filtered.size();
        int i = 0;
        for(String s : filtered) {
            if(i<totalfiltered-1) {
                generalquery = generalquery + " (a.title like '%"+s+"%' OR a.content like '%"+s+"%') OR ";
            }
            else{
                generalquery = generalquery + "  (a.title like '%"+s+"%' OR a.content like '%"+s+"%') ";
            }
            i++;
        }
        //generalquery =generalquery+") order by date,d._id desc";
        generalquery =generalquery+" order by a._id  LIMIT 200";
        return generalquery;
    }

    public static String generateNotesQuery(String querytext,String offset,List<String> filtered){

        String generalquery="select a._id,a.title as title,a.content as description,'' as sectiontitle,'' as ratioheader,'' as date from notes a WHERE " +
                " (a.title like '%"+querytext+"%' OR a.content like '%"+querytext+"%') AND ";






        int totalfiltered = filtered.size();
        int i = 0;
        for(String s : filtered) {
            if(i<totalfiltered-1) {
                generalquery = generalquery + " (a.title like '%"+s+"%' OR a.content like '%"+s+"%') OR ";
            }
            else{
                generalquery = generalquery + "  (a.title like '%"+s+"%' OR a.content like '%"+s+"%') ";
            }
            i++;
        }
        //generalquery =generalquery+") order by date,d._id desc";
        generalquery =generalquery+" order by a._id  LIMIT 200";
        return generalquery;
    }

    public static String generateAnnotationQuery(String querytext,String offset,List<String> filtered){


        String generalquery="select a._id,a.title as title,a.content as description,'' as sectiontitle,'' as ratioheader,'' as date from annotations a WHERE " +
                " (a.title like '%"+querytext+"%' OR a.content like '%"+querytext+"%') AND ";






        int totalfiltered = filtered.size();
        int i = 0;
        for(String s : filtered) {
            if(i<totalfiltered-1) {
                generalquery = generalquery + " (a.title like '%"+s+"%' OR a.content like '%"+s+"%') OR ";
            }
            else{
                generalquery = generalquery + "  (a.title like '%"+s+"%' OR a.content like '%"+s+"%') ";
            }
            i++;
        }
        //generalquery =generalquery+") order by date,d._id desc";
        generalquery =generalquery+" order by a._id  LIMIT 200";
        return generalquery;
    }

    public static String generateArticlesQuery(String querytext,String offset,List<String> filtered){

        String generalquery="select a._id,a.title as title,a.content as description,'' as sectiontitle,'' as ratioheader,'' as date from articles a WHERE " +
                " (a.title like '%"+querytext+"%' OR a.content like '%"+querytext+"%') AND ";






        int totalfiltered = filtered.size();
        int i = 0;
        for(String s : filtered) {
            if(i<totalfiltered-1) {
                generalquery = generalquery + " (a.title like '%"+s+"%' OR a.content like '%"+s+"%') OR ";
            }
            else{
                generalquery = generalquery + "  (a.title like '%"+s+"%' OR a.content like '%"+s+"%') ";
            }
            i++;
        }
        //generalquery =generalquery+") order by date,d._id desc";
        generalquery =generalquery+" order by a._id  LIMIT 200";
        return generalquery;
    }

    public static String generateJournalsQuery(String querytext,String offset,List<String> filtered){


        String generalquery="select a._id,a.title as title,a.content as description,'' as sectiontitle,'' as ratioheader,'' as date from publications a WHERE " +
                " (a.title like '%"+querytext+"%' OR a.content like '%"+querytext+"%') AND ";






        int totalfiltered = filtered.size();
        int i = 0;
        for(String s : filtered) {
            if(i<totalfiltered-1) {
                generalquery = generalquery + " (a.title like '%"+s+"%' OR a.content like '%"+s+"%') OR ";
            }
            else{
                generalquery = generalquery + "  (a.title like '%"+s+"%' OR a.content like '%"+s+"%') ";
            }
            i++;
        }
        //generalquery =generalquery+") order by date,d._id desc";
        generalquery =generalquery+" order by a._id  LIMIT 200";
        return generalquery;
    }

    public static String generateMaximsQuery(String querytext,String offset,List<String> filtered){

        String generalquery="select a._id,a.title as title,a.content as description,'' as sectiontitle,'' as ratioheader,'' as date from maxims a WHERE " +
                " (a.title like '%"+querytext+"%' OR a.content like '%"+querytext+"%') AND ";






        int totalfiltered = filtered.size();
        int i = 0;
        for(String s : filtered) {
            if(i<totalfiltered-1) {
                generalquery = generalquery + " (a.title like '%"+s+"%' OR a.content like '%"+s+"%') OR ";
            }
            else{
                generalquery = generalquery + "  (a.title like '%"+s+"%' OR a.content like '%"+s+"%') ";
            }
            i++;
        }
        //generalquery =generalquery+") order by date,d._id desc";
        generalquery =generalquery+" order by a._id  LIMIT 200";
        return generalquery;
    }

    public static String generateDictionaryQuery(String querytext,String offset,List<String> filtered){


        String generalquery="select a._id,a.title as title,a.content as description,'' as sectiontitle,'' as ratioheader,'' as date from dictionary a WHERE " +
                " (a.title like '%"+querytext+"%' OR a.content like '%"+querytext+"%') AND ";






        int totalfiltered = filtered.size();
        int i = 0;
        for(String s : filtered) {
            if(i<totalfiltered-1) {
                generalquery = generalquery + " (a.title like '%"+s+"%' OR a.content like '%"+s+"%') OR ";
            }
            else{
                generalquery = generalquery + "  (a.title like '%"+s+"%' OR a.content like '%"+s+"%') ";
            }
            i++;
        }
        //generalquery =generalquery+") order by date,d._id desc";
        generalquery =generalquery+" order by a._id  LIMIT 200";
        return generalquery;
    }




    public static String generateSubjectQuery(String querytext,String offset,List<String> filtered){


        String generalquery="select case_title,c._id as id,held as description,d.name as courts,a.name as ratioheader,c.date as year  from subjects a,principles b,summary c,courts d where a._id=b.subjectid and b._id=c.principleid and c.courtid=d._id " +
                " AND (a.name LIKE '%"+querytext+"%' OR b.name like '%"+querytext+"%') ";




/**
        if(filtered.size()>0){
            generalquery = generalquery+" OR ";
        }
        int totalfiltered = filtered.size();
        int i = 0;
        for(String s : filtered) {
            if(i<totalfiltered-1) {
                generalquery = generalquery + " (a.name like '%"+s+"%' OR b.name like '%"+s+"%') OR ";
            }
            else{
                generalquery = generalquery + "  (a.name like '%"+s+"%' OR b.name like '%"+s+"%') ";
            }
            i++;
        }
        */
        //generalquery =generalquery+") order by date,d._id desc";
        generalquery =generalquery+" order by c.date  LIMIT 200";
        return generalquery;
    }
}
