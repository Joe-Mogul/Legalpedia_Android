package com.legalpedia.android.app;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Profile;
import com.legalpedia.android.app.models.ProfileDao;
import com.legalpedia.android.app.models.Subscription;
import com.legalpedia.android.app.models.SubscriptionDao;
import com.legalpedia.android.app.models.User;
import com.legalpedia.android.app.models.UserDao;
import com.legalpedia.android.app.services.LicenseCheckReceiver;
import com.legalpedia.android.app.services.MessageService;
import com.legalpedia.android.app.services.UpdateService;
import com.legalpedia.android.app.util.LGPClient;
import com.legalpedia.android.app.util.Utils;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by adebayoolabode on 11/23/16.
 */

public class LoginActivity extends AppCompatActivity {
    private UpdateService updateSrv;
    private boolean updateBound=false;
    private EditText emailField;
    private EditText passwordField;
    private Button btn_login;
    private TextView link_signup;
    private TextView link_reset;
    private DaoSession daoSession;
    private String username;
    private String password;
    private JSONObject jsonobj;
    private String LOGINPREFERENCES="login_data";
    private SharedPreferences sharedpreferences = null;
    private SharedPreferences.Editor editor = null;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedpreferences=getSharedPreferences(LOGINPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);
        emailField = (EditText) findViewById(R.id.input_email);
        passwordField = (EditText) findViewById(R.id.input_password);
        daoSession = ((App) getApplication()).getDaoSession();
        String susername=sharedpreferences.getString("username","");
        String spassword=sharedpreferences.getString("password","");
        emailField.setText(susername);
        passwordField.setText(spassword);
        try {
            Intent intent = getIntent();
            if (intent.getStringExtra("message") != null) {
                showMessage(intent.getStringExtra("message"));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        btn_login=(Button)findViewById(R.id.btn_login);
        link_signup=(TextView)findViewById(R.id.link_signup);
        link_reset=(TextView)findViewById(R.id.link_reset);
        btn_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

        link_signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });


        link_reset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                goToReset();
            }
        });
    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


    private void goToRegister() {
        Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(intent);


    }


    private void goToReset() {
        Intent intent = new Intent(LoginActivity.this,RequestResetPasswordActivity.class);
        startActivity(intent);


    }


    private void goToForgotPassword() {
        Intent intent = new Intent(LoginActivity.this,LoginActivity.class);
        startActivity(intent);


    }

    public void doLogin() {

        //email and password parameter will be passed from http accessor method once setup

        if (emailField.getText().toString().length()<=0){
            showMessage("email cannot be empty");
            return;
        }
        if (passwordField.getText().toString().length()<=0){
            showMessage("password cannot be empty");
            return;
        }
        username=emailField.getText().toString();
        password=passwordField.getText().toString();
        if (username.length()>0 && password.length()>0){

            // progress bar

            //check if network is available
            if(Utils.hasNetwork(this)) {



                new LoginExecute().execute(username, password);
            }else{
                showMessage("Your network connection may be down.Please retry later");
            }


        }
        else{
            showMessage("Please enter a valid username");

        }



    }


    public void requestPermission(){
        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Legalpedia:Request Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Legalpedia:Request Permission");
                builder.setMessage("This app needs storage permission.");
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
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,true);
            editor.commit();


        }else{
            proceedAfterPermission();
        }
    }



    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        //Toast.makeText(getBaseContext(), "We got the Storage Permission", Toast.LENGTH_LONG).show();
        //start the service if not started
        new StartService().execute(username,password);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            Log.d("Request Permission","0");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                Log.d("Request Permission","1");
                proceedAfterPermission();
            } else {
                Log.d("Request Permission","2");
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    Log.d("Request Permission","3");
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Legalpedia:Permission Request");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


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
                    //Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }

            if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }

            if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }


    class LoginExecute extends AsyncTask<String,Void,JSONObject> {

        private ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,R.style.customDialog);

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
                Log.d("Progress1","Turning off");
                progressDialog.dismiss();
                progressDialog.cancel();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            try {

                if(jsonobj.getString("status").equals("ok")) {


                    if(jsonobj.getString("requiresupdate").equals("1")){
                        final String updateurl=jsonobj.getString("appstoreurl");
                        final String appPackageName = updateurl.replace("https://play.google.com/store/apps/details?id=","");
                        String message="The version of the app is too old. Please update";
                        new AlertDialog.Builder(new ContextThemeWrapper(LoginActivity.this, R.style.Notification))
                                .setTitle("Notification")
                                .setMessage(message)
                                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override public void onClick(DialogInterface dialog, int which) {

                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(updateurl)));
                                        }
                                    }
                                })
                                .create()
                                .show();

                        return;

                    }
                    String sessionid="";

                    JSONObject userobject=jsonobj.getJSONObject("user");
                    int userid=userobject.getInt("uid");
                    Long uid=userobject.getLong("uid");
                    Long pid=userobject.getLong("pid");
                    String secret=userobject.getString("secret");
                    username=userobject.getString("username");
                    String email=userobject.getString("email");
                    String lastname=userobject.getString("lastname");
                    String firstname=userobject.getString("firstname");
                    String newsfeedurl="";
                    try{
                        newsfeedurl=userobject.getString("newsfeedurl");
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                    String flrfeedurl="";
                    try{
                        flrfeedurl=userobject.getString("flrfeedurl");
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                    String plffeedurl="";
                    try{
                        plffeedurl=userobject.getString("plffeedurl");
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                    String profilepics ="";
                    try {
                          profilepics = userobject.getString("profilepics");
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                    String telephone=userobject.getString("phone");
                    String packageid=jsonobj.getString("packageid");
                    String packagename=jsonobj.getString("packagename");
                    String expirydate=jsonobj.getString("expirydate");
                    String lastrenewdate=jsonobj.getString("lastrenewdate");
                    int wsinterval=jsonobj.getInt("wsinterval");

                    try {
                        sessionid = jsonobj.getString("sessionid");
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    String updateinterval=jsonobj.getString("updateinterval");



                    //check if user has validity
                    try{
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));
                        Date expiredate  = sdf1.parse(String.valueOf(expirydate));
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        sdf2.setTimeZone(TimeZone.getTimeZone("GMT"));
                        Date lastrenwdate  =  sdf2.parse(String.valueOf(lastrenewdate));
                        SubscriptionDao subscriptionDao = daoSession.getSubscriptionDao();
                        Subscription subscription = new Subscription();
                        subscription.setExpirydate(expiredate);
                        subscription.setLastrenewdate(lastrenwdate);
                        subscription.setUid(Integer.parseInt(String.valueOf(uid)));
                        subscriptionDao.insertOrReplace(subscription);
                        Calendar c = Calendar.getInstance();
                        try {
                            c.setTime(subscription.getExpirydate());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        long setTimeInMilli = c.getTimeInMillis();
                        long startTimeInMilli=setTimeInMilli-System.currentTimeMillis();
                        System.out.println("Time is "+String.valueOf(startTimeInMilli));
                        if(startTimeInMilli<=0) {
                            String message=jsonobj.getString("message");
                            if(message.equals("Your subscription has expired. Kindly renew it here")) {
                                final String url=jsonobj.getString("url");
                                final String cuid = String.valueOf(uid);
                                new AlertDialog.Builder(LoginActivity.this)
                                        .setTitle("Notification")
                                        .setMessage(message)
                                        .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                String paymenturl = url + "?uid=" + cuid;
                                                Intent intent = new Intent(LoginActivity.this, PaymentViewActivity.class);
                                                intent.putExtra("url", paymenturl);
                                                startActivity(intent);
                                            }
                                        })
                                        .create()
                                        .show();
                                return;
                            }

                        }
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }





                    int isphoneverified=0;
                    int isemailverified=0;
                    int status=0;
                    Date createdate=new Date();



                    User user = new User();
                    user.setId(uid);
                    user.setUsername(username);
                    user.setPassword(password);
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
                    p.setProfilepics(profilepics);
                    ProfileDao profile = daoSession.getProfileDao();
                    System.out.println("Profile Key "+profile.getKey(p));

                    profile.insertOrReplace(p);




                    editor.putString("uid", String.valueOf(uid));
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putString("packageid", packageid);
                    editor.putString("packagename", packagename);
                    editor.putString("expirydate", expirydate);
                    editor.putString("lastrenewdate", lastrenewdate);
                    editor.putString("sessionid", sessionid);
                    editor.putString("lastrenewdate", lastrenewdate);
                    editor.putString("updateinterval", updateinterval);
                    editor.putString("plffeedurl", plffeedurl);
                    editor.putString("flrfeedurl", flrfeedurl);
                    editor.putString("newsfeedurl", newsfeedurl);
                    editor.putInt("wsinterval", wsinterval);
                    editor.commit();

                    File fileDir = new File(Utils.getROOTDIR());
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }

                    try {
                         Intent messageIntent = new Intent(LoginActivity.this, MessageService.class);
                        messageIntent.putExtra("username", username);
                        messageIntent.putExtra("password", password);
                        startService(messageIntent);

                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }


                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                        requestPermission();

                    }else{
                        if(Utils.is3GNetworkEnabled(LoginActivity.this)) {
                            String message="You are on a cellular data plan.To activate Legalpedia Plus you require at least 500MB of data and 1GB of space on this device.Do you wish to continue now or later?";
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Notification")
                                    .setMessage(message)
                                    .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override public void onClick(DialogInterface dialog, int which) {
                                            new StartService().execute(username,password);

                                        }
                                    })
                                    .create()
                                    .show();

                        }else{
                            new StartService().execute(username,password);
                        }

                    }




                }
                else{
                    try {
                        String message = jsonobj.getString("message");
                        if (message.equals("No package configured.Please purchase a package")) {
                            final String url=jsonobj.getString("url");
                            final String uid=jsonobj.getJSONObject("user").getString("uid");
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Notification")
                                    .setMessage(message)
                                    .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override public void onClick(DialogInterface dialog, int which) {

                                            String paymenturl = url+"?uid="+uid;
                                            Intent intent = new Intent(LoginActivity.this, PaymentViewActivity.class);
                                            intent.putExtra("url", paymenturl);
                                            startActivity(intent);
                                        }
                                    })
                                    .create()
                                    .show();

                        } else {
                            showMessage(message);
                        }
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                        showMessage("Unknown error");
                    }

                }
            }
            catch(Exception ex){
                ex.printStackTrace();
                showMessage("Server error.Please try again later.");
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

    public boolean validate() {
        boolean valid = true;

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (email.length()<=0 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("enter a valid email address");
            valid = false;
        } else {
            emailField.setError(null);
        }

        if (password.length()<=0 || password.length() < 4 || password.length() > 10) {
            passwordField.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }


    public int getVersion() {
        int v = 0;
        try {
            v = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }



    class StartService extends AsyncTask<String,Void,Boolean> {
        private boolean status=false;
        @Override
        protected Boolean doInBackground(String... params){


            try{
                if(!sharedpreferences.getBoolean("hashstartedservice",false)) {
                    if (!Utils.isServiceRunning(LoginActivity.this, "com.legalpedia.android.app.services.UpdateService")) {
                        try {
                            editor.putInt("versioncode", getVersion());
                            editor.commit();
                        }
                        catch(Exception ex) {
                           ex.printStackTrace();
                         }
                        Log.d("Service status", "Service not running,starting it");
                        Intent updateIntent = new Intent(LoginActivity.this, UpdateService.class);
                        updateIntent.putExtra("username", params[0]);
                        updateIntent.putExtra("password", params[1]);
                        bindService(updateIntent, updateConnection, Context.BIND_AUTO_CREATE);
                        startService(updateIntent);



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

            if(!sharedpreferences.getBoolean("hashstartedservice",false)){

                editor.putBoolean("hashstartedservice", true);
                editor.commit();
            }



            if(!sharedpreferences.getBoolean("hasplayedintro",false) && (Build.VERSION.SDK_INT >9)) {
                editor.putBoolean("hasplayedintro", true);
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, DefaultIntro.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("username", username);
                editor.putBoolean("hasplayedintro", true);
                editor.commit();
                startActivity(intent);
            }
        }


    }




}
