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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.legalpedia.android.app.adapter.MaximsDetailAdapter;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Maxims;
import com.legalpedia.android.app.models.MaximsDao;
import com.legalpedia.android.app.views.SimpleDividerItemDecoration;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class MaximsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView=null;
    private MaximsDetailAdapter adapter;
    private Context context;
    String maximsid;
    private int offset=0;
    private int limit=20;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private ProgressDialog progressDialog;
    private List<String> maximstitles=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sectionlist);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        setTitle("Maxims");
        context=this;
        progressDialog = new ProgressDialog(context,R.style.customDialog);
        progressDialog.setTitle("Legalpedia");
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Intent intent=getIntent();
        maximsid=intent.getStringExtra("maximid");
        System.out.println("Maximid "+maximsid);
        recyclerView = (RecyclerView)findViewById(R.id.section_recycler);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        //recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new MaximsDetailAdapter(this);
        recyclerView.setAdapter(adapter);


        new FetchFromDatabase().execute(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.sectionlist, menu);





        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            // click on 'up' button in the action bar, handle it here
            //MainActivity.selectedTab = 0;
            /**Intent intent= new Intent(this,DictionaryMaximsActivity.class);
            startActivity(intent);
             */
            onBackPressed();
            return true;


        }



        return super.onOptionsItemSelected(item);
    }


    class FetchFromDatabase extends AsyncTask<Integer, String, List<Maxims>> {

        private List<Maxims> resp;


        @Override
        protected List<Maxims> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {
                resp= new ArrayList<Maxims>();
                DaoSession daoSession = ((App)  getApplication()).getDaoSession();

                MaximsDao maximsDao =daoSession.getMaximsDao();
                QueryBuilder qb = maximsDao.queryBuilder();
                List<Maxims> maximslist=qb.orderAsc(MaximsDao.Properties.Title).list();
                for(Maxims c:maximslist) {
                    if (!maximstitles.contains(c.getTitle())) {
                        resp.add(c);
                        maximstitles.add(c.getTitle());
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();

            }
            return resp;
        }


        @Override
        protected void onPostExecute(List<Maxims> result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();

            adapter.setMaxims(result);
            adapter.notifyDataSetChanged();
            int position = getWordPosition(maximsid,result);
            recyclerView.scrollToPosition(position);

        }


        public int getWordPosition(String maximsid,List<Maxims> results){
            int i=0;
            for(Maxims d : results){

                if(String.valueOf(d.getId()).equals(maximsid)){
                    break;
                }
                i++;
            }

            return i;
        }
        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }
}
