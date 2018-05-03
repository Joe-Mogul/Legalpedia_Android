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

import com.legalpedia.android.app.adapter.DictionaryAdapter;
import com.legalpedia.android.app.adapter.DictionaryDetailAdapter;
import com.legalpedia.android.app.adapter.MaximsDetailAdapter;
import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Dictionary;
import com.legalpedia.android.app.models.DictionaryDao;
import com.legalpedia.android.app.models.Maxims;
import com.legalpedia.android.app.models.MaximsDao;
import com.legalpedia.android.app.views.SimpleDividerItemDecoration;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class DictionaryActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView=null;
    private DictionaryDetailAdapter adapter;
    private Context context;
    String dictionaryid;
    private int offset=0;
    private int limit=20;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private ProgressDialog progressDialog;
    private List<String> dicttitles=new ArrayList<String>();
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
        setTitle("Dictionary");
        context=this;
        progressDialog = new ProgressDialog(context,R.style.customDialog);
        progressDialog.setTitle("Legalpedia");
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Intent intent=getIntent();
        dictionaryid=intent.getStringExtra("dictionaryid");

        recyclerView = (RecyclerView)findViewById(R.id.section_recycler);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration dividerItemDecoration = new SimpleDividerItemDecoration(recyclerView.getContext());
        //recyclerView.addItemDecoration(dividerItemDecoration);
        adapter=new DictionaryDetailAdapter(this);
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
            Intent intent= new Intent(this,DictionaryMaximsActivity.class);
            startActivity(intent);

            return true;


        }



        return super.onOptionsItemSelected(item);
    }


    class FetchFromDatabase extends AsyncTask<Integer, String, List<Dictionary>> {

        private List<Dictionary> resp;


        @Override
        protected List<Dictionary> doInBackground(Integer ...offset) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {
                resp= new ArrayList<Dictionary>();
                DaoSession daoSession = ((App)  getApplication()).getDaoSession();

                DictionaryDao dictionaryDao =daoSession.getDictionaryDao();
                QueryBuilder qb = dictionaryDao.queryBuilder();
                //resp=qb.orderAsc(DictionaryDao.Properties.Title).list();
                List<Dictionary> dictionarylist=qb.orderAsc(DictionaryDao.Properties.Title).list();
                for(Dictionary c:dictionarylist) {
                    if (!dicttitles.contains(c.getTitle())) {
                        resp.add(c);
                        dicttitles.add(c.getTitle());
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();

            }
            return resp;
        }


        @Override
        protected void onPostExecute(List<Dictionary> result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();

            adapter.setDictionary(result);
            adapter.notifyDataSetChanged();
            Log.d("DicictionaryPos",dictionaryid);
            int position = getWordPosition(dictionaryid,result);
            recyclerView.scrollToPosition(position);


        }


        public int getWordPosition(String dictionaryid,List<Dictionary> results){
            int i=0;
            for(Dictionary d : results){

                if(String.valueOf(d.getId()).equals(dictionaryid)){
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
