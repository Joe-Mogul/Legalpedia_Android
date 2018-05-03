package com.legalpedia.android.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.legalpedia.android.app.util.LGPClient;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class ReportBugActivity  extends AppCompatActivity {
    private EditText titleTxt;
    private EditText bodyTxt;
    private ProgressDialog progressDialog ;
    private SharedPreferences sharedpreferences = null;
    private String LOGINPREFERENCES="login_data";
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Have an issue? Tell us about it.");
        setContentView(R.layout.activity_reportbug);
        progressDialog = new ProgressDialog(this,R.style.customDialog);
        sharedpreferences=getSharedPreferences(LOGINPREFERENCES, Context.MODE_PRIVATE);
        uid = sharedpreferences.getString("uid", "");
        titleTxt = (EditText)findViewById(R.id.title);
        bodyTxt = (EditText)findViewById(R.id.body);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reportbug, menu);

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
            String title=titleTxt.getText().toString();
            String description = bodyTxt.getText().toString();



            int status = 1;
            try {
                new ReportBug().execute(uid,title,description);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }

        }



        return super.onOptionsItemSelected(item);
    }



    //String uid,String title,String description
    class ReportBug extends AsyncTask<String,Void,JSONObject> {


        private JSONObject jsonobj;
        @Override
        protected JSONObject doInBackground(String... params) {
            jsonobj = LGPClient.reportBug(params[0],params[1],params[2]);
            return jsonobj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonobj) {
            String message="Unable to report bug now.";
            try{
                message=jsonobj.getString("message");
            }catch(Exception ex){
                ex.printStackTrace();
            }
            try {
                progressDialog.dismiss();
                Log.d("ReportBug",jsonobj.toString());

                if(jsonobj.getString("status").equals("ok")) {
                    message=jsonobj.getString("message");
                    showMessage(message);
                }
                else{
                    message=jsonobj.getString("message");
                    showMessage(message);
                }

            } catch (Exception ex) {
                ex.printStackTrace();


                showMessage(message);
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


    public void showMessage(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ReportBugActivity.this);
        builder.setTitle("Report Bug");
        builder.setMessage(message);
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(ReportBugActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Report Another", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                titleTxt.setText("");
                bodyTxt.setText("");
                dialog.cancel();
            }
        });
        builder.show();
    }

}

