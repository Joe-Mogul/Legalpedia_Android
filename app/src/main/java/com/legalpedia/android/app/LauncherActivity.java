package com.legalpedia.android.app;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Profile;
import com.legalpedia.android.app.models.ProfileDao;
import com.legalpedia.android.app.models.User;
import com.legalpedia.android.app.models.UserDao;
import com.legalpedia.android.app.util.LGPClient;
import com.legalpedia.android.app.services.UpdateService;
import com.legalpedia.android.app.services.UpdateService.*;
import com.legalpedia.android.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class LauncherActivity extends AppCompatActivity {

    private UpdateService updateSrv;
    private boolean updateBound=false;
    private Intent updateIntent;
    private JSONObject jsonobj;
    private String LOGINPREFERENCES="login_data";
    private SharedPreferences sharedpreferences = null;
    private SharedPreferences.Editor editor = null;
    private ProgressBar progressBar;
    private DaoSession daoSession;
    private UserDao userdao;
    private static final int EXTERNAL_READ_PHONE_STATE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private String susername;
    private String spassword;
    private List<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        sharedpreferences=getSharedPreferences(LOGINPREFERENCES, Context.MODE_PRIVATE);
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
        editor = sharedpreferences.edit();
        susername=sharedpreferences.getString("username","");
        spassword=sharedpreferences.getString("password","");

        //we need to redirect the user to the main activity if they have
        //loggedin before
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            requestPermission();

        }else{

           proceedAfterPermission();
        }



    }


    //connect to the service
    private ServiceConnection updateConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UpdateService.UpdateServiceBinder binder = (UpdateService.UpdateServiceBinder)service;
            //get service
            updateSrv = binder.getService();
            //pass list
            //updateSrv.setList(purchases);
            updateBound = true;


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            updateBound = false;

        }
    };



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void  onDestroy(){

        super.onDestroy();
    }


    public void requestPermission(){

        if (ActivityCompat.checkSelfPermission(LauncherActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LauncherActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(LauncherActivity.this);
                builder.setTitle("Legalpedia:Request Permission");
                builder.setMessage("This app needs to read your Device ID.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(LauncherActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, EXTERNAL_READ_PHONE_STATE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.READ_PHONE_STATE,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(LauncherActivity.this);
                builder.setTitle("Legalpedia:Request Permission");
                builder.setMessage("This app needs to read your Device ID.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(LauncherActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, EXTERNAL_READ_PHONE_STATE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.READ_PHONE_STATE,true);
            editor.commit();


        }else{
            proceedAfterPermission();
        }
    }



    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
         //start the service if not started
        daoSession = ((App) getApplication()).getDaoSession();
        userdao = daoSession.getUserDao();

        users = userdao.loadAll();
        if(users.size()>0) {
            Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(susername=="" || spassword==""){
            Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
            startActivity(intent);
        }//it wont get here
        else {

            new LoginExecute().execute(susername, spassword);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_READ_PHONE_STATE_PERMISSION_CONSTANT) {
            Log.d("Request Permission","0");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                Log.d("Request Permission","1");
                proceedAfterPermission();
            } else {
                Log.d("Request Permission","2");
                if (ActivityCompat.shouldShowRequestPermissionRationale(LauncherActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                    //Show Information about why you need the permission
                    Log.d("Request Permission","3");
                    AlertDialog.Builder builder = new AlertDialog.Builder(LauncherActivity.this);
                    builder.setTitle("Legalpedia:Permission Request");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(LauncherActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, EXTERNAL_READ_PHONE_STATE_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Log.d("Request Permission","4");
                    Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {

            if (ActivityCompat.checkSelfPermission(LauncherActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }

            if (ActivityCompat.checkSelfPermission(LauncherActivity.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    //HTTP

    class LoginExecute extends AsyncTask<String,Void,JSONObject> {



        //this method is called first
        @Override
        protected JSONObject doInBackground(String... params){


            jsonobj= LGPClient.doLogin(params[0],params[1]);


            return jsonobj;
        }
        //this is fired after the doInBackground completes
        @Override
        protected void onPostExecute(JSONObject jsonobj){
            try {


                if(jsonobj.getString("status").equals("ok")) {


                    if(jsonobj.getString("requiresupdate").equals("1")){
                        Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
                        intent.putExtra("message", "The version of the app is too old. Please update");
                        startActivity(intent);

                    }


                    JSONObject userobject=jsonobj.getJSONObject("user");
                    int userid=userobject.getInt("uid");
                    Long uid=userobject.getLong("uid");
                    Long pid=userobject.getLong("pid");
                    String password="";
                    String secret=userobject.getString("secret");
                    String username=userobject.getString("username");
                    String email=userobject.getString("email");
                    String lastname=userobject.getString("lastname");
                    String firstname=userobject.getString("firstname");
                    String telephone=userobject.getString("phone");
                    int isphoneverified=0;
                    int isemailverified=0;
                    int status=0;
                    Date createdate=new Date();

                    DaoSession daoSession = ((App) getApplication()).getDaoSession();

                    User user = new User();
                    user.setId(uid);
                    user.setUsername(username);
                    user.setPassword(username);
                    user.setCreatedate(createdate);
                    user.setIsemailverified(isemailverified);
                    user.setIsphoneverified(isphoneverified);
                    user.setSecret(secret);
                    UserDao users = daoSession.getUserDao();
                    List<User> userlist=users.loadAll();

                    users.insertOrReplace(user);


                    Profile p=new Profile();
                    p.setId(pid);
                    p.setUid(userid);
                    p.setEmail(email);
                    p.setFirstname(firstname);
                    p.setLastname(lastname);
                    p.setPhone(telephone);
                    ProfileDao profile = daoSession.getProfileDao();
                    System.out.println("Profile Key "+profile.getKey(p));

                    profile.insertOrReplace(p);




                    editor.putString("uid", String.valueOf(uid));
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.commit();

                    if(!sharedpreferences.getBoolean("hasplayedintro",false) && (Build.VERSION.SDK_INT >9)) {
                        Intent intent = new Intent(LauncherActivity.this, DefaultIntro.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                        intent.putExtra("username", username);
                        editor.putBoolean("hasplayedintro", true);
                        startActivity(intent);
                    }

                    new StartService().execute(username,password);

                    /**File fileDir = new File(Utils.getROOTDIR());
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }*/

                }
                else{
                    Intent intent = new Intent(LauncherActivity.this, LoginActivity.class);
                    intent.putExtra("message", "Invalid username/password");
                    startActivity(intent);

                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }



    }




    class StartService extends AsyncTask<String,Void,Boolean> {
        private boolean status=false;
        @Override
        protected Boolean doInBackground(String... params){


            try{

                if(!sharedpreferences.getBoolean("hashstartedservice",false)) {
                    if (!Utils.isServiceRunning(LauncherActivity.this, "com.legalpedia.android.app.services.UpdateService")) {

                        Log.d("Service status", "Service not running,starting it");
                        Intent updateIntent = new Intent(LauncherActivity.this, UpdateService.class);
                        updateIntent.putExtra("username", params[0]);
                        updateIntent.putExtra("password", params[1]);
                        bindService(updateIntent, updateConnection, Context.BIND_AUTO_CREATE);
                        startService(updateIntent);
                        Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                        startActivity(intent);


                    }
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(Boolean status){
            if(status){
                Log.d("LauncherActivity","Started Service");
            }else{
                Log.d("LauncherActivity","Service failed");
            }
        }


    }



}
