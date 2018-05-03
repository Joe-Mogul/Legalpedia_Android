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
import com.legalpedia.android.app.adapter.PrecedenceAdapter;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Precedence;
import com.legalpedia.android.app.models.PrecedenceDao;
import com.legalpedia.android.app.models.Sections;
import com.legalpedia.android.app.models.SectionsDao;
import com.legalpedia.android.app.views.SimpleDividerItemDecoration;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 5/12/17.
 */

public class PrecedenceListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView=null;
    private PrecedenceAdapter adapter;
    private Context context;
    String category;
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
        Intent intent=getIntent();
        category=intent.getStringExtra("category");
        context=this;
        progressDialog = new ProgressDialog(context,R.style.customDialog);
        progressDialog.setTitle("Legalpedia");
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        recyclerView = (RecyclerView)findViewById(R.id.section_recycler);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new PrecedenceAdapter(this,category);
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
        inflater.inflate(R.menu.precedencelist, menu);





        return true;
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();

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
            /**MainActivity.selectedTab = 3;
            Intent intent= new Intent(this,MainActivity.class);
            startActivity(intent);
            */
            onBackPressed();
            return true;


        }
        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.full) {

            Intent intent=new Intent(PrecedenceListActivity.this,PrecedenceFullDetailViewActivity.class);
            intent.putExtra("category",category);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }




    class FetchFromDatabase extends AsyncTask<Integer, String, List<Precedence>> {

        private List<Precedence> resp;


        @Override
        protected List<Precedence> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {
                resp= new ArrayList<Precedence>();
                DaoSession daoSession = ((App)  getApplication()).getDaoSession();

                PrecedenceDao sectionsDao =daoSession.getPrecedenceDao();
                QueryBuilder qb = sectionsDao.queryBuilder();
                resp=qb.where(PrecedenceDao.Properties.Category.eq(category)).orderAsc(PrecedenceDao.Properties.Category).list();
                System.out.println("Total items "+resp.size());

            } catch (Exception e) {
                e.printStackTrace();

            }
            return resp;
        }


        @Override
        protected void onPostExecute(List<Precedence> result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();

            adapter.addPrecedence(result);
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
