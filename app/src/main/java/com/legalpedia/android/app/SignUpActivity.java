package com.legalpedia.android.app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.legalpedia.android.app.util.LGPClient;

import org.json.JSONObject;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class SignUpActivity extends AppCompatActivity {

    private TextView link_login;
    private Button btn_signup;

    private JSONObject jsonobj;
    private EditText usernameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText telephoneField;
    private ProgressBar progressBar;
    private String telephone;
    private String password;
    private String email;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameField = (EditText) findViewById(R.id.username);
        emailField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);
        telephoneField = (EditText) findViewById(R.id.telephone);


        link_login=(TextView)findViewById(R.id.link_login);
        btn_signup=(Button)findViewById(R.id.btn_signup);
        link_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                doRegister();
            }
        });
    }



    private void goToLogin() {
        Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(intent);


    }




    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void doRegister() {



        //email and password parameter will be passed from http accessor method once setup
        if (usernameField.getText().toString().length()<5){
            showMessage("Your username must be at least 5 characters");
            return;
        }
        if (usernameField.getText().toString().contains(" ")){
            showMessage("Please use a username that has no space characters");
            return;
        }
        if (emailField.getText().toString().length()<=0){
            showMessage("Please enter a valid email address");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailField.getText().toString()).matches()){
            showMessage("Please enter a valid email address");
            return;
        }
        if (passwordField.getText().toString().length()<6){
            showMessage( "Please enter a password with at least 6 characters");
            return;
        }

        if (telephoneField.getText().toString().length()<11){
            showMessage("Please enter a valid mobile phone number.");
            return;
        }
        username=usernameField.getText().toString();
        email=emailField.getText().toString();
        password=passwordField.getText().toString();
        telephone=telephoneField.getText().toString();
        new SignupExecute().execute(username,email,password,telephone);







    }

    class SignupExecute extends AsyncTask<String,Void,JSONObject> {

        private ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,R.style.customDialog);
        //this method is called first
        @Override
        protected JSONObject doInBackground(String... params) {


            jsonobj = LGPClient.doRegister(params[0], params[1], params[2], params[3]);


            return jsonobj;
        }

        //this is fired after the doInBackground completes
        @Override
        protected void onPostExecute(JSONObject jsonobj) {

            try {
                Log.d("Progress1","Turning off");
                progressDialog.dismiss();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            try {

                String status=jsonobj.getString("status").toString();
                Log.d("SignupActivity",status);
                if (jsonobj.getString("status").equals("ok")) {







                    if(jsonobj.getString("verify")!=null) {
                        if (jsonobj.getString("verify").equals("1")) {
                            Log.d("SignupActivity","verify");
                            Intent intent = new Intent(SignUpActivity.this, VerificationActivity.class);
                            intent.putExtra("phone",telephone);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }

                    }else{
                        Log.d("SignupActivity","verify");
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }

                }else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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
