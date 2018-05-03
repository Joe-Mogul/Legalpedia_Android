package com.legalpedia.android.app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.legalpedia.android.app.util.LGPClient;

import org.json.JSONObject;

/**
 * Created by adebayoolabode on 11/23/16.
 */


public class VerificationActivity extends AppCompatActivity {
    private AppCompatTextView timerview;
    private TextView verifylater;
    private AppCompatButton verfiybtn;
    private JSONObject jsonobj;
    private EditText codeField;
    private String telephone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        telephone=getIntent().getStringExtra("phone");
        codeField = (EditText) findViewById(R.id.input_code);
        timerview=(AppCompatTextView) findViewById(R.id.timer);
        verfiybtn = (AppCompatButton) findViewById(R.id.btn_verify);
        verfiybtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                doVerify();
            }
        });
        verifylater = (TextView) findViewById(R.id.verify_later);
        verifylater.setVisibility(View.INVISIBLE);
        verifylater.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
        new CountDownTimer(600000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                String time=String.format("%02d:%02d",(millisUntilFinished/60000),(millisUntilFinished%60000/1000));
                timerview.setText(time);
            }

            @Override
            public void onFinish() {
                timerview.setText("00:00");
                verifylater.setVisibility(View.VISIBLE);
            }
        }.start();

    }

    private void goToLogin() {
        Intent intent = new Intent(VerificationActivity.this,LoginActivity.class);
        startActivity(intent);


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


    public void doVerify(){
        if (codeField.getText().toString().length()<=0){
            Toast.makeText(VerificationActivity.this, "code field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String code=codeField.getText().toString();
        new VerifyExecute().execute(code);
    }


    class VerifyExecute extends AsyncTask<String,Void,JSONObject> {

        private ProgressDialog progressDialog = new ProgressDialog(VerificationActivity.this,R.style.customDialog);
        @Override
        protected JSONObject doInBackground(String... params) {
            jsonobj = LGPClient.doVerify(params[0]);
            return jsonobj;
        }
        @Override
        protected void onPostExecute(JSONObject jsonobj) {
            try {
                try {
                    Log.d("Progress1","Turning off");
                    progressDialog.dismiss();
                    progressDialog.cancel();
                }
                catch(Exception ex){
                    ex.printStackTrace();
                }
                if (jsonobj.getString("status").equals("ok")) {

                    Intent intent = new Intent(VerificationActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                    builder.setTitle(R.string.notify);
                    builder.setMessage(jsonobj.getString("message"));
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
            catch(Exception ex){
                ex.printStackTrace();
                AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                builder.setTitle(R.string.notify);
                builder.setMessage(ex.getMessage());
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



