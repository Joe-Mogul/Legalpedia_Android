package com.legalpedia.android.app;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.legalpedia.android.app.adapter.SearchResultAdapter;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Post;
import com.legalpedia.android.app.models.Ratio;
import com.legalpedia.android.app.models.RatioDao;
import com.legalpedia.android.app.models.SearchResult;
import com.legalpedia.android.app.models.Summary;
import com.legalpedia.android.app.models.SummaryDao;
import com.legalpedia.android.app.ui.PaginationScrollListener;
import com.legalpedia.android.app.util.SearchUtil;
import com.legalpedia.android.app.views.SimpleDividerItemDecoration;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by adebayoolabode on 11/7/16.
 */

public class SearchActivity extends AppCompatActivity {

    //private Cursor c = null;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private SearchResultAdapter adapter;
    private ArrayList<SearchResult> postArrayList;
    private DaoSession daoSession;
    private Database db;
    private ProgressDialog progressDialog;
    private int position=0;
    private int offsetInt =0;
    private int offset=0;
    private int limit=20;
    private boolean isfirst = true;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String query;
    private boolean ispaged=false;
    private int resourceid=0;
    private boolean showProgress=false;
    private int ratiosize = 0;
    private SearchQuery searchtask;
    private List<Ratio> ratiolist;
    private List<SearchResult> resp;
    private List<SearchResult> lawsresp;
    private List<SearchResult> subjectresp;
    private List<SearchResult> summaryresp;
    private List<SearchResult> judgementresp;
    private List<SearchResult> noteresp;
    private List<SearchResult> annotationresp;
    private List<SearchResult> precedenceresp;
    private List<SearchResult> rulesresp;
    private List<SearchResult> articlesresp;
    private List<SearchResult> publicationresp;
    private List<SearchResult> dictionaryresp;
    private List<SearchResult> maximresp;
    private SimpleCursorAdapter myAdapter;
    private  SearchView searchView = null;
    private List<String> filtered;
    private List <String> searchlist;
    private LinearLayout searchProgress;
    private int totalquery=0;
    private int runquery=0;
    private long starttime=0L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        query = intent.getStringExtra("query");
        try{
            position = intent.getIntExtra("position",0);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        try {

            resourceid = intent.getIntExtra("resourceid", 0);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        setTitle(query);
        daoSession = ((App)  getApplication()).getDaoSession();
        db = daoSession.getDatabase();
        searchProgress = (LinearLayout) findViewById(R.id.searchprogress);
        progressDialog = new ProgressDialog(SearchActivity.this, R.style.customDialog);

        emptyView = (TextView) findViewById(R.id.empty_view);
        recyclerView= (RecyclerView) findViewById(R.id.search_results);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(SearchActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        //SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        //recyclerView.addItemDecoration(dividerItemDecoration);
        final String[] from = new String[] {"search"};
        final int[] to = new int[] {android.R.id.text1};
        myAdapter = new SimpleCursorAdapter(SearchActivity.this, android.R.layout.simple_spinner_dropdown_item, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        adapter=new SearchResultAdapter(SearchActivity.this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {

                    isLoading = true;
                    offset += 1;
                    int pager = (offset) * limit;
                    ispaged = true;
                    Log.d("Search","Running from Pagination");
                    showProgress= true;
                    ratiolist= new ArrayList<Ratio>();
                    resp = new ArrayList<SearchResult>();

                    doSearch(query, String.valueOf(pager));

            }

            @Override
            public int getTotalPageCount() {
                return 10000;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        int pager=(offset)*limit;
        showProgress=true;
        ratiolist= new ArrayList<Ratio>();
        resp = new ArrayList<SearchResult>();
        lawsresp = new ArrayList<SearchResult>();
        summaryresp = new ArrayList<SearchResult>();
        subjectresp = new ArrayList<SearchResult>();
        judgementresp = new ArrayList<SearchResult>();
        noteresp = new ArrayList<SearchResult>();
        annotationresp = new ArrayList<SearchResult>();
        precedenceresp = new ArrayList<SearchResult>();
        rulesresp = new ArrayList<SearchResult>();
        articlesresp = new ArrayList<SearchResult>();
        publicationresp = new ArrayList<SearchResult>();
        dictionaryresp = new ArrayList<SearchResult>();
        maximresp = new ArrayList<SearchResult>();
        doSearch(query,String.valueOf(pager));

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.search_menu);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchManager searchManager = (SearchManager) SearchActivity.this.getSystemService(Context.SEARCH_SERVICE);
        //searchView.setSuggestionsAdapter(myAdapter);


        //
        /**searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {

                // Add clicked text to search box
                CursorAdapter ca = searchView.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(position);
                searchView.setQuery(cursor.getString(cursor.getColumnIndex("search")),false);
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }
        });
        */

        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                offset  = 0;
                ratiosize = 0;
                int pager=(offset)*limit;
                ispaged=false;
                adapter.clearResults();
                Log.d("Search","Running from here");
                showProgress = true;
                ratiolist= new ArrayList<Ratio>();
                resp = new ArrayList<SearchResult>();

                doSearch(query,String.valueOf(pager));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                //adapter.filter(searchQuery.toString().trim());
                //listView.invalidate();
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //    finish();

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();
            case R.id.action_search:
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy(){
        //searchtask.cancel(true);
        super.onDestroy();
        /**try {
            if (c != null) {
                c.close();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }*/
    }

    @Override
    protected void onStop(){
        //searchtask.cancel(true);
        super.onStop();
        /**try {
            if (c != null) {
                c.close();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }*/
    }



    public boolean isDuplicate(List<SearchResult> resp,String title){
        boolean result=false;

        for(SearchResult sres: resp){
            if(sres.getTitle().equals(title)){
                return true;
            }
        }

        return result;
    }


    public boolean isDuplicate(List<SearchResult> resp,String title,String description){
        boolean result=false;

        for(SearchResult sres: resp){
            if(sres.getTitle().equals(title) && sres.getDescription().equals(description)){
                return true;
            }
        }

        return result;
    }






    private void doSearch(String searchtext,String offset){
        String[] textfilter = searchtext.split(" ");
        runquery=0;
        searchProgress.setVisibility(View.VISIBLE);
        showProgress("Searching resources.....");
        ratiolist= new ArrayList<Ratio>();
        resp = new ArrayList<SearchResult>();
        subjectresp = new ArrayList<SearchResult>();
        lawsresp = new ArrayList<SearchResult>();
        summaryresp = new ArrayList<SearchResult>();
        judgementresp = new ArrayList<SearchResult>();
        noteresp = new ArrayList<SearchResult>();
        annotationresp = new ArrayList<SearchResult>();
        precedenceresp = new ArrayList<SearchResult>();
        rulesresp = new ArrayList<SearchResult>();
        adapter.clearResults();
        searchlist = new ArrayList<String>(Arrays.asList(textfilter));
        filtered= new ArrayList<String>();
        for(String textq : searchlist){
            textq = textq.trim();
            if(!SearchUtil.wordNotFound(textq)){
                if(!filtered.contains(textq)){
                    filtered.add(textq);
                    System.out.println("Adding "+textq);
                }
            }

        }

        starttime=System.currentTimeMillis();
        Log.d("SearcPosition",String.valueOf(position));
        switch(position){
            case 0:
            totalquery = 3;
            new SearchSubjects().execute(searchtext,offset);
            break;
            case 1:
            totalquery = 5;
            new DoRatioSearch().execute(searchtext,offset);
            break;
            case 2:
            totalquery = 3;
            new SearchLaws().execute(searchtext,offset);
            break;
            case 3:
            totalquery = 3;
            new SearchRules().execute(searchtext,offset);
            break;
            case 4:
            totalquery = 3;
            new SearchPrecedence().execute(searchtext,offset);
            break;
            case 5:
            totalquery = 4;
            new SearchDictionary().execute(searchtext,offset);
            new SearchMaxims().execute(searchtext,offset);
            break;
            case 6:
            totalquery = 4;
            new SearchArticles().execute(searchtext,offset);
            new SearchPublications().execute(searchtext,offset);
            break;
            default:
            break;


        }

        new SearchNotes().execute(searchtext,offset);
        new SearchAnnotations().execute(searchtext,offset);



    }


    public void showProgress(String message){
        try {
            if (progressDialog != null) {
                progressDialog.setTitle("Legalpedia");
                progressDialog.setMessage(message);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void populateAdapter(){
        adapter.clearResults();
        adapter.setResults(subjectresp,ispaged);
        adapter.setResults(summaryresp,ispaged);
        adapter.setResults(judgementresp,ispaged);
        adapter.setResults(lawsresp,ispaged);
        adapter.setResults(precedenceresp,ispaged);
        adapter.setResults(rulesresp,ispaged);
        adapter.setResults(dictionaryresp,ispaged);
        adapter.setResults(maximresp,ispaged);
        adapter.setResults(noteresp,ispaged);
        adapter.setResults(annotationresp,ispaged);
    }


    class DoSummarySearch extends AsyncTask<String, String, List<SearchResult>> {




        @Override
        protected List<SearchResult> doInBackground(String... params) {
            Log.d("Search","Summary Search");

            String searchtext = params[0];
            String offset = params[1];







            String SQL_DISTINCT_ENAME = SearchUtil.generateGeneralQuery(searchtext,offset,ratiolist,filtered);
            Log.d("Query",SQL_DISTINCT_ENAME);
            Cursor c = null;
            try {
                try {
                    c = db.rawQuery(SQL_DISTINCT_ENAME, null);
                }
                catch(Exception ex){
                    ex.printStackTrace();
                    c = null;
                }
                System.out.println("Value of Cursor in summary search");
                System.out.println(c);

                if(c != null){
                    if (c.moveToFirst()) {
                        do {
                            String description = SearchUtil.findParagraph(c.getString(2), searchlist, "<p>");
                            SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
                            String year = "Not available";
                            try {
                                year = dateformat.format(new Date(c.getLong(5)));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            Log.d("SummarySearch",c.getString(4));
                            SearchResult searchresult = new SearchResult(c.getString(0), description, c.getString(1), "Judgement", c.getString(4), year,searchtext,filtered);
                            /**if (!isDuplicate(resp, c.getString(0))) {
                                resp.add(searchresult);
                            }*/
                            resp.add(searchresult);
                            summaryresp.add(searchresult);
                            //resp.add(postArrayList);
                        } while (c.moveToNext());
                    }

                }


            } finally {
                if(c!=null) {
                    c.close();
                }
            }

            Log.d("Results 0 ",String.valueOf(resp.size()));

            return resp;
        }





        @Override
        protected void onPostExecute(List<SearchResult> result) {
            runquery= runquery+1;
            String endtime=String.valueOf((System.currentTimeMillis()-starttime)/1000);
            System.out.println("Query Finished in "+endtime);
            Log.d("RunQuery",String.valueOf(runquery));
            if(runquery>=totalquery){
                runquery = 0;
                searchProgress.setVisibility(View.INVISIBLE);

                if (result.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
                try {
                    progressDialog.dismiss();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            isLoading=false;
            if(result.size()<=0){
                isLastPage=true;
            }



            //showProgress("Updating search result.....");
            Collections.sort(result);

            populateAdapter();
            //adapter.setResults(result,ispaged);

            adapter.notifyDataSetChanged();

            Log.d("NotifyDatasetChanged","Fired");
            if(adapter.getItemCount()<20){
                offset  = offset+1;
                int pager=(offset)*limit;
                if(result.size()>10)
                {
                    showProgress = false;
                }
                if(result.size()>0) {
                    int totalpager=pager+20;
                    Log.d("RatioList",String.valueOf(ratiosize));
                    Log.d("TotalPager",String.valueOf(totalpager));
                    if(ratiosize>totalpager) {
                        Log.d("Paging", String.valueOf(pager));
                        try {

                            doSearch(query, String.valueOf(pager));
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }else{
                        isLastPage=true;
                    }
                }
            }

        }
        @Override
        protected void onPreExecute() {

        }

    }


    class SearchAdvanced extends AsyncTask<String, String, List<SearchResult>> {




        @Override
        protected List<SearchResult> doInBackground(String... params) {
            Log.d("Search","Advanced Search");

            String searchtext = params[0];
            String offset = params[1];





            String SQL_DISTINCT_ENAME = SearchUtil.generateGeneralQuery3(searchtext,offset,ratiolist,filtered);
            Log.d("AdvancedQuery",SQL_DISTINCT_ENAME);
            Cursor c = null;
            try {
                try {
                    c = db.rawQuery(SQL_DISTINCT_ENAME, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    c= null;
                }
                if(c!= null){
                    if (c.moveToFirst()) {
                        do {

                            try {
                                if (c.getString(1) != null) {
                                    String description="";
                                    try {
                                        description = SearchUtil.findParagraph(c.getString(2), searchlist, "<p>");
                                    }
                                    catch(Exception ex){
                                        ex.printStackTrace();
                                    }
                                    SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
                                    String year = "Not available";
                                    try {
                                        year = dateformat.format(new Date(c.getLong(5)));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    try {
                                        String ratiodata = c.getString(6);
                                        Log.d("RatioAdvanced",ratiodata);
                                        String[] ratiodatalist = ratiodata.split("]");
                                        List<String> ratioids= new ArrayList<String>();
                                        for(int i=0;i<=ratiodatalist.length-1;i++){
                                            String rid=ratiodatalist[i].replace("[","");
                                            ratioids.add(rid);
                                        }
                                        for(Ratio ratio:ratiolist){
                                            for(String rid:ratioids){
                                                if(String.valueOf(ratio.getId()).equals(rid)){
                                                    Log.d("RatioHeader",ratio.getTitle());
                                                    Log.d("Case title",c.getString(1));
                                                    SearchResult searchresult = new SearchResult(c.getString(1), description, c.getString(0), "Judgement", ratio.getTitle(), year,searchtext,filtered);

                                                    if (!isDuplicate(resp, c.getString(1),ratio.getTitle())) {
                                                        resp.add(searchresult);
                                                        judgementresp.add(searchresult);
                                                    }
                                                }else{
                                                    SearchResult searchresult = new SearchResult(c.getString(1), description, c.getString(0), "Judgement", c.getString(4), year,searchtext,filtered);
                                                    if (!isDuplicate(resp, c.getString(1))) {
                                                        resp.add(searchresult);
                                                        judgementresp.add(searchresult);
                                                    }
                                                }

                                            }
                                        }
                                    }
                                    catch(Exception ex){
                                        ex.printStackTrace();
                                    }





                                }else{
                                    Log.d("SearchAdvanced","Not Found");
                                }
                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }

                            //resp.add(postArrayList);
                        } while (c.moveToNext());
                    }

                }

            } finally {
                if(c!=null) {
                    c.close();
                }
            }

            return resp;
        }



        @Override
        protected void onPostExecute(List<SearchResult> result) {
            String endtime=String.valueOf((System.currentTimeMillis()-starttime)/1000);
            System.out.println("Query Finished in "+endtime);
            //showProgress("Updating search result.....");
            runquery= runquery+1;
            Log.d("RunQuery",String.valueOf(runquery));
            if(runquery>=totalquery){
                runquery = 0;
                searchProgress.setVisibility(View.INVISIBLE);
                if (result.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }

                try {
                    progressDialog.dismiss();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            isLoading=false;
            if(result.size()<=0){
                isLastPage=true;
            }





            populateAdapter();
            //adapter.setResults(result,ispaged);
            adapter.notifyDataSetChanged();

            Log.d("NotifyDatasetChanged","Fired");
            if(adapter.getItemCount()<20){
                offset  = offset+1;
                int pager=(offset)*limit;
                if(result.size()>10)
                {
                    showProgress = false;
                }
                if(result.size()>0) {
                    int totalpager=pager+20;
                    Log.d("RatioList",String.valueOf(ratiosize));
                    Log.d("TotalPager",String.valueOf(totalpager));
                    if(ratiosize>totalpager) {
                        Log.d("Paging", String.valueOf(pager));
                        try {

                            doSearch(query, String.valueOf(pager));
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }else{
                        isLastPage=true;
                    }
                }
            }

        }
        @Override
        protected void onPreExecute() {

        }

    }


    class SearchSubjects extends AsyncTask<String, String, List<SearchResult>> {




        @Override
        protected List<SearchResult> doInBackground(String... params) {
            Log.d("Search","Subjects Search");

            String searchtext = params[0];
            String offset = params[1];





            String SQL_DISTINCT_ENAME = SearchUtil.generateSubjectQuery(searchtext,offset,filtered);
            Log.d("Query",SQL_DISTINCT_ENAME);
            Cursor c = null;
            try {
                try {
                    c = db.rawQuery(SQL_DISTINCT_ENAME, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    c= null;
                }
                if(c!= null){
                    if (c.moveToFirst()) {
                        do {

                            try {
                                if (c.getString(1) != null) {

                                    String description = SearchUtil.findParagraph(c.getString(2), searchlist, "<p>");
                                    SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
                                    String year = "Not available";
                                    try {
                                        year = dateformat.format(new Date(c.getLong(5)));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    SearchResult searchresult = new SearchResult(c.getString(0), description, c.getString(1), "Subject Matter Index", c.getString(4), year,searchtext,filtered);
                                    if (!isDuplicate(resp, c.getString(1))) {
                                        resp.add(searchresult);
                                        subjectresp.add(searchresult);
                                    }


                                }
                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }

                            //resp.add(postArrayList);
                        } while (c.moveToNext());
                    }

                }

            } finally {
                if(c!=null) {
                    c.close();
                }
            }

            return resp;
        }



        @Override
        protected void onPostExecute(List<SearchResult> result) {
            isLoading=false;
            if(result.size()<=0){
                isLastPage=true;
            }
            runquery= runquery+1;
            Log.d("RunQuery",String.valueOf(runquery));
            if(runquery>=totalquery){
                runquery = 0;
                searchProgress.setVisibility(View.INVISIBLE);
                if (result.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }

                try {
                    progressDialog.dismiss();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }



            populateAdapter();
            adapter.notifyDataSetChanged();
            Log.d("NotifyDatasetChanged","Fired");
            if(adapter.getItemCount()<20){
                offset  = offset+1;
                int pager=(offset)*limit;
                if(result.size()>10)
                {
                    showProgress = false;
                }
                if(result.size()>0) {
                    int totalpager=pager+20;
                    Log.d("RatioList",String.valueOf(ratiosize));
                    Log.d("TotalPager",String.valueOf(totalpager));
                    if(ratiosize>totalpager) {
                        Log.d("Paging", String.valueOf(pager));
                        try {

                            doSearch(query, String.valueOf(pager));
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }else{
                        isLastPage=true;
                    }
                }
            }

        }
        @Override
        protected void onPreExecute() {

        }

    }


    class SearchPrecedence extends AsyncTask<String, String, List<SearchResult>> {




        @Override
        protected List<SearchResult> doInBackground(String... params) {
            Log.d("Search","Precedence Search");

            String searchtext = params[0];
            String offset = params[1];





            String SQL_DISTINCT_ENAME = SearchUtil.generatePrecedenceQuery(searchtext,offset,filtered);
            Log.d("Query",SQL_DISTINCT_ENAME);
            Cursor c = null;
            try {
                try {
                    c = db.rawQuery(SQL_DISTINCT_ENAME, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    c= null;
                }
                if(c!= null){
                    if (c.moveToFirst()) {
                        do {

                            try {
                                if (c.getString(1) != null) {

                                    String description = SearchUtil.findParagraph(c.getString(2), searchlist, "<p>");
                                    SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
                                    String year = "Not available";
                                    try {
                                        year = dateformat.format(new Date(c.getLong(5)));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    SearchResult searchresult = new SearchResult(c.getString(1), description, c.getString(0), "Precedence", c.getString(4), year,searchtext,filtered);
                                    if (!isDuplicate(resp, c.getString(1))) {
                                        resp.add(searchresult);
                                        precedenceresp.add(searchresult);
                                    }


                                }
                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }

                            //resp.add(postArrayList);
                        } while (c.moveToNext());
                    }

                }

            } finally {
                if(c!=null) {
                    c.close();
                }
            }

            return resp;
        }



        @Override
        protected void onPostExecute(List<SearchResult> result) {
            isLoading=false;
            if(result.size()<=0){
                isLastPage=true;
            }
            runquery= runquery+1;
            Log.d("RunQuery",String.valueOf(runquery));
            if(runquery>=totalquery){
                runquery = 0;
                searchProgress.setVisibility(View.INVISIBLE);
                if (result.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }

                try {
                    progressDialog.dismiss();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }



            populateAdapter();
            adapter.notifyDataSetChanged();
            Log.d("NotifyDatasetChanged","Fired");
            if(adapter.getItemCount()<20){
                offset  = offset+1;
                int pager=(offset)*limit;
                if(result.size()>10)
                {
                    showProgress = false;
                }
                if(result.size()>0) {
                    int totalpager=pager+20;
                    Log.d("RatioList",String.valueOf(ratiosize));
                    Log.d("TotalPager",String.valueOf(totalpager));
                    if(ratiosize>totalpager) {
                        Log.d("Paging", String.valueOf(pager));
                        try {

                            doSearch(query, String.valueOf(pager));
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }else{
                        isLastPage=true;
                    }
                }
            }

        }
        @Override
        protected void onPreExecute() {

        }

    }


    class SearchNotes extends AsyncTask<String, String, List<SearchResult>> {




        @Override
        protected List<SearchResult> doInBackground(String... params) {
            Log.d("Search","Notes Search");

            String searchtext = params[0];
            String offset = params[1];





            String SQL_DISTINCT_ENAME = SearchUtil.generateNotesQuery(searchtext,offset,filtered);
            Log.d("Query",SQL_DISTINCT_ENAME);
            Cursor c = null;
            try {
                try {
                    c = db.rawQuery(SQL_DISTINCT_ENAME, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    c= null;
                }
                if(c!= null){
                    if (c.moveToFirst()) {
                        do {

                            try {
                                if (c.getString(1) != null) {

                                    String description = SearchUtil.findParagraph(c.getString(2), searchlist, "<p>");
                                    SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
                                    String year = "Not available";
                                    try {
                                        year = dateformat.format(new Date(c.getLong(5)));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    SearchResult searchresult = new SearchResult(c.getString(1), description, c.getString(0), "Notes", c.getString(4), year,searchtext,filtered);
                                    if (!isDuplicate(resp, c.getString(1))) {
                                        resp.add(searchresult);
                                        noteresp.add(searchresult);
                                    }


                                }
                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }

                            //resp.add(postArrayList);
                        } while (c.moveToNext());
                    }

                }

            } finally {
                if(c!=null) {
                    c.close();
                }
            }

            return resp;
        }



        @Override
        protected void onPostExecute(List<SearchResult> result) {
            isLoading=false;
            if(result.size()<=0){
                isLastPage=true;
            }
            runquery= runquery+1;
            Log.d("RunQuery",String.valueOf(runquery));
            if(runquery>=totalquery){
                runquery = 0;
                searchProgress.setVisibility(View.INVISIBLE);
                if (result.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
                try {
                    progressDialog.dismiss();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }



            populateAdapter();
            adapter.notifyDataSetChanged();
            Log.d("NotifyDatasetChanged","Fired");
            if(adapter.getItemCount()<20){
                offset  = offset+1;
                int pager=(offset)*limit;
                if(result.size()>10)
                {
                    showProgress = false;
                }
                if(result.size()>0) {
                    int totalpager=pager+20;
                    Log.d("RatioList",String.valueOf(ratiosize));
                    Log.d("TotalPager",String.valueOf(totalpager));
                    if(ratiosize>totalpager) {
                        Log.d("Paging", String.valueOf(pager));
                        try {

                            doSearch(query, String.valueOf(pager));
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }else{
                        isLastPage=true;
                    }
                }
            }

        }
        @Override
        protected void onPreExecute() {

        }

    }

    class SearchRules extends AsyncTask<String, String, List<SearchResult>> {




        @Override
        protected List<SearchResult> doInBackground(String... params) {
            Log.d("Search","Rules Search");

            String searchtext = params[0];
            String offset = params[1];





            String SQL_DISTINCT_ENAME = SearchUtil.generateRulesQuery(searchtext,offset,filtered);
            Log.d("Query",SQL_DISTINCT_ENAME);
            Cursor c = null;
            try {
                try {
                    c = db.rawQuery(SQL_DISTINCT_ENAME, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    c= null;
                }
                if(c!= null){
                    if (c.moveToFirst()) {
                        do {

                            try {
                                if (c.getString(1) != null) {

                                    String description = SearchUtil.findParagraph(c.getString(2), searchlist, "<p>");
                                    SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
                                    String year = "Not available";
                                    try {
                                        year = dateformat.format(new Date(c.getLong(5)));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    SearchResult searchresult = new SearchResult(c.getString(1), description, c.getString(0), "Rules", c.getString(4), year,searchtext,filtered);
                                    if (!isDuplicate(resp, c.getString(1))) {
                                        resp.add(searchresult);
                                        rulesresp.add(searchresult);
                                    }


                                }
                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }

                            //resp.add(postArrayList);
                        } while (c.moveToNext());
                    }

                }

            } finally {
                if(c!=null) {
                    c.close();
                }
            }

            return resp;
        }



        @Override
        protected void onPostExecute(List<SearchResult> result) {
            String endtime=String.valueOf((System.currentTimeMillis()-starttime)/1000);
            System.out.println("Query Finished in "+endtime);
            runquery= runquery+1;
            if(runquery>=totalquery){
                runquery = 0;
                searchProgress.setVisibility(View.INVISIBLE);
                if (result.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }


                try {
                    progressDialog.dismiss();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            isLoading=false;
            if(result.size()<=0){
                isLastPage=true;
            }

            populateAdapter();
            adapter.notifyDataSetChanged();
            Log.d("NotifyDatasetChanged","Fired");
            if(adapter.getItemCount()<20){
                offset  = offset+1;
                int pager=(offset)*limit;
                if(result.size()>10)
                {
                    showProgress = false;
                }
                if(result.size()>0) {
                    int totalpager=pager+20;
                    Log.d("RatioList",String.valueOf(ratiosize));
                    Log.d("TotalPager",String.valueOf(totalpager));
                    if(ratiosize>totalpager) {
                        Log.d("Paging", String.valueOf(pager));
                        try {

                            doSearch(query, String.valueOf(pager));
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }else{
                        isLastPage=true;
                    }
                }
            }

        }
        @Override
        protected void onPreExecute() {

        }

    }


    class SearchAnnotations extends AsyncTask<String, String, List<SearchResult>> {




        @Override
        protected List<SearchResult> doInBackground(String... params) {
            Log.d("Search","Annotations Search");

            String searchtext = params[0];
            String offset = params[1];





            String SQL_DISTINCT_ENAME = SearchUtil.generateAnnotationQuery(searchtext,offset,filtered);
            Log.d("Query",SQL_DISTINCT_ENAME);
            Cursor c = null;
            try {
                try {
                    c = db.rawQuery(SQL_DISTINCT_ENAME, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    c= null;
                }
                if(c!= null){
                    if (c.moveToFirst()) {
                        do {

                            try {
                                if (c.getString(1) != null) {

                                    String description = SearchUtil.findParagraph(c.getString(2), searchlist, "<p>");
                                    SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
                                    String year = "Not available";
                                    try {
                                        year = dateformat.format(new Date(c.getLong(5)));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    SearchResult searchresult = new SearchResult(c.getString(1), description, c.getString(0), "Annotation", c.getString(4), year,searchtext,filtered);
                                    if (!isDuplicate(resp, c.getString(1))) {
                                        resp.add(searchresult);
                                        annotationresp.add(searchresult);
                                    }


                                }
                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }

                            //resp.add(postArrayList);
                        } while (c.moveToNext());
                    }

                }

            } finally {
                if(c!=null) {
                    c.close();
                }
            }

            return resp;
        }



        @Override
        protected void onPostExecute(List<SearchResult> result) {
            String endtime=String.valueOf((System.currentTimeMillis()-starttime)/1000);
            System.out.println("Query Finished in "+endtime);
            runquery= runquery+1;
            if(runquery>=totalquery){
                runquery = 0;
                searchProgress.setVisibility(View.INVISIBLE);
                if (result.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }

                try {
                    progressDialog.dismiss();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }

            }
            isLoading=false;
            if(result.size()<=0){
                isLastPage=true;
            }



            populateAdapter();
            adapter.notifyDataSetChanged();
            Log.d("NotifyDatasetChanged","Fired");
            if(adapter.getItemCount()<20){
                offset  = offset+1;
                int pager=(offset)*limit;
                if(result.size()>10)
                {
                    showProgress = false;
                }
                if(result.size()>0) {
                    int totalpager=pager+20;
                    Log.d("RatioList",String.valueOf(ratiosize));
                    Log.d("TotalPager",String.valueOf(totalpager));
                    if(ratiosize>totalpager) {
                        Log.d("Paging", String.valueOf(pager));
                        try {

                            doSearch(query, String.valueOf(pager));
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }else{
                        isLastPage=true;
                    }
                }
            }

        }
        @Override
        protected void onPreExecute() {

        }

    }



    class SearchPublications extends AsyncTask<String, String, List<SearchResult>> {




        @Override
        protected List<SearchResult> doInBackground(String... params) {
            Log.d("Search","Publications Search");

            String searchtext = params[0];
            String offset = params[1];





            String SQL_DISTINCT_ENAME = SearchUtil.generateArticlesQuery(searchtext,offset,filtered);
            Log.d("Query",SQL_DISTINCT_ENAME);
            Cursor c = null;
            try {
                try {
                    c = db.rawQuery(SQL_DISTINCT_ENAME, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    c= null;
                }
                if(c!= null){
                    if (c.moveToFirst()) {
                        do {

                            try {
                                if (c.getString(1) != null) {

                                    String description = SearchUtil.findParagraph(c.getString(2), searchlist, "<p>");
                                    SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
                                    String year = "Not available";
                                    try {
                                        year = dateformat.format(new Date(c.getLong(5)));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    SearchResult searchresult = new SearchResult(c.getString(1), description, c.getString(0), "Publications", c.getString(4), year,searchtext,filtered);
                                    if (!isDuplicate(resp, c.getString(1))) {
                                        resp.add(searchresult);
                                        publicationresp.add(searchresult);
                                    }


                                }
                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }

                            //resp.add(postArrayList);
                        } while (c.moveToNext());
                    }

                }

            } finally {
                if(c!=null) {
                    c.close();
                }
            }

            return resp;
        }



        @Override
        protected void onPostExecute(List<SearchResult> result) {
            String endtime=String.valueOf((System.currentTimeMillis()-starttime)/1000);
            System.out.println("Query Finished in "+endtime);
            runquery= runquery+1;
            if(runquery>=totalquery){
                runquery = 0;
                searchProgress.setVisibility(View.INVISIBLE);
                if (result.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }


                try {
                    progressDialog.dismiss();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            isLoading=false;
            if(result.size()<=0){
                isLastPage=true;
            }

            populateAdapter();
            adapter.notifyDataSetChanged();
            Log.d("NotifyDatasetChanged","Fired");
            if(adapter.getItemCount()<20){
                offset  = offset+1;
                int pager=(offset)*limit;
                if(result.size()>10)
                {
                    showProgress = false;
                }
                if(result.size()>0) {
                    int totalpager=pager+20;
                    Log.d("RatioList",String.valueOf(ratiosize));
                    Log.d("TotalPager",String.valueOf(totalpager));
                    if(ratiosize>totalpager) {
                        Log.d("Paging", String.valueOf(pager));
                        try {

                            doSearch(query, String.valueOf(pager));
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }else{
                        isLastPage=true;
                    }
                }
            }

        }
        @Override
        protected void onPreExecute() {

        }

    }



    class SearchArticles extends AsyncTask<String, String, List<SearchResult>> {




        @Override
        protected List<SearchResult> doInBackground(String... params) {
            Log.d("Search","Articles Search");

            String searchtext = params[0];
            String offset = params[1];





            String SQL_DISTINCT_ENAME = SearchUtil.generateArticlesQuery(searchtext,offset,filtered);
            Log.d("Query",SQL_DISTINCT_ENAME);
            Cursor c = null;
            try {
                try {
                    c = db.rawQuery(SQL_DISTINCT_ENAME, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    c= null;
                }
                if(c!= null){
                    if (c.moveToFirst()) {
                        do {

                            try {
                                if (c.getString(1) != null) {

                                    String description = SearchUtil.findParagraph(c.getString(2), searchlist, "<p>");
                                    SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
                                    String year = "Not available";
                                    try {
                                        year = dateformat.format(new Date(c.getLong(5)));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    SearchResult searchresult = new SearchResult(c.getString(1), description, c.getString(0), "Articles", c.getString(4), year,searchtext,filtered);
                                    if (!isDuplicate(resp, c.getString(1))) {
                                        resp.add(searchresult);
                                        articlesresp.add(searchresult);
                                    }


                                }
                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }

                            //resp.add(postArrayList);
                        } while (c.moveToNext());
                    }

                }

            } finally {
                if(c!=null) {
                    c.close();
                }
            }

            return resp;
        }



        @Override
        protected void onPostExecute(List<SearchResult> result) {
            String endtime=String.valueOf((System.currentTimeMillis()-starttime)/1000);
            System.out.println("Query Finished in "+endtime);
            runquery= runquery+1;
            if(runquery>=totalquery){
                runquery = 0;
                searchProgress.setVisibility(View.INVISIBLE);

                if (result.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }


                try {
                    progressDialog.dismiss();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            isLoading=false;
            if(result.size()<=0){
                isLastPage=true;
            }

            populateAdapter();
            adapter.notifyDataSetChanged();
            Log.d("NotifyDatasetChanged","Fired");
            if(adapter.getItemCount()<20){
                offset  = offset+1;
                int pager=(offset)*limit;
                if(result.size()>10)
                {
                    showProgress = false;
                }
                if(result.size()>0) {
                    int totalpager=pager+20;
                    Log.d("RatioList",String.valueOf(ratiosize));
                    Log.d("TotalPager",String.valueOf(totalpager));
                    if(ratiosize>totalpager) {
                        Log.d("Paging", String.valueOf(pager));
                        try {

                            doSearch(query, String.valueOf(pager));
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }else{
                        isLastPage=true;
                    }
                }
            }

        }
        @Override
        protected void onPreExecute() {

        }

    }


    class SearchDictionary extends AsyncTask<String, String, List<SearchResult>> {




        @Override
        protected List<SearchResult> doInBackground(String... params) {
            Log.d("Search","Dictionary Search");

            String searchtext = params[0];
            String offset = params[1];





            String SQL_DISTINCT_ENAME = SearchUtil.generateDictionaryQuery(searchtext,offset,filtered);
            Log.d("Query",SQL_DISTINCT_ENAME);
            Cursor c = null;
            try {
                try {
                    c = db.rawQuery(SQL_DISTINCT_ENAME, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    c= null;
                }
                if(c!= null){
                    if (c.moveToFirst()) {
                        do {

                            try {
                                if (c.getString(1) != null) {

                                    String description = SearchUtil.findParagraph(c.getString(2), searchlist, "<p>");
                                    SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
                                    String year = "Not available";
                                    try {
                                        year = dateformat.format(new Date(c.getLong(5)));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    SearchResult searchresult = new SearchResult(c.getString(1), description, c.getString(0), "Dictionary", c.getString(4), year,searchtext,filtered);
                                    if (!isDuplicate(resp, c.getString(1))) {
                                        resp.add(searchresult);
                                        dictionaryresp.add(searchresult);
                                    }


                                }
                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }

                            //resp.add(postArrayList);
                        } while (c.moveToNext());
                    }

                }

            } finally {
                if(c!=null) {
                    c.close();
                }
            }

            return resp;
        }



        @Override
        protected void onPostExecute(List<SearchResult> result) {
            String endtime=String.valueOf((System.currentTimeMillis()-starttime)/1000);
            System.out.println("Query Finished in "+endtime);
            runquery= runquery+1;
            if(runquery>=totalquery){
                runquery = 0;
                searchProgress.setVisibility(View.INVISIBLE);

                if (result.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }


                try {
                    progressDialog.dismiss();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }


            }
            isLoading=false;
            if(result.size()<=0){
                isLastPage=true;
            }

            populateAdapter();
            adapter.notifyDataSetChanged();
            Log.d("NotifyDatasetChanged","Fired");
            if(adapter.getItemCount()<20){
                offset  = offset+1;
                int pager=(offset)*limit;
                if(result.size()>10)
                {
                    showProgress = false;
                }
                if(result.size()>0) {
                    int totalpager=pager+20;
                    Log.d("RatioList",String.valueOf(ratiosize));
                    Log.d("TotalPager",String.valueOf(totalpager));
                    if(ratiosize>totalpager) {
                        Log.d("Paging", String.valueOf(pager));
                        try {

                            doSearch(query, String.valueOf(pager));
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }else{
                        isLastPage=true;
                    }
                }
            }

        }
        @Override
        protected void onPreExecute() {

        }

    }

    class SearchMaxims extends AsyncTask<String, String, List<SearchResult>> {




        @Override
        protected List<SearchResult> doInBackground(String... params) {
            Log.d("Search","Maxims Search");

            String searchtext = params[0];
            String offset = params[1];




            String SQL_DISTINCT_ENAME = SearchUtil.generateMaximsQuery(searchtext,offset,filtered);
            Log.d("Query",SQL_DISTINCT_ENAME);
            Cursor c = null;
            try {
                try {
                    c = db.rawQuery(SQL_DISTINCT_ENAME, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    c= null;
                }
                if(c!= null){
                    if (c.moveToFirst()) {
                        do {

                            try {
                                if (c.getString(1) != null) {

                                    String description = SearchUtil.findParagraph(c.getString(2), searchlist, "<p>");
                                    SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
                                    String year = "Not available";
                                    try {
                                        year = dateformat.format(new Date(c.getLong(5)));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    SearchResult searchresult = new SearchResult(c.getString(1), description, c.getString(0), "Maxims", c.getString(4), year,searchtext,filtered);
                                    if (!isDuplicate(resp, c.getString(1))) {
                                        resp.add(searchresult);
                                        maximresp.add(searchresult);
                                    }


                                }
                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }

                            //resp.add(postArrayList);
                        } while (c.moveToNext());
                    }

                }

            } finally {
                if(c!=null) {
                    c.close();
                }
            }

            return resp;
        }



        @Override
        protected void onPostExecute(List<SearchResult> result) {
            String endtime=String.valueOf((System.currentTimeMillis()-starttime)/1000);
            System.out.println("Query Finished in "+endtime);
            runquery= runquery+1;
            if(runquery>=totalquery){
                runquery = 0;
                searchProgress.setVisibility(View.INVISIBLE);
            }
            isLoading=false;
            if(result.size()<=0){
                isLastPage=true;
            }
            if (result.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }


            try {
                progressDialog.dismiss();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            populateAdapter();
            adapter.notifyDataSetChanged();
            Log.d("NotifyDatasetChanged","Fired");
            if(adapter.getItemCount()<20){
                offset  = offset+1;
                int pager=(offset)*limit;
                if(result.size()>10)
                {
                    showProgress = false;
                }
                if(result.size()>0) {
                    int totalpager=pager+20;
                    Log.d("RatioList",String.valueOf(ratiosize));
                    Log.d("TotalPager",String.valueOf(totalpager));
                    if(ratiosize>totalpager) {
                        Log.d("Paging", String.valueOf(pager));
                        try {

                            doSearch(query, String.valueOf(pager));
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }else{
                        isLastPage=true;
                    }
                }
            }

        }
        @Override
        protected void onPreExecute() {

        }

    }




    class SearchLaws extends AsyncTask<String, String, List<SearchResult>> {




        @Override
        protected List<SearchResult> doInBackground(String... params) {
            Log.d("Search","Law Search");

            String searchtext = params[0];
            String offset = params[1];





            String SQL_DISTINCT_ENAME = SearchUtil.generateLawsQuery(searchtext,offset,filtered);
            Log.d("Query",SQL_DISTINCT_ENAME);
            Cursor c = null;
            try {
                try {
                    c = db.rawQuery(SQL_DISTINCT_ENAME, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    c= null;
                }
                if(c!= null){
                    if (c.moveToFirst()) {
                        do {

                            try {
                                if (c.getString(1) != null) {

                                    String description = SearchUtil.findParagraph(c.getString(2), searchlist, "<p>");
                                    SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
                                    String year = "Not available";
                                    try {
                                        year = dateformat.format(new Date(c.getLong(5)));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    SearchResult searchresult = new SearchResult(c.getString(1), description, c.getString(0), "Laws of the Federation", c.getString(4), year,searchtext,filtered);
                                    /**if (!isDuplicate(resp, c.getString(1))) {
                                        resp.add(searchresult);
                                    }*/
                                    resp.add(searchresult);
                                    lawsresp.add(searchresult);


                                }
                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }

                            //resp.add(postArrayList);
                        } while (c.moveToNext());
                    }

                }

            } finally {
                if(c!=null) {
                    c.close();
                }
            }
            Log.d("Laws results"," Total "+String.valueOf(resp.size()));
            return resp;
        }



        @Override
        protected void onPostExecute(List<SearchResult> result) {
            String endtime=String.valueOf((System.currentTimeMillis()-starttime)/1000);
            System.out.println("Query Finished in "+endtime);
            runquery= runquery+1;
            if(runquery>=totalquery){
                runquery = 0;
                searchProgress.setVisibility(View.INVISIBLE);
                if (result.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }

                try {
                    progressDialog.dismiss();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }


            }
            isLoading=false;
            if(result.size()<=0){
                isLastPage=true;
            }



            showProgress("Updating search result.....");
            adapter.clearResults();
            populateAdapter();
            adapter.notifyDataSetChanged();

            Log.d("NotifyDatasetChanged","Fired");
            if(adapter.getItemCount()<20){
                offset  = offset+1;
                int pager=(offset)*limit;
                if(result.size()>10)
                {
                    showProgress = false;
                }
                if(result.size()>0) {
                    int totalpager=pager+20;
                    Log.d("RatioList",String.valueOf(ratiosize));
                    Log.d("TotalPager",String.valueOf(totalpager));
                    if(ratiosize>totalpager) {
                        Log.d("Paging", String.valueOf(pager));
                        try {

                            doSearch(query, String.valueOf(pager));
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }else{
                        isLastPage=true;
                    }
                }
            }

        }
        @Override
        protected void onPreExecute() {

        }

    }


    class DoRatioSearch extends AsyncTask<String, String, List<SearchResult>> {



        private String searchtext;
        private  String offset;
        @Override
        protected List<SearchResult> doInBackground(String... params) {
            Log.d("Search","Running Query");

            searchtext = params[0];
            offset = params[1];


            if(ratiolist.size()<=0) {
                String SQL_DISTINCT_ENAME0 = SearchUtil.fetchRatios(searchtext,false);
                Log.d("RatioQuery", SQL_DISTINCT_ENAME0);
                Cursor c = null;
                try {

                    c = db.rawQuery(SQL_DISTINCT_ENAME0, null);
                    if (c.moveToFirst()) {
                        do {

                            Ratio ratio = new Ratio();
                            ratio.setId(c.getLong(0));
                            ratio.setTitle(c.getString(1));
                            ratio.setContent(c.getString(2));
                            ratiolist.add(ratio);
                            Log.d("ID", String.valueOf(c.getLong(0)));
                            Log.d("Ratio", c.getString(1));
                            //resp.add(postArrayList);
                        } while (c.moveToNext());
                    }
                } finally {
                    c.close();
                }

            }
            ratiosize = ratiolist.size();
            Log.d("Total Ratios",String.valueOf(ratiosize));


            return resp;
        }



        @Override
        protected void onPostExecute(List<SearchResult> result) {
            String endtime=String.valueOf((System.currentTimeMillis()-starttime)/1000);
            System.out.println("Query Finished in "+endtime);
            runquery= runquery+1;
            if(runquery>=totalquery){
                runquery = 0;
                searchProgress.setVisibility(View.INVISIBLE);
            }
            new DoSummarySearch().execute(searchtext,offset);
            new SearchAdvanced().execute(searchtext,offset);
        }
        @Override
        protected void onPreExecute() {

        }

    }



    class SearchQuery extends AsyncTask<String, String, List<SearchResult>> {




        @Override
        protected List<SearchResult> doInBackground(String... params) {
            Log.d("Search","Running Query");

            String searchtext = params[0];
            String offset = params[1];


            Cursor c = null;
            if(ratiolist.size()<=0) {
                String SQL_DISTINCT_ENAME0 = SearchUtil.fetchRatios(searchtext,false);
                Log.d("RatioQuery", SQL_DISTINCT_ENAME0);

                try {

                    c = db.rawQuery(SQL_DISTINCT_ENAME0, null);
                    if (c.moveToFirst()) {
                        do {

                            Ratio ratio = new Ratio();
                            ratio.setId(c.getLong(0));
                            ratio.setTitle(c.getString(1));
                            ratio.setContent(c.getString(2));
                            ratiolist.add(ratio);
                            Log.d("Ratio", c.getString(1));
                            //resp.add(postArrayList);
                        } while (c.moveToNext());
                    }
                } finally {
                    c.close();
                }

            }
            ratiosize = ratiolist.size();
            Log.d("Total Ratios",String.valueOf(ratiosize));

            String SQL_DISTINCT_ENAME = SearchUtil.generateGeneralQuery(searchtext,offset,ratiolist,filtered);
            Log.d("Query",SQL_DISTINCT_ENAME);

            try {
                try {
                    c = db.rawQuery(SQL_DISTINCT_ENAME, null);
                }
                catch(Exception ex){
                    ex.printStackTrace();
                    c = null;
                }


                if(c != null){
                    if (c.moveToFirst()) {
                        do {
                            String description = SearchUtil.findParagraph(c.getString(2), searchlist, "<p>");
                            SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
                            String year = "Not available";
                            try {
                                year = dateformat.format(new Date(c.getLong(5)));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            SearchResult searchresult = new SearchResult(c.getString(0), description, c.getString(1), c.getString(3), c.getString(4), year,searchtext,filtered);
                            if (!isDuplicate(resp, c.getString(0))) {
                                resp.add(searchresult);
                            }
                            //resp.add(postArrayList);
                        } while (c.moveToNext());
                    }

                }


            } finally {
                if(c!=null) {
                    c.close();
                }
            }
            Log.d("Results 0 ",String.valueOf(resp.size()));

            /**String SQL_DISTINCT_ENAME2 = SearchUtil.generateGeneralQuery2(searchtext,offset,ratiolist,filtered);
             Log.d("Query",SQL_DISTINCT_ENAME2);

             try {
             try {
             c = db.rawQuery(SQL_DISTINCT_ENAME2, null);
             } catch (Exception ex) {
             ex.printStackTrace();
             c = null;
             }

             if(c != null){
             if (c.moveToFirst()) {
             do {
             String description = SearchUtil.findParagraph(c.getString(2), searchlist, "<p>");
             SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
             String year = "Not available";
             try {
             year = dateformat.format(new Date(c.getLong(5)));
             } catch (Exception ex) {
             ex.printStackTrace();
             }
             SearchResult searchresult = new SearchResult(c.getString(0), description, c.getString(1), c.getString(3), c.getString(4), year,searchtext,filtered);
             if (!isDuplicate(resp, c.getString(0))) {
             resp.add(searchresult);
             }
             //resp.add(postArrayList);
             } while (c.moveToNext());
             }
             }
             } finally {
             if(c!=null) {
             c.close();
             }
             }

             Log.d("Results 1 ",String.valueOf(resp.size()));
             */
            String SQL_DISTINCT_ENAME3 = SearchUtil.generateGeneralQuery3(searchtext,offset,ratiolist,filtered);
            Log.d("Query",SQL_DISTINCT_ENAME3);

            try {
                try {
                    c = db.rawQuery(SQL_DISTINCT_ENAME3, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    c= null;
                }
                if(c!= null){
                    if (c.moveToFirst()) {
                        do {

                            try {
                                if (c.getString(1) != null) {

                                    String description = SearchUtil.findParagraph(c.getString(2), searchlist, "<p>");
                                    SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
                                    String year = "Not available";
                                    try {
                                        year = dateformat.format(new Date(c.getLong(5)));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    SearchResult searchresult = new SearchResult(c.getString(1), description, c.getString(0), c.getString(3), c.getString(4), year,searchtext,filtered);
                                    if (!isDuplicate(resp, c.getString(1))) {
                                        resp.add(searchresult);
                                    }


                                }
                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }

                            //resp.add(postArrayList);
                        } while (c.moveToNext());
                    }

                }

            } finally {
                if(c!=null) {
                    c.close();
                }
            }

            return resp;
        }



        @Override
        protected void onPostExecute(List<SearchResult> result) {
            isLoading=false;
            if(result.size()<=0){
                isLastPage=true;
            }
            if (result.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }


            try {
                progressDialog.dismiss();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            adapter.setResults(result,ispaged);
            adapter.notifyDataSetChanged();
            Log.d("NotifyDatasetChanged","Fired");
            if(adapter.getItemCount()<20){
                offset  = offset+1;
                int pager=(offset)*limit;
                if(result.size()>10)
                {
                    showProgress = false;
                }
                if(result.size()>0) {
                    int totalpager=pager+20;
                    Log.d("RatioList",String.valueOf(ratiosize));
                    Log.d("TotalPager",String.valueOf(totalpager));
                    if(ratiosize>totalpager) {
                        Log.d("Paging", String.valueOf(pager));
                        try {

                            doSearch(query, String.valueOf(pager));
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }else{
                        isLastPage=true;
                    }
                }
            }

        }
        @Override
        protected void onPreExecute() {
            if(showProgress) {
                //progressDialog.setCancelable(false);
                if(!progressDialog.isShowing()) {
                    try {

                        progressDialog.setTitle("Legalpedia");
                        progressDialog.setMessage("Processing...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                }

            }
        }

    }
    class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }

}
