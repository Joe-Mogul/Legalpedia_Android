package com.legalpedia.android.app;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.legalpedia.android.app.util.LGPClient;
import com.legalpedia.android.app.util.Utils;

import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class RequestResetPasswordActivity extends AppCompatActivity {

    private EditText resetinput;
    private AppCompatButton resetbtn;
    private TextView resetlater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestreset);
        resetinput = (EditText)findViewById(R.id.inputid);
        resetbtn = (AppCompatButton)findViewById(R.id.btn_reset);
        resetlater = (TextView)findViewById(R.id.reset_later);

        resetbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                doReset();
            }
        });

        resetlater.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });

    }

    private void doReset() {
        String verifyinfo = resetinput.getText().toString();
        if(verifyinfo.length()<0){
            showMessage("please enter your email address or registration phone number");

            return;
        }

        new RequestPasswordExecute().execute(verifyinfo);
    }


    private void goToLogin() {
        Intent intent = new Intent(RequestResetPasswordActivity.this,LoginActivity.class);
        startActivity(intent);


    }

    class RequestPasswordExecute extends AsyncTask<String,Void,JSONObject> {

        private ProgressDialog progressDialog = new ProgressDialog(RequestResetPasswordActivity.this,R.style.customDialog);
        private JSONObject jsonobj;
        //this method is called first
        @Override
        protected JSONObject doInBackground(String... params){


            jsonobj= LGPClient.doReset(params[0]);


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
                    goToLogin();
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
