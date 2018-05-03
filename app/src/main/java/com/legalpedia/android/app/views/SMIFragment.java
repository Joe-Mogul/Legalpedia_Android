package com.legalpedia.android.app.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.legalpedia.android.app.AddNoteActivity;
import com.legalpedia.android.app.App;
import com.legalpedia.android.app.DatabaseDetailsActivity;
import com.legalpedia.android.app.MainActivity;
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.adapter.ArticleAdapter;
import com.legalpedia.android.app.adapter.SubjectsAdapter;
import com.legalpedia.android.app.models.Articles;
import com.legalpedia.android.app.models.ArticlesDao;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.PrinciplesDao;
import com.legalpedia.android.app.models.Subjects;
import com.legalpedia.android.app.models.SubjectsDao;
import com.legalpedia.android.app.models.SubjectsStat;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */


public class SMIFragment  extends Fragment {
    private Context ctx=null;
    private RecyclerView recyclerView=null;
    private TextView emptyView;
    private SubjectsAdapter adapter;
    private int offset=0;
    private int limit=20;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private DaoSession daoSession;
    ProgressDialog progressDialog;
    public SMIFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.list_subjects, container, false);
        //progressDialog=new ProgressDialog();
        daoSession = ((App)  getActivity().getApplication()).getDaoSession();
        getActivity().setTitle("Subject Matter Index");
        ctx=rootView.getContext();
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.subject_list);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new SubjectsAdapter(ctx);
        recyclerView.setAdapter(adapter);
        /**recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    Log.v("SubjectsFragment", "Paging....");
                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.v("SubjectsFragment", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            int pager=(offset+1)*10;
                            new FetchSubjects().execute(pager);
                        }
                    }
                }
            }
        });
         */

        //new FetchSubjects().execute(0);
        MainActivity.selectedTab = 1;
        return rootView;
    }

    /**@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.smi, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);



        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                //myAppAdapter.filter(searchQuery.toString().trim());
                //listView.invalidate();
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.database) {

            Intent intent=new Intent(ctx,DatabaseDetailsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.add) {

            Intent intent=new Intent(ctx,DatabaseDetailsActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

*/




    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }



    @Override
    public void onResume()
    {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }

        //INSERT CUSTOM CODE HERE
        try {
            new  FetchSubjects().execute(offset);

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }


    public void searchFragment(String searchtext){
        List<SubjectsStat> resp= new ArrayList<SubjectsStat>();
        //String SQL_DISTINCT_ENAME = "SELECT a."+SubjectsDao.Properties.Id.columnName+",a."+SubjectsDao.Properties.Name.columnName+",count(b."+PrinciplesDao.Properties.Subjectid.columnName+") as principles  FROM subjects a,principles b WHERE b."+PrinciplesDao.Properties.Subjectid.columnName+"=a."+SubjectsDao.Properties.Id.columnName+" AND a."+SubjectsDao.Properties.Name.columnName+" LIKE '"+searchtext+"%' group by a.name ORDER BY a."+SubjectsDao.Properties.Name.columnName;
        String SQL_DISTINCT_ENAME ="";
        if(searchtext.length()>0) {
             SQL_DISTINCT_ENAME = "SELECT _id,name from subjects WHERE  name like '" + searchtext +"%' GROUP BY name ORDER BY name";

        }
        else{
            SQL_DISTINCT_ENAME = "SELECT a."+SubjectsDao.Properties.Sid.columnName+",a."+SubjectsDao.Properties.Name.columnName+",count("+ PrinciplesDao.Properties.Subjectid.columnName+") as principles  FROM subjects a,principles b WHERE "+ PrinciplesDao.Properties.Subjectid.columnName+"=a."+SubjectsDao.Properties.Id.columnName+ " group by a.name ORDER BY a."+SubjectsDao.Properties.Name.columnName;

        }
        Log.d("Query 0", SQL_DISTINCT_ENAME);
        Cursor c = daoSession.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null);
        try{
            if (c.moveToFirst()) {
                do {
                    String count="0";
                    if(c.getColumnCount()>2){
                        count = String.valueOf(c.getString(2));
                    }
                    SubjectsStat subject = new SubjectsStat(c.getInt(0),c.getString(1),count);
                    resp.add(subject);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        Log.d("Total records",String.valueOf(resp.size()));
        //adapter.setSubjects(resp);
        adapter.setSubjectStats(resp);
        adapter.notifyDataSetChanged();
    }



    public void filterFragment(int index){
        List<SubjectsStat> resp= new ArrayList<SubjectsStat>();
        String SQL_DISTINCT_ENAME ="";
        if(index==0) {
            SQL_DISTINCT_ENAME = "SELECT a." + SubjectsDao.Properties.Sid.columnName + ",a." + SubjectsDao.Properties.Name.columnName + ",count(b." + PrinciplesDao.Properties.Subjectid.columnName + ") as principles  FROM subjects a,principles b WHERE b." + PrinciplesDao.Properties.Subjectid.columnName + "=a." + SubjectsDao.Properties.Id.columnName + "  group by a.name ORDER BY a." + SubjectsDao.Properties.Name.columnName;
        }
        else{
            SQL_DISTINCT_ENAME = "SELECT a." + SubjectsDao.Properties.Sid.columnName + ",a." + SubjectsDao.Properties.Name.columnName + ",count(b." + PrinciplesDao.Properties.Subjectid.columnName + ") as principles  FROM subjects a,principles b WHERE b." + PrinciplesDao.Properties.Subjectid.columnName + "=a." + SubjectsDao.Properties.Id.columnName + "  group by a.name ORDER BY a." + SubjectsDao.Properties.Id.columnName;

        }

            Log.d("Query 0",SQL_DISTINCT_ENAME);
        Cursor c = daoSession.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null);
        try{
            if (c.moveToFirst()) {
                do {
                    SubjectsStat subject = new SubjectsStat(c.getInt(0),c.getString(1),String.valueOf(c.getString(2)));
                    resp.add(subject);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        Log.d("Total records",String.valueOf(resp.size()));
        //adapter.setSubjects(resp);
        adapter.addSubjectStats(resp);
        adapter.notifyDataSetChanged();
    }

    public void loadSMI(){
        new  FetchSubjects().execute(offset);
    }

    class FetchSubjects extends AsyncTask<Integer, String, List<SubjectsStat>> {

        private List<SubjectsStat> resp;


        @Override
        protected List<SubjectsStat> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()
            resp = new ArrayList<SubjectsStat>();
            String SQL_DISTINCT_ENAME = "SELECT a."+SubjectsDao.Properties.Sid.columnName+",a."+SubjectsDao.Properties.Name.columnName+",count("+ PrinciplesDao.Properties.Subjectid.columnName+") as principles  FROM subjects a,principles b WHERE "+ PrinciplesDao.Properties.Subjectid.columnName+"=a."+SubjectsDao.Properties.Id.columnName+ " group by a.name ORDER BY a."+SubjectsDao.Properties.Name.columnName;
            Log.d("Query",SQL_DISTINCT_ENAME);
            Cursor c = daoSession.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null);
            try{
                if (c.moveToFirst()) {
                    do {
                        SubjectsStat subject = new SubjectsStat(c.getInt(0),c.getString(1),String.valueOf(c.getString(2)));
                        resp.add(subject);
                    } while (c.moveToNext());
                }
            } finally {
                c.close();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(List<SubjectsStat> result) {
            // execution of result of Long time consuming operation
            if (result.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
            progressDialog.dismiss();
            //adapter.addSubjects(result);
            adapter.addSubjectStats(result);
            adapter.notifyDataSetChanged();

        }


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ctx,R.style.customDialog);
            progressDialog.setTitle("Legalpedia");
            progressDialog.setMessage("Processing...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

}


