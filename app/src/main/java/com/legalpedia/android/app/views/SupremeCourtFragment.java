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
import com.legalpedia.android.app.R;
import com.legalpedia.android.app.adapter.SupremeCourtAdapter;
import com.legalpedia.android.app.models.Articles;
import com.legalpedia.android.app.models.ArticlesDao;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Judgements;
import com.legalpedia.android.app.models.JudgementsDao;
import com.legalpedia.android.app.models.RatioDao;
import com.legalpedia.android.app.models.Summary;
import com.legalpedia.android.app.models.SummaryDao;
import com.legalpedia.android.app.ui.PaginationScrollListener;
import com.legalpedia.android.app.util.ResourceUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by adebayoolabode on 11/19/16.
 */

public class SupremeCourtFragment extends Fragment {
    private Context ctx=null;
    private RecyclerView recyclerView=null;
    private TextView emptyView;
    private SupremeCourtAdapter adapter;
    private int offset=0;
    private int limit=50;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private  DaoSession daoSession;
    public SupremeCourtFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.list_supremecourt, container, false);
        //progressDialog=new ProgressDialog();
        ctx=rootView.getContext();
        daoSession = ((App)  getActivity().getApplication()).getDaoSession();

        getActivity().setTitle("Judgements");
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.supremecourtrecycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new SupremeCourtAdapter(ctx);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                offset += 1;
                int pager=(offset)*limit;

                new  FetchFromDatabase().execute(pager);
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


        return rootView;
    }







    public void searchFragment(String searchtext){
        List<Summary> resp= new ArrayList<Summary>();
        try {
        new SearchQuery().execute(searchtext);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }



    public void filterFragment(int index){
        try {
            new FilterFromDatabase().execute(offset, index);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }


    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            Log.d("Visible","Supreme Court");
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

            new  FetchFromDatabase().execute(offset);

            
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }


    class SearchQuery extends AsyncTask<String, String, List<Summary>> {

        private List<Summary> resp;
        private ProgressDialog progressDialog = new ProgressDialog(ctx,R.style.customDialog);


        @Override
        protected List<Summary> doInBackground(String... params) {
            resp = new ArrayList<Summary>();
            String searchtext = params[0];
            String SQL_DISTINCT_ENAME ="";
            if(searchtext.length()<=0){
                SQL_DISTINCT_ENAME = "SELECT " + SummaryDao.Properties.Id.columnName + "," + SummaryDao.Properties.Case_title.columnName + "," + SummaryDao.Properties.Date.columnName + "  FROM summary WHERE " +   SummaryDao.Properties.Courtid.columnName + "=1  ORDER BY " + SummaryDao.Properties.Date.columnName + " LIMIT 100";

            }else {
                SQL_DISTINCT_ENAME = "SELECT " + SummaryDao.Properties.Id.columnName + "," + SummaryDao.Properties.Case_title.columnName + "," + SummaryDao.Properties.Date.columnName + "  FROM summary WHERE " + SummaryDao.Properties.Case_title.columnName + " LIKE '" + searchtext + "%' AND " + SummaryDao.Properties.Courtid.columnName + "=1  ORDER BY " + SummaryDao.Properties.Date.columnName + " LIMIT 100";
            }
            Log.d("Query",SQL_DISTINCT_ENAME);
            Cursor c = null;
            try{
                 c= daoSession.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null);
                if (c.moveToFirst()) {
                    do {
                        Summary summary = new Summary();
                        summary.setId(c.getLong(0));
                        summary.setCase_title(c.getString(1));
                        try {
                            //SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy");
                            //String year = dateformat.format(new Date(c.getLong(2)));

                            Date date=new Date(c.getLong(2));
                            //Date simpledate = new SimpleDateFormat("yyyy-MM-dd").parse(c.getString(2));
                            Log.d("Search Date",date.toString());
                            summary.setDate(date);
                            //summary.setDate(new SimpleDateFormat("MMMMM dd, yyyy").parse(c.getString(2)));
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                            summary.setDate(new Date());
                        }

                        resp.add(summary);
                    } while (c.moveToNext());
                }
            } finally {
                if(c!=null) {
                    c.close();
                }
            }

            return resp;
        }

        @Override
        protected void onPostExecute(List<Summary> result) {
            // execution of result of Long time consuming operation
            isLoading = false;
            if (result.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            }
            try {
                Log.d("Progress1","Turning off");
                progressDialog.dismiss();
                progressDialog.cancel();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            adapter.setSummary(result);
            adapter.notifyDataSetChanged();

        }

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Legalpedia");
            progressDialog.setMessage("Processing...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }


    }

    class FetchFromDatabase extends AsyncTask<Integer, String, List<Summary>> {

        private List<Summary> resp;
        private ProgressDialog progressDialog = new ProgressDialog(ctx,R.style.customDialog);



        @Override
        protected List<Summary> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()
            Log.v("SupremeCourtFragment", " FetchFromDatabase ");
            try {
                resp= new ArrayList<Summary>();


                SummaryDao summaryDao =daoSession.getSummaryDao();
                //System.out.println("Total Summary items "+summaryDao.loadAll().size());
                System.out.println("Offset "+offset[0]);
                QueryBuilder qb = summaryDao.queryBuilder();
                resp= qb.where(SummaryDao.Properties.Courtid.eq(1)).orderDesc(SummaryDao.Properties.Date).offset(offset[0]).limit(limit).list();


            } catch (Exception e) {
                e.printStackTrace();

            }


            return resp;
        }


        @Override
        protected void onPostExecute(List<Summary> result) {
            // execution of result of Long time consuming operation
            isLoading = false;
            adapter.addSummary(result);
            adapter.notifyDataSetChanged();
            try {
                Log.d("Progress","Turning off");
                progressDialog.dismiss();

            }
            catch(Exception ex){
                ex.printStackTrace();
            }



        }


        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Legalpedia");
            progressDialog.setMessage("Processing...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }



    class FilterFromDatabase extends AsyncTask<Integer, String, List<Summary>> {

        private List<Summary> resp;
        private ProgressDialog progressDialog = new ProgressDialog(ctx,R.style.customDialog);



        @Override
        protected List<Summary> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()
            Log.v("SupremeCourtFragment", " FilterFromDatabase ");
            try {
                resp= new ArrayList<Summary>();


                SummaryDao summaryDao =daoSession.getSummaryDao();
                //System.out.println("Total Summary items "+summaryDao.loadAll().size());
                System.out.println("Offset "+offset[0]);
                QueryBuilder qb = summaryDao.queryBuilder();
                int index=offset[1];
                if(index==0) {
                    resp = qb.where(SummaryDao.Properties.Courtid.eq(1)).orderAsc(SummaryDao.Properties.Case_title).offset(offset[0]).limit(limit).list();

                }
                else if(index==1) {
                    resp = qb.where(SummaryDao.Properties.Courtid.eq(1)).orderDesc(SummaryDao.Properties.Case_title).offset(offset[0]).limit(limit).list();

                }
                else if(index==2) {
                    resp = qb.where(SummaryDao.Properties.Courtid.eq(1)).orderAsc(SummaryDao.Properties.Date).offset(offset[0]).limit(limit).list();

                }
                else{
                    resp = qb.where(SummaryDao.Properties.Courtid.eq(1)).orderDesc(SummaryDao.Properties.Date).offset(offset[0]).limit(limit).list();

                }

            } catch (Exception e) {
                e.printStackTrace();

            }


            return resp;
        }


        @Override
        protected void onPostExecute(List<Summary> result) {
            // execution of result of Long time consuming operation
            isLoading = false;
            adapter.setSummary(result);
            adapter.notifyDataSetChanged();
            try {
                Log.d("Progress","Turning off");
                progressDialog.dismiss();

            }
            catch(Exception ex){
                ex.printStackTrace();
            }



        }


        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Legalpedia");
            progressDialog.setMessage("Processing...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }


}


