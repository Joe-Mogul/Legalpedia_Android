package com.legalpedia.android.app.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.legalpedia.android.app.App;
import com.legalpedia.android.app.LoginActivity;
import com.legalpedia.android.app.MainActivity;
import com.legalpedia.android.app.ProfileViewActivity;
import com.legalpedia.android.app.R;
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

/**
 * Created by adebayoolabode on 6/13/17.
 */

public class DashboardFragment  extends Fragment {

    private DaoSession daoSession;
    private CircleImageView image;
    private Context context;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_BROWSE_IMAGE_REQUEST_CODE = 200;
    private static final int CAMERA_BROWSE_IMAGE_REQUEST_CODE_KITKAT = 201;
    private String uid;
    private static final int MEDIA_TYPE_IMAGE = 1;

    private static final String IMAGE_DIRECTORY_NAME = Utils.getROOTDIR();
    private Uri fileUri;
    private File mFile;
    private MainActivity  activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dash, container, false);
        context = rootView.getContext();
        View photoHeader = rootView.findViewById(R.id.photoHeader);
        activity = (MainActivity)context;
        daoSession = ((App) activity.getApplication()).getDaoSession();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /* For devices equal or higher than lollipop set the translation above everything else */
            photoHeader.setTranslationZ(6);
            /* Redraw the view to show the translation */
            photoHeader.invalidate();
        }
        image=(CircleImageView)rootView.findViewById(R.id.civProfilePic);
        image.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                browseImages();
            }
        });
        new FetchProfile().execute();
        return rootView;

    }


    class FetchProfile extends AsyncTask<String,Void,JSONObject> {

        private ProgressDialog progressDialog = new ProgressDialog(context,R.style.customDialog);
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
                    //firstnameTxt.setText(firstname);
                    //lastnameTxt.setText(lastname);

                    if(profilepics.length()>0){
                        try {
                            new ImageLoader(context).DisplayImage(profilepics, image);
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
                new AlertDialog.Builder(context)
                        .setTitle("Notification")
                        .setMessage(message)
                        // dismisses by default
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {


                                Intent intent = new Intent(context, MainActivity.class);
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


    class UploadFile extends AsyncTask<String,Void,JSONObject> {
        private ProgressDialog progressDialog = new ProgressDialog(context,R.style.customDialog);
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

    public void requireLogin(){
        String message="You may not be logged in right now.To view profile you need to be logged in.Do you want to login now?";
        new AlertDialog.Builder(context)
                .setTitle("Notification")
                .setMessage(message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {


                        Intent intent = new Intent(context , MainActivity.class);
                        startActivity(intent);
                    }
                }) // dismisses by default
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {


                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .create()
                .show();
    }


    private boolean isDeviceSupportCamera() {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_BROWSE_IMAGE_REQUEST_CODE && resultCode == activity.RESULT_OK) {
            fileUri = data.getData();

            String realPath = Utils.getRealPathFromURI_API11to18(context, data.getData());
            Log.d("Image Path 0",data.getData().getPath());


            Log.d("Image Path 0",realPath);
            mFile = new File(realPath);
            Uri uriFromPath = Uri.fromFile(mFile);

            fileUri = uriFromPath;
            image.setVisibility(View.VISIBLE);
            image.setImageBitmap(BitmapFactory.decodeFile(uriFromPath.getPath()));



            new UploadFile().execute(uid,realPath);


        } else if (requestCode == CAMERA_BROWSE_IMAGE_REQUEST_CODE_KITKAT && resultCode == activity.RESULT_OK) {
            fileUri = data.getData();

            String realPath = Utils.getRealPathFromURI_API19(context, data.getData());
            Log.d("Image Path 1",data.getData().getPath());
            Log.d("Image Path 1",realPath);
            mFile = new File(realPath);
            Uri uriFromPath = Uri.fromFile(mFile);
            image.setVisibility(View.VISIBLE);
            image.setImageBitmap(BitmapFactory.decodeFile(uriFromPath.getPath()));

            new UploadFile().execute(uid,realPath);


        } else if (resultCode == activity.RESULT_CANCELED) {

        } else {
            //Failed to capture image
            Toast.makeText(context.getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
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
