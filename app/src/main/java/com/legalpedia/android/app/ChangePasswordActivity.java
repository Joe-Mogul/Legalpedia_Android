package com.legalpedia.android.app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.legalpedia.android.app.util.LGPClient;

import org.json.JSONObject;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class ChangePasswordActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText oldpassword;
    private EditText newpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Change Password");
        oldpassword = (EditText)findViewById(R.id.oldpassword);
        newpassword = (EditText)findViewById(R.id.newpassword);
        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile, menu);

        MenuItem cartItem  = menu.findItem(R.id.save);
        //View cartView = (View) MenuItemCompat.getActionView(cartItem);



        return true;
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();

        }
        if (id == R.id.save) {


            try {
                doChange();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            Intent intent=new Intent(ChangePasswordActivity.this,MainActivity.class);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }

    private void doChange() {
        String oldpasswordtxt = oldpassword.getText().toString();
        String newpasswordtxt = newpassword.getText().toString();
        if(oldpasswordtxt.length()<0){
            showMessage("please enter your old/current password");

            return;
        }

        if(newpasswordtxt.length()<0){
            showMessage("please enter your new password");

            return;
        }

        new ChangePasswordExecute().execute(oldpasswordtxt,newpasswordtxt);
    }


    private void goToLogin() {
        Intent intent = new Intent(ChangePasswordActivity.this,LoginActivity.class);
        startActivity(intent);


    }

    class ChangePasswordExecute extends AsyncTask<String,Void,JSONObject> {

        private ProgressDialog progressDialog = new ProgressDialog(ChangePasswordActivity.this,R.style.customDialog);
        private JSONObject jsonobj;
        //this method is called first
        @Override
        protected JSONObject doInBackground(String... params){


            jsonobj= LGPClient.doChangePassword(params[0],params[1]);


            return jsonobj;
        }
        //this is fired after the doInBackground completes
        @Override
        protected void onPostExecute(JSONObject jsonobj){
            try {
                Log.d("Progress1","Turning off");
                progressDialog.dismiss();
                progressDialog.cancel();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }

            try{
                if(jsonobj.getString("status").equals("ok")){
                    String message="unknown error";
                    try{
                        message = jsonobj.getString("message");
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    showMessage(message);
                }
                else{
                    String message="unknown error";
                    try{
                        message = jsonobj.getString("message");
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    showMessage(message);
                }

            }catch(Exception ex){
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




    public void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(message);
        final int checkedvalue=0;
        //list of items


        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic


                    }


                });



        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }
}
