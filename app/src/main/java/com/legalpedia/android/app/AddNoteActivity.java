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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.legalpedia.android.app.models.DaoSession;
import com.legalpedia.android.app.models.Notes;
import com.legalpedia.android.app.models.NotesDao;
import com.legalpedia.android.app.util.LGPClient;
import com.legalpedia.android.app.util.ResourceUtil;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by adebayoolabode on 11/23/16.
 */

public class AddNoteActivity extends AppCompatActivity {
    private DaoSession daoSession;
    private NotesDao notesDao;
    private EditText titleTxt;
    private EditText bodyTxt;
    private CheckBox checkbox;
    private Toolbar toolbar;
    public int noteid=0;
    public boolean isedit=false;
    private ProgressDialog progressDialog ;
    private SharedPreferences sharedpreferences = null;
    private String LOGINPREFERENCES="login_data";
    private String uid;
    private String resource="0";
    private String resourceid="0";
    private String cancomment="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("New Note");
        progressDialog = new ProgressDialog(this,R.style.customDialog);
        sharedpreferences=getSharedPreferences(LOGINPREFERENCES, Context.MODE_PRIVATE);
        uid = sharedpreferences.getString("uid", "");
        daoSession = ((App)  getApplication()).getDaoSession();
        notesDao = daoSession.getNotesDao();
        titleTxt = (EditText)findViewById(R.id.title);
        bodyTxt = (EditText)findViewById(R.id.body);
        checkbox = (CheckBox)findViewById(R.id.ispublic);
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkbox.isChecked()){
                    checkbox.setText("Private");
                }else{
                    checkbox.setText("Public");
                }
            }
        });
        Log.d("AddNote","Before intent");
        Intent intent= getIntent();
        try {
            if (intent != null) {
                Log.d("AddNote", "During intent");
                noteid = intent.getIntExtra("noteid", 0);
                isedit = intent.getBooleanExtra("isedit", false);
                Log.d("AddNote", " values " + String.valueOf(isedit) + " " + String.valueOf(noteid));

                List<Notes> notes = notesDao.queryBuilder().where(NotesDao.Properties.Id.eq(noteid)).list();
                if (notes.size() > 0) {
                    Log.d("Title", notes.get(0).getTitle());
                    setTitle(notes.get(0).getTitle());
                    titleTxt.setText(notes.get(0).getTitle());
                    bodyTxt.setText(notes.get(0).getContent());
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        Log.d("AddNote","After intent");

    }

    @Override
    public void onPause(){
        super.onPause();
        onBackPressed();

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

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

        if (id == android.R.id.home) {
            onBackPressed();

        }


        if (id == R.id.save) {
            int isprivate = 0;
            if(checkbox.isChecked()) {
                isprivate = 1;
            }

            String title=titleTxt.getText().toString();
            String body = bodyTxt.getText().toString();
            try {
                if(isedit){
                    updateNote(title, body, isprivate);
                }else {
                    saveNote(title, body, isprivate);
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }

        }



        return super.onOptionsItemSelected(item);
    }


    public void updateNote(String title,String body,int isprivate){
        List<Notes> notes =notesDao.queryBuilder().where(NotesDao.Properties.Title.eq(title))
                .orderAsc(NotesDao.Properties.Title)
                .list();
        Calendar calendar = Calendar.getInstance();
        Date today =  calendar.getTime();
        Notes note = notes.get(0);
        note.setIsprivate(isprivate);
        note.setTitle(title);
        note.setContent(body);
        note.setEditdate(today);
        notesDao.update(note);
        //go to note fragment
        String privacy =String.valueOf(isprivate);
        try {
            new SaveNote().execute(uid,resource,resourceid,title,body,privacy,cancomment);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        Intent intent=new Intent(AddNoteActivity.this,NotesListActivity.class);
        startActivity(intent);
    }

    public void saveNote(String title,String body,int isprivate){
        if(title.length()<=0){
            showMessage("You cant save a note without a title.");
            return;
        }
        if(body.length()<=0){
            showMessage("Your note is empty.");
            return;
        }
        Calendar  calendar = Calendar.getInstance();
        Date today =  calendar.getTime();
        resource = ResourceUtil.resource ;
        resourceid = ResourceUtil.resourceid;
        Log.d("Saving Note",resource+" "+resourceid);
        Notes note = new Notes();
        note.setIsprivate(isprivate);
        note.setTitle(title);
        note.setContent(body);
        note.setEditdate(today);
        note.setCreatedate(today);
        note.setResource(Integer.parseInt(resource));
        note.setResourceid(Integer.parseInt(resourceid));
        notesDao.insertOrReplace(note);
        //go to note fragment
        String privacy =String.valueOf(isprivate);
        try {
            new SaveNote().execute(uid,resource,resourceid,title,body,privacy,cancomment);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        ResourceUtil.resource ="0";
        ResourceUtil.resourceid="0";
        Intent intent=new Intent(AddNoteActivity.this,NotesListActivity.class);
        startActivity(intent);
    }


    //String uid,String resource,String resourceid,String title,String content,String isprivate,String cancomment
    class SaveNote extends AsyncTask<String,Void,JSONObject> {


        private JSONObject jsonobj;
        @Override
        protected JSONObject doInBackground(String... params) {
            jsonobj = LGPClient.addNote(params[0],params[1],params[2],params[3],params[4],params[5],params[6]);
            return jsonobj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonobj) {
            try {
                progressDialog.dismiss();
                Log.d("SaveNote",jsonobj.toString());
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
