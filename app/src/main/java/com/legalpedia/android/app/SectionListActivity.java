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

import com.legalpedia.android.app.adapter.LawSectionAdapter;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Sections;
import com.legalpedia.android.app.models.SectionsDao;
import com.legalpedia.android.app.views.SimpleDividerItemDecoration;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 5/12/17.
 */

public class SectionListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView=null;
    private LawSectionAdapter adapter;
    private Context context;
    String lawid;
    String sid;
    private int offset=0;
    private int limit=20;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private ProgressDialog progressDialog;
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
        context=this;
        progressDialog = new ProgressDialog(context,R.style.customDialog);
        progressDialog.setTitle("Legalpedia");
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Intent intent=getIntent();
        lawid=intent.getStringExtra("lawid");
        sid=intent.getStringExtra("sid");
        recyclerView = (RecyclerView)findViewById(R.id.section_recycler);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new LawSectionAdapter(this);
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
                            Log.v("LawChapterFragment", "Last Item Wow !");
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sectionlist, menu);





        return true;
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        MainActivity.selectedTab = 2;
        Log.d("SelectedTab Section",String.valueOf(MainActivity.selectedTab));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {
            // click on 'up' button in the action bar, handle it here
            /**MainActivity.selectedTab = 2;
            Intent intent= new Intent(this,MainActivity.class);
            startActivity(intent);
             */
            onBackPressed();

            return true;


        }
        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.full) {

            Intent intent=new Intent(SectionListActivity.this,LawFullDetailViewActivity.class);
            intent.putExtra("lawid",lawid);
            intent.putExtra("sid",sid);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }


    class FetchFromDatabase extends AsyncTask<Integer, String, List<Sections>> {

        private List<Sections> resp;


        @Override
        protected List<Sections> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {
                resp= new ArrayList<Sections>();
                DaoSession daoSession = ((App)  getApplication()).getDaoSession();

                SectionsDao sectionsDao =daoSession.getSectionsDao();
                QueryBuilder qb = sectionsDao.queryBuilder();
                resp=qb.where(SectionsDao.Properties.Lawid.eq(sid)).orderAsc(SectionsDao.Properties.Title).list();
                //System.out.println("Total items "+resp.size());

            } catch (Exception e) {
                e.printStackTrace();

            }
            return resp;
        }


        @Override
        protected void onPostExecute(List<Sections> result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();

            adapter.addSections(result);
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
