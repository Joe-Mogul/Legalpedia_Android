package com.legalpedia.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Notes;
import com.legalpedia.android.app.models.NotesDao;
import com.legalpedia.android.app.models.Researchrequest;
import com.legalpedia.android.app.models.ResearchrequestDao;

import java.util.List;

/**
 * Created by adebayoolabode on 2/4/17.
 */

public class ResearchDetailActivity extends AppCompatActivity {
    private DaoSession daoSession;
    private ResearchrequestDao researchrequestDao;
    private Toolbar toolbar;
    private TextView bodyTxt;
    public int requestid=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.researchdetails_activity);
        daoSession = ((App)  getApplication()).getDaoSession();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        daoSession = ((App)  getApplication()).getDaoSession();
        researchrequestDao = daoSession.getResearchrequestDao();

        bodyTxt = (TextView)findViewById(R.id.body);
        Intent intent= getIntent();
        if(intent!=null) {
            String requestval = intent.getStringExtra("requestid");
            requestid = Integer.parseInt(requestval);
            Log.d("researchrequestDao"," values "+  " researchrequestDao ID "+String.valueOf(requestid));
        }
        List<Researchrequest> researchrequests =researchrequestDao.queryBuilder().where(ResearchrequestDao.Properties.Id.eq(requestid)).list();
        if(researchrequests.size()>0) {
            Researchrequest researchrequest = researchrequests.get(0);
            setTitle(researchrequest.getTitle());
            bodyTxt.setText(researchrequest.getResearchdetail());
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notedetail, menu);

        MenuItem cartItem  = menu.findItem(R.id.edit);
        //View cartView = (View) MenuItemCompat.getActionView(cartItem);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.edit) {

            Intent intent=new Intent(ResearchDetailActivity.this,AddResearchActivity.class);
            intent.putExtra("requestid",requestid);
            intent.putExtra("isedit",true);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }


}
