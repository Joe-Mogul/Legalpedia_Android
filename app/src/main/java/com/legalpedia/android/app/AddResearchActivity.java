package com.legalpedia.android.app;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Researchrequest;
import com.legalpedia.android.app.models.ResearchrequestDao;
import com.legalpedia.android.app.util.LGPClient;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class AddResearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private DaoSession daoSession;
    private ResearchrequestDao researchRequestDao;
    private EditText titleTxt;
    private EditText descriptionTxt;
    private EditText recommendedcitationsTxt;
    private EditText commentTxt;
    private EditText researchdetailTxt;
    private EditText messageTxt;
    private EditText expecteddateTxt;
    private Toolbar toolbar;
    public int requestid=0;
    public boolean isedit=false;
    private  DatePickerDialog datePickerDialog;
    private ProgressDialog progressDialog ;
    private SharedPreferences sharedpreferences = null;
    private String LOGINPREFERENCES="login_data";
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresearch);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Create Research Request");
        progressDialog = new ProgressDialog(this,R.style.customDialog);
        sharedpreferences=getSharedPreferences(LOGINPREFERENCES, Context.MODE_PRIVATE);
        uid = sharedpreferences.getString("uid", "");
        daoSession = ((App)  getApplication()).getDaoSession();
        researchRequestDao = daoSession.getResearchrequestDao();
        titleTxt = (EditText)findViewById(R.id.title);
        messageTxt = (EditText)findViewById(R.id.message);
        descriptionTxt = (EditText)findViewById(R.id.description);
        recommendedcitationsTxt = (EditText)findViewById(R.id.recommendedcitations);
        commentTxt = (EditText)findViewById(R.id.comment);
        researchdetailTxt = (EditText)findViewById(R.id.researchdetail);
        expecteddateTxt = (EditText)findViewById(R.id.expecteddate);
        expecteddateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddResearch","Text Field Clicked");
                showDate();
            }
        });
        Log.d("AddResearch","Before intent");
        Intent intent= getIntent();
        if(intent!=null) {
            requestid = intent.getIntExtra("requestid", 0);
            isedit = intent.getBooleanExtra("isedit",false);
            List<Researchrequest> researchRequests =researchRequestDao.queryBuilder().where(ResearchrequestDao.Properties.Id.eq(requestid)).list();
            if(researchRequests.size()>0){
                Log.d("Title",researchRequests.get(0).getTitle());
                setTitle(researchRequests.get(0).getTitle());
                titleTxt.setText(researchRequests.get(0).getTitle());
                descriptionTxt.setText(researchRequests.get(0).getDescription());
                recommendedcitationsTxt.setText(researchRequests.get(0).getRecommendedcitations());
                messageTxt.setText(researchRequests.get(0).getMessage());
                commentTxt.setText(researchRequests.get(0).getComment());
                expecteddateTxt.setText(new SimpleDateFormat("yyyy-mm-dd").format(researchRequests.get(0).getExpecteddate()));
                researchdetailTxt.setText(researchRequests.get(0).getResearchdetail());
            }
        }
        Log.d("AddResearch","After intent");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addnote, menu);

        MenuItem cartItem  = menu.findItem(R.id.save);
        //View cartView = (View) MenuItemCompat.getActionView(cartItem);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.save) {
            int isprivate=0;
            String title=titleTxt.getText().toString();
             String message = messageTxt.getText().toString();
            String description = descriptionTxt.getText().toString();
            String comment = commentTxt.getText().toString();
            String researchdetail = researchdetailTxt.getText().toString();
            String recommendedcitations = recommendedcitationsTxt.getText().toString();
            Calendar calendar = Calendar.getInstance();
            Date today =  calendar.getTime();
            Date expecteddate =  calendar.getTime();
            try {
                expecteddate = new SimpleDateFormat("yyyy-mm-d").parse(expecteddateTxt.getText().toString());
            }
            catch(Exception ex){
                ex.printStackTrace();
            }

            int status = 1;
            try {
                if(isedit){
                    updateResearchRequest(title,message,description,comment,researchdetail,recommendedcitations,today,expecteddate,status);
                }else {
                    saveResearchRequest(title,message,description,comment,researchdetail,recommendedcitations,today,expecteddate,status);
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            Intent intent=new Intent(AddResearchActivity.this,ResearchGenieActivity.class);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }


    public void showDate(){
        Calendar calendar = Calendar.getInstance();
        Date today =  calendar.getTime();
        String year = new SimpleDateFormat("yyyy").format(today);
        String month = new SimpleDateFormat("mm").format(today);
        String day = new SimpleDateFormat("dd").format(today);
        int startyear = Integer.parseInt(year);
        int starthMonth = Integer.parseInt(month);
        int startDay = Integer.parseInt(day);
        datePickerDialog = new DatePickerDialog(
                this, this,startyear , starthMonth, startDay);
        datePickerDialog.show();
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date=String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(dayOfMonth);
        expecteddateTxt.setText(date);
        datePickerDialog.hide();
    }



    public void updateResearchRequest(String title,String message,String description,String comment,String researchdetail,String recommendedcitations,Date creatdate,Date expecteddate,int status){
        List<Researchrequest> researchrequests =researchRequestDao.queryBuilder().where(ResearchrequestDao.Properties.Title.eq(title))
                .orderAsc(ResearchrequestDao.Properties.Title)
                .list();
        Calendar calendar = Calendar.getInstance();
        Date today =  calendar.getTime();
        Researchrequest researchRequest = researchrequests.get(0);
        researchRequest.setComment(comment);
        researchRequest.setTitle(title);
        researchRequest.setDescription(description);
        researchRequest.setResearchdetail(researchdetail);
        researchRequest.setCreatedate(creatdate);
        researchRequest.setExpecteddate(expecteddate);
        researchRequest.setMessage(message);
        researchRequest.setStatus(status);
        researchRequestDao.update(researchRequest);
        //go to researchRequestDao fragment
        String formatteddate=new SimpleDateFormat("dd MMMM yyyy").format(expecteddate);
        new PostResearchRequest().execute(uid,message,title,description,researchdetail,recommendedcitations,formatteddate);



    }

    public void saveResearchRequest(String title,String message,String description,String comment,String researchdetail,String recommendedcitations,Date createdate,Date expecteddate,int status){
        Calendar  calendar = Calendar.getInstance();
        Date today =  calendar.getTime();
        Researchrequest researchRequest = new Researchrequest();
        researchRequest.setComment(comment);
        researchRequest.setTitle(title);
        researchRequest.setDescription(description);
        researchRequest.setResearchdetail(researchdetail);
        researchRequest.setCreatedate(createdate);
        String formatteddate=new SimpleDateFormat("dd MMMM yyyy").format(expecteddate);
        researchRequest.setExpecteddate(expecteddate);
        researchRequest.setMessage(message);
        researchRequest.setStatus(status);
        researchRequestDao.insertOrReplace(researchRequest);

        new PostResearchRequest().execute(uid,message,title,description,researchdetail,recommendedcitations,formatteddate);

    }



    //String uid,String message,String title,String description,String researchdetail,String citations,String expecteddate
    class PostResearchRequest extends AsyncTask<String,Void,JSONObject> {


        private JSONObject jsonobj;
        @Override
        protected JSONObject doInBackground(String... params) {
            jsonobj = LGPClient.postResearchRequest(params[0],params[1],params[2],params[3],params[4],params[5],params[6]);
            return jsonobj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonobj) {
            try {
                progressDialog.dismiss();
                Log.d("SaveAnnotation",jsonobj.toString());
            } catch (Exception ex) {
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
    }


}
