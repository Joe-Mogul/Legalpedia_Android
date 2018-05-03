package com.legalpedia.android.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Profile;
import com.legalpedia.android.app.models.ProfileDao;
import com.legalpedia.android.app.util.ImageLoader;
import com.legalpedia.android.app.util.LGPClient;
import com.legalpedia.android.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.legalpedia.android.app.views.CircleImageView;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class ProfileViewActivity extends AppCompatActivity {
    private DaoSession daoSession;
    private String LOGINPREFERENCES="login_data";
    private SharedPreferences sharedpreferences = null;
    private SharedPreferences.Editor editor = null;
    private Toolbar toolbar;
    private EditText firstnameTxt;
    private EditText lastnameTxt;
    private EditText emailTxt;
    private EditText phoneTxt;
    private EditText skypeTxt;
    private EditText addressTxt;
    private EditText address1Txt;
    private EditText cityTxt;
    private EditText townTxt;
    private EditText stateTxt;
    private EditText countryTxt;
    private String username;
    private String password;
    private ProgressDialog progressDialog ;
    private CircleImageView image;
    private AppCompatImageButton uploadimage;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_BROWSE_IMAGE_REQUEST_CODE = 200;
    private static final int CAMERA_BROWSE_IMAGE_REQUEST_CODE_KITKAT = 201;
    private String uid;
    private static final int MEDIA_TYPE_IMAGE = 1;

    private static final String IMAGE_DIRECTORY_NAME = Utils.getROOTDIR();
    private Uri fileUri;
    private File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this,R.style.customDialog);
        setTitle("Profile");
        daoSession = ((App) getApplication()).getDaoSession();
        sharedpreferences = getSharedPreferences(LOGINPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        username = sharedpreferences.getString("username", "");
        password = sharedpreferences.getString("password", "");
        uid = sharedpreferences.getString("uid", "");
        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if(username == "" || password == "") {
            String message="You need to login with a subscription account to access this feature.Do you want to Login?";
            new AlertDialog.Builder(ProfileViewActivity.this)
                    .setTitle("Notification")
                    .setMessage(message)
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {


                            Intent intent = new Intent(ProfileViewActivity.this , MainActivity.class);
                            startActivity(intent);
                        }
                    }) // dismisses by default
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {


                            Intent intent = new Intent(ProfileViewActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .create()
                    .show();
        }
        firstnameTxt = (EditText)findViewById(R.id.firstname);
        lastnameTxt = (EditText)findViewById(R.id.lastname);
        emailTxt = (EditText)findViewById(R.id.email);
        phoneTxt = (EditText)findViewById(R.id.phone);
        skypeTxt = (EditText)findViewById(R.id.skype);
        addressTxt = (EditText)findViewById(R.id.address);
        address1Txt = (EditText)findViewById(R.id.address1);
        cityTxt = (EditText)findViewById(R.id.city);
        stateTxt = (EditText)findViewById(R.id.state);
        townTxt = (EditText)findViewById(R.id.town);
        countryTxt = (EditText)findViewById(R.id.country);
        image=(CircleImageView)findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                browseImages();
            }
        });
        new FetchProfile().execute();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.save) {
            int isprivate=0;
            //String title=titleTxt.getText().toString();
            //String body = bodyTxt.getText().toString();
            try {
                updateProfile();
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            Intent intent=new Intent(ProfileViewActivity.this,MainActivity.class);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }

    //String firstname,String lastname,String email,String phone,String skype,String address,String address1,String city,String town,String country

    public void updateProfile(){

        String firstname=firstnameTxt.getText().toString();
        String lastname=lastnameTxt.getText().toString();
        String email=emailTxt.getText().toString();
        String phone=phoneTxt.getText().toString();
        String address1=addressTxt.getText().toString();
        String address2=address1Txt.getText().toString();
        String skype=skypeTxt.getText().toString();
        String city=cityTxt.getText().toString();
        String town=townTxt.getText().toString();
        String state=stateTxt.getText().toString();
        String country=countryTxt.getText().toString();

        new UpdateProfile().execute(firstname,lastname,email,phone,skype,address1,address2,city,town,state,country);
    }




    public void requireLogin(){
        String message="You may not be logged in right now.To view profile you need to be logged in.Do you want to login now?";
        new AlertDialog.Builder(ProfileViewActivity.this)
                .setTitle("Notification")
                .setMessage(message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {


                        Intent intent = new Intent(ProfileViewActivity.this , MainActivity.class);
                        startActivity(intent);
                    }
                }) // dismisses by default
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {


                        Intent intent = new Intent(ProfileViewActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .create()
                .show();
    }


    class FetchProfile extends AsyncTask<String,Void,JSONObject> {


        private JSONObject jsonobj;
        @Override
        protected JSONObject doInBackground(String... params) {
            jsonobj = LGPClient.getProfile();
            return jsonobj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonobj) {
            try {
                Log.d("ProfileView",jsonobj.toString());
                if(jsonobj.getString("status").equals("ok")){
                    JSONArray jsonusers = jsonobj.getJSONArray("user");
                    JSONObject user= jsonusers.getJSONObject(0);
                    String firstname=user.getString("firstname");
                    String lastname=user.getString("lastname");
                    String email=user.getString("email");
                    String phone=user.getString("phone");
                    String address1=user.getString("address1");
                    String address2=user.getString("address2");
                    String skype=user.getString("skype");
                    String city=user.getString("city");
                    String town=user.getString("town");
                    String state=user.getString("state");
                    String country=user.getString("country");
                    String profilepics=user.getString("profilepics");
                    firstnameTxt.setText(firstname);
                    lastnameTxt.setText(lastname);
                    emailTxt.setText(email);
                    emailTxt.setEnabled(false);
                    phoneTxt.setText(phone);
                    phoneTxt.setEnabled(false);
                    skypeTxt.setText(skype);
                    addressTxt.setText(address1);
                    address1Txt.setText(address2);
                    cityTxt.setText(city);
                    stateTxt.setText(state);
                    townTxt.setText(town);
                    countryTxt.setText(country);
                    if(profilepics.length()>0){
                        try {
                            new ImageLoader(ProfileViewActivity.this).DisplayImage(profilepics, image);
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }else{
                    requireLogin();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                String message="Unable to fetch your profile at the moment.Kindly check your internet connection or data plan and try again";
                new AlertDialog.Builder(ProfileViewActivity.this)
                        .setTitle("Notification")
                        .setMessage(message)
                          // dismisses by default
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {


                                Intent intent = new Intent(ProfileViewActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .create()
                        .show();
            }

            progressDialog.dismiss();

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

    //String firstname,String lastname,String email,String phone,String skype,String address,String address1,String city,String town,String country

    class UpdateProfile extends AsyncTask<String,Void,JSONObject> {


        private JSONObject jsonobj;
        @Override
        protected JSONObject doInBackground(String... params) {
            jsonobj = LGPClient.updateProfile(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9],params[10]);
            return jsonobj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonobj) {
            try {
                progressDialog.dismiss();
                Log.d("ProfileView",jsonobj.toString());
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

    class UploadFile extends AsyncTask<String,Void,JSONObject> {

        private JSONObject jsonobj;

        @Override
        protected JSONObject doInBackground(String... params) {
            Log.d("UploadFile","Doing upload now");
            try {
                Log.d("UploadFile Name",params[1]);
                File file = new File(params[1]);
                jsonobj = LGPClient.uploadProfilePicture(params[0], file);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            return jsonobj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonobj) {
            try {

                Log.d("ProfileUpdate",jsonobj.toString());
                if(jsonobj.getString("status").equals("ok")) {

                    JSONObject data = jsonobj.getJSONArray("user").getJSONObject(0);
                    String email =data.getString("email");
                    String firstname =data.getString("firstname");
                    String lastname =data.getString("lastname");
                    String address1 =data.getString("address1");
                    String address2 =data.getString("address2");
                    String telephone =data.getString("phone");
                    String profilepics =data.getString("profilepics");

                    long pid =data.getLong("id");

                    Profile p = new Profile();
                    p.setId(pid);
                    /**int userid=data.getInt("uid");
                    p.setUid(userid);
                     */
                    p.setEmail(email);
                    p.setFirstname(firstname);
                    p.setLastname(lastname);
                    p.setPhone(telephone);
                    if (profilepics != "") {
                        p.setProfilepics(profilepics);
                    }
                    /**String biography =data.getString("biography");
                    if (biography != "") {
                        p.setBiography(biography);
                    }*/
                    ProfileDao profile = daoSession.getProfileDao();
                    System.out.println("Profile Key " + profile.getKey(p));

                    profile.insertOrReplace(p);


                }

            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            progressDialog.dismiss();
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

    public void browseImages() {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    CAMERA_BROWSE_IMAGE_REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, CAMERA_BROWSE_IMAGE_REQUEST_CODE_KITKAT);

        }
    }

    private boolean isDeviceSupportCamera() {
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_BROWSE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            fileUri = data.getData();

            String realPath = Utils.getRealPathFromURI_API11to18(this, data.getData());
            Log.d("Image Path 0",data.getData().getPath());


            Log.d("Image Path 0",realPath);
            mFile = new File(realPath);
            Uri uriFromPath = Uri.fromFile(mFile);

            fileUri = uriFromPath;
            image.setVisibility(View.VISIBLE);
            image.setImageBitmap(BitmapFactory.decodeFile(uriFromPath.getPath()));



            new UploadFile().execute(uid,realPath);


        } else if (requestCode == CAMERA_BROWSE_IMAGE_REQUEST_CODE_KITKAT && resultCode == RESULT_OK) {
            fileUri = data.getData();

            String realPath = Utils.getRealPathFromURI_API19(this, data.getData());
            Log.d("Image Path 1",data.getData().getPath());
            Log.d("Image Path 1",realPath);
            mFile = new File(realPath);
            Uri uriFromPath = Uri.fromFile(mFile);
            image.setVisibility(View.VISIBLE);
            image.setImageBitmap(BitmapFactory.decodeFile(uriFromPath.getPath()));

            new UploadFile().execute(uid,realPath);


        } else if (resultCode == RESULT_CANCELED) {

        } else {
            //Failed to capture image
            Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        mFile = getOutputMediaFile(type);
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Failed to create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

}
