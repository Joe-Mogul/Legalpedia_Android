package com.legalpedia.android.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.legalpedia.android.app.adapter.PrinciplesAdapter;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Principles;
import com.legalpedia.android.app.models.PrinciplesDao;
import com.legalpedia.android.app.models.PrinciplesStat;
import com.legalpedia.android.app.models.SummaryDao;
import com.legalpedia.android.app.views.SimpleDividerItemDecoration;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class PrinciplesViewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView=null;
    private PrinciplesAdapter adapter;
    private Context context;
    private String subjectid;
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
        subjectid=intent.getStringExtra("subjectid");
        title=intent.getStringExtra("title");
        setTitle(title);
        context=this;
        recyclerView = (RecyclerView)findViewById(R.id.principles_recycler);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new PrinciplesAdapter(this);
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

    class FetchFromDatabase extends AsyncTask<Integer, String, List<PrinciplesStat>> {

        private List<PrinciplesStat> resp;


        @Override
        protected List<PrinciplesStat> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {
                resp= new ArrayList<PrinciplesStat>();
                DaoSession daoSession = ((App)  getApplication()).getDaoSession();

                /**PrinciplesDao principlesDao =daoSession.getPrinciplesDao();
                QueryBuilder qb = principlesDao.queryBuilder();
                resp=qb.where(PrinciplesDao.Properties.Subjectid.eq(subjectid)).orderAsc(PrinciplesDao.Properties.Name).list();
                System.out.println("Total items "+resp.size());
*/
                String SQL_DISTINCT_ENAME = "SELECT a."+PrinciplesDao.Properties.Id.columnName+",a."+PrinciplesDao.Properties.Name.columnName+",count("+ SummaryDao.Properties.Principleid.columnName+") as judgements  FROM principles a,summary b WHERE "+ PrinciplesDao.Properties.Subjectid.columnName+"="+subjectid+" AND "+ SummaryDao.Properties.Principleid.columnName+"=a."+PrinciplesDao.Properties.Id.columnName+ " group by a.name ORDER BY a."+PrinciplesDao.Properties.Name.columnName;
                Log.d("Query",SQL_DISTINCT_ENAME);
                Cursor c = daoSession.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null);
                try{
                    if (c.moveToFirst()) {
                        do {
                            PrinciplesStat principles = new PrinciplesStat(c.getInt(0),c.getString(1),String.valueOf(c.getString(2)));
                            resp.add(principles);
                        } while (c.moveToNext());
                    }
                } finally {
                    c.close();
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
            return resp;
        }


        @Override
        protected void onPostExecute(List<PrinciplesStat> result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();

            adapter.addPrinciplesStat(result);
            adapter.notifyDataSetChanged();

        }


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(PrinciplesViewActivity.this,R.style.customDialog);
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
