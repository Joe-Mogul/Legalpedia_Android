package com.legalpedia.android.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.legalpedia.android.app.adapter.PrinciplesAdapter;
import com.legalpedia.android.app.adapter.SummaryCasesAdapter;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Principles;
import com.legalpedia.android.app.models.PrinciplesDao;
import com.legalpedia.android.app.models.Summary;
import com.legalpedia.android.app.models.SummaryDao;
import com.legalpedia.android.app.views.SimpleDividerItemDecoration;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class SummaryCasesViewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView=null;
    private SummaryCasesAdapter adapter;
    private Context context;
    private String principleid;
    private String title;
    private int offset=0;
    private int limit=20;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principleslist);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        Intent intent=getIntent();
        principleid=intent.getStringExtra("principleid");
        title=intent.getStringExtra("title");
        setTitle(title);
        context=this;
        progressDialog = new ProgressDialog(context,R.style.customDialog);
        progressDialog.setTitle("Legalpedia");
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        recyclerView = (RecyclerView)findViewById(R.id.principles_recycler);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new SummaryCasesAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    Log.v("LawChapterFragment", "Paging....");
                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;

                            //Do pagination.. i.e. fetch new data
                            int pager=(offset+1)*10;
                            new FetchFromDatabase().execute(pager);
                        }
                    }
                }
            }
        });

        new FetchFromDatabase().execute(0);
    }

    class FetchFromDatabase extends AsyncTask<Integer, String, List<Summary>> {

        private List<Summary> resp;


        @Override
        protected List<Summary> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {
                resp= new ArrayList<Summary>();
                DaoSession daoSession = ((App)  getApplication()).getDaoSession();

                SummaryDao summaryDao =daoSession.getSummaryDao();
                QueryBuilder qb = summaryDao.queryBuilder();
                resp=qb.where(SummaryDao.Properties.Principleid.eq(principleid)).orderAsc(SummaryDao.Properties.Case_title).list();
                System.out.println("Total items "+resp.size());

            } catch (Exception e) {
                e.printStackTrace();

            }
            return resp;
        }


        @Override
        protected void onPostExecute(List<Summary> result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();

            adapter.setSummary(result);
            adapter.notifyDataSetChanged();

        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }
}
